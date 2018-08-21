/*
 * File:           Main_netAnalysis.java
 * Author:         Somayeh Kafaie
 * Date:           November, 2017
 * Purpose:        This program reads IG values and p-counts for all pairs of SNPs and create the
 *                 network based on given thresholds (IG_th and p_th). It calculates the adjacency
 *              matrix and the graph and finds main network parameters.
 */

package networkAnalysis;

import networkAnalysis.GraphBuilder.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main_netAnalysis {

	static ArrayList<String> SNPs_Name =  new ArrayList<String>();
	static int SNPs = 9996;
	static double[][] IGMatrix =new double[SNPs][SNPs];
	static short[][] PMatrix =new short[SNPs][SNPs];
	//static String IG_real_file ="/Users/Somayeh/Documents/PostDoc/First/CRC_GWAS_dataset/Mine/5-NetworkCreating/FindingEdges_differentIGs/InfoGain_real.txt";
	static String IG_real_file ="InformationGain_0.txt";
	static String pFile = "PCount_1000.txt";
	static short p_th = 10; //10 out of 1000 permutations -> 0.01
	static double IG_th = 0.014;
	//static String path_p = "/Users/Somayeh/Documents/PostDoc/First/CRC_GWAS_dataset/Mine/5-NetworkCreating/netCreatingParallel/output/pValues/";
	static String path_p ="pValues/";
	static boolean onlyOnePValueFile = true;
	
	public static void main(String[] args) throws IOException
	{
		if (args.length>0)
			if (Integer.parseInt(args[0])==0)
				onlyOnePValueFile = false;
		if (args.length>1)
			IG_th = Double.parseDouble(args[1]);
		if (args.length>2)
			p_th = Short.parseShort(args[2]);
		if (args.length>3)
			pFile = args[3];
		if (args.length>4)
			IG_real_file = args[4];
		if (args.length>5)
			SNPs = Integer.parseInt(args[5]);
				
		FileProcessing.read_IGValues(IG_real_file, SNPs_Name, IGMatrix);
		
		if (SNPs_Name.size()!=SNPs)
		{
			FileProcessing.log("ERROR: Ha!Something wrong happend!", true);
			return;
		}
		
		FileProcessing.writeSNPlist("SNP_list.txt", SNPs_Name);
		
		if (onlyOnePValueFile)
			FileProcessing.read_pValues(pFile, SNPs_Name, PMatrix);
		else
			FileProcessing.read_pValueFiles(path_p, "pValues_", SNPs_Name, PMatrix);
							
		ArrayList<Vertex> vertex_list = new ArrayList<Vertex>();
		Edge[][] edgeArray=new Edge[SNPs][SNPs];
		//NOTE: In current implementation vertex_list contains all SNPs
		Graph SNP_Network = CreateGraph(IG_th, p_th, IGMatrix, PMatrix, vertex_list,edgeArray);
		SNP_Network.setIsolatedVertices(vertex_list);
		//FileProcessing.writeVertexlist("vertex_list.txt", vertex_list);
		
	   	//Finding some features of the network
		int verticesNum = SNP_Network.getVertexNumber();
    	FileProcessing.log("The number of Vertexes = "+verticesNum, true);
    	/*if (verticesNum!=vertex_list.size())
    		FileProcessing.log("ERROR: SNP_list.size() doesn't match the number of vertices in the network!");*/
    	
    	FileProcessing.log("The number of Edges = "+SNP_Network.getEdgeNumber(), true);
    	
    	FileProcessing.log("The degree distribution: "+SNP_Network.printVertexDegreeDistributionNonZero(), false);
    	
    	Vertex[] temp = new Vertex[vertex_list.size()];
    	ArrayList<Integer> CCorders = SNP_Network.getCCorders(vertex_list.toArray(temp));
    	String record ="";
    	for (int i=0;i<CCorders.size();i++)
    		record+=CCorders.get(i)+", ";
    	FileProcessing.log("Size of different components = "+ record, false);
    	
    	int Gsize = SNP_Network.getGCCorder(vertex_list.toArray(temp));
    	FileProcessing.log("Size of the largest components = "+ Gsize, false);
    	
    	FileProcessing.printAllCCs(SNP_Network.getAllCCs(vertex_list.toArray(temp)));
    	
    	//The degree distribution of the largest component doesn't work properly
    	//because the index of the vertices might be larger than the size of the subgraph
    	//FileProcessing.log("The degree distribution of the giant component: "+
    	//SNP_Network.printGCCVertexDegreeDistributionNonZero(vertex_list.toArray(temp), edgeArray));
    	
    	double[] cc=SNP_Network.closenessCentrality(vertex_list.toArray(temp),edgeArray,false);
    	FileProcessing.log("Closeness Centrality:", true);
    	FileProcessing.printDoubleArray(cc, 0);
    	
    	//double[] bc=SNP_Network.betweennessCentrality(vertex_list.toArray(temp));
    	double[] bc= SNP_Network.betweennessCentrality(vertex_list.toArray(temp), edgeArray, false);
       	FileProcessing.log("Betweenness Centrality:", true);
    	FileProcessing.printDoubleArray(bc, 1);
    	
    	double pr[] = SNP_Network.calculatePageRank(vertex_list.toArray(temp), false, edgeArray, false);
    	FileProcessing.log("Page Rank:", true);
    	FileProcessing.printDoubleArray(pr, 0);
    	
    	double pr_weighted[] = SNP_Network.calculatePageRank(vertex_list.toArray(temp), false, edgeArray, true);
    	FileProcessing.log("Page Rank_weighted:", true);
    	FileProcessing.printDoubleArray(pr_weighted, 0);

    	FileProcessing.printWeightedDegreeCentrality(SNP_Network.getVertexWeightedDegreeCentrality(edgeArray));
    	
    	cc=SNP_Network.closenessCentrality(vertex_list.toArray(temp),edgeArray,true);
    	FileProcessing.log("Weighted Closeness Centrality:", true);
    	FileProcessing.printDoubleArray(cc, 0);

    	bc=SNP_Network.betweennessCentrality(vertex_list.toArray(temp), edgeArray, true);
    	FileProcessing.log("Weighted Betweenness Centrality:", true);
    	FileProcessing.printDoubleArray(bc, 1);
    
    	FileProcessing.log("degreeAssortativity = "+SNP_Network.getDegreeAssortativity(), false);
    	
    	FileProcessing.log("Clustering coefficient = "+ SNP_Network.calculateClusteringCoefficient(), false);
    	
    	//calculate KCores
    	for (int K=2;K<6;K++)
    	{
    		//ArrayList<Integer> KCoreVector = SNP_Network.findKCoreNodes(K, vertex_list.toArray(temp));
     		FileProcessing.log("The KCore Nodes for K = "+ K+":", true);
    		FileProcessing.printAllCCs(SNP_Network.findKCoreComponents(K, vertex_list.toArray(temp)));
    	}
    	

	}	
	
	static Graph CreateGraph(double IG, short p, double[][] IGMatrix, short[][] PMatrix, 
			ArrayList<Vertex> vertex_list, Edge[][] edgeArray) throws IOException
	{
		FileProcessing.log("START: Creating Graph for IG >= "+IG, true);
		boolean[][] adjacencyMatrix =new boolean[SNPs][SNPs];
		//Creating all Vertexes
		for (int i=0; i<SNPs;i++)
		{
      		Vertex v= new Vertex(i, SNPs_Name.get(i), 0);
    		vertex_list.add(v);
		}
    	
		//Generating adjacency matrix and all edges
		Graph myNetwork = new Graph(vertex_list.size());
    	for (int i=0; i<SNPs; i++)
    		for (int j=0; j<SNPs; j++)
    		{
    			adjacencyMatrix[i][j]=false;
    			if ((IGMatrix[i][j]>=IG)&&(PMatrix[i][j]<=p)&&(i<j))
    			{
    				adjacencyMatrix[i][j]=true;
    				adjacencyMatrix[j][i]=true;
    				Edge e =new Edge(i,j,vertex_list.get(i).snpName, vertex_list.get(j).snpName, IGMatrix[i][j], PMatrix[i][j]);
    				myNetwork.addEdge(e);
    				edgeArray[i][j]=e;
    				edgeArray[j][i]=e;
    			}
    			else if ((IGMatrix[i][j]>=IG)&&(PMatrix[i][j]<=p)&&(i>j)&&(!adjacencyMatrix[j][i])) 
    			{
    				adjacencyMatrix[i][j]=true;
    				adjacencyMatrix[j][i]=true;
    				Edge e =new Edge(i,j,vertex_list.get(i).snpName, vertex_list.get(j).snpName, IGMatrix[i][j], PMatrix[i][j]);
    				myNetwork.addEdge(e);
    				edgeArray[i][j]=e;
    				edgeArray[j][i]=e;
    				FileProcessing.log("ERROR: Not Symmetric weight for nodes "+j+" and "+i, false);
    			}		
    		}
		
    	//remove the SNPs that are isolated in the network.
    	/*boolean toBeRemoved[]=new boolean[SNPs];
    	int vertexNum=0;
    	for (int i=0; i<SNPs;i++)
    	{
    		toBeRemoved[i]=true;
    		for (int j=0;j<SNPs;j++)
    			if ((adjacencyMatrix[i][j])&&(i!=j))
    			{
    				vertexNum++;
    				toBeRemoved[i]=false;
    				break;
    			}
    	}
    	
		//Creating all Edges
    	Graph myNetwork = new Graph(vertexNum);
    	for (int i=0; i<vertex_list.size(); i++)
    		for (int j=i; j<vertex_list.size(); j++)
    		{
    			if ((adjacencyMatrix[i][j])&&(i!=j))
    			{
    				Edge e =new Edge(i,j,vertex_list.get(i).snpName, vertex_list.get(j).snpName, IGMatrix[i][j], PMatrix[i][j]);
    				myNetwork.addEdge(e);
    			}	
    		}
    	
    	for (int i=SNPs-1;i>=0;i--)
    		if (toBeRemoved[i])
    			vertex_list.remove(i);
    	vertex_list.trimToSize();
    	if (vertexNum!=vertex_list.size())
    		FileProcessing.log("ERROR: vertexNum="+vertexNum+" doesn't match vertex_list.size()="+vertex_list.size());*/
    	
    	return myNetwork;
	}
}
