package ru.operon_regulation.internal.objects;

import ru.operon_regulation.internal.interfaces.IMGS_info;

public abstract class MGS_info implements IMGS_info
{
	private String name;
	private String information;
	public MGS_info(String name,String info)
	{
		this.name=name;
		this.information=info;
	}

	public String getInformation()
	{
		return this.information;
	}

	public String getName()
	{
		return this.name;
	}
	
	
	
}
