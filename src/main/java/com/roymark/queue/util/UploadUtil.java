package com.roymark.queue.util;

import cn.hutool.core.util.StrUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @author huangzien
 * @since 2019-10-14
 */
public class UploadUtil {
	private static final Logger logger = LogManager.getLogger(UploadUtil.class);
	public static String fileupload(HttpServletRequest request, MultipartFile upload,String uploadPath) throws Exception {
//		System.out.println("springmvc开始上传...");
		// 使用fileupload组件完成文件上传
		// 上传位置
		String path = request.getSession().getServletContext().getRealPath("")+uploadPath;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filename = upload.getOriginalFilename();

		String uuid = UUID.randomUUID().toString().replace("-", "");
		filename = uuid + "_" + filename;
		// 完成文件上传
		upload.transferTo(new File(path, filename));

		return uploadPath+filename;
	}

	public static boolean   uploadZipfile(HttpServletRequest request, MultipartFile upload,String exeType,String version)  {
		String hospitalPath="";//医院程序路径1
		String hospitalPath2="";//医院程序路径2
		String middleObjPath="";//中间件路径
		String inInterfacePath="";//内部接口路径
		String outInterfacePath="";//外部接口路径
		String apkUploadPath = "";//apk安卓程序路径
		String xmlPath="";
		boolean blResult=false;
		try {
			if (exeType.equals("1")) //医院程序
			{
				hospitalPath = request.getSession().getServletContext().getRealPath("") + "/RemoteQueue/Hospital/All/";
				hospitalPath2 = request.getSession().getServletContext().getRealPath("") + "/RemoteQueue/Hospital/Update/";
				File file = new File(hospitalPath);
				File file2 = new File(hospitalPath2);
				if (!file.exists()) {
					file.mkdirs();
				}
				else //文件夹1如果默认有，则先删除，再建立
				{
					deleteLocalDir(hospitalPath2);//带子文件夹删除
					file.mkdirs();
				}
				if (StrUtil.isNotEmpty(hospitalPath2) && !file2.exists()) {
					file2.mkdirs();
				}
				String filename = upload.getOriginalFilename();

				upload.transferTo(new File(hospitalPath, filename));
				upload.transferTo(new File(hospitalPath2, filename));
				String fullName1 = hospitalPath + filename;
				String fullName2 = hospitalPath2 + filename;

				File objFile1=new File(fullName1);
				UnPackeUtil.unPackZip(objFile1, "", hospitalPath);
				objFile1.delete();//解压后删除原文件

				File objFile2=new File(fullName2);
				UnPackeUtil.unPackZip(objFile2, "", hospitalPath2);
				objFile2.delete();//解压后删除原文件

			} else if(exeType.equals("2")){  //中间件程序

				middleObjPath = request.getSession().getServletContext().getRealPath("") + "/RemoteQueue/WindowsUpdateClient/";
				xmlPath = request.getSession().getServletContext().getRealPath("") + "/RemoteQueue/WindowsUpdateClient/UpdateNumList.xml";
				File file = new File(middleObjPath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String filename = upload.getOriginalFilename();
				upload.transferTo(new File(middleObjPath, filename));
				String fullName1 = middleObjPath + filename;
				File objFile1=new File(fullName1);
				UnPackeUtil.unPackZip(objFile1, "", middleObjPath);
				objFile1.delete();//解压后删除原文件
				File xmlFile=new File(xmlPath);
				if(xmlFile.exists())
				{
					xmlFile.delete();//已有则先删除再写入；
				}
				Document doc = DocumentHelper.createDocument();
				Element fileRoot = doc.addElement("UpdateNumList");
				//增加子节点
				Element childElement = fileRoot.addElement("UpdateNum");
				Element secondLevelChildElement = childElement.addElement("UpdateClientNum");
				secondLevelChildElement.setText(version);
				//实例化输出格式对象
				OutputFormat format = OutputFormat.createPrettyPrint();
				//设置输出编码
				format.setEncoding("UTF-8");
				XMLWriter writer = new XMLWriter(new FileOutputStream(xmlFile), format);
				writer.write(doc);
				writer.close();
			} else if (exeType.equals("3")) {//内部接口
				inInterfacePath = request.getSession().getServletContext().getRealPath("") + "/RemoteQueue/Queue_In/";
				File file = new File(inInterfacePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String filename = upload.getOriginalFilename();
				upload.transferTo(new File(inInterfacePath, filename));
				String fullName1 = inInterfacePath + filename;
				File objFile1=new File(fullName1);
				UnPackeUtil.unPackZip(objFile1, "", inInterfacePath);
				objFile1.delete();//解压后删除原文件
			} else if (exeType.equals("4")) {//外部接口
				outInterfacePath = request.getSession().getServletContext().getRealPath("") + "/RemoteQueue/Queue_Out/";
				File file = new File(outInterfacePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String filename = upload.getOriginalFilename();
				upload.transferTo(new File(outInterfacePath, filename));
				String fullName1 = outInterfacePath + filename;
				File objFile1=new File(fullName1);
				UnPackeUtil.unPackZip(objFile1, "", outInterfacePath);
				objFile1.delete();//解压后删除原文件
			} else if (exeType.equals("ShowExe_Pharmacy_Clinic_Android")) {
				apkUploadPath = request.getSession().getServletContext().getRealPath("") + "/RemoteQueue/Hospital/APK/ShowExe_Pharmacy_Clinic_Android/";
				File file = new File(apkUploadPath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String filename = upload.getOriginalFilename();
				upload.transferTo(new File(apkUploadPath, filename));
				/*
				 * String fullName1 = apkUploadPath + filename; File objFile1=new
				 * File(fullName1); UnPackeUtil.unPackZip(objFile1, "", apkUploadPath);
				 * objFile1.delete();//解压后删除原文件
				 */			
			}
			blResult=true;
		}
	catch(Exception ex)
	{
		logger.error("UploadUtil error:"+ex.getMessage(),ex);
		blResult=false;
	}
		return blResult;
	}

	public static void deleteLocalDir(String dir) {
		File file = new File(dir);
		if (file.exists()) {
			//delete()方法不能删除非空文件夹，所以得用递归方式将file下所有包含内容删除掉，然后再删除file
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					deleteLocalDir(f.getPath());
				}
			}
			file.delete();
		}
	}
}
