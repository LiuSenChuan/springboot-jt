package com.jt.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.EasyUIImage;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {

	@Value("${image.fileDirPath}")
	private String fileDirPath;
	@Value("${image.urlPath}")
	private String urlPath;
	/**
	 * 实现思路
	 * 1.校验是否为图片(img|png|gif...)
	 * 2.防止恶意程序
	 * 3.分目录保存图片(按照时间划分)
	 * 4.防止图片重名
	 */
	@Override
	public EasyUIImage uploadFile(MultipartFile uploadFile) {

		//图片校验
		//1.1获取图片名称
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();
		//1.2利用正则表达式判断是否为图片
		if (!fileName.matches("^.+\\.(img|png|gif)$")) {
			return EasyUIImage.fail();
		}
		//防止恶意程序....根据宽高实现图片的校验
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			if (width==0||height==0) {
				return EasyUIImage.fail();
			}
			
			//证明图片类型正确,分目录储存
			String dataPath = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
			//3.1本地路径
			String fileLocalPath = fileDirPath + dataPath;
			File dirFile = new File(fileLocalPath);
			if (!dirFile.exists()) {
				 dirFile.mkdirs();
			}
			
			//实现文件上传
			String uuid = UUID.randomUUID().toString().replace("-", "");
			//4.1截取后缀
			int index = fileName.lastIndexOf(".");
			String fileType = fileName.substring(index);
			String realFileName = uuid + fileType;
			//4.2全路径
			String realFilePath = fileLocalPath + realFileName;
			uploadFile.transferTo(new File(realFilePath));
			 //虚拟路径
			String url = urlPath + dataPath + realFileName;
			return EasyUIImage.success(url, width, height);
		} catch (IOException e) {
			e.printStackTrace();
			return EasyUIImage.fail();
		}
		
		
		
		
	}

}
