package org.exodus.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class TestProxy {

	public static void main(String[] args) throws InterruptedException, IOException {
		RequestConfig requestConfig = RequestConfig.custom()
				   .setSocketTimeout(3000)
				   .setConnectTimeout(3000)
                                   .build();
		CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom()
				   .setDefaultRequestConfig(requestConfig)
	                           .build();
		try {
			httpAsyncClient.start();
			final HttpPost request = new HttpPost("http://127.0.0.1:8080/users/1/");
			List <NameValuePair> params = new ArrayList<NameValuePair>();
	    	        params.add(new BasicNameValuePair("age", "99"));
	                params.add(new BasicNameValuePair("name", "changqing"));
		    	params.add(new BasicNameValuePair("id", "23"));
		    	request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
 
			System.out.println("start ...........");
			final CountDownLatch latch = new CountDownLatch(1);
			httpAsyncClient.execute(request, new FutureCallback<HttpResponse>() {
				//无论完成还是失败都调用countDown()
				@Override
				public void completed(final HttpResponse response) {
//					latch.countDown();
					System.out.println(request.getRequestLine() + "->"
							+ response.getStatusLine());
					String content;
					try {
						content = EntityUtils.toString(response.getEntity(), "UTF-8");
						System.out.println(" response content is : " + content);
					} catch (ParseException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
				}
				@Override
				public void failed(final Exception ex) {
//					latch.countDown();
					System.out.println(request.getRequestLine() + "->" + ex);
				}
				@Override
				public void cancelled() {
//					latch.countDown();
					System.out.println(request.getRequestLine()
							+ " cancelled");
				}
			});
//			for (final HttpGet request : requests) {
//				httpAsyncClient.execute(request, new FutureCallback<HttpResponse>() {
//					//无论完成还是失败都调用countDown()
//					@Override
//					public void completed(final HttpResponse response) {
//						latch.countDown();
//						System.out.println(request.getRequestLine() + "->"
//								+ response.getStatusLine());
//						String content;
//						try {
//							content = EntityUtils.toString(response.getEntity(), "UTF-8");
//							System.out.println(" response content is : " + content);
//						} catch (ParseException | IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//			            
//					}
//					@Override
//					public void failed(final Exception ex) {
//						latch.countDown();
//						System.out.println(request.getRequestLine() + "->" + ex);
//					}
//					@Override
//					public void cancelled() {
//						latch.countDown();
//						System.out.println(request.getRequestLine()
//								+ " cancelled");
//					}
//				});
//			}
			latch.await();
			System.out.println("Shutting down");
		}finally {
			httpAsyncClient.close();
		}
		System.out.println("Done");
		

	}

}
