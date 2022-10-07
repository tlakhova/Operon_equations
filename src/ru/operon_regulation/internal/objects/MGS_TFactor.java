package ru.operon_regulation.internal.objects;

import java.util.Arrays;

import ru.operon_regulation.internal.interfaces.IMGS_TFactor;

// This class obtained information from input file about TF name, its regulation type and left and right TFBS positions
// The class needs for check of TFBS position overlap for each TF
// Checking site overlaps is done in pairs each time

public class MGS_TFactor extends MGS_info implements IMGS_TFactor
{
	private String[] TFsynonyms;
	private String TFtype;
	private int leftPosition;
	private int rightPosition;
	/**
	 * @param TFname - name of the TFactor
	 * @param info
	 * @param type
	 * @param leftPos - is positive or 0
	 * @param rightPos - is positive or 0
	 * @param synonyms
	 */
	public MGS_TFactor(String TFname, String info, String type,int leftPos,int rightPos, String[] synonyms)
	{
		super(TFname, info);
		TFsynonyms = Arrays.copyOf(synonyms, synonyms.length);
		TFtype = type;
			
		leftPosition= Math.abs(leftPos);
		rightPosition= Math.abs(rightPos);
		if (rightPosition<leftPosition)
		{
			rightPos=leftPosition;
			leftPosition=rightPosition;
			rightPosition=rightPos;
		}
	}

	@Override
	public int getLeftPosition()
	{
		return leftPosition;
	}

	@Override
	public int getRightPosition()
	{	
		return rightPosition;
	}

	@Override
	public String[] getSynonyms()
	{
		return TFsynonyms;
	}

	@Override
	public String getType()
	{
		return TFtype;
	}

	/*
	 * there could be only next situations:
	 *    |###|                     |###|
	 * 1) -------------  2) ------------- 
	 *          |****|      |****|
	 *          
	 *       |###|            |###|
	 * 3) -------------  4) ------------- 
	 *         |*****|      |********|
	 *         
	 *       |#######|          |####|
	 * 5) -------------  6) ------------- 
	 *         |***|         |*****|
	 *   
	 * only 1 and 2 has no interactions, thats why we search them.  
	 *         
	 * */
	public boolean isInterceptWith(IMGS_TFactor tf)
	{
		boolean ret = true;
		if ( 
				 ((this.leftPosition==0)&&(this.rightPosition==0))
			  ||((tf.getLeftPosition()==0)&&(tf.getRightPosition()==0))
			)
		{
			ret = false;
		}
		else
		{
			if ( 
					 (this.leftPosition > tf.getRightPosition())
				  ||(this.rightPosition < tf.getLeftPosition())
				 )
			{
				ret = false;
			}
		}
		return ret;
	}
	
	

}
