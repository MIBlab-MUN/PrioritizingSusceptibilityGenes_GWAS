/*
 * File:       MainNetPermutations.java
 * Author:     Somayeh Kafaie
 * Date:       November, 2017
 * Purpose:    For all SNPs selected after filtering, we calculate the pairwise information gain
 *                using the following java code. We also, generate 1000 permutations of the dataset
 *                by shuffling phenotypes and assigning them to the samples and calculate the
 *                pair-wise information gain for all SNPs of every permutated dataset as well.
 *                In fact, to get the result faster, we ran 100 different instances of this code,
 *                where each generates the IG between all SNPs as well as the IG for
 *                10 (i.e., permutationNum) permutations and calculates P (P here is a short number
 *                counting the number of permutations with value greater than or equal to the value
 *                of the IG for our dataset). Also, permutationIndex represents the offset of permutations,
 *                where for permutationIndex=i we create 10 permutations with
 *                seeds = [i*permutationNum , (i+1)*permutationNum)
 */

import java.io.IOException;
import java.util.*;

public class MainNetPermutations {

	static String inputFile = "mergedCRC_Filtered_SURF.txt";
	static String InfoGainFile = "InformationGain.txt";
	static String pCountFile = "pCount.txt";
	static int personalEntries = 6;
	static int permutationNum = 10;
	static int permutationIndex=0;
	//static double significance_th = 0.01;

	public static void main(String[] args) throws IOException
	{	
		if (args.length>0) 
			permutationIndex = Integer.parseInt(args[0]);
		if (args.length>1)
			permutationNum = Integer.parseInt(args[1]);
		if (args.length>2)
			inputFile = args[2];
		
		FileProcessing.logFile="log_"+permutationIndex+".txt";
		InfoGainFile = "InformationGain_"+permutationIndex+".txt";
		pCountFile = "pCount_"+permutationIndex+".txt";
		
		//Reading the dataset from the file
		FileProcessing.log("Start: reading the dataset!");
		ArrayList<Integer>[] myData =FileProcessing.readFile(inputFile, personalEntries);
		FileProcessing.log("End: reading the dataset!");
		
		//Calculation information Gain
		FileProcessing.log("Start: calculating IG for real dataset!");
		double[][] IG = calculateInformationGain(myData, FileProcessing.phenoType);
		FileProcessing.log("End: calculating IG for real dataset!!");
		
		//generating permutation dataSets and calculating information for them
		double[][][] IG_permute = new double[permutationNum][][];
		int offset = permutationIndex * permutationNum;
		for (int i=0;i<permutationNum;i++)
		{
			FileProcessing.log("Start: Shuffling phenotypes for permutation_"+(offset+i));
			ArrayList<Integer> permutation_pheno = new ArrayList<Integer>(FileProcessing.phenoType);		
			Collections.shuffle(permutation_pheno, new Random(offset+i));
			FileProcessing.log("End: Shuffling phenotypes for permutation_"+(offset+i));
			
			FileProcessing.log("Start: calculating IG_permute["+(offset+i)+"]!");
			IG_permute[i] = calculateInformationGain(myData, permutation_pheno);
			FileProcessing.log("End: calculating IG_permute["+(offset+i)+"]!");
		}
		
		if (permutationIndex==0)
		{
			FileProcessing.log("Start: writing IGs!");
			FileProcessing.writeInformationGain(InfoGainFile, IG, personalEntries);
			FileProcessing.log("End: writing IGs!");
		}
		
		//Assessing the significant level of each pair IG
		FileProcessing.log("Start: calculating P!");
		int[][] p = countSignificance(IG, IG_permute);
		FileProcessing.log("End: calculating P!");
		
		FileProcessing.log("Start: writing P count!");
		FileProcessing.writePCounts(pCountFile, p, personalEntries);
		FileProcessing.log("End: writing P count!");
	}
	
	//Calculate the interaction effect between any two SNPs
	private static double[][] calculateInformationGain(ArrayList<Integer>[] myData, ArrayList<Integer> phenoType) throws IOException
	{
		int SNPs = myData.length;
		double[][] IG_temp = new double[SNPs][SNPs];
		for (int i=0;i<SNPs;i++)
			for (int j=0;j<SNPs;j++)
				IG_temp[i][j] = InfoTheoMeasure.infoGain(myData[i], myData[j], phenoType);
		return IG_temp;
	}
	
	//Assessing the significant level of each pair IG
	private static int[][] countSignificance(double[][] IG, double[][][] IG_permute)
	{	
		int SNPs = IG.length;
		if (SNPs!=IG[0].length)
			return null;
        int[][] p=new int[SNPs][SNPs];
        for (int i=0; i<SNPs;i++)
            for (int j=0;j<SNPs;j++)
            {
                int n=0;
                for (int k=0;k<IG_permute.length;k++)
                    if (IG_permute[k][i][j]>=IG[i][j])
                        n++;
                p[i][j]=n;
            }
        
        return p;
    }	
}
