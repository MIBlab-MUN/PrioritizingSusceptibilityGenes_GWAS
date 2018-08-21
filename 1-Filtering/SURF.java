/******
 File:       SURF.java
 Author:     Somayeh Kafaie
 Date:       October, 2017
 Purpose:    This is the implementation of SURF based on:
 "Greene, C. S., Penrod, N. M., Kiralis, J., and Moore, J. H. (2009). Spatially
 uniform ReliefF (SURF) for computationally-efficient filtering of gene-gene
 interactions. BioData Mining, 2(1), 5."
 ******/

package Filtering;

import java.io.IOException;
import java.util.*;

public class SURF {
	private static double T; //represents the neighborhoof of each sample
	 
	  //Implementing SURF Function
	  public static double[] SURF_func(ArrayList<Integer>[] myData,int[] phenoType, int m_s) throws IOException
	  {
		  int samples=myData.length;//myData[0].length; //the number of rows in myData
		  int attributes=myData[0].size();//myData[1].length;//the number of SNPs
		  double[][] scores = calculateDistance(myData); 
		  double[] W = new double[attributes]; 
		  //Arrays.fill(W, 0); //no need because java initialize integer arrays to 0
		  T = calculateT(scores);
		  for (int i=0;i<m_s;i++) {
			    int selectedSample = i;
			    List<Integer> hits = new ArrayList<Integer>();
			    List<Integer> misses =  new ArrayList<Integer>();
			    for (int j=0;j<samples;j++)
			    {
			    	if (scores[selectedSample][j]<T && scores[selectedSample][j]>0)
			    	{
			    		if (phenoType[selectedSample]==phenoType[j])
			    			hits.add(j); //hits
			    		
			    		else 
			    			misses.add(j); //misses
			    	}
			    }

			    for (int a = 0; a<attributes;a++)
			    {
			    	for (Integer hitSample:hits)
			    		W[a] = W[a] - (attributeDifference(myData,a,selectedSample,hitSample)/(m_s * hits.size()));
			    	for (Integer missSample:misses)
			    		W[a] = W[a] + (attributeDifference(myData,a,selectedSample,missSample)/(m_s * misses.size()));
			    }
			    FileProcessing.log("SURF_loop= "+i);
			  }
			  return W; 
	  }
	//Calculating the "difference value" between the same attribute on two individuals
    //Function "diff" in paper nomenclature
    private static double attributeDifference(ArrayList<Integer>[] myData, int attribute, int sample1, int sample2)
    {
        //if (myData[sample1][attribute] == myData[sample2][attribute])
    	if (myData[sample1].get(attribute) == myData[sample2].get(attribute))
            return 0;
        else return 1;
    }

	
	//Calculating the distance between any two samples
	private static double[][] calculateDistance(ArrayList<Integer>[] myData) throws IOException 
	{
	  FileProcessing.log("calculating distance...");
	  int samples=myData.length;//myData[0].length; //the number of rows in myData
	  int attributes=myData[0].size();//myData[1].length;//the number of SNPs
	  double[][] scores = new double[samples][samples];
	  for (int i=0; i<samples;i++)
		  for (int j=0;j<samples;j++)
		  {
			  scores[i][j]=0;
			  if (i<j)
			  {
				  for (int a=0; a<attributes; a++)
					  scores[i][j]+=attributeDifference(myData,a,i,j);
			  }
			  else if (i>j)
				  scores[i][j]=scores[j][i];
		  }
	  FileProcessing.log("END of calculating distance!");
	  return scores;
	}
	
	//Calculating the average value T
	private static double calculateT(double[][] scores)
	{
		double T=0;
		int samples = scores[0].length;
		for (int i=0;i<samples;i++)
			for (int j=0;j<samples;j++)
				T+=scores[i][j];
		T = T / (Math.pow(samples, 2) - samples);
		return T;
	}
}
