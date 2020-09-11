package org.vaadin.spring.tutorial;

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
import com.iqvia.rbm.reports.canvas.CanvasRequest;
import com.iqvia.rbm.reports.canvas.SignedRequest;

@Component
public class CanvasFilter implements Filter {
	
	String consumerSecret;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CanvasFilter.class);
	
	//protected String ssoSubjectAttribute  = "ping.sso.subject";
	//protected String ssoDomainAttribute  = "ping.sso.userdomain";
	
	public static final String CANVAS_CONTEXT_ATT = "canvas-context-object";
	public static final String CANVAS_CONTEXT_JSON_ATT = "canvas-context-json";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		//TODO get from filter config
		consumerSecret = "80DF7DF4D1E73D11C65C884A9FE8C4E1749914AF247BA5D39786F5099FC05589";
		//consumerSecret = System.getenv("CANVAS_CONSUMER_SECRET");
		
		//System.out.println("init filter");
		//System.out.println("secret");
		LOGGER.info("init filter");
		//LOGGER.warn("init filter warn");
	}
	
	String setcook = "Set-Cookie";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;

		//httpRes.addHeader(setcook, "SameSite=None");
		
		//LOGGER.info("doFilter request: {}",request);
		
		//request.getParameterNames()
		
		//LOGGER.info("request attributes: {}",Collections.list(request.getAttributeNames()));
		
		//LOGGER.info("request parameters: {}",Collections.list(request.getParameterNames()));
		
		HttpSession session = httpReq.getSession();
		
		LOGGER.info("doFilter set cookie: {}",httpRes.getHeader("Set-Cookie"));
		
		/*if(response.containsHeader("Set-Cookie")) {
			String header = response.getHeader("Set-Cookie")+";SameSite=None";
			response.setHeader("Set-Cookie", header);
		}*/
		
		if(httpRes.containsHeader(setcook)) {
			//httpRes.addHeader(setcook, "SameSite=None");
			String header = httpRes.getHeader(setcook)+";SameSite=None";
			httpRes.setHeader(setcook, header);
		}
		
		LOGGER.info("doFilter set cookie: {}",httpRes.getHeader("Set-Cookie"));
		
		LOGGER.info("doFilter session: {}",session);
		LOGGER.info("doFilter session id: {}",session.getId());
		
		//LOGGER.info("doFilter request: {}",request.get`);
		
		
		
		// Pull the signed request out of the request body and verify/decode it.
		Map<String, String[]> parameters = request.getParameterMap();
		String[] signedRequest = parameters.get("signed_request");
		//System.out.println("signedRequest: "+signedRequest);
		
		LOGGER.info("signedRequest: {}",new Object[] {signedRequest});
		
		if(signedRequest != null && signedRequest.length > 0) {
			JsonNode signedRequestJson = SignedRequest.verifyAndDecodeAsJson(signedRequest[0], consumerSecret);
			
			LOGGER.info("signedRequestJson: {}",signedRequestJson);
			
			CanvasRequest canvasRequest = SignedRequest.verifyAndDecode(signedRequest[0], consumerSecret);
			
			LOGGER.info("setting context attribute to: {}",canvasRequest);
			
			//request.setAttribute(CANVAS_CONTEXT_ATT, canvasRequest);
			
			session.setAttribute(CANVAS_CONTEXT_ATT, canvasRequest);
			session.setAttribute(CANVAS_CONTEXT_JSON_ATT, signedRequestJson);
			
			
			//System.out.println("signedRequestJson: "+signedRequestJson);
			//LOGGER.info("signedRequestJson: {}",signedRequestJson);
		}
	
		//LOGGER.info("current context: {}",request.getAttribute(CANVAS_CONTEXT_ATT));
		
		LOGGER.info("current session context: {}",session.getAttribute(CANVAS_CONTEXT_ATT));
		
		//String yourConsumerSecret=System.getenv("CANVAS_CONSUMER_SECRET");
		//String yourConsumerSecret = "A2DF24FB0F47ABE94C2E128B7C219B90A6C80C0B451CFF0450ED567702F640DB";
		
		chain.doFilter(request, response);
		
		
		
		LOGGER.info("Filter done");
		LOGGER.info("resp headers: {}",httpRes.getHeaderNames());
		String setcookie = httpRes.getHeader(setcook);
		LOGGER.info("cookie header 1: {}",setcookie);
		//httpRes.addHeader(setcook, "SameSite=None");
		//setcookie = setcookie+";SameSite=None";
		//httpRes.setHeader(setcook, setcookie);
		//LOGGER.info("cookie header 2: {}",httpRes.getHeader(setcook));
		
		
		
	}
	
	

	@Override
	public void destroy() {
		
	}

}
