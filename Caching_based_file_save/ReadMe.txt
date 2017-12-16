
Distributed File System - Caching based File server




****************************Concept*************************************

- The Client which reads a File from server, also gets the File version from the Server. The Version is saved in Client (Both the cached copy version identity and the Current Server version identity)
  The next time the clients checks the copy it is holding with the Server version before it makes a read request to the server.
  If the cached copy is same as the current server copy then the clinet uses the local copy. If not it requests the server





****************************Implementation******************************

- Coded in Java Spring boot
- The Server exposes Rest APIs for the Clients to request the server file
- When a client reads a file, a file version is also passed along with it.
- The client caches a copy and also maintains two version details (Server version and Cached version)
- When there is next request for read, it compares the version, if the version matches, it referes to the local copy
- If the values do not match then it requests the server
- Server also maintains a copy. When client saves the file, the version is updated in server and also the server version is passed to client along with success message so that the client can save the version details




****************************Demonstration*******************************

Requirements: Java 8, RestAPI tool (Postman)

- jar file are in this folder for Server, Client. The jar can be run with the command      "java -jar <filename>.jar"
- The Server runs on port 8080 and Client on 8090




(GET requests are to be used)
Steps:
 1 The Clinet can request for a file, this can be triggered with the api: http://localhost:8090/RequestFile
 2 The clinet send a read request to file
 3 The server sends the current server version, the client saves the cache copy version and and server copy version
 4 The Client can initiate the read request, this can be triggered with the api: http://localhost:8090/RequestFile
 5 The clinet will find the local version as the server version is not changed.
 6 The A message from the client will be returned saying that a local copy was available (The Client will not have requested the server)
 7 A save file request can be triggered with http://localhost:8090/SaveFile
 8 The Client will add some lines and save the file
 9 The version is incremented in server and the server sends success message along with the latest version, it is updated in the client
 



****************************Limitations**************************

Limitations for the demonstrations are below:

- The demonstration uses only one client
- There is no chance of the client facing a situation where it has an outdated version
- But this establishes the caching logic, this system forms the base for the Replication Service where such scenarios are encountered