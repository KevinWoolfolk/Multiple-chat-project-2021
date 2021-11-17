//Kevin Woolfolk 
package chat;


import javax.swing.DefaultListModel;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Client implements Runnable{
	
	//Variables
	String userName;
	int port;
	DefaultListModel<String> chatArea;
	MulticastSocket socket;
	boolean messageExist;
	boolean exit;
	private InetAddress group;
	
	//Constructor
	Client(String userName, int port,DefaultListModel<String> chatArea,MulticastSocket socket ,boolean exit,InetAddress group){
	     this.userName = userName;
	     this.port = port;
	     this.socket = socket;
	     this.chatArea = chatArea;
	     this.exit = exit;
	     this.group = group;
	}
	
	//Method to set UserName
	 public void setUserName(String userName) {
		 this.userName = userName;
	 }
	 
	 //Method to exit from the socket
	 public void exit(){
		 this.exit = true;
	 }
	
	 //Thread that works as a message reader
	@Override
	public void run() {
		
	        //Read threads message
			while(!exit){
				//inisiate buffer
				 byte[] buffer = new byte[1000]; 
	             DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,group,port); 
	             String message; 
	             
	           
	             //Check if message Exist
	             try{
	            	 //receive message from socket
	            	 socket.receive(datagram); 
	                 message = new String(buffer,0,datagram.getLength(),"UTF-8"); 
	                 //Add to model JList
	                 chatArea.addElement(message);
	            	 
	             }catch(IOException e) 
	             { 
	                 System.out.println("Socket closed!"); 
	             } 
				  
				
			}
			//If user exit end socket
			try {
				socket.leaveGroup(group);
				socket.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
	}
	 
	


