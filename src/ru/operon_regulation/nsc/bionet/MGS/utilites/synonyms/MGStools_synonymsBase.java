package ru.operon_regulation.nsc.bionet.MGS.utilites.synonyms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.TreeMap;

import ru.operon_regulation.nsc.bionet.MGS.utilites.MGSfilesHelper;

public class MGStools_synonymsBase implements IMGStools_synonyms
{

	private String baseName;
	private String[] baseNameSynonyms;
	private String baseInfo;

	/**
	 * ������������ ���������� ������������� ��������� �� ���������. �.�. ������
	 * �������� ������������� ���������� ������������� (int). ��� ����������
	 * (�����������) ��������� ����� ������ ������������� ����� �������������
	 * (counter+1), ������ ����������.
	 */
	private Integer counter;

	private Integer getNewCounter()
	{
		counter++;
		return new Integer(counter);
	}

	private TreeMap<String, String> synonymsToCanonocalName;
	private TreeMap<String, Integer> synonymsToEntityCounter;
	private TreeMap<Integer, String[]> entityCounterToSynonyms;

	/**
	 * @param name
	 *            - canonical name of the synonyms base
	 * @param synonyms
	 *            - synonyms of the base
	 * @param info
	 *            - information about this synonyms base
	 */
	public MGStools_synonymsBase(String name, String[] synonyms, String info)
	{
		counter = 0;
		baseName = name;
		baseNameSynonyms = new String[synonyms.length];
		Arrays.copyOfRange(synonyms, 0, synonyms.length);
		baseInfo = info;

		synonymsToCanonocalName = new TreeMap<String, String>();
		synonymsToEntityCounter = new TreeMap<String, Integer>();
		entityCounterToSynonyms = new TreeMap<Integer, String[]>();
	}

	public String getNameOfIt()
	{
		return baseName;
	}

	public String[] getNameSynonymsOfIt()
	{
		return baseNameSynonyms;
	}

	protected void addNameSynonyms(final String[] syn)
	{
		ArrayList<String> ret = new ArrayList<String>();
		for (String s : baseNameSynonyms)
		{
			if (!ret.contains(s))
			{
				ret.add(s);
			}
		}

		for (String s : syn)
		{
			if (!ret.contains(s))
			{
				ret.add(s);
			}
		}
		baseNameSynonyms = ret.toArray(new String[0]);
	}

	public String getInformationOfIt()
	{
		return baseInfo;
	}

	public String[] getSynonymsFor(String synonym)
	{
		String[] ret = null;
		Integer entity = synonymsToEntityCounter.get(synonym);
		if (entity != null)
		{
			String[] list = entityCounterToSynonyms.get(entity);
			ret = Arrays.copyOfRange(list, 0, list.length);
		}
		if (ret == null)
		{
			ret = IMGStools_synonyms.EMPTY_ARRAY;
		}
		return ret;
	}

	public String getCanonicalNameFor(String synonym)
	{
		return synonymsToCanonocalName.get(synonym);
	}

	public boolean isTheSame(String synonym_1, String synonym_2)
	{
		boolean ret = false;
		String canonical_1 = synonymsToCanonocalName.get(synonym_1);
		if (canonical_1 != null)
		{
			String canonical_2 = synonymsToCanonocalName.get(synonym_2);
			if ((canonical_2 != null) & (canonical_1.equals(canonical_2)))
			{
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * TODO �������� �������� �� �������������� �������� ������
	 * 
	 */

	/**
	 * @param canonicalName
	 * @param synonyms
	 * @return return true if there was at least one synonym in base already
	 */
	public boolean addSynonymsRow(final String canonicalName,
			final String[] synonyms)
	{
		boolean ret = false;
		// counter++;

		String[] entitySynonyms = new String[synonyms.length + 1];
		entitySynonyms[0] = canonicalName;

		ArrayList<Integer> connectList = new ArrayList<Integer>();
		Integer isIn = synonymsToEntityCounter.get(canonicalName);
		if (isIn != null)
		{
			connectList.add(isIn);
			isIn = null;
		}
		int i = 0;
		for (String s : synonyms)
		{
			i++;
			isIn = synonymsToEntityCounter.get(s);
			entitySynonyms[i] = s;
			if (isIn != null)
			{
				if (!connectList.contains(isIn))
				{
					connectList.add(isIn);
				}
				isIn = null;
			}
		}
		if (connectList.size() == 0)
		{
			// we have not found synonym in base
			//			
			// synonymsToCanonocalName.put(canonicalName, canonicalName);
			// synonymsToEntityCounter.put(canonicalName, new Integer(counter));
			Integer indexOfRow = getNewCounter();
			for (String s : entitySynonyms)
			{
				synonymsToCanonocalName.put(s, entitySynonyms[0]);
				synonymsToEntityCounter.put(s, indexOfRow);

			}
			entityCounterToSynonyms.put(indexOfRow, entitySynonyms);
		} else
		{
			// we have found synonym in base
			ret = true;
			connectSynonymsRows(connectList, entitySynonyms);
		}

		return ret;
	}

	private void connectSynonymsRows(ArrayList<Integer> connectList,
			String[] synonymsToInsert)
	{
		ArrayList<String> newRow = new ArrayList<String>();
		for (Integer index : connectList)
		{
			// old set of synonyms
			for (String s : entityCounterToSynonyms.get(index))
			{
				// they have not similar values
				newRow.add(s);
			}
			entityCounterToSynonyms.remove(index);
		}
		// new set of synonyms
		for (String s : synonymsToInsert)
		{
			// it could be similar values
			if (!newRow.contains(s))
			{
				newRow.add(s);
			}
		}
		Integer indexOfRow = getNewCounter();

		entityCounterToSynonyms.put(indexOfRow, newRow.toArray(new String[0]));
		String canonicalName = newRow.get(0);
		for (String s : newRow)
		{
			synonymsToCanonocalName.put(s, canonicalName);
			synonymsToEntityCounter.put(s, indexOfRow);
		}
	}

	@Override
	public void addSynonymsFromText(String text)
	{
		StringTokenizer lineToken = new StringTokenizer(text, "\r\n");
		while (lineToken.hasMoreTokens())
		{
			StringTokenizer synToken = new StringTokenizer(lineToken
					.nextToken(), ",;");
			ArrayList<String> tmp = new ArrayList<String>();
			while (synToken.hasMoreTokens())
			{
				tmp.add(synToken.nextToken().trim());
			}
			this.addSynonymsRow(tmp.get(0), tmp.toArray(new String[0]));
		}
	}

	@Override
	public boolean isOneOfSynonyms(String synonym)
	{
		return synonymsToCanonocalName.containsKey(synonym);
	}

}
