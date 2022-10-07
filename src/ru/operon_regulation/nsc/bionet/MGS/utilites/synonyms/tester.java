package ru.operon_regulation.nsc.bionet.MGS.utilites.synonyms;

import java.util.TreeMap;

public class tester
{
	static public void main(String[] args)
	{
		String path = "C:\\temp\\MGStools\\synonyms\\test.xml";
		IMGStools_synonymsContainer gg = new MGStools_synonymsContainer(path,true);
		System.out.println("classes");
		for (String s : gg.getAvailableClasses())
		{
			System.out.println(s);
		}

		// String testSyn = "sibml";
		// String[] testSyn2 = new String[] { "TYPE","K","i","BLOCK","P" };
		TreeMap<String, String[]> testSynMap = new TreeMap<String, String[]>();
		testSynMap
				.put("sibml", new String[] { "TYPE", "K", "i", "BLOCK", "P" });
		testSynMap.put("subst", new String[] { "C6H8O-2",
				"(2Z)-dodec-2-enoic acid", "C18H32O2",
				"1_3_4_6_7_9_9b-heptaazaphenalene" });

		for (String testSyn : testSynMap.keySet())
		{
			String[] testSyn2 = testSynMap.get(testSyn);

			System.out.println("Synonyms of: " + testSyn);
			for (String s : gg.getSynonymsOfClassName(testSyn))
			{
				System.out.println(s);
			}
			IMGStools_synonyms syn = gg.getSynonymsOfClass(testSyn);
			if (syn == null)
			{
				System.out.println("there are no synonyms for " + testSyn);
			} else
			{
				System.out.println("name of class " + testSyn + ": "
						+ syn.getNameOfIt());

				for (String syn2 : testSyn2)
				{
					System.out.println("synonym of \"" + syn2 + "\" in class "
							+ testSyn + ":");
					for (String s : syn.getSynonymsFor(syn2))
					{
						System.out.print(", " + s);
					}
					System.out.println();
				}
			}
		}
	}
	/*
	 * '<SINONIM(B,b,BLOCK,block)>' + imya blocka
	 * '<SINONIM(J,j,REJIM,rejim,Rejim)>' + rejim ispol"zovaniya blocka
	 * '<SINONIM(T,t,TYPE,type)>' '<SINONIM(Cm,cm,CM,Cmpd,compound,substrate)>'
	 * '<SINONIM(P,p,PROTEIN,PROT,protein,prot)>'
	 * '<SINONIM(K,k,CONST,const,CONSTANT,constant)>'
	 * '<SINONIM(Sp,SP,sp,STRUCT_PARAM,struct_param)>'
	 * '<SINONIM(I,i,NAME,name)>'
	 */
}