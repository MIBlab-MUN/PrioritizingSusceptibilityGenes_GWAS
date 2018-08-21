/******
 File:       FileProcessing.java
 Author:     Somayeh Kafaie
 Date:       October, 2017
 Purpose:    The implementation of some utility functions to read from and write into text files.
 ******/

package Filtering;

import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.util.*;

public class FileProcessing {
 
	static int[] phenoType;
	static String[][] personalInfo;
	static ArrayList<String> headers;
	static String logFile = "log.txt"; 
	static int samples;
	
    static void log(String message)  throws IOException{
        System.out.println(message);
        try(FileWriter fw = new FileWriter(logFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
        	out.println(message);
        }
        catch (IOException e) {
        	    System.out.println(e.getMessage());
        	}
    }
    //myData is an array of the lists where each list represents the attributes of a
    //sample; so the array index denotes the sample index!
    static ArrayList<Integer>[] readFile(String inputFile, int samplesUserDefined, int personalEntries) throws IOException
    {
    	LineNumberReader  lnr = new LineNumberReader(new FileReader(new File(inputFile)));
    	lnr.skip(Long.MAX_VALUE);
    	samples = lnr.getLineNumber(); //the line index starts at 0 but we don't need the first line (headers)
    	lnr.close();
    	if (samples!=samplesUserDefined)
    	{
    		log("The read line number from the file("+ samples+") is different "
    				+ "from the user defined value("+samplesUserDefined+")");
    		return null;
    	}
    	
    	ArrayList<Integer>[] myData = new ArrayList[samples];
		//ArrayList<Integer>[] myData =  (ArrayList<Integer>[])new ArrayList[samples];
    	int readSample = 0;
    	phenoType = new int[samples];
    	personalInfo = new String[samples][personalEntries];
    	FileInputStream inputStream = null;
    	Scanner sc = null;
    	try {
    	    inputStream = new FileInputStream(inputFile);
    	    sc = new Scanner(inputStream, "UTF-8");
    	    boolean first = true;
    	    while (sc.hasNextLine()) {
    	        String line = sc.nextLine();
        		String[] cells = line.split(" ");
    	        if (first) 
    	        {
    	        	headers = new ArrayList<String>();
    	        	for (int i=0;i< cells.length;i++)
    	        		headers.add(cells[i]);
    	        	first = false;
    	        	continue;
    	        }
    	        myData[readSample] = new ArrayList<Integer>();
    	        for (int i=0;i< cells.length;i++)
    	        {
    	        	if (i==5) //phenotype
    	        		phenoType[readSample]=Integer.parseInt(cells[i]);
    	        	if (i<6)
    	        		personalInfo[readSample][i]=cells[i];
    	        	else //if (i>=6)
    	        		myData[readSample].add(Integer.parseInt(cells[i]));
    	        }
    	        readSample++;
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
    	if (samples==readSample)
    		return myData;
    	else 
    	{
    		log("The read line number ("+ samples+") is different from the "
    				+ "the number of lines read("+readSample+")! We go with the latter one!");
    		samples = readSample;
    		return myData;
    	}
    }
    
    static void writeFile(String outputFile, ArrayList<Integer>[] myData, int personalEntries) throws IOException
    {
    	FileWriter writer=null;
    	int samples = myData.length;
    	try
    	{
    		writer = new FileWriter(outputFile);
    		String record="";
    		for (String head_name:headers)
    			record = record + head_name + " "; 
    		record = record.trim();
    		record+='\n';
    		writer.write(record);
    		for (int i=0;i<samples;i++)
    		{
    			record="";
    			for (int j=0;j<personalEntries;j++)
    				record = record + personalInfo[i][j]+" ";
    			for (Integer attr:myData[i])
    				record = record + attr + " "; 
    			record = record.trim();
    			record+='\n';
    			writer.write(record);
    		}
    		
    	} finally {
    		if (writer!=null)
    			writer.close();
    	}
    }

}
