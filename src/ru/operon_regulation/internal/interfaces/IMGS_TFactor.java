package ru.operon_regulation.internal.interfaces;

public interface IMGS_TFactor extends IMGS_info
{
	final String ACTIVATOR="activator";
	final String REPRESSOR="repressor";
	final String REGULATOR="regulator";
	final String DUAL="dual";
	
	String[] getSynonyms();
	String getType();
	int getLeftPosition();
	int getRightPosition();
	
	/**
	 * @param tf - aim TFactor
	 * @return true, if this two TFactors has interception in connector positions
	 */
	boolean isInterceptWith(IMGS_TFactor tf);
}
