package ru.operon_regulation.nsc.bionet.MGS.utilites.synonyms;

public interface IMGStools_synonyms
{
	final String[] EMPTY_ARRAY = new String[0];
	
	/** 
	 * @return the canonical name of it base.  
	 */
	String getNameOfIt();
	/** 
	 * @return the set of name synonyms for it base.  
	 */
	String[] getNameSynonymsOfIt();
	/** 
	 * @return the it base information .  
	 */
	String getInformationOfIt();
	
	/** 
	 * @return the canonical name of it base.  
	 */
	boolean isOneOfSynonyms(String synonym);
	
	/**
	 * @param synonym - synonym to search
	 * @return the canonical name for selected synonym. If there is no such synonym in base it return synonym itself.  
	 */
	String getCanonicalNameFor(String synonym);
	
	/**
	 * @param synonym - synonym to search
	 * @return returns set of synonyms for the selected one, returns {@code MGStools_synonyms.EMPTY_ARRAY} if there is no such synonym in base.
	 */
	String[] getSynonymsFor(String synonym);
	
	/**
	 * @param synonym_1 - synonym to compare
	 * @param synonym_2 - synonym to compare
	 * @return returns true if the inputs are synonyms of the same entity  
	 */
	boolean isTheSame(String synonym_1,String synonym_2);
	
	/**
	 * @param text - text that contain synonyms rows. elements in row separate with ',' symbol.
	 */
	void addSynonymsFromText(String text);
}