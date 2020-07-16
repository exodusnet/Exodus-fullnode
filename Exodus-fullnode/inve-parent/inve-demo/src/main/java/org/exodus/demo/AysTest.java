package org.exodus.demo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

public class AysTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		 // 默认的配置  
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();  
        try {  
             httpclient.start();  
             HttpGet request = new HttpGet("http://127.0.0.1:8080/hello");  
             Future<HttpResponse> future = httpclient.execute(request, null);  
             // 获取结果               
             HttpResponse response = future.get(); 
             System.out.println(response.getStatusLine());
             String content = EntityUtils.toString(response.getEntity(), "UTF-8");
			 System.out.println(" response content is : " + content);
             System.out.println("Shutting down");  
        } finally {  
             httpclient.close();  
        }  
        System.out.println("Done");  
    }  

}
