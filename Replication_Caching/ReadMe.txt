
Distributed File System - Replication




****************************Concept*************************************

- The Client works based on the caching concept. If another client saves changes to server, server version is incremented. 
  The change in version is pushed to the clients in the file system. The client updates the version in its copy.





****************************Implementation******************************

- Coded in Java Spring boot
- The Server exposes Rest APIs for the Clients to request the server file
- The clients follow the caching logic
- When the file is saved in the server, the server pushes the version update to the clients by calling the clients
- The clients update the server version details for their reference


****************************Demonstration*******************************

Requirements: Java 8, RestAPI tool (Postman)

- jar file are in this folder for Server, Client. The jar can be run with the command      "java -jar <filename>.jar"
- The Server runs on port 8080 and Client1, Clinet2 on 8090 and 9000




(GET requests are to be used)
Steps:
 1 The Clinet1 can request for a file, this can be triggered with the api: http://localhost:8090/RequestFile
 2 The Clinet1 sends a read request to file
 3 The Client1 saves the version details
 4 The Client2 can be triggered for file request using: http://localhost:9000/RequestFile
 5 The Client2 also gets the file and it maintains a record of the versions
 6 The Client1 can be triggered to save the file using : http://localhost:8090/SaveFile
 7 The file is saved to Server, the version is updated
 8 The server pushed the updates to all the clients
 9 The Client2 cache copy is now outdated
 10 If client2 is triggered for file it finds that the cache copy does not match the server copy and it request the server instead of using the local copy
 



****************************Limitations**************************

Limitations for the demonstrations are below:

- The locaking concept is implicit, the demonstration follows the lockgin protocol
- The list of clients in the File system is a list maintained in the server, new clients cannot get the version update unless updated in the server