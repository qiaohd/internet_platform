package com.ufgov.ip.service.system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.DatapartitionServiceI;
import com.ufgov.ip.dao.system.DataPartitionMapper;
import com.ufgov.ip.dao.system.IpHirerMapper;
import com.ufgov.ip.entity.database.ConnectionInfo;
import com.ufgov.ip.entity.database.Database;
import com.ufgov.ip.entity.system.IpDataPartition;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.service.base.MysqlConnection;
import com.ufgov.ip.utils.JdomXMLUtil;
import com.ufgov.ip.utils.PropertyUtil;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.ufgov.ip.utils.UUIDTools;
import com.yonyou.iuap.mybatis.type.PageResult;

@Service(version = "0.0.1")
public class DataPartitionService implements DatapartitionServiceI{
	
	@Autowired
	DataPartitionMapper dataPartitionMapper;
	@Autowired
	HirerAccountService hirerAccountService;
	@Autowired
	private JdomXMLUtil jdomXMLUtil;
	@Autowired
	private IpHirerMapper ipHirerMapper;
	
	String defaultSchema= "information_schema";
	
	public Connection testCon() {
		String web_inf_Path=new File(DataPartitionService.class.getResource("/").getPath()).getParent();
		try {
			File file = new ClassPathResource("db").getFile();
			File[] tempList = file.listFiles();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		/*String driver="com.mysql.jdbc.Driver";   //获取mysql数据库的驱动类  
        String url="jdbc:mysql://localhost:3306/fntop"; //连接数据库（kucun是数据库名）  
        String name="root";//连接mysql的用户名  
        String pwd="root";//连接mysql的密码  
        try{  
            Class.forName(driver);  
            Connection conn=DriverManager.getConnection(url,name,pwd);//获取连接对象  
            return conn;  
        }catch(ClassNotFoundException e){  
            e.printStackTrace();  
            return null;  
        }catch(SQLException e){  
            e.printStackTrace();  
            return null;  
        }  	*/
		String path="D:\\sql";
		File file=new File(path);
		File[] tempList = file.listFiles();
		System.out.println("该目录下对象个数："+tempList.length);
		for (int i = 0; i < tempList.length; i++) {
		   if (tempList[i].isFile()) {
			   System.out.println("文     件："+tempList[i]);		   
			   SQLExec sqlExec = new SQLExec();
			   sqlExec.setDriver("com.mysql.jdbc.Driver");
			   sqlExec.setUrl("jdbc:mysql://localhost:3306/fntop");
			   sqlExec.setUserid("root");
			   sqlExec.setPassword("root");
			   sqlExec.setSrc(tempList[i]);
			   sqlExec.setPrint(true); //设置是否输出
			   //输出到文件 sql.out 中；不设置该属性，默认输出到控制台
			   // sqlExec.setOutput(new File("d:/script/sql.out"));
			   sqlExec.setProject(new Project()); // 要指定这个属性，不然会出错
			   sqlExec.execute();
		   }
		  }
		return null;
	}
	
	/*public static void main(String[] args) {
		DataPartitionService aaController = new DataPartitionService();
		aaController.testCon();
	}*/

	@Override
	public Map<String, String> testConn(Map reqMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		resultMap.put("reason", "检查通过！");
		
		String driver=(String) reqMap.get("dbDriver");   //获取mysql数据库的驱动类  
		String host = (String) reqMap.get("host");
		String port = (String) reqMap.get("port");
		// String schemaName = (String) reqMap.get("schemaName");
		String schemaName = defaultSchema;
		String name = (String) reqMap.get("userName");
		String pwd = (String) reqMap.get("password");
		// "jdbc:mysql://localhost:3306/fntop"   连接数据库
		StringBuffer url=new StringBuffer("jdbc:mysql://")
			.append(host).append(":")
			.append(port).append("/")
			.append(schemaName);  
		Connection conn= null;
        try{  
            Class.forName(driver);  
            conn=DriverManager.getConnection(url.toString(),name,pwd);//获取连接对象  
        }catch(Exception e){  
            e.printStackTrace();  
            resultMap.put("result", "fail");
    		resultMap.put("reason", "请确认参数设置是否正确及网络是否畅通！");  
	    }finally{
        	if (conn != null) {
                try {
                  conn.close();
                } catch (SQLException localSQLException) {
                	localSQLException.printStackTrace(); 
                }	                  
	        }
			
		}
        return resultMap;
	}

	@Override
	public Map<String, String> savePartition(Map reqMap) {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		resultMap.put("reason", "保存成功！");
		
		String driver=(String) reqMap.get("dbDriver");   //获取mysql数据库的驱动类  
		String host = (String) reqMap.get("host");
		String port = (String) reqMap.get("port");
		String schemaName = defaultSchema;
		String name = (String) reqMap.get("userName");
		String pwd = (String) reqMap.get("password");
		String areaName = (String) reqMap.get("areaName");
		
		StringBuffer url=new StringBuffer("jdbc:mysql://")
			.append(host).append(":")
			.append(port).append("/")
			.append(schemaName);
		
		String dbUrl = url.toString();
		
		IpDataPartition ipDataPartitionInf = dataPartitionMapper.selectByUrl(dbUrl);
		
		if(ipDataPartitionInf!=null){
			 resultMap.put("result", "fail");
			 resultMap.put("reason", "已设置该数据库，请确认！");
			 return resultMap;
		}	
		
		// TODO 自动生成的方法存根
		try {
			// 赋值 
			IpDataPartition ipDataPartition = new IpDataPartition();
			ipDataPartition.setData_id(UUIDTools.uuid());
			ipDataPartition.setHost(host);
			ipDataPartition.setDb_driver(driver);
			// ipDataPartition.setSchema_name(schemaName);
			ipDataPartition.setPort(port);
			ipDataPartition.setUser_name(name);
			ipDataPartition.setPassword(pwd);
			ipDataPartition.setArea_name(areaName);
			Date nowDate = new Date();
			ipDataPartition.setCreate_date(nowDate);
			ipDataPartition.setUpdate_date(nowDate);
			ipDataPartition.setUrl(dbUrl);
			
			dataPartitionMapper.insert(ipDataPartition);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO 自动生成的 catch 块
			resultMap.put("result", "fail");
			resultMap.put("reason", "数据分区入库失败，请确认数据的正确性！");
			return resultMap;
		}
		return resultMap;
	}

	@Override
	public List<IpDataPartition> getMainPage() {
		
		 return dataPartitionMapper.getMainPage();
		// TODO 自动生成的方法存根
	}
    
	@Override
	public List<IpHirer> getHirerInfo(PageRequest pageRequest) {
		// TODO 自动生成的方法存根
		List<IpHirer> content=dataPartitionMapper.getHirerInfo();
		PageResult<IpHirer> aaa= dataPartitionMapper.getHirerInforetrievePage(pageRequest);
		return content ;
	}

	@Override
	public Map<String, String> auditHirerInfo(String hirerId) {
		// TODO 自动生成的方法存根
		Map<String, String> resultMap = new HashMap<String, String>();
		IpHirer ipHirer;
		try {
			
			resultMap.put("result", "success");
			resultMap.put("reason", "生效成功！");
			ipHirer = hirerAccountService.findHirerByHirerId(hirerId);
			if(ipHirer!=null){
				String dataId= ipHirer.getDataId();
				if("".equals(dataId) || dataId==null){
					// 更新租户信息
					Boolean isUpdate= updateHirer(null,hirerId);
					if(!isUpdate){
						resultMap.put("result", "fail");
						resultMap.put("reason", "更新租户信息失败！");
						return resultMap;
					}
				}else{
					IpDataPartition ipDataPartition = new IpDataPartition();
					ipDataPartition.setData_id(dataId);
					IpDataPartition newIpDataPartition = dataPartitionMapper.getDataPartitionByCondition(ipDataPartition);
					ConnectionInfo info = new ConnectionInfo();
					info.setDbname(defaultSchema);
					info.setHost(newIpDataPartition.getHost());
					info.setPort(newIpDataPartition.getPort());
					info.setUsername(newIpDataPartition.getUser_name());
					info.setPassword(newIpDataPartition.getPassword());
					MysqlConnection mysqlConnection =new MysqlConnection(info);
					
					String schemaName="ipdb_"+ipHirer.getHirerNo().toString();
					// 1、创建schema
					List<Database> databases = mysqlConnection.showDbs();
					for (Database database : databases) {
						if(schemaName==database.getName()){
							resultMap.put("result", "fail");
							resultMap.put("reason", "数据库schema已存在，请确认！");
							return resultMap;
						}
					}
					
					Boolean iSCreate = mysqlConnection.createSchema(schemaName);
					if(!iSCreate){
						resultMap.put("result", "fail");
						resultMap.put("reason", "创建schema失败，请手动创建！");
						return resultMap;
					}
					
					StringBuffer url=new StringBuffer("jdbc:mysql://")
					.append(info.getHost()).append(":").append(info.getPort()).append("/").append(schemaName);
				
					String dbUrl = url.toString();
					info.setDbUrl(dbUrl);
					info.setDbname(schemaName);
					// 2、更新租户信息
					Boolean isUpdate= updateHirer(info,hirerId);
					if(!isUpdate){
						resultMap.put("result", "fail");
						resultMap.put("reason", "更新租户信息失败！");
						return resultMap;
					}

					// 3、修改mycatschema 
					Boolean isModifySchema = modifySchema(info);
					if(!isModifySchema){
						resultMap.put("result", "fail");
						resultMap.put("reason", "修改mycat配置失败，请手动修改！");
						return resultMap;
					}
					// 4、修改server.xml 
					Boolean isModifyServer = modifyServer(info);
					if(!isModifyServer){
						resultMap.put("result", "fail");
						resultMap.put("reason", "修改mycat配置失败，请手动修改！");
						return resultMap;
					}
					
					// 5、在创建好的schema中执行初始脚本 
					Boolean isExcSql =  excSql(info);
					if(!isExcSql){
						resultMap.put("result", "fail");
						resultMap.put("reason", "初始化脚本失败！");
						return resultMap;
					}
					
					
				}
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			throw e;
		}
		return resultMap;
	}
	
	
	public static void main(String[] args) {
		DataPartitionService dataPartitionService = new DataPartitionService();
		dataPartitionService.test();
		
	}
	
	private  void test(){
		String aaString = getClass().getResource("/").getFile().toString();
		//String aaString1 = System.getProperty("user.dir");
		String parentPath=getClass().getResource("../").getFile().toString();  
		File directory = new File("");//设定为当前文件夹 
		
		try {
			System.out.println(directory.getCanonicalPath());
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}//获取标准的路径 
		System.out.println(directory.getAbsolutePath());//获取绝对路径 
		

		
		System.out.println(aaString);
		System.out.println(parentPath);
	}
	
	

	/**
	 * 执行初始化脚本 
	 * @param info
	 */
	private Boolean excSql(ConnectionInfo info){
		try {
			String path=System.getProperty("user.dir") +File.separator+"db"+File.separator+"ip"+File.separator+"initDB";
			
			String outPath=path+File.separator+info.getDbname();
			
			File outfile=new File(outPath);
			if (!outfile.exists()) {
				outfile.mkdir();
			}
			
			File file=new File(path);
			File[] tempList = file.listFiles();
			if(tempList!=null){
				for (int i = 0; i < tempList.length; i++) {
				   if (tempList[i].isFile() && tempList[i].getName().endsWith(".sql")) {
					   System.out.println("文     件："+tempList[i]);		   
					   SQLExec sqlExec = new SQLExec();
					   sqlExec.setDriver("com.mysql.jdbc.Driver");
					   sqlExec.setUrl(info.getDbUrl());
					   sqlExec.setUserid(info.getUsername());
					   sqlExec.setPassword(info.getPassword());
					   sqlExec.setSrc(tempList[i]);
					   sqlExec.setPrint(false); //设置是否输出
					   //输出到文件 sql.out 中；不设置该属性，默认输出到控制台
					   sqlExec.setOutput(new File(outPath+"\\"+tempList[i].getName().substring(tempList[i].getName().length()-3)));
					   sqlExec.setProject(new Project()); // 要指定这个属性，不然会出错
					   sqlExec.execute();
				   }
				}
			}
			return true;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
			/*resultMap.put("result", "fail");
			resultMap.put("reason", "初始化脚本错误，请确认脚本的正确性！");
			return resultMap;*/
		}
	}
	
	/**
	 * 修改schema 
	 * @param info
	 */
	public Boolean modifySchema(ConnectionInfo info){
		String xmlPath= PropertyUtil.getPropertiesKey("application.properties","mycat.config.schema");
		SAXBuilder sb = new SAXBuilder();
		sb.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = null;
        try {       	
        	String schemaName = info.getDbname()+"Schema";
        	String dataNodeName = info.getDbname()+"DataNode";
        	String dataHostName = info.getDbname()+"DataHost";
        	// 获取根节点 
            doc =sb.build(xmlPath);
	    	Element root = doc.getRootElement();
	    	List list = root.getContent();
	    	int i=0;
	    	int a=0;
	    	int b=0;
	    	int c=0;
	    	for (Object obj : list) {
	    		i++;
	    		if(obj instanceof Element){
	    			Element element = (Element) obj;
		    		if("schema".equals(element.getName())){
		    			a=i;
		    		}
		    		if("dataNode".equals(element.getName())){
		    			b=i;
		    		}
		    		if("dataHost".equals(element.getName())){
		    			c=i;
		    		}
	    		}
			}
	    	// 创建schema元素 
	    	Element schema = new Element("schema");
	    	schema.setAttribute("name", schemaName);
	    	schema.setAttribute("checkSQLschema", "false");
	    	schema.setAttribute("sqlMaxLimit", "100");
	    	schema.setAttribute("dataNode", dataNodeName);
	    	// 创建dataNode元素
	    	Element dataNode = new Element("dataNode");
	    	dataNode.setAttribute("name", dataNodeName);
	    	dataNode.setAttribute("dataHost", dataHostName);
	    	dataNode.setAttribute("database", info.getDbname());
	    	
	    	// 创建dataHost元素 
	    	Element dataHost = new Element("dataHost");
	    	dataHost.setAttribute("name",dataHostName);
	    	dataHost.setAttribute("maxCon","1000");
	    	dataHost.setAttribute("minCon","10");
	    	dataHost.setAttribute("balance","0");
	    	dataHost.setAttribute("writeType","0");
	    	dataHost.setAttribute("dbType","mysql");
	    	dataHost.setAttribute("dbDriver","native");
	    	dataHost.setAttribute("switchType","1");
	    	dataHost.setAttribute("slaveThreshold","100");
	    	// 创建子元素心跳检查语句 
	    	Element heartbeat = new Element("heartbeat");
	    	heartbeat.addContent("select user()");
	    	// 创建子元素写数据库配置 
	    	Element writeHost = new Element("writeHost");
	    	writeHost.setAttribute("host", "hostM1");
	    	writeHost.setAttribute("url", info.getHost()+":"+info.getPort());
	    	writeHost.setAttribute("user", info.getUsername());
	    	writeHost.setAttribute("password", info.getPassword());
	    	// 将子元素写入 dataHost
	    	dataHost.addContent(heartbeat);
	    	dataHost.addContent(writeHost);
	    	// schema，dataNode，dataHost内容写入根节点
	    	root.addContent((a+1),schema);
	    	root.addContent((b+2),dataNode);
	    	root.addContent((c+3),dataHost);
	    
	    	// 将doc对象输出到文件   	 
	    	jdomXMLUtil.saveXML(doc,xmlPath);
	    	
	    	return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 修改mycat资源server配置 
	 * @param info
	 * @return
	 */
	private Boolean modifyServer(ConnectionInfo info) {
		// TODO 自动生成的方法存根
		String xmlPath= PropertyUtil.getPropertiesKey("application.properties","mycat.config.server");
		SAXBuilder sb = new SAXBuilder();
		sb.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = null;
        try {       	
        	String schemaName = info.getDbname()+"Schema";
        	// 获取根节点 
        	 doc =sb.build(xmlPath);
 	    	Element root = doc.getRootElement();
 	    	List<Element> list = root.getChildren();
 	    	for (Element el : list) {
 	    		if (("root").equals(el.getAttributeValue("name"))) {
 	    			List<Element> proElements = el.getChildren();
 	    			for (Element element : proElements) {
 	    				if(("schemas").equals(element.getAttributeValue("name"))){
 	    					element.setText(element.getText()+","+schemaName);
 	    				}
 					}
 	    			
 	    		}
 	    	}
 	    	jdomXMLUtil.saveXML(doc,xmlPath);
 	    	return true;
        }catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	private boolean updateHirer(ConnectionInfo info,String hirerId) {
		// TODO 自动生成的方法存根
		try {
			IpHirer ipHirer = new IpHirer();
			ipHirer.setIsValid("1");
			ipHirer.setUpdateDate(new Date());
			if(info!=null){
				ipHirer.setDbSchema(info.getDbname());
				ipHirer.setMycatSchema(info.getDbname()+"Schema");
			}
			ipHirer.setHirerId(hirerId);
			ipHirerMapper.update(ipHirer);
			return true;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static void modifyServer() {
		// TODO 自动生成的方法存根
		String xmlPath= "D:\\mycat\\conf\\server.xml";
		SAXBuilder sb = new SAXBuilder();
		sb.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = null;
        try {       	
        	String schemaName = "ip_Schema";
        	// 获取根节点 
            doc =sb.build(xmlPath);
	    	Element root = doc.getRootElement();
	    	List<Element> list = root.getChildren();
	    	for (Element el : list) {
	    		if (("user").equals(el.getAttributeValue("name"))) {
	    			List<Element> proElements = el.getChildren();
	    			for (Element element : proElements) {
	    				if(("schemas").equals(element.getAttributeValue("name"))){
	    					element.setText(element.getText()+","+schemaName);
	    				}
					}
	    			
	    		}
	    	}
	    	
        }catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void modifySchema(){
		String xmlPath= "D:\\mycat\\conf\\schema.xml";
		SAXBuilder sb = new SAXBuilder();
		sb.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = null;
        try {       	
        	String schemaName = "Schema1*";
        	String dataNodeName = "DataNode1*";
        	String dataHostName = "DataHost1*";
        	// 获取根节点 
            doc =sb.build(xmlPath);
	    	Element root = doc.getRootElement();
	    	List list = root.getContent();
	    	
	    	int i=0;
	    	int a=0;
	    	int b=0;
	    	int c=0;
	    	for (Object obj : list) {
	    		i++;
	    		if(obj instanceof Element){
	    			Element element = (Element) obj;
		    		if("schema".equals(element.getName())){
		    			a=i;
		    		}
		    		if("dataNode".equals(element.getName())){
		    			b=i;
		    		}
		    		if("dataHost".equals(element.getName())){
		    			c=i;
		    		}
	    		}
			}
	    	// 创建schema元素 
	    	// 添加注释
	    	
	    	Element schema = new Element("schema");
	    	schema.setAttribute("name", schemaName);
	    	schema.setAttribute("checkSQLschema", "false");
	    	schema.setAttribute("sqlMaxLimit", "100");
	    	schema.setAttribute("dataNode", dataNodeName);
	    	// 创建dataNode元素
	    	Element dataNode = new Element("dataNode");
	    	dataNode.setAttribute("name", dataNodeName);
	    	dataNode.setAttribute("dataHost", dataHostName);
	    	dataNode.setAttribute("database", "database");
	    	
	    	// 创建dataHost元素 
	    	Element dataHost = new Element("dataHost");
	    	dataHost.setAttribute("name",dataHostName);
	    	dataHost.setAttribute("maxCon","1000");
	    	dataHost.setAttribute("minCon","10");
	    	dataHost.setAttribute("balance","0");
	    	dataHost.setAttribute("writeType","0");
	    	dataHost.setAttribute("dbType","mysql");
	    	dataHost.setAttribute("dbDriver","native");
	    	dataHost.setAttribute("switchType","1");
	    	dataHost.setAttribute("slaveThreshold","100");
	    	// 创建子元素心跳检查语句 
	    	Element heartbeat = new Element("heartbeat");
	    	heartbeat.addContent("select user()");
	    	// 创建子元素写数据库配置 
	    	Element writeHost = new Element("writeHost");
	    	writeHost.setAttribute("host", "hostM1");
	    	writeHost.setAttribute("url", ":");
	    	writeHost.setAttribute("user", "");
	    	writeHost.setAttribute("password", "");
	    	// 将子元素写入 dataHost
	    	dataHost.addContent(heartbeat);
	    	dataHost.addContent(writeHost);
	    	// schema，dataNode，dataHost内容写入根节点
	    	
	    	root.addContent((a+1),schema);
	    	root.addContent((b+2),dataNode);
	    	root.addContent((c+3),dataHost);
	    
	    	// 将doc对象输出到文件   	 
	    	saveXML(doc,xmlPath);	 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*public static void main(String[] args) {
		modifySchema();
	}
	*/
	
	 // 将doc对象输出到文件
	 public static void saveXML(Document doc,String xmlPath) {
        try {
            // 创建xml文件输出流
            XMLOutputter xmlopt = new XMLOutputter();

            // 创建文件输出流
            FileWriter writer = new FileWriter(xmlPath);

           // 指定文档格式
           Format fm = Format.getPrettyFormat();

           xmlopt.setFormat(fm);

           // 将doc写入到指定的文件中
           xmlopt.output(doc, writer);
           writer.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
	
	
//
//	/*
//	 * mybatis 分页插件
//	 */
//	@Override
//	public PageInfo<IpHirer> getHirerInfo(Integer pageNo, Integer pageSize) {
//		
//			pageNo=pageNo==null?1:pageNo;
//			if(pageNo>0){
//				pageNo++;
//			}
//			pageSize=pageSize==null?10:pageSize;
//			//PageHelper.startPage(pageNo,pageSize);
//			List<IpHirer> content=dataPartitionMapper.getHirerInfoMy(pageNo*pageSize,pageSize);
//			PageInfo<IpHirer> pageInfo=new PageInfo<IpHirer>(content);
//			return pageInfo;
//	}

	@Override
	public int getHirerInfoCounts(String isVaild) {
		// TODO 自动生成的方法存根
		return dataPartitionMapper.getHirerInfoCounts(isVaild);
	}

	@Override
	public List<IpHirer> getHirerInfo(int pageNumber, int pageSize,String isVaild) {
		// TODO 自动生成的方法存根
		int startRow=pageNumber*pageSize;
		return dataPartitionMapper.getHirerInfoMy(startRow, pageSize,isVaild);
	}

}
