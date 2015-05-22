package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread{

	private String   address;
	private int      port;
	private String   hashCommand;
	private TextView hashResultTextView;
	
	private Socket   socket;
	
	public ClientThread(
			String address,
			int port,
			String hashCommand,
			TextView hashResultTextView) {
		this.address                 = address;
		this.port                    = port;
		this.hashCommand             = hashCommand;
		this.hashResultTextView = hashResultTextView;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(hashCommand);
				printWriter.flush();
				String hashResult;
				while ((hashResult = bufferedReader.readLine()) != null) {
					final String finalizedHashResult = hashResult;
					hashResultTextView.post(new Runnable() {
						@Override
						public void run() {
							hashResultTextView.append(finalizedHashResult + "\n");
						}
					});
				}
			} else {
				Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}

}
