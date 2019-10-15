package com.example.tapp.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.example.tapp.data.exception.GeneralException;


@Component 
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:utils.yml") 

public class FileHandler {



	   private static final Logger log = LoggerFactory.getLogger(FileHandler.class); 


	   @Value("${exam.application.storage.location}") 
	   public String STORAGE; 
	 
	   @Value("${exam.application.storage.report-folder}") 
	   public String REPORT_FOLDER; 
	 
	   @Value("${exam.application.storage.no-image-name}") 
	   public String NO_IMAGE; 
	 
	   @Value("${exam.application.storage.user-profiles}") 
	   public String USER_PROFILES; 

	   @PostConstruct 
	   public void init() { 
	       log.info("Preparing storage."); 
	       String reportPath = STORAGE + REPORT_FOLDER; 
	       File file = new File(reportPath); 
	        if (!file.exists()) 
	           file.mkdir(); 

	       String userProfilePath = STORAGE + USER_PROFILES; 
	       File dirUserProfile = new File(userProfilePath); 
	       if (!dirUserProfile.exists()) 
	           dirUserProfile.mkdir(); 
	       log.info("Storage Prepared."); 
	   } 

	   public String saveUserProfile(byte[] fileData, String fileName) throws GeneralException { 
	       String newName = getNewName(getExtension(fileName)); 
	       String pathName = new StringBuilder(STORAGE).append(USER_PROFILES).append(File.separator).append(newName) 
	               .toString(); 
	       save(fileData, pathName); 
	       return newName; 
	   } 
	 
	   public boolean deleteUserProfile(String fileName) { 
	       String path = new StringBuilder(STORAGE).append(USER_PROFILES).append(File.separator).append(fileName) 
	               .toString(); 
	       return delete(path); 
	   } 

	   public byte[] getUserProfileImage(String fileName) { 
	       String path = new StringBuilder(STORAGE).append(USER_PROFILES).append(File.separator).append(fileName) 
	               .toString(); 
	       return readFileAsBytes(path); 
	   } 

	   public String saveReportFile(byte[] fileData, String fileName) throws GeneralException { 
	       
	String newName = getNewName(getExtension(fileName)); 
	       String pathName = new StringBuilder(STORAGE).append(REPORT_FOLDER).append(File.separator).append(newName) 
	               .toString(); 
	      
	save(fileData, pathName); 
	       return newName; 
	   } 

	    
	public boolean deleteReportFile(String fileName) { 
	     
	  String pathName = new StringBuilder(STORAGE).append(REPORT_FOLDER).append(File.separator).append(fileName) 
	               .toString(); 
 return delete(pathName); 
	   } 

	   public byte[] getReportFile(String fileName) { 
	       String pathName = new StringBuilder(STORAGE).append(REPORT_FOLDER).append(File.separator).append(fileName) 
	               .toString(); 
	       return readFileAsBytes(pathName); 
	   } 

	   public byte[] getDefaultNoImage() { 
	       String pathName = new StringBuilder(STORAGE).append(NO_IMAGE).toString(); 
	       return readFileAsBytes(pathName); 
	   } 

	   private String getNewName(String extention) { 
	       return UUID.randomUUID().toString() + "." + extention; 
	   } 

	   private String getExtension(String fileName) { 
	       String[] temp = fileName.split("\\."); 
	       return temp[temp.length - 1]; 
	   } 

	   private void save(byte[] fileData, String pathname) throws GeneralException { 
	       File file = new File(pathname); 
	       if (file.exists()) 
	           throw new GeneralException("File already exist."); 
	       try { 
	           file.createNewFile(); 
	           FileOutputStream fos = new FileOutputStream(file); 
	           fos.write(fileData); 
	           fos.close(); 
	       } catch (Exception e) { 
	           log.info(e.getMessage()); 
	           throw new GeneralException(e.getMessage()); 
	       } 
	   } 

	   private boolean delete(String pathname) { 
	       File file = new File(pathname); 
	       if (!file.exists()) 
	           return false; 
	       return file.delete(); 
	   } 

	   /* 
	    * private File getFile(String pathName) { return new File(pathName); } 
	    */ 

	   private byte[] readFileAsBytes(String pathName) { 
	       try { 
	           File file = new File(pathName); 
	           byte[] data = new byte[(int) file.length()]; 
	           FileInputStream fis = new FileInputStream(file); 
	           fis.read(data); 
	           fis.close(); 
	           return data; 
	       } catch (IOException e) { 
	           log.info(e.getMessage()); 
	           return null; 
	       } 

	   } 

	} 

