package org.exodus.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteToJson {
	
	public static void writer(Integer name, String json) {
		try{
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(name+".json"))));
		writer.write(json);
		writer.close();
		}catch(IOException ex){
		    ex.printStackTrace();
		}
	}
	
 
}
