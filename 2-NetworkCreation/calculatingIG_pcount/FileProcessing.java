/*
 * File:       FileProcessing.java
 * Author:     Somayeh Kafaie
 * Date:       November, 2017
 * Purpose:    The implementation of some utility functions to read from and write into text files.
 */

import java.io.*;
import java.text.SimpleDateFormat;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.util.*;

public class FileProcessing {
 
	static ArrayList<Integer> phenoType = new ArrayList<Integer>();
	//static String[][] personalInfo;
	static ArrayList<String>[] personalInfo = new ArrayList[MainNetPermutations.personalEntries];  ;
	static ArrayList<String> headers;
	static String logFile = "log.txt"; 
	static int SNPs;
	
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
    
    //myData is an array of the lists where each list represents the values of a
    //a given attribute (SNP) for different samples; so the array index denotes 
    // the SNP index!
    static ArrayList<Integer>[] readFile(String inputFile, int	personalEntries) throws IOException
    {
    	BufferedReader bufReader = new BufferedReader(new FileReader(inputFile));
   		String firstLine = bufReader.readLine();
   		bufReader.close();
    	String[] headerTitle = firstLine.split(" ");
        headers = new ArrayList<String>();
        for (int i=0;i< headerTitle.length;i++)
        		headers.add(headerTitle[i]);
        SNPs = headerTitle.length - personalEntries;
        
       	ArrayList<Integer>[] myData = new ArrayList[SNPs];
    	//int readSample = 0;  	
    	FileInputStream inputStream = null;
    	Scanner sc = null;
    	try {
    	    inputStream = new FileInputStream(inputFile);
    	    sc = new Scanner(inputStream, "UTF-8");
    	    boolean isFirstLine = true;
	        boolean isFirstSample=true;
    	    while (sc.hasNextLine()) {
    	        String line = sc.nextLine();
    	        if (isFirstLine) // We have the information of the first line
    	        {
    	        	isFirstLine = false;
    	        	continue;
    	        }
        		String[] cells = line.split(" ");
     	        for (int i=0;i< cells.length;i++)
    	        {
    	        	if (i==5) //phenotype
    	        		phenoType.add(Integer.parseInt(cells[i]));
    	        	if (i<6)
    	        	{
    	        		if (isFirstSample)
    	        			personalInfo[i] = new ArrayList<String>();
    	        		personalInfo[i].add(cells[i]);
    	        	}
    	        	else //if (i>=6)
    	        	{
    	        		if (isFirstSample)
    	        			myData[i-personalEntries] = new ArrayList<Integer>();
    	        		myData[i-personalEntries].add(Integer.parseInt(cells[i]));
    	        	}
    	        }
     	       isFirstSample=false;
    	        //readSample++;
    	    }
    	    // note that Scanner suppresses exceptions
    	    if (sc.ioException() != null) {
    	        throw sc.ioException();
    	    }
    	} finally {
    	    if (inputStream != null) {
    	        inputStream.close();
    	    }
    	    if (sc != null) {
    	        sc.close();
    	    }
    	}
    		return myData;
    }
    
    //write the information gain for the given subset into a file 
    static public void writeInformationGain(String outputFile, double[][] IG, 
    										int personalEntries) throws IOException
    {
    	FileWriter writer=null;
    	try
    	{
     		writer = new FileWriter(outputFile);
    		String record="SNP1,SNP2,IG_real \n";
     		writer.write(record);
     		for (int i=0;i<IG.length;i++)
     			for (int j=0; j<IG[0].length;j++)
     			{	
     				record = headers.get(i+personalEntries) + ","+ headers.get(j+personalEntries);
     				record = record + "," + IG[i][j] +"\n";
     				writer.write(record);
     			}
    		
    	} finally {
    		if (writer!=null)
    			writer.close();
    	}
    }
    
    static public void  writePCounts(String outputFile, int[][] p, 
    		int personalEntries) throws IOException
    {
    	FileWriter writer=null;
    	try
    	{
    		writer = new FileWriter(outputFile);
    		String record="SNP1,SNP2,p_count\n";   		
     		writer.write(record);
     		for (int i=0;i<p.length;i++)
     			for (int j=0; j<p[0].length;j++)
     			{	
     				record = headers.get(i+personalEntries) + ","+ headers.get(j+personalEntries);
     				record = record + "," + p[i][j]+"\n";
     				writer.write(record);
     			}
    		
    	} finally {
    		if (writer!=null)
    			writer.close();
    	}
    }
}

