package com.jije.boh.demo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jije.boh.core.memsession.MemSessionUtil;
import com.jije.boh.demo.dto.UserInfo;

public class LoginAction extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public LoginAction() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// 从数据库中读取用户信息
		// 假定登录成功　为userInfo
		UserInfo userInfo = new UserInfo("hpt", 666, "hpt");

		// 登录成功后清除原有的cookie
		MemSessionUtil.invalidate(request, response);

		String sessionId = MemSessionUtil.generateId();
		MemSessionUtil.setCookie(request, response, sessionId);

		MemSessionUtil.setAttribute(sessionId, "current_user", userInfo);

		response.sendRedirect("login_success.jsp");
	}

}
