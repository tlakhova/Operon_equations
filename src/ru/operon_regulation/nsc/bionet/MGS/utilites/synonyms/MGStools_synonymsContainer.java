package ru.operon_regulation.nsc.bionet.MGS.utilites.synonyms;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.operon_regulation.nsc.bionet.MGS.utilites.MGSfilesHelper;

public class MGStools_synonymsContainer implements IMGStools_synonymsContainer
{
	final String headerNodeName = "synonyms_parameters";
	final String synonymClassNodeName = "synonym_class";
	final String synonymClassFileLinkNodeName = "synonym_file";
	final String synonymClassSynonymLinkNodeName = "synonym";
	final String nameAttribute = "name";
	final String infoAttribute = "info";
	final String idAttribute = "id";

	private String containerName = "";
	private String containerInfo = "";
	private String containerId = "";

	/**
	 * link from synonyms class and its synonyms
	 */
	HashMap<String, MGStools_synonymsBase> synonymsBases;
	HashMap<String, ArrayList<String>> classesFiles; // key = className; value =
	// TreeMap
	// {String,boolean}
	// filePath and fileType

	MGStools_synonymsBase synonymsOfClasses;

	/**
	 * @param filePath - properties file path or properties file content
	 * @param isPath - if it is<b>{@code true}</b>, the {@code 'filePath'} variable should be the absolute path to the properties file,
	 * <br> if it is <b>{@code false}</b>, the {@code 'filePath'} variable should be the content of the properties file,
	 */
	public MGStools_synonymsContainer(String filePath, boolean isPath)
	{
		String content = filePath;
		if (isPath)
		{
			content = MGSfilesHelper.getFileContent(content);
		}
		Initialization(content);
	}

	/**
	 * @param propertiesFileContent - content of the properties file (XML)
	 */
	private void Initialization(String propertiesFileContent)
	{
		synonymsBases = new HashMap<String, MGStools_synonymsBase>();
		classesFiles = new HashMap<String, ArrayList<String>>();
		synonymsOfClasses = new MGStools_synonymsBase("base", new String[0],
				"synonyms of classes");

		InitializationOfClasses(propertiesFileContent);
		InitializationOfClassesSynonyms();

	}

	private void InitializationOfClassesSynonyms()
	{
		for (String s : this.getAvailableClasses())
		{
			MGStools_synonymsBase synonyms = (MGStools_synonymsBase) this
					.getSynonymsOfClass(s);

			for (String file : classesFiles.get(s))
			{
				String fileContent = MGSfilesHelper.getFileContent(file);

				StringTokenizer lineToken = new StringTokenizer(fileContent,
						"\r\n");
				while (lineToken.hasMoreTokens())
				{
					StringTokenizer synToken = new StringTokenizer(lineToken
							.nextToken(), ",;");
					ArrayList<String> tmp = new ArrayList<String>();
					while (synToken.hasMoreTokens())
					{
						tmp.add(synToken.nextToken().trim());
					}
					synonyms.addSynonymsRow(tmp.get(0), tmp
							.toArray(new String[0]));
				}
			}
		}

	}

	/**
	 * @param fileContent
	 *            - content of the parameters file
	 * @return
	 */
	private boolean InitializationOfClasses(String fileContent)
	{
		boolean ret = false;
		if (!fileContent.equals(""))
		{
			try
			{
				StringReader strR = new StringReader(fileContent);
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new InputSource(strR));
				ret = parseDocument(doc);
			} catch (ParserConfigurationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	private boolean parseDocument(Document doc)
	{

		NodeList ndl = doc.getChildNodes();

		int sz = ndl.getLength();
		for (int i = 0; i < sz; i++)
		{
			Node currNode = ndl.item(i);
			if (currNode.getNodeName().toLowerCase().equals(headerNodeName))
			{
				Node tmp = currNode.getAttributes().getNamedItem(nameAttribute);
				if (tmp != null)
				{
					containerName = tmp.getNodeValue();
				}
				tmp = null;
				tmp = currNode.getAttributes().getNamedItem(infoAttribute);
				if (tmp != null)
				{
					containerInfo = tmp.getNodeValue();
				}
				tmp = null;
				tmp = currNode.getAttributes().getNamedItem(idAttribute);
				if (tmp != null)
				{
					containerId = tmp.getNodeValue();
				}

				NodeList synonymClasses = currNode.getChildNodes();
				int synClassesSz = synonymClasses.getLength();
				for (int j = 0; j < synClassesSz; j++)
				{
					Node classNode = synonymClasses.item(j);
					if (classNode.getNodeName().toLowerCase().equals(
							synonymClassNodeName))
					{
						addSynonymsClass(classNode);
					}
				}
			}
		}
		return true;
	}

	private void addSynonymsClass(Node classNode)
	{
		ArrayList<String> filesWithBases = new ArrayList<String>();
		TreeMap<String, Boolean> filesWithXMLtype = new TreeMap<String, Boolean>();
		ArrayList<String> classNameSynonyms = new ArrayList<String>();

		String className = "";
		Node tmp;
		tmp = classNode.getAttributes().getNamedItem("name");
		if (tmp != null)
		{
			className = tmp.getNodeValue();

			NodeList ndl = classNode.getChildNodes();
			int sz = ndl.getLength();
			for (int i = 0; i < sz; i++)
			{
				Node currNode = ndl.item(i);

				if (currNode.getNodeName().toLowerCase().equals(
						synonymClassFileLinkNodeName))
				{
					String file;
					boolean type;
					tmp = currNode.getAttributes().getNamedItem("name");
					if (tmp != null)
					{
						file = tmp.getNodeValue();
						filesWithBases.add(file);

						tmp = currNode.getAttributes().getNamedItem("xml");
						if (tmp != null)
						{
							type = (tmp.getNodeValue().equals("true") ? true
									: false);
							filesWithXMLtype.put(file, type);
						} else
						{
							filesWithXMLtype.put(file, false);
						}
					}
				}
				if (currNode.getNodeName().toLowerCase().equals(
						synonymClassSynonymLinkNodeName))
				{
					tmp = currNode.getAttributes().getNamedItem("name");
					if (tmp != null)
					{
						String syn = tmp.getNodeValue();
						classNameSynonyms.add(syn);
					}
				}
			}
		}
		// insert it to the base
		String[] synonyms = classNameSynonyms.toArray(new String[0]);

		// set synonyms of the current class
		boolean isFoundSimilarities = synonymsOfClasses.addSynonymsRow(
				className, synonyms);

		/*
		 * after it operation we can get at least two classes with similarities
		 * in synonyms. it means that we have at least two same classes
		 */

		if (!isFoundSimilarities)
		{
			MGStools_synonymsBase newClass = new MGStools_synonymsBase(
					className, synonyms, "");
			synonymsBases.put(className, newClass);
			classesFiles.put(className, filesWithBases);

		} else
		{
			// we have at least two classes with synonyms
			// note! we not created the synonyms set with current data
			String[] classSynonyms = this.getSynonymsOfClassName(className);

			IMGStools_synonyms previousClassToModify = this
					.getSynonymsOfClass(className);
			String classNameToModify = previousClassToModify.getNameOfIt(); // the
			// class
			// that
			// takes
			// all
			// synonyms
			// and
			// files

			// set all new synonyms
			((MGStools_synonymsBase) previousClassToModify)
					.addNameSynonyms(classSynonyms);

			ArrayList<String> files = classesFiles.get(classNameToModify); // files
			// with
			// synonyms
			// of
			// owner
			// class

			// set files of current data set
			for (String filesToInsert : filesWithBases)
			{
				if (!files.contains(filesToInsert))
				{
					files.add(filesToInsert);
				}
			}

			// set files of others classes that already created
			for (String s : classSynonyms)
			{
				IMGStools_synonyms previousClassToErase = synonymsBases.get(s);

				if ((previousClassToErase != null)
						&& (previousClassToErase != previousClassToModify))
				{
					String classNameToErase = previousClassToErase
							.getNameOfIt();
					for (String filesToInsert : classesFiles
							.get(classNameToErase))
					{
						if (!files.contains(filesToInsert))
						{
							files.add(filesToInsert);
						}
					}
					synonymsBases.remove(classNameToErase);
				}
			}
		}

	}

	public String[] getAvailableClasses()
	{
		return synonymsBases.keySet().toArray(new String[0]);
	}

	public IMGStools_synonyms getSynonymsOfClass(String classname)
	{
		String canonic = synonymsOfClasses.getCanonicalNameFor(classname);
		return synonymsBases.get(canonic);

	}

	public String[] getSynonymsOfClassName(String classname)
	{
		return synonymsOfClasses.getSynonymsFor(classname);
	}
}
