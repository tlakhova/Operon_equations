package ru.nsc.bionet.MGS.utilites;

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
