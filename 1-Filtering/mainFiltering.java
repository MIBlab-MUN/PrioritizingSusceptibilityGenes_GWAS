/******
File:       mainFiltering.java
Author:     Somayeh Kafaie
Date:       October, 2017
Purpose:    This program receives genome information of samples froma file (inputFile) in the following format
            and returns the result in a file (outputFile) with a similar format including only selected SNPs (instead of all of them).
            - inputFileFormat:
            It is a table where each row represents a sample and the first 6 columns are personal information
            of that sample and the rest of the columns are SNPs, where the column labels reflect the snp name (e.g. snp1) with the name of the minor allele appended (i.e. snp1_2 in the first instance, as 2 is the minor allele) for the additive component.  Assuming A is the minor allele, will recode genotypes as follows:
                SNP       SNP_A
                ---       -----
                A A   ->    0
                A T   ->    1
                T T   ->    2
                0 0   ->   NA
 This code is able to use (ReliefF + TURF) or (SURF + TURF) for filtering.
******/
package Filtering;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class mainFiltering {
	static boolean useSURF = false;
	static int samples = 8;//1098;
	static String inputFile = "test.txt";//"mergedCRC_Imputation.txt";
	static String outputFile = "out.txt";//"mergedCRC_Filtered.txt";
	static double percent_r = 0.01;
	static int m_t = 2;//300;
	static int m_s;// = samples; 
	static int snp_th = 8;//10000;
	static int cut_RF = 5; //10
	static int personalEntries = 6;

	public static void main(String[] args) throws IOException
	{   
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		FileProcessing.log("Started at " + timeStamp);
		FileProcessing.log("You can set the parameters in the following order:");
		FileProcessing.log("<ReliefF=0/SURF=1> <number_of_samples> <m_t> <snp_th> <percent_r> <inpuet_file_name> <output_file_name>");
		if (args.length>0) {
			if (Integer.parseInt(args[0])==1)
				useSURF=true;
			if(args.length>1) {
				samples = Integer.parseInt(args[1]);
				if (args.length>2) {
					m_t = Integer.parseInt(args[2]);
					if (args.length>3) {
						snp_th = Integer.parseInt(args[3]);
						if (args.length>4) {
							percent_r = Double.parseDouble(args[4]);
							if (args.length>5) {
								inputFile = args[5];
								if (args.length>6)
									outputFile = args[6];
							}
						}
					}
				}				
			}
		}
		//ArrayList<Integer>[] myData =FileProcessing.readFile(inputFile,samples);
		ArrayList<Integer>[] myData =FileProcessing.readFile(inputFile, samples, personalEntries);
		if (myData!=null)
		{
			m_s = FileProcessing.samples;
			TURF.TURF_func(myData, FileProcessing.phenoType, percent_r, m_t, m_s, snp_th, inputFile, outputFile, useSURF, cut_RF);
			FileProcessing.writeFile(outputFile, myData, personalEntries);
		}
	}
}
