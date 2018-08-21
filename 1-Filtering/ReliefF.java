/******
 File:       ReliefF.java
 Author:     Somayeh Kafaie
 Date:       October, 2017
 Purpose:    This is the implementation of ReliefF based on:
 "Kira, K. and Rendell, L. A. (1992). A practical approach to feature selection.
 In D. Sleeman and P. Edwards, editors, Machine Learning Proceedings of the AAAI’92,
 pages 249 – 256. Morgan Kaufmann, San Francisco (CA)."
 ******/
package Filtering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReliefF {
	static int BIG_NUMBER = 10000000;

	  //Implementing SURF Function
	  public static double[] ReliefF_func(ArrayList<Integer>[] myData,int[] phenoType, int m_s, int cut_RF) throws IOException
	  {
		  int attributes=myData[0].size();//myData[1].length;//the number of SNPs
		  double[] W = new double[attributes]; 
		  //Arrays.fill(W, 0); //no need because java initialize integer arrays to 0
		  for (int i=0;i<m_s;i++) {
			    int selectedSample = i;
			    //Calculate the distance of selectedSample with others
				double[] scores = calculateDistance(selectedSample,myData); 
			    //sorting scores and having the indexes of sorted array
			    ArrayIndexComparator comparator = new ArrayIndexComparator(scores);
			    Integer[] Indexes = comparator.createIndexArray();
			    Arrays.sort(Indexes, comparator);
			    List<Integer> hits = new ArrayList<Integer>();
			    List<Integer> misses =  new ArrayList<Integer>();
			    for (int j=0;j<cut_RF;j++)
			    {
			    	int otherSample = Indexes[j];
			    	if (phenoType[selectedSample]==phenoType[otherSample])
			    		hits.add(otherSample); //hits
			    	else 
			    		misses.add(otherSample); //misses
			    }
			    
			    for (int a = 0; a<attributes;a++)
			    {
			    	for (Integer hitSample:hits)
			    		W[a] = W[a] - (attributeDifference(myData,a,selectedSample,hitSample)/(m_s * hits.size()));
			    	for (Integer missSample:misses)
			    		W[a] = W[a] + (attributeDifference(myData,a,selectedSample,missSample)/(m_s * misses.size()));
			    }
			    FileProcessing.log("ReliefF_loop= "+i);
			  }
			  return W; 
	  }
	//Calculating the "difference value" between the same attribute on two individuals
  //Function "diff" in paper nomenclature
  private static double attributeDifference(ArrayList<Integer>[] myData, int attribute, int sample1, int sample2)
  {
  	if (myData[sample1].get(attribute) == myData[sample2].get(attribute))
          return 0;
      else return 1;
  }

	//Calculating the distance between mySample and other samples
	private static double[] calculateDistance(int mySample, ArrayList<Integer>[] myData) throws IOException 
	{
	  FileProcessing.log("calculating distance for sample "+mySample);
	  int samples=myData.length;//myData[0].length; //the number of rows in myData
	  int attributes=myData[0].size();//myData[1].length;//the number of SNPs
	  double[] scores = new double[samples];
	  for (int i=0; i<samples;i++)
		  {
			  scores[i]=0;
			  for (int a=0; a<attributes; a++)
				  scores[i]+=attributeDifference(myData,a,mySample,i);
		  }
	  scores[mySample]=BIG_NUMBER;
	  FileProcessing.log("END of calculating distance!");
	  return scores;
	}
}
