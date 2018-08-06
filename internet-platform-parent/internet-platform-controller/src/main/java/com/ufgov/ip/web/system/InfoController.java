package com.ufgov.ip.web.system;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/info")
public class InfoController {

	@RequestMapping(method = RequestMethod.GET,value="vcodeError")
	public String vcode_error(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		return "vcodeError";
	}
	
	@RequestMapping(method = RequestMethod.GET,value="secrecy_treaty")
	public String secrecy_treaty(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		return "secrecy_treaty";
	}
	
	@RequestMapping(method = RequestMethod.GET,value="serve_treaty")
	public String serve_treaty(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		return "serve_treaty";
	}
	
}
