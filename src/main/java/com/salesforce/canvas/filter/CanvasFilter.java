package com.salesforce.canvas.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class CanvasFilter implements Filter {
	
	String clientSecret;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CanvasFilter.class);
	
	//protected String ssoSubjectAttribute  = "ping.sso.subject";
	//protected String ssoDomainAttribute  = "ping.sso.userdomain";
	
	public static final String CANVAS_CONTEXT_ATT = "canvas-context-object";
	public static final String CANVAS_CONTEXT_JSON_ATT = "canvas-context-json";
	public static final String CLIENT_SECRET_ATT = "client-secret";
	public static final String SET_COOKIE = "Set-Cookie";
	public static final String SIGNED_REQUEST = "signed_request";
	public static final String SAMESITE_COOKIE_SUFFIX = ";SameSite=None";
	
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		//clientSecret = filterConfig.getInitParameter(CLIENT_SECRET_ATT);

		clientSecret = "80DF7DF4D1E73D11C65C884A9FE8C4E1749914AF247BA5D39786F5099FC05589";

	}
	
	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		LOGGER.info("doFilter");
		
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;

		HttpSession session = httpReq.getSession();
		
		LOGGER.info("doFilter set cookie: {}",httpRes.getHeader(SET_COOKIE));

		//If the response is sending a cookie then make sure we add the samesite value
		if(httpRes.containsHeader(SET_COOKIE)) {
			String header = httpRes.getHeader(SET_COOKIE)+SAMESITE_COOKIE_SUFFIX;
			httpRes.setHeader(SET_COOKIE, header);
		}
		

		// Pull the signed request out of the request body and verify/decode it.
		Map<String, String[]> parameters = request.getParameterMap();
		String[] signedRequest = parameters.get(SIGNED_REQUEST);
		
		//System.out.println("signedRequest: "+signedRequest);
		
		LOGGER.info("signedRequest: {}",new Object[] {signedRequest});
		
		if(signedRequest != null && signedRequest.length > 0) {
			JsonNode signedRequestJson = SignedRequest.verifyAndDecodeAsJson(signedRequest[0], clientSecret);
			
			LOGGER.info("signedRequestJson: {}",signedRequestJson);
			
			CanvasRequest canvasRequest = SignedRequest.verifyAndDecode(signedRequest[0], clientSecret);
			
			LOGGER.info("setting context attribute to: {}",canvasRequest);
			
			//request.setAttribute(CANVAS_CONTEXT_ATT, canvasRequest);
			
			session.setAttribute(CANVAS_CONTEXT_ATT, canvasRequest);
			session.setAttribute(CANVAS_CONTEXT_JSON_ATT, signedRequestJson);
			
			
			//System.out.println("signedRequestJson: "+signedRequestJson);
			//LOGGER.info("signedRequestJson: {}",signedRequestJson);
		}
	
		//LOGGER.info("current context: {}",request.getAttribute(CANVAS_CONTEXT_ATT));
		
		LOGGER.info("current session context: {}",session.getAttribute(CANVAS_CONTEXT_ATT));

		chain.doFilter(request, response);
		
		
		
	
		LOGGER.info("resp headers: {}",httpRes.getHeaderNames());
		String setcookie = httpRes.getHeader(SET_COOKIE);
		LOGGER.info("cookie header 1: {}",setcookie);
		
		LOGGER.info("Filter done");

		
	}
	
	

	@Override
	public void destroy() {
		
	}

}
