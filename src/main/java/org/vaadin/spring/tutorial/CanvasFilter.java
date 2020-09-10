package org.vaadin.spring.tutorial;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iqvia.rbm.reports.canvas.SignedRequest;

@Component
public class CanvasFilter implements Filter {
	
	String consumerSecret;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CanvasFilter.class);
	
	//protected String ssoSubjectAttribute  = "ping.sso.subject";
	//protected String ssoDomainAttribute  = "ping.sso.userdomain";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		//TODO get from filter config
		consumerSecret = "80DF7DF4D1E73D11C65C884A9FE8C4E1749914AF247BA5D39786F5099FC05589";
		//consumerSecret = System.getenv("CANVAS_CONSUMER_SECRET");
		
		System.out.println("init filter");
		System.out.println("secret");
		LOGGER.warn("init filter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		System.out.println("doFilter");
		
		// Pull the signed request out of the request body and verify/decode it.
		Map<String, String[]> parameters = request.getParameterMap();
		String[] signedRequest = parameters.get("signed_request");
		System.out.println("signedRequest: "+signedRequest);
		
		if(signedRequest != null && signedRequest.length > 0) {
			String signedRequestJson = SignedRequest.verifyAndDecodeAsJson(signedRequest[0], consumerSecret);
			System.out.println("signedRequestJson: "+signedRequestJson);
		}
	
		//String yourConsumerSecret=System.getenv("CANVAS_CONSUMER_SECRET");
		//String yourConsumerSecret = "A2DF24FB0F47ABE94C2E128B7C219B90A6C80C0B451CFF0450ED567702F640DB";
		
		chain.doFilter(request, response);
		
		
		
		
	}

	@Override
	public void destroy() {
		
	}

}
