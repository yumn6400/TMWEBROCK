package com.thinking.machines.webrock.pojo;
import javax.servlet.*;
import java.util.*;
public class ApplicationScope
{
private  static ServletContext servletContext;
public  ApplicationScope(ServletContext servletContext)
{
this.servletContext=servletContext;
}
public void setAttribute(String key,Object value)
{
this.servletContext.setAttribute(key,value);
}
public  static Object getAttribute(String key)
{
return servletContext.getAttribute(key);
}
}