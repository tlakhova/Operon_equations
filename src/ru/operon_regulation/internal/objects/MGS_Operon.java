package ru.operon_regulation.internal.objects;

import java.util.ArrayList;
import java.util.Arrays;

import ru.operon_regulation.internal.interfaces.IMGS_Gene;
import ru.operon_regulation.internal.interfaces.IMGS_Operon;
import ru.operon_regulation.internal.interfaces.IMGS_TFactor;

public class MGS_Operon extends MGS_info implements IMGS_Operon
{
	private IMGS_Gene[] OperonGenes;
	private IMGS_TFactor[] OperonTFactors;
	public MGS_Operon(String OPname, String info, IMGS_Gene[] genes, IMGS_TFactor[] TFactors)
	{
		super(OPname, info);
		OperonGenes = Arrays.copyOf(genes, genes.length);
		OperonTFactors = Arrays.copyOf(TFactors, TFactors.length);
	}

	@Override
	public IMGS_Gene[] getGenes()
	{
		return OperonGenes;
	}

	@Override
	public IMGS_TFactor[] getTFactors()
	{
		return OperonTFactors;
	}

}
