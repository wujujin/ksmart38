package ksmart38.mybatis.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor{
	
	
	private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String sessionId = (String)session.getAttribute("SID");
		String sessionLevel = (String)session.getAttribute("SLEVEL");
		String requestURI = request.getRequestURI();
		
		log.info("LoginInterceptor============================================= START");
		log.info("sessionId  	::  {}", sessionId );
		log.info("sessionLevel  ::  {}", sessionLevel);
		log.info("requestURI  ::  {}", requestURI);
		
		if(sessionId == null && !"/addMember".equals(requestURI)) {
			response.sendRedirect("/login");
			return false;
		}
		if("구매자".equals(sessionLevel)) {
			if(!"/logout".equals(requestURI)) {
				response.sendRedirect("/");
				return false;
			}
		}else if("판매자".equals(sessionLevel)) {
			if(!("/goodsList".equals(requestURI) | "/addGoods".equals(requestURI) |
			   "/modifyGoods".equals(requestURI) | "/removeGoods".equals(requestURI) | "/logout".equals(requestURI))) {
				
				// 가정 : 해당 로그인 아이디에 따른 메뉴를 디비로 가져올 때
				List<String> list = new ArrayList<String>();
				list.add("/addMember");
				
				log.info("test :: {}" ,list.contains(requestURI));
				
				// 현재 요청받은 주소와 사용자 접근 주소가 일치하지 않을 떄
				if(!list.contains(requestURI)) {
					response.sendRedirect("/");
					return false;
				}
				
				response.sendRedirect("/");
				return false;
			}
		}
	
		return true;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("LoginInterceptor============================================= END");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
