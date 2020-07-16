package org.exodus.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReadConfig {

	public static void main(String[] args) {
		String path = new File("").getAbsolutePath();
		System.out.println(path);
        JSONParser parser = new JSONParser();
		try {
        	Object object = parser.parse(new FileReader(path.substring(0,path.length()-5)+"config.json"));
        	JSONObject jsonObject = (JSONObject)object;
        	JSONArray seeds = (JSONArray)jsonObject.get("seeds");
        	JSONArray nodes = (JSONArray)jsonObject.get("nodes");
        	System.out.println("Seeds:");
        	for(Object seed: seeds) {
        		System.out.println(seed.toString());
        	}
        	System.out.println("Nodes:");
        	for(Object node: nodes) {
        		System.out.println(node.toString());
        		JSONObject nodeObject = (JSONObject)object;
        		JSONObject nb=(JSONObject)parser.parse(node.toString());
        		
       		   System.out.println(nb.get("pubkey"));
        	}
        }catch(FileNotFoundException fe)
        {
            fe.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}

}
