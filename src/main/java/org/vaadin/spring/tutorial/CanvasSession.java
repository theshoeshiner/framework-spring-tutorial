package org.vaadin.spring.tutorial;

public class CanvasSession {
	
	protected static ThreadLocal<CanvasSession> THREAD_LOCAL = new ThreadLocal<CanvasSession>();
	
	/*
	 * public static CanvasSession getInstance(){ //thread local value overrides
	 * VaadinSession value - used in background threads CanvasSession session =
	 * THREAD_LOCAL.get(); if(session == null){ session = (AppSession)
	 * VaadinSession.getCurrent().getAttribute(KEY); if(session == null) session =
	 * new AppSession(); } return session; }
	 */

}
