package com.thinking.machines.webrock.pojo;
import javax.servlet.http.*;
import java.util.*;
public class SessionScope
{
private  static HttpSession httpSession;
public  SessionScope(HttpSession httpSession)
{
this.httpSession=httpSession;
}
public void setAttribute(String key,Object value)
{
this.httpSession.setAttribute(key,value);
}
public  static Object getAttribute(String key)
{
Object value=null;
try
{
value=httpSession.getAttribute(key);
}catch(Exception exception)
{
}
return value;
}
public  void setMaxInactiveInterval(int value)
{
this.httpSession.setMaxInactiveInterval(value);
}
}