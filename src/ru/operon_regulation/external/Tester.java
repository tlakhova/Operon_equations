package ru.operon_regulation.external;

import java.io.File;

import javax.swing.JFrame;

import ru.operon_regulation.internal.MGS_OperonBase;
import ru.operon_regulation.internal.OperonFabric;
import ru.operon_regulation.internal.OperonMathModelGenerator;
import ru.operon_regulation.internal.gui.MGS_OperonRegulationGui;
import ru.operon_regulation.internal.interfaces.IMGS_OperonBase;
import ru.operon_regulation.nsc.bionet.MGS.utilites.MGSfilesHelper;

public class Tester
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		if (args.length < 2)
		{
			System.out.println("Not enough parameters!");
			System.out.println("1) work folder!");
			System.out.println("2) base file!");
		} else
		{
			String outFolder = args[0];
			String pathToTheBase = outFolder + File.separator + args[1];
			String resultFolder = outFolder + File.separator+"result";
			File rFolder = new File(resultFolder);
			rFolder.mkdirs();
			String content = MGSfilesHelper.getFileContent(pathToTheBase);
			IMGS_OperonBase base = OperonFabric.createOperonBase(content);
			//OperonFabric.printOperonBase(base,outPath);

			//	   MGS_OperonRegulationGui gui = new MGS_OperonRegulationGui("Operon GUI",base);
			//	   gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//	   gui.setVisible(true);

			OperonMathModelGenerator.makeModelsAsText(base, resultFolder);
		}
	}

}
