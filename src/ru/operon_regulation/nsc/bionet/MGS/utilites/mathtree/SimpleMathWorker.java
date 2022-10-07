package ru.operon_regulation.nsc.bionet.MGS.utilites.mathtree;

import java.util.Map;

//This class uses by building math model
//It

public class SimpleMathWorker
{
	/**
	 * this operation could change all elements that you set in selected formula
	 * 
	 * @param inMath - the simple mathematical formula
	 * @param changer - the map of elements to change
	 * @return the formula string after modification  
	 */
	public static String changeAllElementsFor(String inMath,final Map<String, String> changer)
	{
		String ret="";
		SimpleMathTree mathTree = new SimpleMathTree(inMath,0,'+',null);
		changeAllNames(mathTree,changer);
		ret= mathTree.getAsString();
		return ret;
	}

	private static void changeAllNames(SimpleMathTree mathTree,
			final Map<String, String> changer)
	{
		if(mathTree.isFormula())
		{
			for(SimpleMathTree child:mathTree.getChildren())
			{
				changeAllNames(child,changer);
			}
		}
		else
		{
			String elemName=mathTree.getNodeName();
			String changeVal =changer.get(elemName); 
			if(changeVal!=null)
			{
				mathTree.modifyName(changeVal);
			}
		}
	}
}
