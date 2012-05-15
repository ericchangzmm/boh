package com.jije.boh.mvc.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MvcRouter {

	String service(String module, String subModule, String function, HttpServletRequest request, HttpServletResponse response);
}
