/*
 * File:           Main_calSig_linkedList.java
 * Author:         Somayeh Kafaie
 * Date:           February, 2018
 * Purpose:        This program reads the list of edges of the network (for a given IG_th) and creates its
 *                 adjacency matrix and graph. Then, by swapping the edges (n_swapped_edges*#edges) times,
 *                 we create random networks and repeat this to generate 1000 (i.e., n_rand_networks)
 *                 random networks. By measuring clustering coefficient and assortativity coefficient
 *                 for the real network as well as all random networks, we calculate p value and return it.
 */

package calSignificance_linkedList;

import calSignificance_linkedList.GraphBuilder.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main_calSig_linkedList {
	static ArrayList<String> SNPs_Name =  new ArrayList<String>();
	static int SNPs = 9996;
	static String edge_list_file = "edgeList_0.014.txt"; //the first line represents the # of nodes and edges
	//how many random networks do we need?
	static String logFile = "log_"; 
	static int n_rand_networks = 1000;
	//how many swapped edges for each random network?
	static int n_swapped_edges = 10; //it should be multiplied by the number of edges
	static int edge_num = 0;
	static int node_num = 0;
	
	public static void main(String[] args) throws IOException
	{
		if (args.length>0)
			if (Integer.parseInt(args[0])==0)
				n_rand_networks = Integer.parseInt(args[0]); 
		if (args.length>1)
			if (Integer.parseInt(args[1])==0)
				n_swapped_edges = Integer.parseInt(args[1]); 
		if (args.length>2)
			edge_list_file = args[2];
		if (args.length>3)
			if (Integer.parseInt(args[3])==0)
				SNPs = Integer.parseInt(args[3]);
		
		logFile = logFile + edge_list_file;
			
		boolean[][] adjacencyMatrix = FileProcessing.readEdgeList(edge_list_file, SNPs);
		
		ArrayList<Integer>[] edgeList = new ArrayList[SNPs];
		
		ArrayList<Vertex> vertex_list = new ArrayList<Vertex>();
		Edge[][] edgeArray=new Edge[SNPs][SNPs];
		//NOTE: In current implementation vertex_list contains all SNPs
		Graph SNP_Network = CreateGraph(vertex_list, edgeArray, adjacencyMatrix, edgeList);
		SNP_Network.setIsolatedVertices(vertex_list);
		
		//check edgeList
		int total = 0;
		for (int i=0;i<SNPs;i++)
			total+=edgeList[i].size();
		if ((total/2)!=edge_num)
		{
			FileProcessing.log("Error: the total edges in edgeList("+total/2+") doesn't match the number of edges!", true);
			return;
		}
		
	   	//Finding some features of the network
    	FileProcessing.log("The number of Vertexes = "+SNP_Network.getVertexNumber(), true);
        int n_edge = SNP_Network.getEdgeNumber();
    	FileProcessing.log("The number of Edges = "+n_edge, true);
        
        n_swapped_edges = n_swapped_edges * n_edge;
        
    	double deg_assortativity = SNP_Network.getDegreeAssortativity();
    	FileProcessing.log("degreeAssortativity = "+deg_assortativity, false);	
    	double clustering_coefficient = SNP_Network.calculateClusteringCoefficient();
    	FileProcessing.log("Clustering coefficient = "+ clustering_coefficient, false);
    	
    	//generate random networks and calculate degree assortativity and clustering coefficient for each of them
    	double[] clustering_co = new double[n_rand_networks];
    	double[] degree_assort = new double[n_rand_networks];
    	for (int i = 0; i < n_rand_networks; i++)
    	{
    		FileProcessing.log("calculating for random network #"+(i+1), true);
    		Graph rand_network = SNP_Network.getEdgeSwapped_withEdgeList(n_swapped_edges, edgeList);
    		degree_assort[i] = rand_network.getDegreeAssortativity();
    		clustering_co[i] = rand_network.calculateClusteringCoefficient();
            FileProcessing.log("randNetwork#"+(i+1)+":\t"+degree_assort[i]+"\t"+clustering_co[i], false);
    	} 	
    	
    	//calculate the significance
    	int Ln_a = 0;
    	int Ln_c = 0;
        int Hn_a = 0;
        int Hn_c = 0;
    	for (int i = 0; i < n_rand_networks; i++)
    	{
            if (degree_assort[i] >= deg_assortativity)
                Hn_a++;
            if (clustering_co[i] >= clustering_coefficient)
            	Hn_c++;
            if (degree_assort[i] <= deg_assortativity)
                Ln_a++;
            if (clustering_co[i] <= clustering_coefficient)
                Ln_c++;
    	}
    	double Lp_a = (double)Ln_a/n_rand_networks;
    	FileProcessing.log("The significance of degree assortativity as a low value (Lp_a) = " + Lp_a, false);

    	double Lp_c = (double)Ln_c/n_rand_networks;
    	FileProcessing.log("The significance of clustering coefficient as a low value (Lp_c)= " + Lp_c, false);

        double Hp_a = (double)Hn_a/n_rand_networks;
        FileProcessing.log("The significance of degree assortativity as a high value (Hp_a) = " + Hp_a, false);
        
        double Hp_c = (double)Hn_c/n_rand_networks;
        FileProcessing.log("The significance of clustering coefficient as a high value (Hp_c)= " + Hp_c, false);

	}	
	
	static Graph CreateGraph(ArrayList<Vertex> vertex_list, Edge[][] edgeArray,
			boolean[][] adjacencyMatrix, ArrayList<Integer>[] edgeList) throws IOException
	{
		FileProcessing.log("START: Creating Graph", true);
		//Creating all Vertexes
		for (int i=0; i<SNPs;i++)
		{
      		Vertex v= new Vertex(i, String.valueOf(i), 0);
    		vertex_list.add(v);
		}
    	
		//Generating all edges
		Graph myNetwork = new Graph(vertex_list.size());
    	for (int i=0; i<SNPs; i++)
    	{
    		edgeList[i] = new ArrayList<Integer>();
    		for (int j=0; j<SNPs; j++)
    		{
    			if (adjacencyMatrix[i][j])
    			{
    				edgeList[i].add(j);
    				if (i<j)
    				{
    					Edge e =new Edge(i,j,vertex_list.get(i).snpName, vertex_list.get(j).snpName);
    					myNetwork.addEdge(e);
    					edgeArray[i][j]=e;
    					edgeArray[j][i]=e;
    				}	
    			}
    		}
    	}
		
    	return myNetwork;
	}

}
