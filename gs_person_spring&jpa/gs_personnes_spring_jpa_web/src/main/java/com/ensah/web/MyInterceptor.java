package com.ensah.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Exemple d'un intercepteur
 *
 */
public class MyInterceptor implements HandlerInterceptor {
	private Logger LOGGER = Logger.getLogger(getClass().getName());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		LOGGER.debug("The handler is :"+handler+" Inside preHandle : " + request.getRequestURI());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		LOGGER.debug("modelAndView="+modelAndView + " Inside postHandle" + request.getRequestURI());
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {

		LOGGER.debug("Inside afterCompletion" + request.getRequestURI());
	}
}