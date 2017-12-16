
Distributed File System - Transparent File System




****************************Concept*************************************

- Server Locks the File in the Server on first come first serve, using a priority queue. 
  The file can be accessed for reading which is not affected by the lock, but the client trying to save the file needs to be on the top of the priority queue (Access is denied if otherwise).
  If a client is low on the priority queue, the client will have to wait to save the file for the higher priority Client to save the file and release the file





****************************Implementation******************************

- Coded in Java Spring boot
- The Server exposes Rest APIs for the Clients to request read or to save the file
- The First Client to request read gets saved in a queue, the file is locked for the requesting client.
- Any following attemps from the same client does not get added to the queue, as the client already holds a lock
- The next client getting the read access with be added in the queue. Access is denied if attempts to save
- When the first requestor on the queue saves the file, the client is removed from the queue and the following client comes up in the queue.
- For demonstration, the Client propgrams writes some values before it tries to save the files.




****************************Demonstration*******************************

Requirements: Java 8, RestAPI tool (Postman)

- jar file are in this folder for Server, Client 1, Client 2. The jar can be run with the command      "java -jar xyz.jar"
- The server runs on port 8080 and Client 1 and Client 2 on 8090 and 9000 respectievely


(GET requests are to be used)
Steps:
 1 Client1 can be triggered for file request using api : http://localhost:8090/RequestFile
 2 Client1 recieves the file (Contents of the file are recieved on the tool, the contents are saved on the client side as a file)
 3 Client2 can be triggered for request file using the api: http://localhost:9000/RequestFile
 4 Client2 also recives the files
 5 Client2 can attempt to save the file by triggering : http://localhost:9000/SaveFile   (The client program writes some lines to the file before attempting File save)
 6 Client2 gets a denial from the server
 7 Client1 can attempt to save the file by triggering : http://localhost:8090/SaveFile
 8 The file is saved in the server copy and Client1 gets a success message
 9 Client2 can attempt save and the save will be successful 




****************************Limitations**************************

Limitations for the demonstrations are below:

- Only one file is used for demonstration
