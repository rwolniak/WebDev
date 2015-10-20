package com.boozallen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Data Access Object
 * 
 * Used to read/write counter value to/from a file
 * @author ryanwolniak
 *
 */
public class DAO {
	public int getCount(){
		//initialize count and readers/writers
		int count = 0;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		PrintWriter writer = null;
		
		try{
			//load file
			File f = new File("FileCounter.initial");
			if(!f.exists()){
				//create file if it did not previously exist
				f.createNewFile();
				writer = new PrintWriter(new FileWriter(f));
				writer.println(0);
			}
			if(writer != null){
				writer.close();
			}
			
			fileReader = new FileReader(f);
			bufferedReader = new BufferedReader(fileReader);
			String initial = bufferedReader.readLine();
			count = Integer.parseInt(initial);
		} catch (Exception e){
			if(writer != null){
				writer.close();
			}
		}
		if(bufferedReader != null){
			try{
				bufferedReader.close();
			} catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		return count;
	}
	
	/**
	 * Save count
	 * @param count
	 * @throws Exception
	 */
	public void save(int count) throws Exception {
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		fileWriter = new FileWriter("FileCounter.initial");
		printWriter = new PrintWriter(fileWriter);
		printWriter.println(count);
		
		if(printWriter != null){
			printWriter.close();
		}
	}
	
	/**
	 * Reset value of count to 0
	 * @throws IOException
	 */
	public void reset() throws IOException {
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		fileWriter = new FileWriter("FileCounter.initial");
		printWriter = new PrintWriter(fileWriter);
		printWriter.println(0);
		
		if(printWriter != null){
			printWriter.close();
		}
	}
}
