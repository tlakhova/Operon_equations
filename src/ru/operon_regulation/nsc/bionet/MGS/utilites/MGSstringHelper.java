package ru.operon_regulation.nsc.bionet.MGS.utilites;

public class MGSstringHelper
{
	public final static String NEXT_LINE = "\n";

	/**
	 * @param inStr
	 *            - string to change
	 * @return the input string where all space characters (<b>{@code' '}</b>)
	 *         are changed on symbol <b>{@code '_'}</b>
	 */
	static public String changeAllSpacesOn_(String inStr)
	{
		return inStr.replaceAll("", "_");
	}

	/**
	 * @param inStr
	 *            - string to change
	 * @param spaces
	 *            - set of symbols that you want to change
	 * @return the string <b>{@code inStr}</b> where all symbols that set in <b>
	 *         {@code spaces}</b> are changed on symbol <b>{@code '_'}</b>
	 */
	static public String changeAllSpacesOn_(String inStr, final String spaces)
	{
		for (int i = 0; i < spaces.length(); i++)
		{
			inStr = inStr.replaceAll("[" + spaces.charAt(i) + "]", "_");
		}
		return inStr;
	}

	/**
	 * @param inStr
	 *            - string to change
	 * @return the input string where all special characters are changed on XML
	 *         compatible symbols
	 */
	static public String make_XML_HTML_Compatible(String inStr)
	{
		String ret = "";
		int sz = inStr.length();
		for (int i = 0; i < sz; i++)
		{
			String strToSet = "";
			boolean flag = true;
			switch (inStr.charAt(i))
			{
			case '\"':
				strToSet = "quot";
				break;
			case '&':
				strToSet = "amp";
				break;
			case '\'':
				strToSet = "apos";
				break;
			case '<':
				strToSet = "lt";
				break;
			case '>':
				strToSet = "gt";
				break;
			case '|':
				strToSet = "brvbar";
				break;
			case '~':
				strToSet = "tilde";
				break;
			case '-':
				strToSet = "minus";
				break;			
			default:
				flag = false;
			}
			if (flag)
			{
				strToSet = "&" + strToSet;
			}
			ret.concat(strToSet);
		}

		return ret;
	}
	/**
	 * @param in -
	 *           string to replace all \' characters
	 * @return string that could be correct insert into the oracle table
	 */
	static public String makeOracleCompartableString(String in)
	{
		return in.replace("\'", "\'\'");
	}
}
