package me.shad3n.abyssal_logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;


public class ServerThread extends Thread{
	
	Socket socket;
	private Consumer<String> onJSONReceived;
	BufferedReader in;

	
	public ServerThread(Socket socket, Consumer<String> onJSONReceived) {
		this.socket =socket;
		this.onJSONReceived = onJSONReceived;
		try {
			socket.setKeepAlive(false);
			socket.setSoTimeout(10000);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	@Override
	public void run() {
		try {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null)
				sb.append(line).append("\n");
			onJSONReceived.accept(sb.toString());
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
}
