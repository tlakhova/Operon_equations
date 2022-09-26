package ru.operon_regulation.internal;

import java.util.ArrayList;
import java.util.List;

import ru.operon_regulation.internal.interfaces.IMGS_Operon;
import ru.operon_regulation.internal.interfaces.IMGS_OperonBase;


public class MGS_OperonBase implements IMGS_OperonBase
{
	ArrayList<IMGS_Operon> operonContainer;

	public MGS_OperonBase()
	{
		operonContainer = new ArrayList<IMGS_Operon>();
	}
	
	public List<IMGS_Operon> getOperons()
	{
		return operonContainer;
	}
	
	public void addOperon(IMGS_Operon op)
	{
		if(!operonContainer.contains(op))
		{
			operonContainer.add(op);
		}
	}
}
