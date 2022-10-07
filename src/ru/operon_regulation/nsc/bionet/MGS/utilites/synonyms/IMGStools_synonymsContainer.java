package ru.operon_regulation.nsc.bionet.MGS.utilites.synonyms;

public interface IMGStools_synonymsContainer
{
	/**
	 * This method is used for set or switch between parameters 
	 * <br>After it you should call the {@code Initialization()} method/
	 * @param paramsFilePath - path to file with parameters
	 */
//	void setParameters(String paramsFilePath);
	
	
	/**
	 * 
	 * It method is used to initiate synonyms container by selected file  
	 * @param FilePath - file with parameters
	 * @return true if the initialization finished well. 
	 */
//	boolean Initialization(String FilePath);
	
	/**
	 * @return names of all available synonym classes
	 */
	String[] getAvailableClasses();
	
	/**
	 * @param Class - the name of class
	 * @return synonyms of selected class name.
	 */
	String[] getSynonymsOfClassName(String classname);
	
	/**
	 * @param classname - the name of class
	 * @return return the {@code MGStools_synonyms} object for selected class, <b>{@code null}</b> if there is no such class. 
	 */
	IMGStools_synonyms getSynonymsOfClass(String classname);
	
}
