package com.anemoi.tax.reconciliation;

public class StudyTable {
	
	int start ;
	int total = 10;
	int length ;
	
	public StudyTable(int startOfTable, int lengthOfTable)
		{
		this.length = lengthOfTable ;
		this.start = startOfTable ;
		}
	
	public void generate()
		{
		System.out.print("Study Table " + start + " to " + (start + total) + " \n\n  ");
		
		for(int row = 1 ; row <= length ; row++)
			{
			int value = start  * row  ;
			System.out.print("\n\n"+ row + " =>\t" + +value);
			for(int diff = 1 ; diff < total ; diff++)
				{
				value = value + row ;
				System.out.print("\t"+ value);
				}				
			}		
		}
	
	
	public static void main(String[] argv)
		{
		StudyTable	numtable = new StudyTable(11,12) ;		
		numtable.generate();
		}
	
}
