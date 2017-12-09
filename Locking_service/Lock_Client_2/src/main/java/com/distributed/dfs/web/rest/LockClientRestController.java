package com.distributed.dfs.web.rest; 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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


@Controller("LockClientRestController")
public class LockClientRestController 
{
	//Record File
	static String USER_IDENTITY = "CLIENT_2";
    static String SERVER_URL = "http://localhost:8080/";
	
	
	public String callFileLock()
	{
		URI append_url;
		try {
			append_url = new URI (SERVER_URL+ "LockFile/" + USER_IDENTITY);//restTemplate.put(append_url, String.class);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> Response  = restTemplate.getForEntity(append_url, String.class);
			return Response.getBody();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return "REQUEST_UNSUCCESSFUL";
		}
	}
	
	public String callFileRelease()
	{
		URI append_url;
		try {
			append_url = new URI (SERVER_URL+ "ReleaseFile/" + USER_IDENTITY);//restTemplate.put(append_url, String.class);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> Response  = restTemplate.getForEntity(append_url, String.class);
			return Response.getBody();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return "REQUEST_UNSUCCESSFUL";
		}
	}
	
	@RequestMapping(value = "/InitiateLock", method = RequestMethod.GET)
    @ResponseBody
    public String InitiateLock()  
    {
		System.out.println("InitiateLock Process started");
		return callFileLock();
    }
	
	
	@RequestMapping(value = "/InitiateRelease", method = RequestMethod.GET)
    @ResponseBody
    public String InitiateRelease()  
    {
		System.out.println("InitiateRelease Process started");
		return callFileRelease();
    }
	
	
    
    
	
  

    
    
}