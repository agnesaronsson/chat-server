/*@author Agnes Aronsson [agar3573]*/
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Server extends JFrame{
	private JTextArea chatWindow;
	private String message = "";
	private ServerSocket serverSocket;
	private int serverPort;
	private ArrayList<ClientThread> clients = new ArrayList<>();
	private int users = clients.size();
	
	//constructor
	public Server(int port) {
		super("Chat Server");
		serverPort = port;
		
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		add(new JScrollPane(chatWindow));
		setSize(400,200);
		setVisible(true);
	}
	
	//set up and run the server
	public void start() {
		try {
			serverSocket = new ServerSocket(serverPort);
			setTitle("Server: " + serverSocket.getInetAddress().getLocalHost().getHostName() + " Port: " + serverPort + " Clients: " + users + "\n");
			acceptClients();
		} catch(IOException ioe) {
			System.out.println("Could not listen on port: " + serverPort);
			System.exit(1);
		}
	}
	
	private void acceptClients() {
		while(true) { 		
			try {
				Socket clientSocket = serverSocket.accept();
				ClientThread client = new ClientThread(clientSocket);
				
				message = "CONNECTED: " + clientSocket.getInetAddress().getHostAddress() + "\n";
				showMessage(message);
				message = clientSocket.getInetAddress().getHostAddress() + " have joined"; 
				messageAllClients(message);
				
				clients.add(client);
			} catch(IOException ioe) {
				System.out.println("Accept failed on: " + serverPort);
			}
		}
	}

	// during the chat conversation
//	private void chat() throws IOException {
//		for(Socket socket : clients) {
//			try {
//				while(true) {
//					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//					showMessage(in.readLine());
//					messageAllClients(in.readLine());
//				}
//			} catch (IOException ioe) {
//				ioe.printStackTrace();
//			}
//		}
//	}
	
//	private void messageClient(Socket socket, String message) {
//		try {
//			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1"), true);
//			out.println(message);
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		}
//	}
	
	private void messageAllClients(String message) {
		for(ClientThread client : clients) {
			try {
				PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getSocket().getOutputStream(), "ISO-8859-1"), true);
				out.println(message);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}	
	}
	
	// updates chat window
	private void showMessage(final String message) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					chatWindow.append(message);
				}
			}
		);
	}
	
	public static void main(String[] args) {
		Server agnes;
		if(args.length == 1) {
			agnes = new Server(Integer.parseInt(args[0]));
		} else {
			agnes = new Server(2000);
		}
		agnes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		agnes.start();
	}
}
