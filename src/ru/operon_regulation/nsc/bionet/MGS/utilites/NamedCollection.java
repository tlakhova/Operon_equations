package ru.operon_regulation.nsc.bionet.MGS.utilites;
//In current version don't used the class
public class NamedCollection
{
	private String name;
	private Object[] elements = null;
	private Object[] EMPTY = new Object[0];

	public NamedCollection(String name, Object[] elements)
	{
		super();
		this.name = name;
		this.elements = elements;
	}

	public String getName()
	{
		return name;
	}

	public Object[] getElements()
	{
		if (elements == null)
		{
			return EMPTY;
		}
		return elements;
	}
}
