/******
 File:       TURF.java
 Author:     Somayeh Kafaie
 Date:       October, 2017
 Purpose:    This is the implementation of SURF based on:
 "Moore, J. H. and White, B. C. (2007). Tuning ReliefF for genome-wide genetic
 analysis. In Proceedings of the 5th European Conference on Evolutionary Computation,
 Machine Learning and Data Mining in Bioinformatics, EvoBIO’07, pages 166–175,
 Berlin, Heidelberg. Springer-Verlag."
 ******/

package Filtering;

import java.io.IOException;
import java.lang.String;
import java.util.*;



public class TURF {
	

	public static void TURF_func(ArrayList<Integer>[] myData, int[] phenoType, double percent_r,
			int m_t, int m_s, int snp_th, String inputFile, String outputFile, boolean useSURF, int cut_RF) throws IOException
	{
		int n_SNP = myData[0].size();//myData[1].length;
		int samples=myData.length;
		int t_loop = 1;
		while(n_SNP>snp_th) {
			
			double[] W;
			if (useSURF)
				W = SURF.SURF_func(myData, phenoType, m_s);
			else
				W = ReliefF.ReliefF_func(myData, phenoType, m_s, cut_RF);
		    int n_removed = (int) Math.ceil(n_SNP * percent_r);
		    //sorting W and having the indexes of sorted array
		    ArrayIndexComparator comparator = new ArrayIndexComparator(W);
		    Integer[] Indexes = comparator.createIndexArray();
		    Arrays.sort(Indexes, comparator);
		    // Now the sortedIndexes are in appropriate order.
		    Integer[] sortedIndexes= new Integer[n_removed];;
		    //if (n_removed>1) //Sort the indexes in the ascending order
		    //{ 
		    	for (int k=0;k<n_removed;k++)
		    		sortedIndexes[k] = Indexes[k];
		    	Arrays.sort(sortedIndexes, Collections.reverseOrder());
		    //}
		    //else
		    	//sortedIndexes[0] = Indexes[0];
		    for (int r=0;r<n_removed;r++)
	    	{
	    		int x = sortedIndexes[r];
	    		FileProcessing.headers.remove(x+6);
	    	}
		    for (int p=0;p<samples;p++)
		    {
		    	for (int r=0;r<n_removed;r++)
		    	{
		    		int x = sortedIndexes[r];
		    		myData[p].remove(x);
		    	}
		    	myData[p].trimToSize();
		    }
		    FileProcessing.log("TURF_loop= "+t_loop);
		    n_SNP = myData[0].size();//myData[1].length;
		    t_loop++;
		  }
		  //filteredData <- cbind(dataSet[, 1:6, with=FALSE],myData)
		  //fwrite(filteredData, outputFile, showProgress = FALSE, sep=" ", quote = FALSE)
		  //return(myData)	
	}
	

}
