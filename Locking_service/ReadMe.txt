
Distributed File System - Locking Service.




****************************Concept*******************************

- Server Locks the File in the Server for the first Client who requests first and 
  the second Client cannot access the file without the first requestor releasing the lock.





****************************Implementation************************

- Coded in Java Spring boot
- The Server exposes Rest APIs for the Clients to apply lock or to release lock
- The First Client to apply the lock gets saved in the server, the second Client gets a denial message if locking is attempted
- The first Client should release the lock so that the second Client can access the lock
- A demo file is used in the server




****************************Demonstration************************

Requirements: Java 8, RestAPI tool (Postman)

- jar file are in this folder for Server, Client 1, Client 2. The jar can be run with the command      "java -jar xyz.jar"
- The server runs on port 8080 and Client 1 and Client 2 on 8090 and 9000 respectievely


(GET requests are to be used)
Steps:
 1 Client1 apply the lock using the api: http://localhost:8090/InitiateLock
 2 Client1 recieves the message that the file is locked for Client1
 3 Client2 can try locaking using the api: http://localhost:9000/InitiateLock
 4 Client2 recieves the message that the file cannot be locaked and it is licked by Client1
 5 Client1 can release the lock using api : http://localhost:8080/InitiateRelease
 6 Client2 can apply lock using the same api above




****************************Limitations**************************

Limitations for the demonstrations are below:

- Only one client can own a lock in a given time
- Though a file is used in the server, file access is not given on owning a lock






