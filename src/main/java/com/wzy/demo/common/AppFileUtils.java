package com.wzy.demo.common;

import java.io.File;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;

public class AppFileUtils {

    public static final String UPLOAD_PATH= Constast.UPLOAD_PATH;
    /**
     * 根据文件老名字得到新名字
     * @param oldName 文件老名字
     * @return 新名字由32位随机数加图片后缀组成
     */
    public static String createNewFileName(String oldName) {
        //获取文件名后缀
        String stuff=oldName.substring(oldName.lastIndexOf("."), oldName.length());
        //将UUID与文件名后缀进行拼接，生成新的文件名  生成的UUID为32位
        return IdUtil.simpleUUID().toUpperCase()+stuff;
    }

    /**
     * 文件下载
     * @param path
     * @return
     */
    public static ResponseEntity<Object> createResponseEntity(String path) {
        //1,构造文件对象
        File file=new File(UPLOAD_PATH, path);
        if(file.exists()) {
            //将下载的文件，封装byte[]
            byte[] bytes=null;
            try {
                bytes = FileUtil.readBytes(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //创建封装响应头信息的对象
            HttpHeaders header=new HttpHeaders();
            //封装响应内容类型(APPLICATION_OCTET_STREAM 响应的内容不限定)
            header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            //创建ResponseEntity对象
            ResponseEntity<Object> entity= new ResponseEntity<Object>(bytes, header, HttpStatus.CREATED);
            return entity;
        }
        return null;
    }

    /**
     * 更该文件的名字 去掉_temp
     * @param fileName
     * @return
     */
    public static String renameFile(String fileName) {
        File file = new File(UPLOAD_PATH,fileName);
        String replace = fileName.replace("_temp","");
        if (file.exists()){
            file.renameTo(new File(UPLOAD_PATH,replace));
        }
        return replace;
    }

    /**
     * 根据老路径删除文件
     * @param oldPath
     */
    public static void removeFileByPath(String oldPath) {
        //图片的路径不是默认图片的路径
        File file = new File(UPLOAD_PATH,oldPath);
        if (file.exists()){
            file.delete();
        }

    }    
}
