/*
 * File:       FileProcessing.java
 * Author:     Somayeh Kafaie
 * Date:       February, 2018
 * Purpose:    The implementation of some utility functions to read from and write into text files.
 */

package calSignificance_linkedList;

import calSignificance_linkedList.GraphBuilder.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileProcessing {

	static int p_fileNum = 100;
	static int SNPs;
	
    static void log(String message, boolean addTime)  throws IOException{
        System.out.println(message);
        try(FileWriter fw = new FileWriter(Main_calSig_linkedList.logFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
        	if (addTime)
        	{
        		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        		message = message +" at "+ timeStamp;
        	}
        	out.println(message);
        }
        catch (IOException e) {
        	    System.out.println(e.getMessage());
        	}
    }
    
    //This function reads a list of edges each represented in one line 
    //in this format: <node1><space><node2>
    static boolean[][] readEdgeList(String filename, int SNPs) throws IOException
    {
    	boolean[][] adjMatrix =new boolean[SNPs][SNPs];
    	for (int i=0;i<SNPs;i++)
    		for (int j=0;j<SNPs;j++)
    			adjMatrix[i][j]=false;
    	
		log("START: Reading edgelist file", true);
    	FileInputStream inputStream = null;
    	Scanner sc = null;
    	boolean first = true;
    	try {
    	    inputStream = new FileInputStream(filename);
    	    sc = new Scanner(inputStream, "UTF-8");
    	    while (sc.hasNextLine()) {
    	        String line = sc.nextLine();
          		String[] cells = line.split(" ");
          		if (first) { //the first line represents the number of nodes and edges
          			Main_calSig_linkedList.node_num = Integer.parseInt(cells[0]);
          			Main_calSig_linkedList.edge_num = Integer.parseInt(cells[1]);
          			first = false;
          		}
          		else {
          			int node1 = Integer.parseInt(cells[0]);
          			int node2 = Integer.parseInt(cells[1]);
          			adjMatrix[node1][node2] = true;
          			adjMatrix[node2][node1] = true; //we consider it an undirected graph
          		}
    	    }
    	    if (sc.ioException() != null) {
    	        throw sc.ioException();
    	    }
    	} finally {
    	    if (inputStream != null) 
    	        inputStream.close();
    	    if (sc != null)
    	        sc.close();
    	}
    	log("END: Reading edgelist file", true);

    	return adjMatrix;
    }
  
    static public void writeAdjacencyMatrix(String outputFile, boolean[][] AM) throws IOException
	    {
		   log("START: writing Adjacency Matrix", true);
	    	FileWriter writer=null;
	    	try
	    	{
	    		writer = new FileWriter(outputFile);
	     		for (int i=0;i<AM.length;i++)
	     		{
	     			String record="";
	     			for (int j=0; j<AM[0].length;j++)
	     			{
	     				if (AM[i][j])
	     					record += "1 ";
	     				else
	     					record += "0 ";
	     			}
    	     		record= record.trim()+"\n";
    				writer.write(record);
	     		}
	    		
	    	} finally {
	    		if (writer!=null)
	    			writer.close();
	    	}
	    	log("END: writing Adjacency Matrix", true);
	    }
	   
	   static public void writeSNPlist(String outputFile, ArrayList<String> list) throws IOException
	    {
		   log("START: writing SNPlist", true);
	    	FileWriter writer=null;
	    	try
	    	{
	    		writer = new FileWriter(outputFile);
	     		for (int i=0;i<list.size();i++)
	     		{
	     			String record=i + " "+list.get(i)+"\n";
	     			writer.write(record);
	     		}
	    		
	    	} finally {
	    		if (writer!=null)
	    			writer.close();
	    	}
	    	log("END: writing SNPlist", true);
	    }
	   
	   static public void writeVertexlist(String outputFile, ArrayList<Vertex> list)throws IOException
	   {
		   log("START: writing vertex_list", true);
	    	FileWriter writer=null;
	    	try
	    	{
	    		writer = new FileWriter(outputFile);
	     		for (int i=0;i<list.size();i++)
	     		{
	     			String record=list.get(i).snpIndex + " "+list.get(i).snpName+"\n";
	     			writer.write(record);
	     		}
	    		
	    	} finally {
	    		if (writer!=null)
	    			writer.close();
	    	}
	    	log("END: writing vertex_list", true);
	   }
	   
	   static public void printAllCCs(ArrayList<ArrayList<Integer>> allCCs) throws IOException
	   {
		   log("There are "+allCCs.size()+" component(s).", true);
		   for (int i=0;i<allCCs.size();i++)
		   {
			   log("The vertices in component#"+i+":", true);
			   //String vertexList="";
			   for (int j=0;j<allCCs.get(i).size();j++)
				   log(allCCs.get(i).get(j).toString(), false);
				   //vertexList= vertexList+ allCCs.get(i).get(j)+",";
			   //log(vertexList, false);
		   }
	   }
	   
	   static public void printWeightedDegreeCentrality(ArrayList<Double> list)throws IOException
	   {
		   log("Weighted Degree Centrality:", true);
		   for (int i=0;i<list.size();i++)	
			   if (list.get(i)!=0)
			   log(i+"\t"+list.get(i), false);
	   }
	   
	   static public void printDoubleArray(double[] myArray, int min) throws IOException
	   {
		   for (int i=0;i<myArray.length;i++)
			   if (myArray[i]>min)
				   log(i+"\t"+myArray[i], false);
	   }
}
