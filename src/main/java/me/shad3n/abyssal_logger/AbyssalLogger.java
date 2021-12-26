package me.shad3n.abyssal_logger;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbyssalLogger implements Runnable{
	
	private final int serverPort;
	private ServerSocket serverSocket;

	public AbyssalLogger(int port) {
		this.serverPort = port;	
	}

	public abstract void onMessageReceived(String onJSONReceived);

	public void begin() throws IOException {
		serverSocket =  new ServerSocket(serverPort);
		Thread thread = new Thread(this);
		thread.start();
		Runtime.getRuntime().addShutdownHook(new Thread(){
		    @Override
		    public void run()
		    {
		    	stop();
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
    }
	
	public void stopApp(int delayMs) {
		new Timer().schedule( 
	        new TimerTask() {
	            @Override
	            public void run() {
					try {
						stopServer();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.exit(0);
	            }
	        }, 
        delayMs);
	}

    public void run() { 
        while(true) {
			try {
				new ServerThread( serverSocket.accept(),this::onMessageReceived).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }

	public void stopServer() throws IOException {
		serverSocket.close();
	} 
   
	
}
