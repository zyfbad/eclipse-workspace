package com.sportsshop.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;
import util.FastDFSClient;

@RestController
public class UploadController {
	
	@Value("${FILE_SERVER_URL}")
	private String file_server_url;
	
	@RequestMapping("/upload")
	public Result upload(MultipartFile file) {
		
		//获取文件的全名称以截取拓展名
		String originalFilename = file.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
		
		
		
		try {
			util.FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
			String fileId = client.uploadFile(file.getBytes(), extName);
			
			String url = file_server_url + fileId;
			return new Result(true, url);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "上传失败");
		}
		
		
	}
}
