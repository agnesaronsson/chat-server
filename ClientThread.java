/*@author Agnes Aronsson [agar3573]*/
import java.io.*;
import java.net.*;

public class ClientThread implements Runnable {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	Thread t = new Thread(this);
	
	public ClientThread(Socket socket) {
		this.socket = socket;
		t.start();
	}
	
	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1"), true);
			
			while(!socket.isClosed()) {
				String message = in.readLine();
			if(message != null) {
					out.println(message);
				}
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public BufferedReader getReader() {
		return in;
	}
	
	public PrintWriter getWriter() {
		return out;
	}
}
