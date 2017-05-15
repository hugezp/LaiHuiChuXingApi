package com.lhcx.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	/**
	 * 
	 * @param request
	 * @param file
	 * @param pathName
	 * @return
	 */
	String uploadFile(HttpServletRequest request, MultipartFile file,String pathName);
}
