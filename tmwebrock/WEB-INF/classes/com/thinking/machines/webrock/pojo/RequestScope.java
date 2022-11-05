package com.thinking.machines.webrock.pojo;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
public class RequestScope
{
private  static HttpServletRequest httpServletRequest;
public  RequestScope(HttpServletRequest httpServletRequest)
{
this.httpServletRequest=httpServletRequest;
}
public void setAttribute(String key,Object value)
{
this.httpServletRequest.setAttribute(key,value);
}
public  static Object getAttribute(String key)
{
return httpServletRequest.getAttribute(key);
}
}