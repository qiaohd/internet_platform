package com.ufgov.ip.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.springframework.web.multipart.MultipartFile;

public class ImageCutTool {

	
	 public static void abscut(MultipartFile pictureFile,String srcImageFile,int x, int y, int destWidth,  
	            int destHeight,int srcWidth,int srcHeight) {  
		 FileInputStream is = null;
		 ByteArrayInputStream bis=null;
	     ImageInputStream iis = null;  
	        
	        try {  
	            Image img;  
	            ImageFilter cropFilter;  
	            // 读取源图像  
	            BufferedImage bi = ImageIO.read(new File(srcImageFile));  
	                     
	           if (srcWidth >= destWidth && srcHeight >= destHeight) {  
	                Image image = bi.getScaledInstance(320, 320,  
	                        Image.SCALE_DEFAULT);  
	                // 改进的想法:是否可用多线程加快切割速度  
	                // 四个参数分别为图像起点坐标和宽高  
	                // 即: CropImageFilter(int x,int y,int width,int height)  
	                cropFilter = new CropImageFilter(x, y, destWidth, destHeight); 
	                //Rectangle rect = new Rectangle(x, y, destWidth, destHeight); 
	                img = Toolkit.getDefaultToolkit().createImage(  
	                        new FilteredImageSource(image.getSource(), cropFilter));  
	                BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_BGR); 
	                Graphics g = tag.getGraphics();  
	                g.drawImage(img, 0, 0, null); // 绘制缩小后的图  
	                g.dispose();
	                
	                //File file=new File("src\\main\\webapp\\upload\\cutImages\\"+"a.jpg");
	                // 输出为文件  
	                ImageIO.write(tag, "JPG", new File(srcImageFile));  
	            }  
	          
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	
	
}
	 

