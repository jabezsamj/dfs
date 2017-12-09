package com.distributed.dfs.web.rest; 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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


@Controller("LockServerRestController")
public class LockServerRestController 
{
	//Record File
	static String RECORD_FILE_NAME = "access_file.txt";
	HashMap<String,String> lockedFiles =new HashMap<String,String>();
	
	public static void setRecordFile()
	{
		File f = new File(RECORD_FILE_NAME);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String lockFile(String requestor)
	{
		if(lockedFiles.containsKey(RECORD_FILE_NAME))
		{
			Object value = lockedFiles.get(RECORD_FILE_NAME);
			return("FILE_IN_USE_BY : " + value.toString());
			
		}
		else
		{
			lockedFiles.put(RECORD_FILE_NAME, requestor);
			for (Map.Entry entry : lockedFiles.entrySet()) 
			{
			    System.out.println(entry.getKey() + ", " + entry.getValue());
			}
			return("FILE_LOCK_SUCCESSFUL_FOR : " + requestor);
		}
		
		
	
	}
	
	
	public String releaseFile(String requestor)
	{
		if(lockedFiles.containsValue(requestor))
		{
			lockedFiles.values().remove(requestor);
			for (Map.Entry entry : lockedFiles.entrySet()) 
			{
			    System.out.println(entry.getKey() + ", " + entry.getValue());
			}
			return("LOCK_REMOVED_FOR : " + requestor);
		}
		else
		{
			return("NO_LOCKS_FOR : " + requestor);
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
	
	@RequestMapping(value = "/ReleaseFile/{requestor}", method = RequestMethod.GET)
    @ResponseBody
    public String ReleaseFile(@PathVariable("requestor") String requestor) 
    {
		System.out.println("Release Request Triggered");
		String result = releaseFile(requestor);
		return result;
    }
	
    static 
    {
    	setRecordFile();
    	System.out.println("File created");
    }

    
    
}