package ru.operon_regulation.nsc.bionet.MGS.utilites;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

//This classes needs for read and write files
// If the file does not exist, an exception will be raised

public class MGSfilesHelper
{
	static public String getFileContent(File fileToRead)
	{
		String ret = "";
		RandomAccessFile raf = null;
		try
		{
			raf = new RandomAccessFile(fileToRead, "r");
			byte[] buf;
			buf = new byte[(int) raf.length()];
			raf.read(buf);
			ret = new String(buf);
			raf.close();
		} catch (IOException e)
		{
			ret = "";
			e.printStackTrace();
		}
		return ret;
	}

	static public String getFileContent(String path)
	{
		String ret = "";
		File fl = new File(path);
		ret = getFileContent(fl);
		return ret;
	}

	static public boolean writeStringInFile(String content, String fileName)
	{
		boolean ret = true;
		File outFile = new File(fileName);
		ret = writeStringInFile(content, outFile);
		return ret;
	}

	static public boolean writeStringInFile(String content, File fileName)
	{
		boolean ret = true;
		try
		{
			File outFolder = new File(fileName.getAbsolutePath().substring(0,
					fileName.getAbsolutePath().lastIndexOf("\\")));
			outFolder.mkdirs();
			if (!fileName.exists())
			{
				fileName.createNewFile();
			}
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(
					fileName), "windows-1250");
			// FileWriter fw = new FileWriter(outFile);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return ret;
	}

}
