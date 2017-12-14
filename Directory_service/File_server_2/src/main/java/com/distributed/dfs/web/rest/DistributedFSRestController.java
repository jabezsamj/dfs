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


@Controller("DistributedFSRestController")
public class DistributedFSRestController 
{
	//Record File
	static String USER_IDENTITY = "FS_2";
	static File RECORD_FILE_1 = new File("server_copy_one.txt");
	static File RECORD_FILE_2 = new File("server_copy_two.txt");
	static File RECORD_FILE_3 = new File("server_copy_three.txt");
	
    static String DIRECTORY_SERVER_URL = "http://localhost:8080/";
    static HashMap<String, String> fileMap = new HashMap<String, String>();
    final static String newLine = System.getProperty("line.separator");
	
	public String callFileRequest(String fileName)
	{
		if(fileMap.containsKey(fileName))
		{
			File foundFile = new File(fileMap.get(fileName));
			try {
				return FileUtils.readFileToString(foundFile);
			} catch (IOException e) {
				return "ERROR_READING_FILE";
			}
		}
		else
		{
			return "FILE_NOT_FOUND";
		}
	}
	

	@RequestMapping(value = "/RequestFile/{fileName}", method = RequestMethod.GET)
    @ResponseBody
    public String InitiateRequest(@PathVariable("fileName") String fileName)  
    {
		System.out.println("Initiated File Request");
		return callFileRequest(fileName);
    }
	
	
	static
	{
		try {
			FileUtils.writeStringToFile(RECORD_FILE_1, "File_one_Line_By_"+ USER_IDENTITY, true);
			FileUtils.writeStringToFile(RECORD_FILE_2, "File_two_Line_By_"+ USER_IDENTITY, true);
			FileUtils.writeStringToFile(RECORD_FILE_3, "File_three_Line_By_"+ USER_IDENTITY, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fileMap.put("fileOne", "server_copy_one.txt");
		fileMap.put("fileTwo", "server_copy_two.txt");
		fileMap.put("fileThree", "server_copy_three.txt");	
	}

   
}