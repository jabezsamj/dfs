package servers;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SingleThreadedServer implements Runnable
{
    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    public SingleThreadedServer(int port)
    {
        this.serverPort = port;
    }

    public void run()
    {
        synchronized(this)
        {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        
        while(! isStopped())
        {
            Socket clientSocket = null;
            try 
            {
                clientSocket = this.serverSocket.accept();
            } 
            catch (IOException e) 
            {
                if(isStopped()) 
                {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            try 
            {
                processClientRequest(clientSocket);
            } 
            catch (Exception e) 
            {
                //log exception and go on to next request.
            }
        }
        
        System.out.println("Server Stopped.");
    }

    private void processClientRequest(Socket clientSocket)
    throws Exception 
    {
        InputStream  input  = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream();
        long time = System.currentTimeMillis();
        
        
        String msg_val = "Hello";
        byte[] response = msg_val.getBytes();


        output.write(response);

        output.close();
        input.close();
        System.out.println("Request processed: " + time);
    }

    private synchronized boolean isStopped() 
    {
        return this.isStopped;
    }

    public synchronized void stop()
    {
        this.isStopped = true;
        try 
        {
            this.serverSocket.close();
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() 
    {
        try 
        {
            this.serverSocket = new ServerSocket(this.serverPort);
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Cannot open port: " + this.serverPort, e);
        }
    }
}
