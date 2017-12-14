package com.distributed.dfs.web.rest; 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


@Controller("ReplicationFSClientRestController")
public class ReplicationFSClientRestController 
{
	//Record File
	static String USER_IDENTITY = "CLIENT_1";
	static String RECORD_FILE_NAME = "client_copy.txt";
	final static String newLine = System.getProperty("line.separator");
    static String SERVER_URL = "http://localhost:8080/";
    static LinkedHashMap<String,Integer> currentVersion =new LinkedHashMap<String,Integer>();
	
	
	public void saveServerVersion(int versionNo)
	{
		currentVersion.put(RECORD_FILE_NAME+"_SERVER", versionNo);
	}
	
    
	public void saveLocalVersion(int versionNo)
	{
		currentVersion.put(RECORD_FILE_NAME+"_LOCAL", versionNo);
	}
	
    
	
	public String checkVersion()
	{
		if(currentVersion.containsKey(RECORD_FILE_NAME+"_SERVER") && currentVersion.containsKey(RECORD_FILE_NAME+"_LOCAL"))
		{
			if(currentVersion.get(RECORD_FILE_NAME+"_SERVER") == currentVersion.get(RECORD_FILE_NAME+"_LOCAL"))
			{
				return "LOCAL_VERSION";
			}
			return "NO_LOCAL_VERSION";	
		}
		
		return "NO_LOCAL_VERSION";
	}
	
	
	public String callFileLock()
	{
		URI append_url;
		try {
			append_url = new URI (SERVER_URL+ "LockFile/" + USER_IDENTITY);//restTemplate.put(append_url, String.class);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> Response  = restTemplate.getForEntity(append_url, String.class);
			return Response.getBody();
		} catch (URISyntaxException e) {
			return "REQUEST_UNSUCCESSFUL";
		}
	}
	
	
	public String callFileRequest() 
	{
		
		String versionStatus = checkVersion();
		if(versionStatus == "LOCAL_VERSION"){
			String lockStatus = callFileLock();
			return "LOCAL_VERSION_AVAILABLE  LOCK_STATUS : "+lockStatus;
		}
		
		URI append_url;
		try 
		{
			append_url = new URI (SERVER_URL+ "RequestFile/" + USER_IDENTITY);//restTemplate.put(append_url, String.class);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> Response  = restTemplate.getForEntity(append_url, String.class);
			//FileUtils.writeStringToFile(new File(RECORD_FILE_NAME), Response.getBody());
			String[] responseArray = Response.getBody().split("::");
			saveServerVersion(Integer.parseInt(responseArray[1]));
			saveLocalVersion(Integer.parseInt(responseArray[1]));
			System.out.println("Current File Version");
			for (Map.Entry entry : currentVersion.entrySet()) 
			{
			    System.out.println(entry.getKey() + ", " + entry.getValue());
			}
			return "RECIEVED_FILE_SUCCESSFULLY";
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
			return "REQUEST_UNSUCCESSFUL";
		}
	}
	

	public String writeToFile()
	{
		File file = new File(RECORD_FILE_NAME);
		for(int i=1;i<=50;i++)
		{
			try 
			{
	          FileUtils.writeStringToFile(file, newLine+"LINE_ADDED_BY_" + USER_IDENTITY, true);
		    } 
			catch (IOException e) {
			e.printStackTrace();
		    }
		}
       return "WRITE_SUCCESSFUL";
	}
	
	

	
	public String callFileSave()
	{
		String fileCacheResult = writeToFile();
		if(fileCacheResult == "FILE_CANNOT_BE_SAVED")
		{
			return "FILE_CANNOT_BE_SAVED";
		}
		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
    	parameters.add("file", new FileSystemResource(RECORD_FILE_NAME));
    	HttpHeaders headers = new HttpHeaders();
    	headers.set("Content-Type", "multipart/form-data");
    	headers.set("Accept", "text/plain");
    	RestTemplate restTemplate = new RestTemplate();
    	String result = restTemplate.postForObject(SERVER_URL+ "SaveFile/" + USER_IDENTITY, new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
    	if(result.contains("FILE_SAVED_SUCCESSFULLY_VERSION")){
    	String[] resultSplit = result.split("_");
    	saveLocalVersion(Integer.parseInt(resultSplit[4]));
    	saveServerVersion(Integer.parseInt(resultSplit[4]));
    	   System.out.println("Current File Version");
		   for (Map.Entry entry : currentVersion.entrySet()) 
		   {
		    System.out.println(entry.getKey() + ", " + entry.getValue());
		   }
    	}
		return result;
	}
	
	
	
	public void updateVersion(int versionNo)
	{
		saveServerVersion(versionNo);
		System.out.println("Current File Version");
		   for (Map.Entry entry : currentVersion.entrySet()) 
		   {
		    System.out.println(entry.getKey() + ", " + entry.getValue());
		   }
	}
	

	
	@RequestMapping(value = "/UpdateVersion/{versionNo}", method = RequestMethod.POST)
    @ResponseBody
    public void RequestFile(@PathVariable("versionNo") String versionNo) 
    {
		System.out.println("Recieved Version Update");
		updateVersion(Integer.parseInt(versionNo));	
    }
	
	
	@RequestMapping(value = "/RequestFile", method = RequestMethod.GET)
    @ResponseBody
    public String InitiateRequest()  
    {
		System.out.println("Initiate File Request");
		return callFileRequest();
    }
	
	
	@RequestMapping(value = "/SaveFile", method = RequestMethod.GET)
    @ResponseBody
    public String InitiateSave()  
    {
		System.out.println("Initiate Save File");
		return callFileSave();
    }
 
}