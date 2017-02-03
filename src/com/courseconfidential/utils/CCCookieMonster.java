package com.courseconfidential.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CCCookieMonster {

	private Cookie[] cookies;
	HttpServletRequest request;
	HttpServletResponse response;
	
	//primary constructor - preffered method of instantiation	
	public CCCookieMonster(HttpServletRequest currentRequest, HttpServletResponse currentResponse)
	{
		cookies = currentRequest.getCookies();
		request = currentRequest;
		response = currentResponse;
	}
	
	public CCCookieMonster(Cookie[] current)
	{
		cookies = current;
	}
	
	//check if there are any cookies avalable for this session
	public boolean hasCookies()
	{
		if(!(cookies == null))
			return true;
		else 
			return false;
	}
	
	
	public void generateUserCookies(int account, String username)
	{

	}
	
	public Cookie createCookie(String key, String value, int expirationDays)
	{
		Cookie cookie = new Cookie(key,value);
			cookie.setMaxAge(60*60*(expirationDays*24));
		
		return cookie;
	}
	
	public Cookie getCookie(String key)
	{
		if(!hasCookies())
			throw new NullPointerException("BYBCookieMonster: no cookies found for this user");
		for(Cookie c: cookies)
		{
			if(c.getName().equalsIgnoreCase(key))
			{
				return c;
			}
		}
		
		return null;
		
	}
	
	public boolean existsCookie(String key)
	{
		if(!hasCookies())
			throw new NullPointerException("BYBCookieMonster: no cookies found for this user");
		boolean found = false;
		
		for(Cookie c: cookies)
		{
			if(c.getName().equalsIgnoreCase(key))
			{
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	public void listCookies()
	{
		if(!hasCookies())
			System.out.println("BYBCookieMonster: no cookies found for this user");
		
		System.out.println("BYBCookieMonster: cookie list"); 
		
		for(Cookie c: cookies)
		{
			System.out.println(c.getName()+" : "+c.getValue());
		}
	}
	
	/**
	 * Destroys all possible cookies; typically called to log a user out.
	 */
	public void eatCookies()
	{
		if(!hasCookies())
			return;
		
		for(Cookie c: cookies)
		{
			Cookie curr = c;
				c.setMaxAge(0);
			response.addCookie(curr);
		}
	}
}
