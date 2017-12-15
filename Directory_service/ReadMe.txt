
Distributed File System - Directory Service.




****************************Concept*************************************

- The Directory service has a map of the different File servers. When there is a request for file, 
  the Directory server uses the file route to make a decision on where to find the files, and redirects the requests.
  The files are accessed by the client via the directory server.





****************************Implementation******************************

- Coded in Java Spring boot
- The Server exposes Rest APIs for the Clients to request file
- The server maintains a map of 'Fileroutes' and the corresponding file server location
- Three file servers can be accessed from the Directory server, each hosted on different ports
- The the file request should contain two parts seperated by hyphen symbol (-)
- Using regular expression the file location is split into two parts
- The Directory server uses the first part of the route to redirect to the correct File server
- The Directory server appends the second part of the request when redirecting to the File server
- The appended second part is used by the File server to locate the file in the File server (File server maintains a map to convert the filenames)
- The response is passed back from the File Server to the requesting client via the directory server




****************************Demonstration*******************************

Requirements: Java 8, RestAPI tool (Postman)

- jar file are in this folder for Server, Client 1, Client 2. The jar can be run with the command      "java -jar <filename>.jar"
- The DirectoryServer runs on port 8080 and FileServer1,2,3 on 8090,9000,9010 respectievely

- There are Three FileServers
- There are three files in each of the FileServers, the files are identified by the name:  fileOne, fileTwo, fileThree
- Dummy lines are entered in these file (These lines self identify the filelocation)
- There are specific file routes to access each of the File servers, the Routes for the three File Servers are: froute1, froute2, froute3 
- These names are to be stricly to be followed 


(GET requests are to be used)
Steps:
 1 The Files can be accessed via the directory server with an api like : http://localhost:8080/FindFile/<fileRouteName>-<fileName>
          Eg:-   http://localhost:8080/FindFile/froute1-fileThree
 
 2 Similar requests can be used to access any of the 9 files as mentioned above
 3 Any change in the api format will recieve an error from the server



****************************Limitations**************************

Limitations for the demonstrations are below:

- The setup is using 3 FileServers and 3 files per FileServer