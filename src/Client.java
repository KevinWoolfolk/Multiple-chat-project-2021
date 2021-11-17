//Kevin Woolfolk 



import javax.swing.DefaultListModel;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
				
				 byte[] buffer = new byte[1000]; 
	             DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,group,port); 
	             String message; 
	             
	             //Check if message Exist
	             try{
	            	 //Remove upper text so list can always be visible
		            	 if(chatArea.size() == 10) {
		            		 chatArea.remove(chatArea.size()-10);
		            	 }
		            	 
		            	 //receive message from socket
		            	 socket.receive(datagram); 
		            	 message = new String(buffer,0,datagram.getLength(),"UTF-8");
		            	 
		                 
		                
		            	//Check broadcast test 
		            	 boolean isFound = message.indexOf("100broadcast") !=-1? true: false;
		            	 boolean isFound1000 = message.indexOf("1000broadcast") !=-1? true: false;
		            	 boolean isFound10000 = message.indexOf("10000broadcast") !=-1? true: false;
		            	 
		          
	
		            	 //Add to model JList
		                 chatArea.addElement(message);
		                 
		                 //Broadcast 100 message
		                 if(isFound) {
		            		 for(int i=0;i<100;i++) {
			            		 if(chatArea.size() == 10) {
				            		 chatArea.remove(chatArea.size()-10);
				            	 }
			            		 Thread.sleep(100);
			            		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
			 					 LocalDateTime now = LocalDateTime.now(); 
			 					
			 					//Add message to user screen
			 					 String newMessage = "King User 100("+dtf.format(now)+")> Broadcast number: "+ String.valueOf(i);;
			            		 chatArea.addElement(newMessage);
		            		 }
		            	 }
		               //Broadcast 1000 message
		                 if(isFound1000) {
		            		 for(int i=0;i<1000;i++) {
			            		 if(chatArea.size() == 10) {
				            		 chatArea.remove(chatArea.size()-10);
				            	 }
			            		 Thread.sleep(10);
			            		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
			 					 LocalDateTime now = LocalDateTime.now(); 
			 					
			 					//Add message to user screen
			 					 String newMessage = "King User 1000("+dtf.format(now)+")> Broadcast number: "+ String.valueOf(i);;
			            		 chatArea.addElement(newMessage);
		            		 }
		            	 }
		                 
		                 //Broadcast 10000 message
		                 if(isFound10000) {
		            		 for(int i=0;i<10000;i++) {
			            		 if(chatArea.size() == 10) {
				            		 chatArea.remove(chatArea.size()-10);
				            	 }
			            		 Thread.sleep(1);
			            		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
			 					 LocalDateTime now = LocalDateTime.now(); 
			 					
			 					//Add message to user screen
			 					 String newMessage = "King User 10000("+dtf.format(now)+")> Broadcast number: "+ String.valueOf(i);;
			            		 chatArea.addElement(newMessage);
		            		 }
		            	 }
	            	 
	            	 
	             }catch(IOException e) 
	             { 
	                 System.out.println("Socket closed!"); 
	             } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
	 
	


