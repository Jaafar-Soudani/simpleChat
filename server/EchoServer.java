// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
	
	//Instance variables **********************************************
	  
	  /**
	   * The interface type variable.  It allows the implementation of 
	   * the display method in the client.
	   */
	  ChatIF serverUI; 
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI; 
    
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  String[] message = msg.toString().split(" ", 2);
	  switch(message[0]) 
	  { 
	  	case "#login":
	  		if(client.getInfo("loginId") == null) 
	  		{
	  			client.setInfo("loginId", message[1].trim());
	  		}else 
	  		{
	  			try 
	  			{
		  			client.sendToClient("Cannot log in already logged in user, terminating connection");
		  			client.close();
	  			}catch(IOException e) {}
	  		}
	  		break;
	  		
	  	default:
	  		 serverUI.display("Message received: " + msg + " from " + client);
	  	    this.sendToAllClients(client.getInfo("loginId") + ": " +  msg);
	  }
   
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    serverUI.display
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  
  /*  
	public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  */
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) 
  {
	  serverUI.display("Client connected " + client.getInetAddress());
  }
  
  synchronized protected void clientException( ConnectionToClient client, Throwable exception) 
  {
	 serverUI.display("Client disconnected " + client.getInetAddress());
  }



	public void handleMessageFromServerUI(String message) 
	{
		String[] command = message.split(" ", 2);
		switch(command[0]) 
		{
			case "#quit":
				try
				{
					close();
				}catch(IOException e) {}
				System.exit(0);
				break;
			case "#stop":
				stopListening();
				break;
			case "#close":
				try
				{
					close();
				}catch(IOException e) {}
				break;
			case "#setport":
				try {
			  		setPort(Integer.parseInt(command[1].trim()));
			  		}catch(ArrayIndexOutOfBoundsException e) {}
			  	break;
			case "#start":
				try
				{
					listen();
				}catch(IOException e) {}
				break;
			case "#getport":
				serverUI.display(String.valueOf(getPort()));
		  		break;
			default:
				if(message.charAt(0) == '#') 
		  		{
		  			System.out.println("Unknown command");
		  		}
				else 
				{	
		  			serverUI.display(message);
		  			sendToAllClients("SERVER MSG> " + message);
				}
				break;
		}
		
		
	}

}
//End of EchoServer class
