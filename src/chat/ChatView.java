//Kevin Woolfolk 
package chat;

//imports
import java.awt.EventQueue;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import javax.swing.JFrame;
import javax.swing.JTextField;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JList;

import javax.swing.JPanel;
import java.awt.Color;

public class ChatView {

	JFrame frame;
	
	//Set all variables
	private JTextField textField_message;
	private JTextField userNameField;
	String userName = "";
	InetAddress group = null;
	MulticastSocket socket = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatView window = new ChatView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//All the JFrame was made in the Design part putting JList,JLabel,JButton,JPanel,JTextfield and DefaultListModel
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.ORANGE);
        panel.setBounds(41, 11, 330, 188);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        
        JButton btnNewButton = new JButton("Start chat");
        
        btnNewButton.setBounds(91, 140, 136, 23);
        panel.add(btnNewButton);
        
        userNameField = new JTextField();
        userNameField.setBounds(45, 109, 230, 20);
        panel.add(userNameField);
        userNameField.setColumns(10);
        
        JLabel lblEnterYourUsername = new JLabel("Enter your username");
        lblEnterYourUsername.setForeground(Color.BLACK);
        lblEnterYourUsername.setBounds(108, 60, 167, 23);
        panel.add(lblEnterYourUsername);
        
      
        
        
        JButton btnSend = new JButton("Send");
        btnSend.setBounds(293, 216, 89, 23);
        frame.getContentPane().add(btnSend);
        
        JLabel chatArea = new JLabel("CHAT");
        chatArea.setBounds(173, 11, 105, 14);
        frame.getContentPane().add(chatArea);
        
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>( model );
        list.setBounds(13, 33, 369, 172);
        frame.getContentPane().add(list);
        
        list.setLayoutOrientation(JList.VERTICAL);
        
        textField_message = new JTextField();
        textField_message.setBounds(20, 217, 272, 20);
        frame.getContentPane().add(textField_message);
        textField_message.setColumns(10);
        
        JButton leave_chat = new JButton("Exit");
       
        leave_chat.setBounds(361, 0, 73, 23);
        frame.getContentPane().add(leave_chat);
		
        
        //Initiate port
		int port = 9000;
		
		
		
		try {
			//Connect to socket Server by Inet Address and MulticastSocket.
			group = InetAddress.getByName("239.0.0.0");
			socket = new MulticastSocket(port);
			//Put it on 0 because we are localhost
			socket.setTimeToLive(0); 
			socket.joinGroup(group); 
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
	 //Create user and start Thread
		Client client = new Client(userName,port,model,socket,false,group);
		Thread clientThread = new Thread(client);
		clientThread.start();
		
		//First listener for userName button to set a Username
		btnNewButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		//Get Username and set the name to the client.
        		userName = userNameField.getText().toString();
        		client.setUserName(userName);
        		
        		//Set visible false the orange screen so chat start
        		panel.setVisible(false);
        	}
        });
		
		//Second listener to send message to server
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//Get DateTime from action to send message
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
				LocalDateTime now = LocalDateTime.now(); 
				
				//Add message to user screen
				String newMessage = userName+"("+dtf.format(now)+")> "+ textField_message.getText();
				byte[] buffer = newMessage.getBytes(); 
				DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,group,port); 
				try {
					//Send data to socket and users
					socket.send(datagram);
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
				
			    //Reset Textfield
			    textField_message.setText("");
			}
		});
		
		//Third listener to exit 
		 leave_chat.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent arg0) {
	        		//Close socket properly and application window
	        		client.exit();
	        		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	        	}
	     });
	}
}
