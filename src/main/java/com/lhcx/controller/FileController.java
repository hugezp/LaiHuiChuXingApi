package com.lhcx.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.service.IFileService;
import com.lhcx.utils.Utils;

/**
 * 文件上传接口
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/api/file")
public class FileController {	
	private static Logger log = Logger.getLogger(FileController.class);
	
	@Autowired
	private IFileService fileService;
	
	/**
	 * 文件上传
	 * @content-type:multipart/form-data
	 * @param request
	 * @param jsonRequest
	 * @param myfile
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value = "/upload.do", method = RequestMethod.POST)
    public ResponseEntity<String> upload(HttpServletRequest request,@RequestParam("file") MultipartFile myfile) {    	
    	//取得参数值
        String jsonpCallback = request.getParameter("jsonpCallback");
        
    	ResultBean<?> resultBean = null;
    	Map<String,Object> result = new HashMap<String, Object>();
        String fileName = "";
        // 原始文件名   创建页面元素时的alt和title属性
        String originalFileName = "";
        String filePath = "";

        try {
            // 取得文件的原始文件名称
            fileName = myfile.getOriginalFilename();
            originalFileName = fileName;

            if(!fileName.isEmpty()){
                filePath = fileService.uploadFile(request,myfile,Utils.FILE_UPLOAD_PATH);
                int index = filePath.lastIndexOf("/");
                fileName = filePath.substring(index,filePath.length());
            } else {
                throw new IOException("文件名为空!");
            }
            result.put("url",filePath);
            result.put("title", originalFileName);
            result.put("original", originalFileName);
            resultBean = new ResultBean<Object>(ResponseCode.FILE_SUCCESS.value(),ResponseCode.FILE_SUCCESS.message(),result); 
            
        }catch (Exception e) {
            log.error("文件 "+fileName+" 上传失败!"+ e.getMessage());
            e.printStackTrace();
            result.put("url","");
            result.put("title", "");
            result.put("original", "");
            resultBean = new ResultBean<Object>(ResponseCode.FILE_FAILED.value(),ResponseCode.FILE_FAILED.message(),result); 
    		
        }
        return Utils.resultResponseJson(resultBean,jsonpCallback);
    }

}
