package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.util.Log;

public class CommunicationThread extends Thread {

	
	private ServerThread serverThread;
	private Socket       socket;
	
	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}
	
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				HashMap<String, Value> data = serverThread.getData();
				
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
					String hashCommand     = bufferedReader.readLine();
					String[] tokens = hashCommand.split("[,]");
					String command = tokens[0];
					String key = tokens[1];
					String value = null;
					String result = " ";
					if(command.equals(Constants.PUT)){
						value = tokens[2];
						//daca cheia exista, o actualizez
						if (data.containsKey(key)) {
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
							int timestamp = data.get(key).getTimestamp();
							//create value
							Value v = new Value();
							v.setV(value);
							v.setTimestamp(getTimestamp());
							
							//modify entry
							data.remove(key);
							data.put(key, v);
							
							result = "modified";
							
					}
						else {
							Value v = new Value();
							v.setV(value);
							v.setTimestamp(getTimestamp());
							data.put(key, v);
							result = "inserted";
						}
					}
					
					
					if(command.equals(Constants.GET)){
						if (data.containsKey(key)) {
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
							int timestamp = data.get(key).getTimestamp();
							if(getTimestamp() - timestamp < 60){
								result = data.get(key).getV();
							}
							else
								result = "NONE";
							
						}
						else {
							result = "NONE";
						}
					}
					printWriter.println(result);
					printWriter.flush();
				}		
					socket.close();
			}
					catch (IOException ioException) {
						Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
						if (Constants.DEBUG) {
							ioException.printStackTrace();
						}
				} 
		}
			else {
					Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
				}
			}

	
			
		public int getTimestamp(){
			int timestamp = 0;
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);
			HttpResponse httpGetResponse;
			try {
				httpGetResponse = httpClient.execute(httpGet);
				HttpEntity httpGetEntity = httpGetResponse.getEntity();
				  if (httpGetEntity != null) {  
				    // do something with the response
					String date = EntityUtils.toString(httpGetEntity);
					String[] tokens = date.split("[-T:+]");
					
				    Log.i(Constants.TAG, EntityUtils.toString(httpGetEntity));
				    
				    //compute Timestamp
				    
				  } 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			  
			  return timestamp;
		}
	
}
