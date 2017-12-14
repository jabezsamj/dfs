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


@Controller("DirectoryServiceServerRestController")
public class DirectoryServiceServerRestController 
{
	
	static HashMap<String, String> fileLocMap = new HashMap<String, String>();

	public String findFileLoc(String requestor)
	{
		if(fileLocMap.containsKey(requestor))
		{
			return fileLocMap.get(requestor);
		}
		else
		{
			return "FILE_ROUTE_NOT_FOUND";
		}
	}
	
	@RequestMapping(value = "/FindFile/{fileRequest}", method = RequestMethod.GET)
    @ResponseBody
    public String RequestFile(@PathVariable("fileRequest") String fileRequest) 
    {
		System.out.println("File Request Triggered");
		String[] fileRoute = fileRequest.split("-");
		String fileLoc = findFileLoc(fileRoute[0]);
		if(fileLoc == "FILE_ROUTE_NOT_FOUND")
		{
			return "FILE_ROUTE_NOT_FOUND";
		}
		
		if(fileRoute.length == 1)
		{
			return "FILE_ROUTE_NOT_FOUND";
		}
    	
		String fileLocUri = fileLoc + fileRoute[1];
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.getForEntity(fileLocUri, String.class);
		return result.getBody(); 	
    }
	

    static 
    {
    	fileLocMap.put("froute1", "http://localhost:8090/RequestFile/");
    	fileLocMap.put("froute2", "http://localhost:9000/RequestFile/");
    	fileLocMap.put("froute3", "http://localhost:9010/RequestFile/");
    }

    
    
}