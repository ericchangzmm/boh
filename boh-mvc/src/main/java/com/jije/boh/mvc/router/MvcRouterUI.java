package com.jije.boh.mvc.router;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.support.ServletContextResource;

import com.jije.boh.mvc.MvcConfig;
import com.jije.boh.mvc.MvcException;

@Component("mvcRouterUI")
public class MvcRouterUI implements MvcRouter {

	@Override
	public String service(String module, String subModule, String function,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			ServletContext servletContext = request.getSession()
					.getServletContext();
			String jsonFilePath = prepareJsonFilePath(module, subModule,
					function);

			Resource jsonResource = new ServletContextResource(servletContext,
					jsonFilePath);

			EncodedResource encodedResource = new EncodedResource(jsonResource,
					"UTF-8");

			return FileCopyUtils.copyToString(encodedResource.getReader());
		} catch (IOException e) {
			throw new MvcException("file not exists");
		}
	}

	private String prepareJsonFilePath(String module, String subModule,
			String function) {

		StringBuilder sb = new StringBuilder(MvcConfig.UI_JSON_FOLDER);
		sb.append(File.separator);
		sb.append(module);
		if (StringUtils.isNotEmpty(subModule)) {
			sb.append(File.separator);
			sb.append(subModule);
		}
		sb.append(File.separator);
		sb.append(function);
		sb.append(".json");

		return sb.toString();
	}

}
