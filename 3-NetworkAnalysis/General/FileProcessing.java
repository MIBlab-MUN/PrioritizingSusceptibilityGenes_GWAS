/*
 * File:       FileProcessing.java
 * Author:     Somayeh Kafaie
 * Date:       November, 2017
 * Purpose:    The implementation of some utility functions to read from and write into text files.
 */

package networkAnalysis;

import networkAnalysis.GraphBuilder.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileProcessing {
 
	static int p_fileNum = 100;
	static String logFile = "log"+Main_netAnalysis.IG_th+".txt"; 
	static int SNPs;
	
    static public void log(String message, boolean addTime)  throws IOException{
        System.out.println(message);
        try(FileWriter fw = new FileWriter(logFile, true);
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
    
    static void read_IGValues(String filename, ArrayList<String> SNPs_Name, double[][] IGMatrix) throws IOException
    {
			log("START: Reading IG file", true);
			String inputFile = filename;
	    	FileInputStream inputStream = null;
	    	Scanner sc = null;
	    	try {
	    		boolean firstLine=true;
	    	    inputStream = new FileInputStream(inputFile);
	    	    sc = new Scanner(inputStream, "UTF-8");
	    	    while (sc.hasNextLine()) {
	    	        String line = sc.nextLine();
	    	        if (firstLine)
	    	        {
	    	        	firstLine=false;
	    	        	continue;
	    	        }
	        		String[] cells = line.split(",");
	        		if (!SNPs_Name.contains(cells[0]))
	        			SNPs_Name.add(cells[0]);
	        		int firstIndex = SNPs_Name.indexOf(cells[0]);
	        		if (!SNPs_Name.contains(cells[1]))
	        			SNPs_Name.add(cells[1]);
	        		int secondIndex = SNPs_Name.indexOf(cells[1]);
	        		double value = Double.parseDouble(cells[2]);
	        		IGMatrix[firstIndex][secondIndex] = value;
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
	    	log("END: Reading IG file", true);
    }
    
    static void read_pValues(String inputFile, ArrayList<String> SNPs_Name, short[][] PMatrix) throws IOException
    {
		log("START: Reading PCount file!", true);
	    FileInputStream inputStream = null;
	    Scanner sc = null;
	    try {
	    	boolean firstLine=true;
	    	inputStream = new FileInputStream(inputFile);
	    	sc = new Scanner(inputStream, "UTF-8");
	    	while (sc.hasNextLine()) 
	    	{
	    		String line = sc.nextLine();
	    		if (firstLine)
	    	    {
	    	        firstLine=false;
	    	        continue;
	    	    }
	    		String[] cells = line.split(",");
	        	try
	        	{
	        		int firstIndex = SNPs_Name.indexOf(cells[0]);
	        		int secondIndex = SNPs_Name.indexOf(cells[1]);
	        		short value = Short.parseShort(cells[2]);
	        		PMatrix[firstIndex][secondIndex] = value;
	        	}
	        	catch(NumberFormatException ex )
	        	{
	        		FileProcessing.log("SNP "+cells[0]+" OR SNP "+ cells[1]+" doesnot exist!", true);
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
	    log("END: Reading P_value file!", true);
    }
    
    //We have 100 PCount files each representing pCount for 100 permutations between 100 SNPs and all others
    static void read_pValueFiles(String path, String filename, ArrayList<String> SNPs_Name, short[][] PMatrix) throws IOException
    {
		for (int i=0;i<p_fileNum;i++)
		{
			log("START: Reading P_value file# "+i, true);
			String inputFile = path+filename+i+".txt";
	    	FileInputStream inputStream = null;
	    	Scanner sc = null;
	    	try {
	    		boolean firstLine=true;
	    	    inputStream = new FileInputStream(inputFile);
	    	    sc = new Scanner(inputStream, "UTF-8");
	    	    while (sc.hasNextLine()) {
	    	        String line = sc.nextLine();
	    	        if (firstLine)
	    	        {
	    	        	firstLine=false;
	    	        	continue;
	    	        }
	        		String[] cells = line.split(",");
	        		int firstIndex = SNPs_Name.indexOf(cells[0]);
	        		int secondIndex = SNPs_Name.indexOf(cells[1]);
	        		short value = (short)(Double.parseDouble(cells[2])*1000);
	        		PMatrix[firstIndex][secondIndex] = value;
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
	    	log("END: Reading P_value file# "+i, true);
		}
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

