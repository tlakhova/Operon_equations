package ru.operon_regulation.internal.interfaces;

public interface IMGS_Operon extends IMGS_info
{
	IMGS_Gene[] getGenes();
	IMGS_TFactor[] getTFactors();
}
