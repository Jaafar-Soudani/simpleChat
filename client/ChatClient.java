// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  /*
   * Variable to hold the login ID
   */
  String loginId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }
  
  //setter
  public void SetLoginID(String id) 
  {
	  loginId = id;
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display("> " + msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	  String[] command = message.split(" ", 2);

	  switch(command[0]) 
	  {
	  	case "#quit":
	  		quit();
	  		break;
	  	case "#logoff":
			try {
				closeConnection();
				System.out.println("Connection closed");	
			} catch (IOException e1) {}
	  		break;
	  	case "#sethost":
	  		try 
	  		{
	  		setHost(command[1].trim());
	  		}catch(ArrayIndexOutOfBoundsException e) {}
	  		break;
	  	case "#setport":
	  		try {
		  		setPort(Integer.parseInt(command[1].trim()));
		  		}catch(ArrayIndexOutOfBoundsException e) {}
		  	break;
	  	case "#login":
	  		try 
	  		{
	  			openConnection();
			} catch (IOException e1) {}
	  		break;
	  	case "#gethost":
	  		clientUI.display(getHost());
	  		break;
	  	case "#getport":
	  		clientUI.display(String.valueOf(getPort()));
	  		break;
	  	default:
	  		if(message.charAt(0) == '#') 
	  		{
	  			System.out.println("Unknown command");
	  		}else
	  		{
	  			try
		  	    {
		  	      sendToServer(message);
		  	    }
		  	    catch(IOException e)
		  	    {
		  	      clientUI.display
		  	        ("Could not send message to server.  Terminating client.");
		  	      quit();
		  	    }
	  		}
	  		break;
	  }
    
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /*
   * Method to be called after the connection has been closed
   * */
  @Override
  	protected void connectionClosed() 
  	{
  		//clientUI.display("The server has shut down.");
  	}
  	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
	protected void connectionException(Exception exception) 
	{
	  clientUI.display("The server has shut down");
		quit();
	}
  /**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
  @Override
	protected void connectionEstablished() {
	  try
	  {
		  sendToServer("#login " + loginId);
	  }catch(IOException e) {};
	}
}
//End of ChatClient class
