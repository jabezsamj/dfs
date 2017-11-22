package servers;


public class RunMultiThreadServer {
	
    public static void main (String args[])
    {
    	
    	MultiThreadedServer server = new MultiThreadedServer(9000);
    	new Thread(server).start();

    	MultiThreadedServer server = new MultiThreadedServer(9000);
        new Thread(server).start();
        
        final String host = "localhost";
        final int portNumber = 9000;
        //System.out.println("Stopping Server");
        //server.stop();
        
        for(int i=0;i<2;i++)
        {
            try
            {
            Socket socket = new Socket(host, portNumber);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("server says:" + br.readLine());

            socket.close();
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    	
    }
	
	

}
