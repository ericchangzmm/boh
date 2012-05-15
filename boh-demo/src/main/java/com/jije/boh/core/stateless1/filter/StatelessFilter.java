package com.jije.boh.core.stateless1.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jije.boh.core.spring.SpringContextHolder;
import com.jije.boh.core.stateless1.ISessionBackend;
import com.jije.boh.core.stateless1.wrappers.StatelessRequestWrapper;

public class StatelessFilter implements Filter {

	private static Logger logger = LoggerFactory
			.getLogger(StatelessFilter.class);

	private static final String PARAM_EXCLUDE_PATTERNS = "excludePatterns";
	private static final String EXCLUDE_PATTERN_SEPARATOR = ",";

	private List<Pattern> excludePatterns = null;
	private ISessionBackend backend = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		logger.info("Stateless filter init...");

		initExcludedPattern(filterConfig);

		initSessionBackend();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		if (isExcluded(httpRequest)) {
			chain.doFilter(request, response);
			return;
		}

		// Wrap request
		logger.info("Wrap request...");
		HttpServletRequest statelessRequest = new StatelessRequestWrapper(
				httpRequest, httpResponse, backend);

		chain.doFilter(statelessRequest, response);

		logger.info("write session...");
		((StatelessRequestWrapper) statelessRequest).writeSession();
	}

	@Override
	public void destroy() {

	}

	private boolean isExcluded(HttpServletRequest httpRequest) {

		if (this.excludePatterns == null)
			return false;

		String uri = httpRequest.getRequestURI();
		logger.debug("Check URI : " + uri);

		try {
			uri = new URI(uri).normalize().toString();
			for (Pattern pattern : this.excludePatterns) {
				if (pattern.matcher(uri).matches()) {
					logger.info("URI excluded : " + uri);
					return true;
				}
			}
		} catch (URISyntaxException e) {
			logger.warn("The following URI has a bad syntax. URI : " + uri, e);
		}
		return false;
	}

	private void initExcludedPattern(FilterConfig filterConfig) {

		String excludedPatternStr = filterConfig
				.getInitParameter(PARAM_EXCLUDE_PATTERNS);
		if (excludedPatternStr != null) {
			String[] splittedExcludedPattern = excludedPatternStr
					.split(EXCLUDE_PATTERN_SEPARATOR);
			List<Pattern> patterns = new ArrayList<Pattern>();
			Pattern pattern = null;
			for (String element : splittedExcludedPattern) {
				pattern = Pattern.compile(element);
				patterns.add(pattern);
			}
			this.excludePatterns = patterns;
		}
	}

	private void initSessionBackend() {

		this.backend = SpringContextHolder.getBean("memcacheBackend");
		this.backend.init(new HashMap<String, String>());
	}
}
