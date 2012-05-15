package com.jije.boh.mvc.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.jije.boh.mvc.MvcConfig;
import com.jije.boh.mvc.MvcException;

@Component("mvcRouterUIDebug")
public class MvcRouterUIDebug implements MvcRouter {

	@Override
	public String service(String module, String subModule, String function,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			String paramStr = parse(request.getParameterMap());

			String url = prepareUrl(module, subModule, function);

			return process(url, paramStr);
		} catch (Exception e) {
			throw new MvcException(e.getMessage());
		}

	}

	private String prepareUrl(String module, String subModule, String function) {

		StringBuilder sb = new StringBuilder();
		sb.append(MvcConfig.forwardUrl);
		sb.append(MvcConfig.forwardUrl.endsWith("/") ? "ajax/" : "/ajax/");
		sb.append(module);
		sb.append("/");
		if (StringUtils.isNotEmpty(subModule)) {
			sb.append(subModule);
			sb.append("/");
		}
		sb.append(function);

		return sb.toString();
	}

	private String process(String url, String paramStr)
			throws MalformedURLException, IOException {

		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();

		conn.setDoOutput(true);
		conn.connect();

		OutputStream outputStream = conn.getOutputStream();
		outputStream.write(paramStr.getBytes("utf-8"));
		outputStream.flush();
		outputStream.close();

		return FileCopyUtils.copyToString(new BufferedReader(
				new InputStreamReader(conn.getInputStream())));

	}

	@SuppressWarnings("rawtypes")
	private String parse(Map map) {
		String result = "";
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry me = (Entry) iter.next();
			String key = (String) me.getKey();
			String[] varr = (String[]) me.getValue();

			// 重新组装参数字符串
			for (int i = 0; i < varr.length; i++) {
				result += "&" + key + "=" + varr[i];
			}
		}
		return result.replaceAll("^&", "");
	}

}
