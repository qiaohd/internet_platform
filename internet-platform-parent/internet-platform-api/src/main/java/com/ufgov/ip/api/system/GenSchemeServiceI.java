package com.ufgov.ip.api.system;

import java.util.List;

import com.ufgov.ip.entity.system.GenScheme;

public interface GenSchemeServiceI {

	public GenScheme get(String id);
	
	public List<GenScheme> find(GenScheme genScheme);
	
	public String save(GenScheme genScheme,String flag,String cuser);
	
	public void delete(GenScheme genScheme);
	
}
