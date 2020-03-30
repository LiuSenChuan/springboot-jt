package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.EasyUIImage;

@RestController
public class FileController {

	
	@Autowired
	private FileService fileService;
	/**
	 * 文件上传需要添加文件上传视图解析器. 该解析器由SpringMVC负责管理.
	 * 编辑参数接收类型. MultipartFile
	 * @param fileImage
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//获取图片名称
		String name = fileImage.getOriginalFilename();
		//准备文件上传路径
		File dirFile = new File("D:\\JT_IMAGE");
		//校验路径是否存在
		if (!dirFile.exists()) {
			//如果路径不存在,则创建路径
			dirFile.mkdirs();
		}
		//实现文件上传
		String filePath = "D:/JT_IMAGE/" + name;
		fileImage.transferTo(new File(filePath));
		return "调用成功";
	}

	/**
	 * /pic/upload?dir=image
	 * @param uploadFile
	 * @return
	 */
	@RequestMapping("/pic/upload")
	public EasyUIImage uploadFile(MultipartFile uploadFile) {
		return fileService.uploadFile(uploadFile);
		
	}
}
