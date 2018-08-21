# PrioritizingSusceptibilityGenes_GWAS
These java codes have been used to analyze CRC data-set described in the following manuscript: "A Network Approach to Prioritizing Susceptibility Genes for Genome-wide Association Studies"

------------------------------------------------------------------
If this file does not answer your questions, please
feel free to e-mail any questions to Ting Hu or Somayeh Kafaie at:

    ting.hu@mun.ca
    somayeh.kafaie@mun.ca

------------------------------------------------------------------

DIRECTORIES:

    1-Filtering:    	 
	This program receives genome information of samples froma file (inputFile) in the 	
	following format and returns the result in a file (outputFile) with a similar 		
	format including only selected SNPs (instead of all of them).
        - inputFileFormat:
            It is a table where each row represents a sample and the first 6 columns are 	    
	    personal information of that sample and the rest of the columns are SNPs, 		    
	    where the column labels reflect the snp name (e.g. snp1) with the name of the 	    
	    minor allele appended (i.e. snp1_2 in the first instance, as 2 is the minor 	    
	    allele) for the additive component.  Assuming A is the minor allele, it will 		    
	    recode genotypes as follows:
                SNP       SNP_A
                ---       -----
                A A   ->    0
                A T   ->    1
                T T   ->    2
                0 0   ->   NA
 	   This code is able to use (ReliefF + TURF) or (SURF + TURF) for filtering.

    2-NetworkCreation:       
	2-1-calculatingIG_pcount: 
	   For all SNPs selected after filtering, we calculate the pairwise information 	   
	   gain using the following java code. We also, generate 1000 permutations of the 	   
	   dataset by shuffling phenotypes and assigning them to the samples and calculate 	  
	   the pair-wise information gain for all SNPs of every permutated dataset as well.
	   
	   As implemented in “calculatingIG_pcount/MainNetPermutations.java”, to get the 	   
	   result faster, we ran 100 different instances of the code, where each generates 	   
	   the IG between all SNPs as well as the IG for 10 permutations and calculates P 	   
	   (P here is a short number counting the number of permutations with value 		   
	   greater than or equal to the value of the IG for our dataset). 
	   
	2-2-mergingPerms_netParameters:
	   The result of the previous step is used to create the network for different IG 	   
	   cut off values (from 0.02 to 0.008 decrementing by 0.001) and observe the 		   
	   change of different features like the number of vertices, the number of edges, 	   
	   the degree distribution and the size of the largest component.

    3-NetworkAnalysis:   
	3-1-General:
	   This code reads IG values and p-counts for all pairs of SNPs and creates the
 	   network based on given thresholds (IG_th and p_th). It calculates the adjacency
           matrix and the graph and finds main network parameters.

	3-2-calSignificance_Clustering_Assortativity:
	   This program reads the list of edges of the network (for a given IG_th) and 		   
	   creates its adjacency matrix and graph. Then, by swapping the edges 			   
	   (n_swapped_edges*#edges) times, we create random networks and repeat this to 	   
	   generate 1000 (i.e., n_rand_networks) random networks. By measuring clustering 	   
	   coefficient and assortativity coefficient for the real network as well as all 	   
	   random networks, we calculate p value and return it.

------------------------------------------------------------------
PACKAGES TO INSTALL:

  The code has been written in java using Eclipse IDE.

------------------------------------------------------------------
