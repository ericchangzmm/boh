package com.jije.boh.demo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jije.boh.core.memsession.MemSessionUtil;

public class LogoutAction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    public LogoutAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MemSessionUtil.invalidate(request, response);
		
		response.sendRedirect("login_success.jsp");
	}

}
