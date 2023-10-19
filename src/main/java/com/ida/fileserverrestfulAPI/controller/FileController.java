package com.ida.fileserverrestfulAPI.controller;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/file")
public class FileController {
	@RequestMapping(value="/download/{recvNo}/{fileName}",method= RequestMethod.GET)
	//查詢(下載檔案)
	public ResponseEntity<Object> download(@PathVariable(name="recvNo") String recvNo,@PathVariable(name="fileName") String fileName) throws IOException {
		String path = String.format("%s/%s/%s","Case",recvNo,fileName);
		File file = new File(path);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", String.format("attachment; filename=%s", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> 
		responseEntity = ResponseEntity.ok()
						.headers(headers)
						.contentLength(file.length())
						.contentType(MediaType.parseMediaType("application/txt"))
						.body(resource);
		
		return responseEntity;
	}
	@RequestMapping(value="/upload/{recvNo}",method= RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	//新增(上傳檔案)
	public String fileUpload(@RequestParam("file") MultipartFile file,@PathVariable(name="recvNo") String recvNo) throws IOException {
		File uploadFile = new File("/var/tmp/"+file.getOriginalFilename());
		uploadFile.createNewFile();
		FileOutputStream out = new FileOutputStream(uploadFile);
		out.write(file.getBytes());
		out.close();
		return "File is upload successfully";
	}
}
