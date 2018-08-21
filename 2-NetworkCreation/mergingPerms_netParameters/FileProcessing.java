/*
 * File:       FileProcessing.java
 * Author:     Somayeh Kafaie
 * Date:       November, 2017
 * Purpose:    The implementation of some utility functions to read from and write into text files.
 */

//package graphGeneration_IGs_pCount;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileProcessing {
 
	static String logFile = "log.txt"; 
	static int SNPs;
	static int p_fileNum = 100;
	
    static void log(String message)  throws IOException{
        System.out.println(message);
        try(FileWriter fw = new FileWriter(logFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
        	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        	message = message +" at "+ timeStamp;
        	out.println(message);
        }
        catch (IOException e) {
        	    System.out.println(e.getMessage());
        	}
    }
    
    static void read_IGValues(String filename, ArrayList<String> SNPs_Name, double[][] IGMatrix) throws IOException
    {
			log("START: Reading IG file");
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
	    	log("END: Reading IG file");
    }
    
    //We have 100 PCount files each representing pCount for 10 permutations for all SNPs
    static void read_pValues(String path, String filename, ArrayList<String> SNPs_Name, short[][] PMatrix) throws IOException
    {
		for (int i=0;i<p_fileNum;i++)
		{
			log("START: Reading PCount file# "+i);
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
	        		try
	        		{
	        			int firstIndex = SNPs_Name.indexOf(cells[0]);
	        			int secondIndex = SNPs_Name.indexOf(cells[1]);
	        			short value = Short.parseShort(cells[2]);
	        			PMatrix[firstIndex][secondIndex] += value;
	        		}
	        		catch(NumberFormatException ex )
	        		{
	        			FileProcessing.log("SNP "+cells[0]+" OR SNP "+ cells[1]+" doesnot exist!");
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
	    	log("END: Reading P_value file# "+i);
		}
    }
    
	   static public void writeAdjacencyMatrix(String outputFile, boolean[][] AM) throws IOException
	    {
		   log("START: writing Adjacency Matrix");
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
	    	log("END: writing Adjacency Matrix");
	    }
	   
	   static public void writeSNPlist(String outputFile, ArrayList<String> list) throws IOException
	    {
		   log("START: writing SNPlist");
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
	    	log("END: writing SNPlist");
	    }
	   
	   static void write_pValues(String outputFile, ArrayList<String> SNPs_Name, short[][] PMatrix) throws IOException
	   {
		   log("START: writing PValues");
	    	FileWriter writer=null;
	    	try
	    	{
	    		writer = new FileWriter(outputFile);
	     		for (int i=0;i<PMatrix.length;i++)
	     			for (int j=0; j<PMatrix[0].length;j++)
	     			{
	     				String record=SNPs_Name.get(i)+","+SNPs_Name.get(j)+","+PMatrix[i][j]+"\n";
	     				writer.write(record);
	     			}
	    		
	    	} finally {
	    		if (writer!=null)
	    			writer.close();
	    	}
	    	log("END: writing PValues");	   
	   }
}

