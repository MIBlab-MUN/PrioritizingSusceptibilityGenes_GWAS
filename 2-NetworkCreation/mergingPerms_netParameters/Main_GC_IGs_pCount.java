/*
 * File:           Main_GC_IGs_pCount.java
 * Author:         Somayeh Kafaie
 * Date:           November, 2017
 * Purpose:        This program receives a the value of information gain (IG) for all pairs of SNPs
 *                 as well as the significance value calculated (p count) and calculates the adjacency
 *                 matrix and creates the graph for different IG cut-off values and for each of
 *                 the generated graphs finds some basic parameters calculated in "GraphBuilder.java"
 *                 and originally implemented by Dr. Ting Hu.
 *                 Note that here p counts are short numbers between 0 and 1000.
 */

//package graphGeneration_IGs_pCount;

import java.io.IOException;
import java.util.ArrayList;

import GraphBuilder.*;

public class Main_GC_IGs_pCount {

	static ArrayList<String> SNPs_Name =  new ArrayList<String>();
	static int SNPs = 9996;
	static double[][] IGMatrix =new double[SNPs][SNPs];
	static short[][] PMatrix =new short[SNPs][SNPs];
	//static String IG_real_file ="/Users/Somayeh/Documents/PostDoc/First/CRC_GWAS_dataset/Mine/5-NetworkCreating/FindingEdges_differentIGs/InfoGain_real.txt";
	static String IG_real_file ="InformationGain_0.txt";
	static String pOutputFile = "PCount_1000.txt";
	static short p_th = 10; //10 out of 1000 permutations -> 0.01
	//static String path_p = "/Users/Somayeh/Documents/PostDoc/First/CRC_GWAS_dataset/Mine/5-NetworkCreating/netCreatingParallel/output/pValues/";
	static String path_p ="pCount/";
	
	public static void main(String[] args) throws IOException
	{
		if (args.length>1)
			p_th = Short.parseShort(args[1]);
		if (args.length>2)
			path_p = args[2];
		if (args.length>3)
			IG_real_file = args[3];
		if (args.length>4)
			SNPs = Integer.parseInt(args[4]);
		
		//Initialize PMatrix just to be sure!
		for (int i=0;i<SNPs; i++)
			for (int j=0;j<SNPs;j++)
				PMatrix[i][j]=0;
		
		FileProcessing.read_IGValues(IG_real_file, SNPs_Name, IGMatrix);
		
		if (SNPs_Name.size()!=SNPs)
		{
			FileProcessing.log("Ha!Something wrong happend!");
			return;
		}
		
		FileProcessing.writeSNPlist("SNP_list.txt", SNPs_Name);

		FileProcessing.read_pValues(path_p, "pCount_", SNPs_Name, PMatrix);
				
		FileProcessing.write_pValues(pOutputFile, SNPs_Name, PMatrix);
			
		//just for testing
		CreateGraph(-1, p_th, IGMatrix, PMatrix);
		
		double current_TG_th =0.020;
		while (current_TG_th > 0.007)
		{
			CreateGraph(current_TG_th, p_th, IGMatrix, PMatrix);
			current_TG_th = current_TG_th - 0.001;
		}
		
	}	
	
	static void CreateGraph(double IG, short p, double[][] IGMatrix, short[][] PMatrix) throws IOException
	{
		FileProcessing.log("START: Creating Graph for IG >= "+IG);
		boolean[][] adjacencyMatrix =new boolean[SNPs][SNPs];
		ArrayList<Vertex> SNP_list = new ArrayList<Vertex>();
		FileProcessing.log("START: Creating all Vertexes");
		for (int i=0; i<SNPs;i++)
		{
      		Vertex v= new Vertex(i, SNPs_Name.get(i), 0);
    		SNP_list.add(v);
		}
		FileProcessing.log("END: Creating all Vertexes");	
    	
		FileProcessing.log("START: Creating all Edges");
    	Graph myNetwork = new Graph(SNP_list.size());
    	for (int i=0; i<SNPs; i++)
    		for (int j=0; j<SNPs; j++)
    		{
    			adjacencyMatrix[i][j]=false;
    			if ((IGMatrix[i][j]>=IG)&&(PMatrix[i][j]<=p)&&(i<j))
    			{
    				adjacencyMatrix[i][j]=true;
    				Edge e =new Edge(i,j,SNP_list.get(i).snpName, SNP_list.get(j).snpName, IGMatrix[i][j], PMatrix[i][j]);
    				myNetwork.addEdge(e);
    			}
    			else if ((IGMatrix[i][j]>=IG)&&(PMatrix[i][j]<=p)&&(i>j)&&(!adjacencyMatrix[j][i])) 
    			{
    				adjacencyMatrix[i][j]=true;
    				adjacencyMatrix[j][i]=true;
    				Edge e =new Edge(i,j,SNP_list.get(i).snpName, SNP_list.get(j).snpName, IGMatrix[i][j], PMatrix[i][j]);
    				myNetwork.addEdge(e);
    				FileProcessing.log("Not Symmetric weight for nodes "+j+" and "+i);
    			}		
    		}
    	FileProcessing.log("END: Creating all Edges");
    	
    	//FileProcessing.log("START: Finding some features of the network");
    	FileProcessing.log("The number of Vertexes = "+myNetwork.getVertexNumber());
    	
    	FileProcessing.log("The number of Edges = "+myNetwork.getEdgeNumber());
    	
    	FileProcessing.log("The degree distribution: "+myNetwork.printVertexDegreeDistributionNonZero());
    	
    	Vertex[] temp = new Vertex[SNP_list.size()];
    	ArrayList<Integer> CCorders = myNetwork.getCCorders(SNP_list.toArray(temp));
    	String record ="";
    	for (int i=0;i<CCorders.size();i++)
    		record+=CCorders.get(i)+", ";
    	FileProcessing.log("Size of different components = "+ record);
    	int Gsize = myNetwork.getGCCorder(SNP_list.toArray(temp));
    	FileProcessing.log("Size of the largest components = "+ Gsize);

    	FileProcessing.log("END: Creating Graph for IG >= "+IG);
    	
    	String output = "AM_"+IG+".txt";
    	FileProcessing.writeAdjacencyMatrix(output, adjacencyMatrix);
		
	}
}
