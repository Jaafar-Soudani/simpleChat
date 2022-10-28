import java.io.*;
import java.util.Scanner;

import common.*;
import server.*;

public class ServerConsole implements ChatIF  
{
	
	//Class variables *************************************************
	  
	  /**
	   * The default port to host on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	 //Instance variables **********************************************
	  
	  /**
	   * The instance of the server that created this ConsoleChat.
	   */
		EchoServer server;
	  
	  
	  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 

	  /**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	  public void display(String message) 
	  {
	    System.out.println("> " + message);
	  }
	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the ServerConsole UI.
	   *
	   * @param port The port to host on.
	   */
	  
	  
	  public ServerConsole(int port) 
	  {
		  server = new EchoServer(port, this);
		  try {
			  server.listen();
		  }catch(IOException e) {}
		  fromConsole = new Scanner(System.in);
		 
	  }
	  
	  
	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	  public void accept() 
	  {
		  try
		    {

		      String message;

		      while (true) 
		      {
		        message = fromConsole.nextLine();
		        server.handleMessageFromServerUI(message);
		      }
		    } 
		    catch (Exception ex) 
		    {
		      System.out.println
		        ("Unexpected error while reading from console!");
		    }
	  }
	  
	//Class methods ***************************************************
	  
	  /**
	   * This method is responsible for the creation of the Client UI.
	   *
	   * @param args[0] The host to connect to.
	   */
	  public static void main(String[] args) 
	  {
	    String host = "";
	    int port = DEFAULT_PORT;

	    try
	    {
	      host = args[0];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "localhost";
	    }
	    
	    try 
	    {
	    	port = Integer.parseInt(args[1]);
	    }catch(Exception e) 
	    {
	    	port = DEFAULT_PORT;
	    }
	    
	    ServerConsole chat= new ServerConsole(port);
	    chat.accept();  //Wait for console data
	  }

}


