package com.ufgov.ip.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 批量导入工具，主要获得excel中的数据和标题
 * @author zhangbch
 *
 */
public class BathImportUtil {
	
    private Workbook wb;
    private Sheet sheet;
    private Row row;
    
    public String[] readExcelTitle(InputStream is,String baseURL) throws FileNotFoundException {
    	String suffix = baseURL.substring(baseURL.lastIndexOf("."));  // 文件后辍.
		ArrayList<String[]> list = new ArrayList<String[]>();
    	is=new FileInputStream(new File(baseURL));
    	
    	//支持office2007
    			try{
    				if (".xlsx".equals(suffix.toLowerCase())) {
    					wb = new XSSFWorkbook(is);
    				}
    				else{
    					//支持office2003
    		        	wb = new HSSFWorkbook(is);
    				}
    			}catch(Exception ex){
    				ex.printStackTrace();
    			}
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getCellFormatValue(row.getCell(i));
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    public List<String[]> readExcelContent(InputStream is,String formFileName) {
		String suffix = formFileName.substring(formFileName.lastIndexOf("."));  // 文件后辍.
		ArrayList<String[]> list = new ArrayList<String[]>();
		//支持office2007
		try{
			if (".xlsx".equals(suffix.toLowerCase())) {
				wb = new XSSFWorkbook(is);
			}
			else{
				//支持office2003
	        	wb = new HSSFWorkbook(is);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
        sheet = wb.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            String[] valStr = new String[colNum];
            while (j < colNum) {
            	String content = "";
            	content = getCellFormatValue(row.getCell(j)).trim();
                valStr[j] = content;
                j++;
            }
            list.add(valStr);
        }
       
        return list;
    }

    private String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
            	cell.setCellType(Cell.CELL_TYPE_STRING);
            	cellvalue = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_FORMULA: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                }
                break;
            }
            case HSSFCell.CELL_TYPE_STRING:
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            default:
                cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }
    
    @SuppressWarnings("resource")
	public static boolean checkFormat(BathImportUtil excelReader,InputStream is,InputStream template_is,String baseURL,HttpServletRequest request){
		
    	try{
    	//校验1_格式校验
		String[] readExcelTitle = excelReader.readExcelTitle(is,baseURL);//获得上传的excel标题
		String url2 = UploadFileURLUtils.getUrl("template", "templateFilePath");
		String path_pro = request.getServletContext().getRealPath("/");
		path_pro=path_pro + "\\" + url2;
		path_pro=path_pro.replace("\\", "/");
		File file = new File(path_pro);
		
		String templateName="";
		File[] listFiles = file.listFiles();
		if(listFiles!=null){
			for (File file2 : listFiles) {
				if(file2.isFile()){
					templateName=file2.getName();
					break;
				}
			}
		}
		String path = file.getAbsolutePath();
		String url=path+"\\" + templateName;
		url=url.replace("\\", "/");
		template_is = new FileInputStream(url);
		String[] templateTitle = excelReader.readExcelTitle(is,url);//获得模板的excel标题
		boolean equals = Arrays.equals(readExcelTitle, templateTitle);
    	return equals;
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
		return false;
    	
    }
    
    
    public static void main(String[] args) throws FileNotFoundException {
		
    	/*BathImportUtil util=new BathImportUtil();
        InputStream is = new FileInputStream("D:\\Book1.xlsx");
    	String[] readExcelTitle = util.readExcelTitle(is);
    	for (String string : readExcelTitle) {
			System.out.println(string);
		}
    	*/
    	
	}
}
