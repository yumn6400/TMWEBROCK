package com.thinking.machines.webrock.Login;
import com.thinking.machines.webrock.pojo.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
public class LoginForm extends HttpServlet
{
public void doGet(HttpServletRequest request,HttpServletResponse response)
{
String id=request.getParameter("id");
String password=request.getParameter("password");
if(id.equals("id")==true && password.equals("password")==true)
{
HttpSession session=request.getSession();
String uniqueId=UUID.randomUUID().toString();
SessionScope sessionScope=new SessionScope(request.getSession());
sessionScope.setAttribute("uniqueID",uniqueId);
try
{
RequestDispatcher requestDispatcher=request.getRequestDispatcher("/main/requestJSONTest/process2");
requestDispatcher.forward(request,response);
}catch(Exception e){}
}
else
{
try
{
RequestDispatcher requestDispatcher=request.getRequestDispatcher("index.html");
requestDispatcher.forward(request,response);
}catch(Exception e){}
}
}
public void doPost(HttpServletRequest request,HttpServletResponse response)
{
doGet(request,response);
}
}
