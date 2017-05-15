package com.lhcx.service.impl;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.lhcx.service.IFileService;
import com.lhcx.utils.MD5Kit;
import com.lhcx.utils.OssUtil;
import com.lhcx.utils.Utils;

/**
 * Created by Administrator on 2017/4/14.
 */
@Component
public class FileServiceImpl implements IFileService {
    @Autowired
    private OssUtil ossUtil;
    
    public String uploadFile(HttpServletRequest request, MultipartFile file,String pathName){
        // 文件保存路径
        String filePath = "";
        if(Utils.isNullOrEmpty(pathName)){
        	pathName = ossUtil.getOssConfigure().getBucketName();
        }
        
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                int index = fileName.lastIndexOf(".");
                String extention = fileName.substring(index);//文件扩展名
                String orginalName = fileName.substring(0,index);

                // 添加时间戳 保证上传的文件名称唯一
                fileName = MD5Kit.encode(orginalName) + "_"+ System.currentTimeMillis() + extention;
                String directory =  request.getSession().getServletContext().getRealPath("/") + pathName+ "/";
                filePath =  directory + fileName;

                // 创建保存的文件

                File newFile = new File(directory);
                //判断上传文件的保存目录是否存在
                if (!newFile.exists()) {
                    //创建目录
                    newFile.mkdir();
                }

                // 转存文件-本地
                file.transferTo(new File(filePath));

                //上传到oss
                String image_local = filePath.substring(filePath.indexOf(pathName));
                String arr[] = image_local.split("\\\\");
                String image_oss = arr[arr.length - 1];
                if (ossUtil.uploadFileWithResult(request, image_oss, image_local)) {
                    filePath = ossUtil.getOssConfigure().getAccessUrl() + image_oss;
                }

            } catch (Exception e) {
                e.printStackTrace();
                filePath = "";
            }
        }
        return filePath;
    }
}
