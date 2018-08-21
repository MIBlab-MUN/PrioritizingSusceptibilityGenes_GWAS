/*
 * File:       InfoTheoMeasure.java
 * Author:     Ting Hu and Somayeh Kafaie
 * Date:       November, 2017
 * Purpose:    To calculate information gain
 */

import java.io.IOException;
import java.util.ArrayList;

public class InfoTheoMeasure
{
	final static int BASE =2;
		
	//******** H(1)  //SOMAYEH: H(C) and randvar denotes phenotype
	public static double singleEntropy (ArrayList<Integer> randVar)
	{
		double h=0;
		
		ArrayList<Integer> uniqueElement = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementCount = new ArrayList<Integer>();
		
		int varSize = randVar.size();
		
		for(int i=0;i<varSize;i++)
		{
			int instance = randVar.get(i);
			if(!uniqueElement.contains(instance))
			{
				uniqueElement.add(instance);
				uniqueElementCount.add(1);
			}
			else 
			{
				int index = uniqueElement.indexOf(instance);
				int count = uniqueElementCount.get(index);				
				count++;
				uniqueElementCount.set(index, count);
			}				
		}
		
		int numberOfUniqueElement = uniqueElement.size();
		double[] frequency = new double[numberOfUniqueElement];
		//SOMAYEH: H(C)=SUM[f(c)*log(1/f(c))]=SUM[f(c)* ( log 1 - log(f(c)) )] = SUM[-f(c)*log(f(c))]
		for(int j=0;j<numberOfUniqueElement;j++)
		{
			frequency[j]=(double) uniqueElementCount.get(j)/varSize;
			h += - frequency[j]* (Math.log(frequency[j])/Math.log(BASE)); //decimal to binary log
		}
		
		if(uniqueElement.size()!=uniqueElementCount.size())
			System.out.println("Wrong counting the unique elements!!!");
			
//		System.out.println(uniqueElement.toString());
//		System.out.println(uniqueElementCount.toString());		
		return h;
	}
	
	
	//******** H(1,2) //SOMAYEH: H(C|A) and randVar1 denotes phenotype and randVar2 denotes their value for a given SNP
	public static double jointEntropy (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2)  
	{
		double hh=0;
		if (randVar1.size()!= randVar2.size())
			System.out.println("2 variables have different sample sizes!!!");
		
		ArrayList<Integer> uniqueElementVar1 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar2 = new ArrayList<Integer>();
		
		for (int i=0;i<randVar1.size();i++)
		{
			if(!uniqueElementVar1.contains(randVar1.get(i)))
				uniqueElementVar1.add(randVar1.get(i));
	
			if(!uniqueElementVar2.contains(randVar2.get(i)))
				uniqueElementVar2.add(randVar2.get(i));
		}
		
		int spaceVar1 = uniqueElementVar1.size();
		int spaceVar2 = uniqueElementVar2.size();
		
		int[][] jointFrequency = new int[spaceVar1][spaceVar2];
		for(int i=0;i<spaceVar1;i++)
			for(int j=0;j<spaceVar2;j++)
			{
				jointFrequency[i][j]=0;
			}
		
		for(int i=0;i<randVar1.size();i++)
			{
				int elementIndex1 = uniqueElementVar1.indexOf(randVar1.get(i));
				int elementIndex2 = uniqueElementVar2.indexOf(randVar2.get(i));
				
				jointFrequency[elementIndex1][elementIndex2]++;
			}
		
		for(int i=0;i<spaceVar1;i++)
			for(int j=0;j<spaceVar2;j++)
			{
				double prob = (double) jointFrequency[i][j] / (double)(randVar1.size());
				if (prob!=0)
					hh += - prob * (Math.log(prob) / Math.log(BASE));  //???what about p(a)
				else
					hh += 0;
			}			
		return hh;
	}
	
	
	//******** H(1,2,3)
	public static double joint3Entropy (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3) 
	{
		double joint3h=0;
		if (randVar1.size()!= randVar2.size() || randVar1.size()!= randVar3.size() || randVar2.size()!= randVar3.size())
			System.out.println("3 variables have different sample sizes!!!");
				
		ArrayList<Integer> uniqueElementVar1 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar2 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar3 = new ArrayList<Integer>();
		
		for (int i=0;i<randVar1.size();i++)
		{
			if(!uniqueElementVar1.contains(randVar1.get(i)))
				uniqueElementVar1.add(randVar1.get(i));
		
			if(!uniqueElementVar2.contains(randVar2.get(i)))
				uniqueElementVar2.add(randVar2.get(i));
			
			if(!uniqueElementVar3.contains(randVar3.get(i)))
				uniqueElementVar3.add(randVar3.get(i));
		}
		
		int spaceVar1 = uniqueElementVar1.size();
		int spaceVar2 = uniqueElementVar2.size();
		int spaceVar3 = uniqueElementVar3.size();
		//System.out.println("s1 = "+ spaceVar1+ "  s2 = "+ spaceVar2+ "  s3 = "+ spaceVar3);

		
		int[][][] jointFrequency = new int[spaceVar1][spaceVar2][spaceVar3];
		for(int i=0;i<spaceVar1;i++)
			for(int j=0;j<spaceVar2;j++)
				for(int k=0;k<spaceVar3;k++)
			{
				jointFrequency[i][j][k]=0;
			}
				
		for(int i=0;i<randVar1.size();i++)
		{
			int elementIndex1 = uniqueElementVar1.indexOf(randVar1.get(i));
			int elementIndex2 = uniqueElementVar2.indexOf(randVar2.get(i));
			int elementIndex3 = uniqueElementVar3.indexOf(randVar3.get(i));
			
			jointFrequency[elementIndex1][elementIndex2][elementIndex3]++;
		}
	
		for(int i=0;i<spaceVar1;i++)
			for(int j=0;j<spaceVar2;j++)
				for(int k=0; k<spaceVar3; k++)
				{
					double prob = (double) jointFrequency[i][j][k] / (double)(randVar1.size());
					if (prob!=0)
						joint3h += (- prob) * (Math.log(prob) / Math.log(BASE)); 
					else
						joint3h += 0;
				}	
		return joint3h;
	}
	
	
	//******** H(1,2,3,4)
	public static double joint4Entropy (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3, ArrayList<Integer> randVar4)
	{
		double joint4h=0;
		if (randVar1.size()!= randVar2.size() || randVar1.size()!= randVar4.size() || randVar2.size()!= randVar3.size())
			System.out.println("4 variables have different sample sizes!!!");
		
		ArrayList<Integer> uniqueElementVar1 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar2 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar3 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar4 = new ArrayList<Integer>();
		
		for (int i=0;i<randVar1.size();i++)
		{
			if(!uniqueElementVar1.contains(randVar1.get(i)))
				uniqueElementVar1.add(randVar1.get(i));
		
			if(!uniqueElementVar2.contains(randVar2.get(i)))
				uniqueElementVar2.add(randVar2.get(i));
			
			if(!uniqueElementVar3.contains(randVar3.get(i)))
				uniqueElementVar3.add(randVar3.get(i));
			
			if(!uniqueElementVar4.contains(randVar4.get(i)))
				uniqueElementVar4.add(randVar4.get(i));
		}
		
		int spaceVar1 = uniqueElementVar1.size();
		int spaceVar2 = uniqueElementVar2.size();
		int spaceVar3 = uniqueElementVar3.size();
		int spaceVar4 = uniqueElementVar4.size();

		int[][][][] jointFrequency = new int[spaceVar1][spaceVar2][spaceVar3][spaceVar4];
		for(int i=0;i<spaceVar1;i++)
			for(int j=0;j<spaceVar2;j++)
				for(int k=0;k<spaceVar3;k++)
					for(int l=0;l<spaceVar4;l++)
					{
						jointFrequency[i][j][k][l]=0;
					}
				
		for(int i=0;i<randVar1.size();i++)
		{
			int elementIndex1 = uniqueElementVar1.indexOf(randVar1.get(i));
			int elementIndex2 = uniqueElementVar2.indexOf(randVar2.get(i));
			int elementIndex3 = uniqueElementVar3.indexOf(randVar3.get(i));
			int elementIndex4 = uniqueElementVar4.indexOf(randVar4.get(i));
			
			jointFrequency[elementIndex1][elementIndex2][elementIndex3][elementIndex4]++;
		}
	
		for(int i=0;i<spaceVar1;i++)
			for(int j=0;j<spaceVar2;j++)
				for(int k=0; k<spaceVar3; k++)
					for (int l=0;l<spaceVar4;l++)
					{
						double prob = (double) jointFrequency[i][j][k][l] / (double)(randVar1.size());
						if (prob!=0)
							joint4h += - prob * (Math.log(prob) / Math.log(BASE)); 
						else
							joint4h += 0;
					}				
		return joint4h;
	}
	
	
	//******** H(1,2,3,4,5)
	public static double joint5Entropy (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3, ArrayList<Integer> randVar4, ArrayList<Integer> randVar5)
	{
		double joint5h=0;
		if (randVar1.size()!= randVar2.size() || randVar1.size()!= randVar4.size() || randVar2.size()!= randVar3.size() || randVar1.size()!= randVar5.size() )
			System.out.println("5 variables have different sample sizes!!!");
		
		ArrayList<Integer> uniqueElementVar1 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar2 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar3 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar4 = new ArrayList<Integer>();
		ArrayList<Integer> uniqueElementVar5 = new ArrayList<Integer>();
		
		for (int i=0;i<randVar1.size();i++)
		{
			if(!uniqueElementVar1.contains(randVar1.get(i)))
				uniqueElementVar1.add(randVar1.get(i));
		
			if(!uniqueElementVar2.contains(randVar2.get(i)))
				uniqueElementVar2.add(randVar2.get(i));
			
			if(!uniqueElementVar3.contains(randVar3.get(i)))
				uniqueElementVar3.add(randVar3.get(i));
			
			if(!uniqueElementVar4.contains(randVar4.get(i)))
				uniqueElementVar4.add(randVar4.get(i));
			
			if(!uniqueElementVar5.contains(randVar5.get(i)))
				uniqueElementVar5.add(randVar5.get(i));
		}
		
		int spaceVar1 = uniqueElementVar1.size();
		int spaceVar2 = uniqueElementVar2.size();
		int spaceVar3 = uniqueElementVar3.size();
		int spaceVar4 = uniqueElementVar4.size();
		int spaceVar5 = uniqueElementVar5.size();

		int[][][][][] jointFrequency = new int[spaceVar1][spaceVar2][spaceVar3][spaceVar4][spaceVar5];
		for(int i=0;i<spaceVar1;i++)
			for(int j=0;j<spaceVar2;j++)
				for(int k=0;k<spaceVar3;k++)
					for(int l=0;l<spaceVar4;l++)
						for(int m=0;m<spaceVar5;m++)
						{
							jointFrequency[i][j][k][l][m]=0;
						}
				
		for(int i=0;i<randVar1.size();i++)
		{
			int elementIndex1 = uniqueElementVar1.indexOf(randVar1.get(i));
			int elementIndex2 = uniqueElementVar2.indexOf(randVar2.get(i));
			int elementIndex3 = uniqueElementVar3.indexOf(randVar3.get(i));
			int elementIndex4 = uniqueElementVar4.indexOf(randVar4.get(i));
			int elementIndex5 = uniqueElementVar5.indexOf(randVar5.get(i));
			
			jointFrequency[elementIndex1][elementIndex2][elementIndex3][elementIndex4][elementIndex5]++;
		}
	
		for(int i=0;i<spaceVar1;i++)
			for(int j=0;j<spaceVar2;j++)
				for(int k=0; k<spaceVar3; k++)
					for (int l=0;l<spaceVar4;l++)
						for(int m=0;m<spaceVar5;m++)
						{
							double prob = (double) jointFrequency[i][j][k][l][m] / (double)(randVar1.size());
							if (prob!=0)
								joint5h += - prob * (Math.log(prob) / Math.log(BASE)); 
							else
								joint5h += 0;
						}				
			return joint5h;
	}
	
	
	
	
	//******** I(1;2)
	public static double mutualInfo (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2)
	{
		double mutInfo = singleEntropy(randVar1) + singleEntropy(randVar2) - jointEntropy (randVar1,randVar2);		
		return mutInfo;
	}
	
	
	//******** I(1,2;3)
	public static double mutualInfo21 (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3) throws IOException  
	{
		double ret = jointEntropy(randVar1,randVar2) + singleEntropy(randVar3) - joint3Entropy(randVar1,randVar2,randVar3);		
		//FileProcessing.log("joint3Entropy(randVar1,randVar2,randVar3) = "+ joint3Entropy(randVar1,randVar2,randVar3));
		return ret;
	}
	
	
	//******** I(1,2,3;4)
	public static double mutualInfo31 (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3, ArrayList<Integer> randVar4)  
	{
		double ret = joint3Entropy(randVar1,randVar2,randVar3) + singleEntropy(randVar4) - joint4Entropy(randVar1,randVar2,randVar3,randVar4);		
		return ret;
	}
	
	//******** I(1,2,3,4;5)
	public static double mutualInfo41 (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3, ArrayList<Integer> randVar4, ArrayList<Integer> randVar5)  
	{
		double ret = joint4Entropy(randVar1,randVar2,randVar3,randVar4) + singleEntropy(randVar5) - joint5Entropy(randVar1,randVar2,randVar3,randVar4,randVar5);		
		return ret;
	}
	
	
	//******** IG(1;2;3)
	public static double infoGain (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3) throws IOException
	{
		double infoG = mutualInfo21(randVar1,randVar2,randVar3) - mutualInfo(randVar1,randVar3) - mutualInfo(randVar2,randVar3);
						
//		double infoG	- singleEntropy(randVar1) - singleEntropy(randVar2) - singleEntropy(randVar3)
//						+ jointEntropy(randVar1,randVar2) + jointEntropy(randVar1,randVar3) + jointEntropy(randVar2,randVar3)
//						- joint3Entropy(randVar1,randVar2,randVar3);
		//FileProcessing.log("mutualInfo21 = "+mutualInfo21(randVar1,randVar2,randVar3));
		return infoG;
	}
	
	
	//******** IG(1;2;3;4) DA version
	public static double infoGain3DA (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3, ArrayList<Integer> randVar4) throws IOException
	{	
		double ret = mutualInfo31(randVar1,randVar2,randVar3,randVar4);
		
		double partition1 = mutualInfo(randVar1,randVar4) + mutualInfo21(randVar2,randVar3,randVar4);
		double partition2 = mutualInfo(randVar2,randVar4) + mutualInfo21(randVar1,randVar3,randVar4);
		double partition3 = mutualInfo(randVar3,randVar4) + mutualInfo21(randVar1,randVar2,randVar4);
		double partition4 = mutualInfo(randVar1,randVar4) + mutualInfo(randVar2,randVar4) + mutualInfo(randVar3,randVar4);
		
		double max = partition1;
		if(partition2 > max)
			max = partition2;
		if(partition3 > max)
			max = partition3;
		if(partition4 > max)
			max = partition4;
		
		ret = ret - max;		
		return ret;
	}
	
	
	//******** IG(1;2;3;4) GC version
	public static double infoGain3GC (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3, ArrayList<Integer> randVar4) throws IOException
	{	
		double ret = mutualInfo31(randVar1,randVar2,randVar3,randVar4)
					-infoGain(randVar1,randVar2,randVar4) - infoGain(randVar1,randVar3,randVar4) - infoGain(randVar2,randVar3,randVar4)
					- mutualInfo(randVar1,randVar4) - mutualInfo(randVar2,randVar4) - mutualInfo(randVar3,randVar4);		
		return ret;
	}
	
	
	//******** IG(1;2;3;4) JK version
	public static double infoGain3JK (ArrayList<Integer> randVar1, ArrayList<Integer> randVar2, ArrayList<Integer> randVar3, ArrayList<Integer> randVar4) throws IOException
	{	
		double ret = mutualInfo31(randVar1,randVar2,randVar3,randVar4)
					- mutualInfo(randVar1,randVar4) - mutualInfo(randVar2,randVar4) - mutualInfo(randVar3,randVar4);		
		double ig12 = infoGain(randVar1,randVar2,randVar4);
		double ig13 = infoGain(randVar1,randVar3,randVar4);
		double ig23 = infoGain(randVar2,randVar3,randVar4);
		if(ig12>0)
			ret = ret -ig12;
		if(ig13>0)
			ret = ret -ig13;
		if(ig23>0)
			ret = ret -ig23;
		return ret;
	}
	
	
	
	
	
}
