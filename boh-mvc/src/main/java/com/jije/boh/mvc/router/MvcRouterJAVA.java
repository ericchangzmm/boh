package com.jije.boh.mvc.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.jije.boh.mvc.MvcException;

@Component("mvcRouterJAVA")
public class MvcRouterJAVA implements MvcRouter {

	@Override
	public String service(String module, String subModule, String function,
			HttpServletRequest request, HttpServletResponse response) {

		throw new MvcException("not support");
	}

}
