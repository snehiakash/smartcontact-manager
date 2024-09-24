package com.smart.services;

import org.springframework.web.context.request.ServletRequestAttributes;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {
	

	public void removeMessageFromSession() {
		try {
			System.out.println("Removing Message from Session");
		HttpSession session=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(); 
		session.removeAttribute("message");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
