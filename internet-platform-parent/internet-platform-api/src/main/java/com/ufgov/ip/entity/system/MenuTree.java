package com.ufgov.ip.entity.system;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: MenuTree
 *
 */
//@Entity

public class MenuTree implements Serializable {

	
	private static final long serialVersionUID = 1L;
    
	private String id;
	private String pId;
	private String url;
	private String name;
	
	
	
	
	
	public String getId() {
		return id;
	}





	public void setId(String id) {
		this.id = id;
	}





	public String getPId() {
		return pId;
	}





	public void setPId(String pId) {
		this.pId = pId;
	}





	public String getUrl() {
		return url;
	}





	public void setUrl(String url) {
		this.url = url;
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}





	public MenuTree() {
		super();
	}
   
}
