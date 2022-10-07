package ru.operon_regulation.nsc.bionet.MGS.utilites.mathtree;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * it class used to parse string with equation (a=s+d^r*h) into the tree
 */
public class SimpleMathTree
{
	private String nodeName = "";

	private char nodePrefix = ' '; // '-' or '/' for Plus and multiplier

	private char operation;

	private boolean isFormula = true;

	private ArrayList<SimpleMathTree> children;

	private SimpleMathTree parent;

	private ArrayList<String> errorList;

	private static final String supportedOperations = "=+-/*^";

	private static final String supportedBreaks = "()";

	private static final String supportedSeparators = supportedBreaks
			+ supportedOperations;

	/**
	 * It method make changes only for leaves of tree. 
	 * @param NewName - the new name for this node
	 * @return false if it was not the leaf (if was formula) 
	 */
	public boolean modifyName(String NewName)
	{
		NewName.trim();
		boolean ret=true;
		if(this.isFormula()||(NewName.equals("")))
		{
			ret=false;
		}
		if(ret)
		{
			this.nodeName=NewName;
		}
		return ret;
	}
	
	/**
	 * @param mathString -
	 *            equation like (a=s+d^r*h). it's parsed it and return object.
	 * @param startPosition -
	 *            position of mathString in base formula. it need's for
	 *            generation errors.
	 */
	public SimpleMathTree(String mathString, int startPosition,char inNodePrefix,
			SimpleMathTree prnt)
	{
		super();
		nodeName = "";
		nodePrefix = ' ';
		operation = ' ';
		isFormula = true;
		this.parent = prnt;
		children = new ArrayList<SimpleMathTree>();
		errorList = new ArrayList<String>();
		
		mathString = mathString.trim();

		String[] res = checkEqualSymbol(mathString, 0);
		if (res.length != 1)
		{
			this.isFormula = true;
			this.operation = '=';
			this.nodePrefix = inNodePrefix;
//			System.out.println("==equal");
//			System.out.println(mathString);
//			System.out.println(res.length);
			for (String str : res)
			{
//				System.out.print("|" + str);
				if (!str.equals(""))
				{
					SimpleMathTree newChild=new SimpleMathTree(str, 0,' ', this);
					this.addChild(newChild);
				}
			}
//			System.out.println();
		} else
		{
			res = getElementsByOperation(mathString, 0, 0);
			if (res.length != 1)
			{
				this.isFormula = true;
				this.operation = '+';
				this.nodePrefix = inNodePrefix; 
//				System.out.println("==sum");
//				System.out.println(mathString);
//				System.out.println(res.length);
				for (String str : res)
				{
//					System.out.print("|" + str);
					if (!str.equals(""))
					{
						char prefix=' ';
						if(supportedOperations.indexOf(str.charAt(0))!=(-1))
						{
							prefix = str.charAt(0);
							str=str.substring(1);
						}
						SimpleMathTree newChild=new SimpleMathTree(str, 0,prefix,this);
						this.addChild(newChild);
					}
				}
//				System.out.println();
			} else
			{
				res = getElementsByOperation(mathString, 0, 1);
				if (res.length != 1)
				{
					this.isFormula = true;
					this.operation = '*';
					this.nodePrefix = inNodePrefix;
//					System.out.println("==mult");
//					System.out.println(mathString);
//					System.out.println(res.length);
					for (String str : res)
					{
//						System.out.print("|" + str);
						if (!str.equals(""))
						{
							char prefix=' ';
							if(supportedOperations.indexOf(str.charAt(0))!=(-1))
							{
								prefix = str.charAt(0);
								str=str.substring(1);
							}
							SimpleMathTree newChild=new SimpleMathTree(str, 0,prefix, this);
							this.addChild(newChild);
						}
					}
//					System.out.println();
				} else
				{
					res = getElementsByOperation(mathString, 0, 2);

					if (res.length != 1)
					{
						this.isFormula = true;
						this.operation = '^';
						this.nodePrefix=inNodePrefix;
//						System.out.println("==pow");
//						System.out.println(mathString);
//						System.out.println(res.length);
						for (String str : res)
						{
//							System.out.print("|" + str);
							if (!str.equals(""))
							{
								char prefix=' ';
								if(supportedOperations.indexOf(str.charAt(0))!=(-1))
								{
									prefix = str.charAt(0);
									str=str.substring(1);
								}
								SimpleMathTree newChild=new SimpleMathTree(str, 0,prefix, this);

								this.addChild(newChild);
							}
						}
//						System.out.println();
					} else
					{
						if (!mathString.equals(""))
						{
							if (mathString.length() > 0)
							{
//								if (supportedOperations.indexOf(mathString
//										.charAt(0)) >= 0)
//								{
//									this.nodePrefix = mathString.charAt(0);
//									mathString = mathString.substring(1);
//								}
								if (mathString.length() == 0)
								{
									// error
									parent.children.remove(this);
									this.parent = null;
								} else if (mathString.charAt(0) == '(')
								{
									// it's Formula
									if (mathString
											.charAt(mathString.length() - 1) == ')')
									{
										mathString = mathString.substring(1,
												mathString.length() - 1);
									}
									SimpleMathTree newSubTree = new SimpleMathTree(
											mathString, 0,inNodePrefix, parent);
//									newSubTree.nodePrefix = this
//											.getNodePrefix();
									parent.addChild(newSubTree);
									parent.children.remove(this);
									this.parent = null;
								} else
								{
									this.nodeName = mathString.trim();
									this.isFormula = false;
									this.nodePrefix = inNodePrefix;
								}
							}
						}
					}
				}
			}
		}
	}

	public boolean hasChild()
	{
		return (children.size() > 0);
	}

	/**
	 * @return true if prefix one of "/-"
	 */
	public boolean isNegativePrefix()
	{
		return ("/-".indexOf(this.nodePrefix) !=(-1));
	}

	public String getNodeName()
	{
		return nodeName;
	}

	public char getNodePrefix()
	{
		return nodePrefix;
	}

	public char getOperation()
	{
		return operation;
	}

	public boolean isFormula()
	{
		return isFormula;
	}

	public SimpleMathTree[] getChildren()
	{
		return children.toArray(new SimpleMathTree[0]);
	}

	public SimpleMathTree getParent()
	{
		return parent;
	}

	public void addChild(SimpleMathTree chld)
	{
		if ((chld.getChildren().length > 0)
				|| ((chld.getChildren().length == 0) && (!chld.getNodeName()
						.equals(""))))
		{
			this.children.add(chld);
			chld.parent = this;
		}
	}

	public ArrayList<String> getErrorList()
	{
		return errorList;
	}

	private String[] checkEqualSymbol(String math, int pos)
	{

		int eqPos = math.indexOf("=");
		if (eqPos > 0)
		{
			String[] ret = new String[2];
			ret[0] = math.substring(0, eqPos);
			ret[1] = math.substring(eqPos + 1);
			return ret;
		} else
		{
			String[] ret = new String[1];
			ret[0] = math;
			return ret;
		}
	}

	/**
	 * @param math -
	 *            formula to parse
	 * @param pos -
	 *            position of formula in main text
	 * @param operationFlag -
	 *            0-sum, 1-multi, 2-power
	 * @return
	 */
	private String[] getElementsByOperation(String math, int pos,
			int operationFlag)
	{
		ArrayList<String> ret = new ArrayList<String>();
		int symbolCounter = 0;
		int borderCounter = 0;
		boolean startWord = true;
		int startPos = 0;
		int finishPos = 0;
		boolean havePow = false;
		while ((symbolCounter < math.length()) && (borderCounter >= 0))
		{
			switch (math.charAt(symbolCounter))
			{
				case '(':
					borderCounter++;
					break;
				case ')':
					borderCounter--;
					break;
			}
			boolean makeParseFlag = false;
			switch (operationFlag)
			{
				case 0:
				{
					switch (math.charAt(symbolCounter))
					{
						case '+':
						case '-':
							makeParseFlag = true;
							break;
					}
					break;
				}
				case 1:
				{
					switch (math.charAt(symbolCounter))
					{
						case '*':
						case '/':
							makeParseFlag = true;
							break;
					}
					break;
				}
				case 2:
				{
					switch (math.charAt(symbolCounter))
					{
						case '^':
							if ((!havePow)&&(borderCounter==0))
							{
								makeParseFlag = true;
								havePow = true;
							}
							break;
					}
					break;
				}
			}
			if (makeParseFlag)
			{
				makeParseFlag = false;
				if (borderCounter == 0)
				{
					finishPos = symbolCounter;
					String elem = math.substring(startPos, finishPos);
					if (!elem.equals(""))
					{
						ret.add(elem);
					}
					startPos = finishPos;
				}
			}
			symbolCounter++;
			if (symbolCounter == math.length())
			{
				if (borderCounter == 0)
				{
					finishPos = symbolCounter;
					ret.add(math.substring(startPos, finishPos));
				}
			}
		}
		return ret.toArray(new String[0]);
	}

	/**
	 * @param math -
	 *            string which should be not start form symbol "(" or ")"
	 * @return array[2]. <br>
	 *         second is the counter of borders. <br>
	 *         <b>!!!</b> if it more then "0" it means that there not enough
	 *         borders. <br>
	 *         normal value of second is "0". <br>
	 *         first is the end position of the substring for parameter "math"
	 *         which start from first symbol and end by last symbol of this
	 *         level. <br>
	 *         for example: for string "a+b*(c-b)+dd)+jjj(...)..." it return
	 *         position of last symbol for substring "a+b*(c-b)+dd", means 12
	 */
	private int[] getBorderValue(String math)
	{
		int symbolCounter = 0;
		int borderCounter = 0;
		while ((symbolCounter != math.length()) && (borderCounter > 0))
		{
			switch (math.charAt(symbolCounter))
			{
				case '(':
					borderCounter++;
					break;
				case ')':
					borderCounter--;
					break;
			}
			symbolCounter++;
		}
		int[] ret = new int[2];
		ret[0] = symbolCounter;
		ret[1] = borderCounter;
		return ret;
	}

	@Override
	public String toString()
	{
		return this.getAsString();
	}

	public String getAsString()
	{
		String ret = "";
		if (!this.hasChild())
		{
			ret = this.nodeName;
		} else
		{
			boolean isFirst = true;
			for (SimpleMathTree child : this.children)
			{
				if (isFirst)
				{
					isFirst = false;
					ret += (child.isNegativePrefix()) ? child.getNodePrefix()
							: "";
				} else
				{
					ret += (child.isNegativePrefix()) ? child.getNodePrefix()
							: this.getOperation();
				}

				if (child.isFormula())
				{
					ret += "(" + child.toString() + ")";
				} else
				{
					ret += child.getNodeName();
				}
			}
		}
		return ret;
	}
}
