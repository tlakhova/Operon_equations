package ru.operon_regulation.internal;

import java.util.ArrayList;
import java.util.StringTokenizer;

import ru.operon_regulation.internal.interfaces.IMGS_Gene;
import ru.operon_regulation.internal.interfaces.IMGS_Operon;
import ru.operon_regulation.internal.interfaces.IMGS_OperonBase;
import ru.operon_regulation.internal.interfaces.IMGS_TFactor;
import ru.operon_regulation.internal.objects.MGS_Operon;
import ru.operon_regulation.internal.objects.MGS_TFactor;
import ru.operon_regulation.nsc.bionet.MGS.utilites.MGSfilesHelper;
import ru.operon_regulation.nsc.bionet.MGS.utilites.MGSstringHelper;

public class OperonFabric
{
	static public IMGS_OperonBase createOperonBase(String baseContent)
	{
		IMGS_OperonBase opBase = new MGS_OperonBase();
		operonParser(baseContent, opBase);
//		printOperonBase(opBase);
		return opBase;
	}

	static public void operonParser(String content, IMGS_OperonBase opBase)
	{
		StringTokenizer lineToken = new StringTokenizer(content, MGSstringHelper.NEXT_LINE);
		String currentName = "";
		ArrayList<IMGS_TFactor> TFactors = new ArrayList<IMGS_TFactor>();
		//IMGS_Operon currentOperon = null;
		boolean doFlag = lineToken.hasMoreTokens();
		while (doFlag)
		{
			String line = lineToken.nextToken();
			StringTokenizer partToken = new StringTokenizer(line, "\t");
			String opName = partToken.nextToken();
			String TFName = partToken.nextToken();
			String startPos = partToken.nextToken();
			String finishPos = partToken.nextToken();
			String type = partToken.nextToken();

			IMGS_TFactor tf = new MGS_TFactor(TFName, "", type, Integer
					.valueOf(startPos), Integer.valueOf(finishPos),
					new String[] { TFName });

			doFlag = lineToken.hasMoreTokens(); // 

			if ((!currentName.equals(opName)) || !doFlag)// end of the operon description
			{
				if (currentName.equals(""))
				{
					currentName = opName;					
				}else
				{
					//// !!! the last and previous TF is in same Operon
					if (!doFlag && currentName.equals(opName))
					{
						TFactors.add(tf);
					}
					IMGS_Operon op = new MGS_Operon(currentName, "",
							new IMGS_Gene[0], TFactors.toArray(new IMGS_TFactor[0]));
					opBase.addOperon(op);
					if (!doFlag && !currentName.equals(opName))
					{
						op = null;
						TFactors.clear();// start new set of data
						currentName = opName;
						TFactors.add(tf);
						op = new MGS_Operon(currentName, "",
								new IMGS_Gene[0], TFactors.toArray(new IMGS_TFactor[0]));
						opBase.addOperon(op);
					}			
					
					if (doFlag)
					{
						TFactors.clear();// start new set of data
						currentName = opName;
					}
				}
			}
			if (doFlag)
			{
				TFactors.add(tf);
			}
		}
	}

	static public void printOperonBase(IMGS_OperonBase opBase,String outPath)
	{
		String printString="";
		for(IMGS_Operon oper:opBase.getOperons())
		{
			printString+=oper.getName()+MGSstringHelper.NEXT_LINE;
			for(IMGS_TFactor tf:oper.getTFactors())
			{
				printString+="\t"+(tf.getName()+"\t"+tf.getType()+"\t"+tf.getLeftPosition()+"\t"+tf.getRightPosition()+MGSstringHelper.NEXT_LINE);
			}
		}	
		MGSfilesHelper.writeStringInFile(printString, outPath);
	}
}
