package ru.operon_regulation.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import ru.operon_regulation.internal.interfaces.IMGS_Operon;
import ru.operon_regulation.internal.interfaces.IMGS_OperonBase;
import ru.operon_regulation.internal.interfaces.IMGS_TFactor;
import ru.operon_regulation.nsc.bionet.MGS.utilites.MGSfilesHelper;
import ru.operon_regulation.nsc.bionet.MGS.utilites.MGSstringHelper;
import ru.operon_regulation.nsc.bionet.MGS.utilites.mathtree.SimpleMathWorker;


// This class is needed for construction frame math model
// Here the TF binding options are enumerated

public class OperonMathModelGenerator
{
	// TFactorsMaxBorder - max value TF for corresponding an operon
	// because of combinatorial complexity
	static final int TFactorsMaxBorder = 15;
	static final String paramInitialName = "V_0";
	static final String paramName = "k_";
	static final String paramBasalName = "a_basal";
	static final String paramDegreeName = "n_";
	static final String paramLevelName = "level_";

	//A create model as text for each operon
	public static void makeModelsAsText(IMGS_OperonBase base, String outFolder)
	{
		String outFileContent = "";
		int operonSize = base.getOperons().size();
		for (IMGS_Operon oper : base.getOperons())
		{
			outFileContent = "";
			String operName = oper.getName();

			System.out.println(operonSize + "\t" + operName);
			operonSize--;

			outFileContent += "Mathematical model transcription of the operon: "
					+ operName + MGSstringHelper.NEXT_LINE;

			//Checking the number of TFs for one operon
			// If TFs are > 15, the model will not be built.
			//
			if (oper.getTFactors().length > TFactorsMaxBorder)
			{
				outFileContent += "Mathematical model couldn't be generated for the appropriate time due to TFactors size"+MGSstringHelper.NEXT_LINE;
				outFileContent += "Support only operons with less then "
						+ TFactorsMaxBorder + " TFactors"+MGSstringHelper.NEXT_LINE;
			} else
			{
				// paramName, in paramComments
				ArrayList<String[]> parameters = new ArrayList<String[]>();
				// Set the parameters: V_0, a_basal and 1 -  that are in the formula for each operon
				parameters.add(new String[] { paramInitialName,
				"Initial rate" });

				parameters.add(new String[] { paramBasalName,
						"Basal activity" });
				// in formula
				String[] nomenatorDenomenator = new String[] {
						paramBasalName, "1" };

				IMGS_TFactor[] TFactors = oper.getTFactors();						

				int sz = TFactors.length;
				//elements needed for realization deep-first search algorithm
				boolean[] elements = new boolean[sz];
				Arrays.fill(elements, false);
				for (int i = 0; i < sz; i++)
				{
					parameters.add(new String[] { paramName + (i + 1),
							TFactors[i].getName() + " activity" });
//					parameters.add(new String[] { paramDegreeName+ (i + 1),
//							" Hill's coefficient for " + TFactors[i].getName() });

				}
				
				/** counter of generated[0] states and blocked[1] states */
				int stateCounter[]={0,0};

				// Recursive function
				// Here implemented by deep-first search algorithm
				giveFreeElement(elements, 0, TFactors, nomenatorDenomenator,
						parameters,stateCounter);		
				
				
				outFileContent += "Generated  states: "+stateCounter[0]+MGSstringHelper.NEXT_LINE+
			                      "Blocked states: "+stateCounter[1]+MGSstringHelper.NEXT_LINE+
				                   "======================"+MGSstringHelper.NEXT_LINE;
				// Variables
				outFileContent += "variables:"+MGSstringHelper.NEXT_LINE;
				int counter=0;
				for (IMGS_TFactor factor : TFactors)
				{
					counter++;
					outFileContent += counter+"\t"+factor.getName() + "\t;\t"
							+ factor.getLeftPosition() + "\t"
							+ factor.getRightPosition() + "\t" + factor.getType()
							+ MGSstringHelper.NEXT_LINE;
				}
				// Parameters				
				outFileContent += "parameters: "+parameters.size()+MGSstringHelper.NEXT_LINE;
				for (String[] param : parameters)
				{
					outFileContent += param[0] + "\t;\t" + param[1] + MGSstringHelper.NEXT_LINE;
				}
				outFileContent += MGSstringHelper.NEXT_LINE;
				//				outFileContent += "Velocity:\n";
				//				String FinalFormula = "V=" + "(" + nomenatorDenomenator[0] + ")"
				//						+ "/" + "(" + nomenatorDenomenator[1] + ")";
				//				outFileContent += FinalFormula;
				//				outFileContent += "\n";

				// Main part
				nomenatorDenomenator[0]= SimpleMathWorker.changeAllElementsFor(nomenatorDenomenator[0], new TreeMap<String, String>());
				outFileContent += "Numerator = " + nomenatorDenomenator[0] + MGSstringHelper.NEXT_LINE;
				outFileContent += "Denominator = " + nomenatorDenomenator[1] + MGSstringHelper.NEXT_LINE;
				outFileContent += "f = "+ paramInitialName+"*Numerator/Denominator"+MGSstringHelper.NEXT_LINE;
			}
			MGSfilesHelper.writeStringInFile(outFileContent, outFolder
					+ File.separator + operName + ".txt");
		}
	}

	/**
	 * that function creates main formulas
	 * @param elements
	 * @param start
	 * @param Tfactors
	 * @param nomenatorDenomenator
	 */
	static private void giveFreeElement(boolean[] elements, int start,
			IMGS_TFactor[] Tfactors, String[] nomenatorDenomenator,
			ArrayList<String[]> parameters, int[] stateCounter)
	{
		//System.err.print(start+"\t");
		//System.err.println(elements.toString());
		boolean interceptFlag = false;
		for (int i = start; i < elements.length; i++)
		{

			elements[i] = true; // to be able to move to another node
			interceptFlag = false;
			for (int backInd = i - 1; backInd >= 0; backInd--)
			{
				if (elements[backInd])
				{
					if (Tfactors[backInd].isInterceptWith(Tfactors[i]))
					{
						interceptFlag = true;
						break;
					}
				}
			}
			if (!interceptFlag)
			{
				stateCounter[0]+=1;
				//generation of the terms of the equation depending on the location of the binding sites
				printIt(elements, Tfactors, nomenatorDenomenator, parameters);
				giveFreeElement(elements, i + 1, Tfactors, nomenatorDenomenator,
						parameters,stateCounter);
				
			}
			else				
			{  // generation of blocked states
				stateCounter[1]+=1;
			}
			elements[i] = false; // to be able to move to another node
		}
	}

	/**
	 * method to print current solution
	 * @param elements
	 * @param TFactors
	 * @param nominatorDenominator  - String[2]; [0] - nominator; [1] - denominator
	 * @param parameters
	 */
	private static void printIt(boolean[] elements, IMGS_TFactor[] TFactors, String[] nominatorDenominator, ArrayList<String[]> parameters)
	{
		//		MyCounter++;
		String subFormula = "";
		String SigmaParameter = "";
		String OmegaParameter = "";
		String MultiHillParameter = "";
		boolean isRepressor = false;
		int elCounter = 2;// element counter. If there is only one element, the "omega" parameter is not needed.
        String namesHeaders="";

		//Calculation of the unique index for the Hill parameter
		int isComplexState = 2;    // if it will be 0 - then it is Complex State like Tfi*Tfj...
		for (int i = 0; i < TFactors.length; i++)
		{
			if (elements[i])
			{
				isComplexState--;
			}
			if (isComplexState==0)
			{
				break;
			}
		}
		//Constructing a term for each TF or
		// combination of TFs depending on the location of their TFBSs
		for (int i = 0; i < TFactors.length; i++)
		{
			if (elements[i])
			{
				if (elCounter > 0)
				{
					elCounter--;
				}
				IMGS_TFactor Tfact_1 = TFactors[i];
				String Tf_1Name = Tfact_1.getName();
				namesHeaders+=" "+Tf_1Name+"(#"+(i+1)+")";

				if (Tfact_1.getType().equals(IMGS_TFactor.REPRESSOR))
				{
					isRepressor = true;
				}
				
				String postfix=""+(i + 1);

				// SigmaParameter - is the TF combination index for sigma parameter like "123_12_13"
				SigmaParameter += ((postfix.length()>1)?"_":"") + postfix;

				// OmegaParameter - is the TF combination index for omega parameter like "123_12_13"
				// w_i
				OmegaParameter += ((postfix.length() > 1) ? "_" : "") + postfix;

				// Hill part
				// for complex index Hill degree
				String HillPostfixAdderPRM = "h";
//				MultiHillParameter = paramDegreeName + SigmaParameter + (isComplexState>0?"":"h");
				MultiHillParameter = paramDegreeName + (i + 1) + (isComplexState>0?"":HillPostfixAdderPRM);
				int ADDER=1;
				while (1==1) {
					String tmpINDX = MultiHillParameter+ADDER;
					boolean EXIT_FLAG=true;
					for (String[] entry : parameters) {
						if (entry[0].equals(tmpINDX)){
							EXIT_FLAG=false;
//							MultiHillParameter = MultiHillParameter + HillPostfixAdderPRM;
							ADDER++;
						}
					}
					if (EXIT_FLAG){
						MultiHillParameter = MultiHillParameter+ADDER;
						break;
					}
				}
				parameters
						.add(new String[] { MultiHillParameter, namesHeaders+"  Hill parameter" });

				subFormula += "*(" + Tf_1Name + "/" + paramName + (i + 1) + ")^" +  MultiHillParameter;

			}
		}
		// Now the subFormula = *(TFi/ki)^ni*(TFi2/ki2)^ni2*(TFi2/ki2)^ni2*(TFi3/ki3)^ni3*(TFi4/ki4)^ni4
		subFormula = subFormula.substring(1); // erase first "*"
		//SigmaParameter = SigmaParameter.substring(1); // erase first "*"

		//====================
        // calculate denominator first
		// elCounter == 0 when TFBSs the TF pairs considered in this iteration do not overlap
		if (elCounter == 0)
		{
			OmegaParameter = "w" + OmegaParameter;
			// parameter in nominator
			// level_ij*w_ij*
			parameters
					.add(new String[] { OmegaParameter, " cooperation parameter" });

			// for denominator
			subFormula = OmegaParameter + "*" + subFormula;

			SigmaParameter = paramLevelName+SigmaParameter;
			parameters
					.add(new String[] { SigmaParameter, namesHeaders+"  activity parameter" });
			nominatorDenominator[0] += "+" + SigmaParameter+ "*" + subFormula;
		}
		else
		{
			OmegaParameter = "";
			//====================
			// calculate nominator when considering a single TF

			SigmaParameter = paramLevelName+SigmaParameter;
			parameters
					.add(new String[]{SigmaParameter, namesHeaders + " rate activity parameter"});
			nominatorDenominator[0] += "+" + SigmaParameter + "*" + subFormula;

		}
		// write denominator
		nominatorDenominator[1] += "+" + "(" + subFormula + ")";

	}
}
