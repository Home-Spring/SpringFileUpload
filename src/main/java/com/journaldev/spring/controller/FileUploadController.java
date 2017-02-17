package com.journaldev.spring.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.journaldev.spring.CreatedObjResp;

@Controller
public class FileUploadController {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    /**
     * Upload single file using Spring Controller
     * ******************************************
     * 200 OK («хорошо»)
     * 201 Created («создано»)
     * 400 Bad Request («плохой, неверный запрос»)
     * 406 Not Acceptable («неприемлемо»)
     *
     * @param name
     * @param file
     * @return
     */
    @RequestMapping(value = "/photo/upload", method = RequestMethod.POST)
    public @ResponseBody String photoUpload(@RequestParam("name") String name,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestParam(required = false, defaultValue = "false") boolean cachedImage) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File        dir = new File(rootPath + File.separator + "photos");
                if (!dir.exists()) dir.mkdirs();

                // Create the file on server
                File             serverFile = new File(dir.getAbsolutePath() + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                CreatedObjResp createdObjResp = new CreatedObjResp(name);
                logger.info("Server File Location=" + serverFile.getAbsolutePath() + " | " + createdObjResp.getId());
                return new CreatedObjResp(name).toString();
            } catch (Exception e) {
                logger.error("You failed to upload " + name + " => " + e.getMessage());
                return new CreatedObjResp().toString();
            }
        } else {
            logger.error("You failed to upload " + name + " because the file was empty.");
            return new CreatedObjResp().toString();
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value="/singleUpload")
    public String singleUpload(){
        return "singleUpload";
    }

    @RequestMapping(value="/singleSave", method=RequestMethod.POST )
    public @ResponseBody String singleSave(@RequestParam("file") MultipartFile file,
                                           @RequestParam("desc") String desc ){
        System.out.println("File Description:"+desc);
        String name = null;
        if (!file.isEmpty()) {
            try {
                name = file.getOriginalFilename();
                byte[] bytes = file.getBytes();

                /* Creating the directory to store file */
                String rootPath = System.getProperty("catalina.home");
                File        dir = new File(rootPath + File.separator + "photos");
                if (!dir.exists()) dir.mkdirs();
                /* Create the file on server */
                File serverFile = new File(dir.getAbsolutePath() + File.separator + name);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                return "You have successfully uploaded " + name;
            } catch (Exception e) {
                return "You failed to upload " + name + ": " + e.getMessage();
            }
        } else {
            return "Unable to upload. File is empty.";
        }
    }

    @RequestMapping(value="/multipleUpload")
    public String multiUpload(){
        return "multipleUpload";
    }

    @RequestMapping(value="/multipleSave", method=RequestMethod.POST )
    public @ResponseBody String multipleSave(@RequestParam("file") MultipartFile[] files){
        String name = null;
        String resp = "";
        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    name = files[i].getOriginalFilename();
                    byte[] bytes = files[i].getBytes();

                    /* Creating the directory to store file */
                    String rootPath = System.getProperty("catalina.home");
                    File        dir = new File(rootPath + File.separator + "photos");
                    if (!dir.exists()) dir.mkdirs();
                    /* Create the file on server */
                    File serverFile = new File(dir.getAbsolutePath() + File.separator + name);

                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    resp += "You have successfully uploaded " + name +"<br/>";
                } catch (Exception e) {
                    return "You failed to upload " + name + ": " + e.getMessage() +"<br/>";
                }
            }
            return resp;
        } else {
            return "Unable to upload. File is empty.";
        }
    }

}
