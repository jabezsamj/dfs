package com.distributed.dfs.web.rest; 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Controller("CachingFSServerRestController")
public class CachingFSServerRestController 
{
	//Record File
	static String RECORD_FILE_NAME = "access_file.txt";
	int ENTRY_COUNT =0;
	final static String newLine = System.getProperty("line.separator");
	LinkedHashMap<String,String> lockedFiles =new LinkedHashMap<String,String>();
	static LinkedHashMap<String,Integer> fileVersion =new LinkedHashMap<String,Integer>();
	static int versionNo = 0;
	
	
	public static void setRecordFile()
	{
	    File file = new File(RECORD_FILE_NAME);
		try {
			FileUtils.writeStringToFile(file, "Line1_BY_SERVER");
			FileUtils.writeStringToFile(file, newLine+"Line2_BY_SERVER", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fileVersion.put(RECORD_FILE_NAME, ++versionNo);
		System.out.println("File Version");
		for (Map.Entry entry : fileVersion.entrySet()) 
		{
		    System.out.println(entry.getKey() + ", " + entry.getValue());
		}
		
	}
	
	
	public String lockFile(String requestor)
	{
		if(lockedFiles.containsValue(requestor))
		{
		  return("REQUESTOR_ALREADY_HAS_A_LOCK");
			
		}
		else
		{
		  lockedFiles.put(RECORD_FILE_NAME +"_ACCESS_"+ ++ENTRY_COUNT, requestor);
			for (Map.Entry entry : lockedFiles.entrySet()) 
			{
			    System.out.println(entry.getKey() + ", " + entry.getValue());
			}
		  return("FILE_LOCK_SUCCESSFUL_FOR : " + requestor);
		}
	}
	

	public String releaseFile(String requestor)
	{
		if(lockedFiles.isEmpty())
		{
			return("NO_ENTRIES");
		}
		Map.Entry<String,String> firstEntry = lockedFiles.entrySet().iterator().next();
		
		if(firstEntry.getValue().equals(requestor))
		{
			lockedFiles.remove(firstEntry.getKey());
			for (Map.Entry entry : lockedFiles.entrySet()) 
			{
			    System.out.println(entry.getKey() + ", " + entry.getValue());
			}
			return("LOCK_REMOVED_FOR_REQUESTOR");
		}
		else
		{
			return("REQUESTOR_LOCK_IS_NOT_ON_TOP");
		}
	
	}
	
	
	@RequestMapping(value = "/LockFile/{requestor}", method = RequestMethod.GET)
    @ResponseBody
    public String LockFile(@PathVariable("requestor") String requestor) 
    {
		System.out.println("Lock Request Triggered");
		String result = lockFile(requestor);
		return result;
    }
	
	
	@RequestMapping(value = "/RequestFile/{requestor}", method = RequestMethod.GET)
    @ResponseBody
    public String RequestFile(@PathVariable("requestor") String requestor) 
    {
		System.out.println("File Request Triggered");
		String result = lockFile(requestor);
		
    	try {
    		File file = new File(RECORD_FILE_NAME);
    		String transferFile = FileUtils.readFileToString(file);
    		return transferFile+"::"+fileVersion.get(RECORD_FILE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
			return "Error";
		} 	
    }
	

	
	@RequestMapping(value = "/SaveFile/{requestor}", method = RequestMethod.POST, headers = "content-type=multipart/*")
    @ResponseBody
    public String ReleaseFile(@PathVariable("requestor") String requestor,  @RequestParam("file") MultipartFile transferFile) 
    {
		System.out.println("Save File Request Triggered");
		String result = releaseFile(requestor);
		if(result == "REQUESTOR_LOCK_IS_NOT_ON_TOP" || result == "NO_ENTRIES")
		{
			return "FILE_CANNOT_BE_SAVED";
		}
		else
		{
		    try 
		    {
			InputStream is = transferFile.getInputStream();
			String isToString = IOUtils.toString(is);
			FileUtils.writeStringToFile(new File(RECORD_FILE_NAME), isToString);
			
			fileVersion.put(RECORD_FILE_NAME, ++versionNo);
			System.out.println("File Version");
			  for (Map.Entry entry : fileVersion.entrySet()) {
			    System.out.println(entry.getKey() + ", " + entry.getValue());
			  }
			
			} 
		    catch (IOException e) 
		    {
			e.printStackTrace();
		    }
		    return "FILE_SAVED_SUCCESSFULLY_VERSION_" + versionNo;
		}
		
		
    }
	
	
    static 
    {
    	setRecordFile();
    	System.out.println("File created");
    }

    
    
}