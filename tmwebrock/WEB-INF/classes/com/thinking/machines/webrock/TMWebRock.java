package com.thinking.machines.webrock;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.model.*;
import com.thinking.machines.webrock.annotation.*;
import com.thinking.machines.webrock.exception.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.io.*;
import com.google.gson.*;
public class TMWebRock extends HttpServlet
{
private static ApplicationScope applicationScope;
private static SessionScope sessionScope;
private static RequestScope requestScope;
private static ApplicationDirectory applicationDirectory;
//result from service used as parameter for another service when forwarding
private static Object resultFromService; 


public void init()
{
ServletContext servletContext=getServletContext();
applicationScope=new ApplicationScope(servletContext);
}

public void doGet(HttpServletRequest request,HttpServletResponse response)
{
StringBuffer sb=null;
String line = null;
String path=null;
String requestMethod=null;
try
{

path=request.getRequestURL().substring(31);
if(path.substring(0,5).equals("/main")==true)path=path.substring(5);
requestMethod="/"+request.getMethod();
System.out.println(path);

//request containing json part starts here

sb= new StringBuffer();
BufferedReader br= request.getReader();
while ((line = br.readLine()) != null)sb.append(line);
}
catch (Exception e)
{
System.out.println(e);
}
String jsonString=null;
if(sb!=null)jsonString=sb.toString();

//request containing json part ends here 

WebRockModel model=new WebRockModel();
Map<String,Services> map=model.getServiceMap();
if(map.containsKey(path)==true)
{
Services s=map.get(path);
if(s==null)
{
try
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
return;
}catch(Exception e){}
}
try
{
Object obj=s.getServiceClass().newInstance();




//securedAccess part starts here

if(s.getSecuredAccess()==true)
{
Annotation secure=s.getServiceClass().getAnnotation(SecuredAccess.class);
if(secure!=null)
{
String className=((SecuredAccess)secure).checkPost();
String methodName=((SecuredAccess)secure).guard();
Class secureClass=Class.forName(className);
Object secureObject=secureClass.newInstance();

Object value=request.getAttribute("uniqueID");
boolean found=false;
if(value!=null)found=true;
if(found==false)
{
value=request.getSession().getAttribute("uniqueID");
if(value!=null)found=true;
}
if(found==false)
{
value=request.getServletContext().getAttribute("uniqueID");
if(value!=null)found=true;
}
if(found==false)
{
try
{
response.sendError(HttpServletResponse.SC_NOT_FOUND);
return;
}catch(Exception e){}

RequestDispatcher requestDispatcher=request.getRequestDispatcher("index.html");
requestDispatcher.forward(request,response);
return;
}
Class parameters[]={Object.class};
Method initMethod=secureClass.getMethod("init",parameters);
Object arguments[]={value};
initMethod.invoke(secureObject,arguments);
Method secureMethod=secureClass.getMethod(methodName);
if(secureMethod!=null)
{
boolean output=(boolean)secureMethod.invoke(secureObject);
if(output==false)
{

}
}
}
}

//secured Access Part ends here



//Inject feature starts here
if(s.getInjectApplicationScope()==true)
{
try
{
Class parameters[]={ApplicationScope.class};
Method m=s.getServiceClass().getMethod("setApplicationScope",parameters);
if(m!=null)
{
Object arguments[]={applicationScope};
m.invoke(obj,arguments);
}
}catch(Exception ex){}
}
else if(s.getInjectSessionScope()==true)
{
HttpSession httpSession=request.getSession();
sessionScope=new SessionScope(httpSession);
try
{
Class parameters[]={SessionScope.class};
Method m=s.getServiceClass().getMethod("setSessionScope",parameters);
if(m!=null)
{
Object arguments[]={sessionScope};
m.invoke(obj,arguments);
}
}catch(Exception ex){}
}
else if(s.getInjectRequestScope()==true)
{
requestScope=new RequestScope(request);
try
{
Class parameters[]={RequestScope.class};
Method m=s.getServiceClass().getMethod("setRequestScope",parameters);
if(m!=null)
{
Object arguments[]={requestScope};
m.invoke(obj,arguments);
}
}catch(Exception ex){
}
}
else if(s.getInjectApplicationDirectory()==true)
{
try
{
ServletContext servletContext=getServletContext();
File file=new File(servletContext.getRealPath(request.getServletPath()));
applicationDirectory=new ApplicationDirectory(file);
Class parameters[]={ApplicationDirectory.class};
Method m=s.getServiceClass().getMethod("setApplicationDirectory",parameters);
if(m!=null)
{
Object arguments[]={applicationDirectory};
m.invoke(obj,arguments);
}
}catch(Exception ex){}

}
//Inject feature ends here
//Autowired feature starts here
if(Services.getAutowiredMap().containsKey(s.getServiceClass()))
{
List<AutowiredProperty> list=Services.getAutowiredMap().get(s.getServiceClass());
for(AutowiredProperty autowired:list)
{
String strName=autowired.getPropertyClass().getName();
Class c=Class.forName(strName);
String name=autowired.getName();

Object value=request.getAttribute(name);
boolean found=false;
if(value!=null)
{
if(c.isInstance(value))
{
found=true;
}
}
if(found==false)
{
value=request.getSession().getAttribute(name);
if(value!=null)
{
if(c.isInstance(value))
{
found=true;
}
}
}
if(found==false)
{
value=request.getServletContext().getAttribute(name);
if(value!=null)
{
if(c.isInstance(value))
{
found=true;
}
}
}
if(found==true)
{
try
{
Class parameters[]={value.getClass()};
int pos=0;
int i=0;
for(i=0;i<strName.length();i++)if(strName.charAt(i)=='.')pos=i;
strName="set"+strName.substring(pos+1,pos+2).toUpperCase()+strName.substring(pos+2);
Method m=s.getServiceClass().getMethod(strName,parameters);
if(m!=null)
{
Object o=s.getServiceClass().newInstance();
Object arguments[]={value};
m.invoke(obj,arguments);
}
}catch(Exception ex){}
}
}
}
//Autowired feature ends here
if(s.getFields().size()>0)
{
boolean found=false;
Object value=null;
String parameterName;
for(int k=0;k<s.getFields().size();k++)
{
Annotation kkk=s.getFields().get(k).getAnnotation(InjectRequestParameter.class);
if(kkk!=null)
{
parameterName=((InjectRequestParameter)kkk).value();
found=false;
value=request.getParameter(parameterName);
if(value!=null)found=true;
if(found==false)
{
value=request.getAttribute(parameterName);
if(value!=null)found=true;
}
if(found==false)
{
value=request.getSession().getAttribute(parameterName);
if(value!=null)found=true;
}
if(found==false)
{
value=request.getServletContext().getAttribute(parameterName);
if(value!=null)found=true;
}
if(found==true)
{
String name=s.getFields().get(k).getName();
Method m=null;
if(s.getFields().get(k).getType().toString().equals("long"))
{
Class parameters[] ={long.class};
name="set"+name.substring(0,1).toUpperCase()+name.substring(1);
m=s.getServiceClass().getMethod(name,parameters);
}
else if(s.getFields().get(k).getType().toString().equals("int"))
{
Class []parameters={int.class};
name="set"+name.substring(0,1).toUpperCase()+name.substring(1);
m=s.getServiceClass().getMethod(name,parameters);
}
else if(s.getFields().get(k).getType().toString().equals("short"))
{
Class []parameters={short.class};
name="set"+name.substring(0,1).toUpperCase()+name.substring(1);
m=s.getServiceClass().getMethod(name,parameters);
}
else if(s.getFields().get(k).getType().toString().equals("byte"))
{
Class []parameters={byte.class};
name="set"+name.substring(0,1).toUpperCase()+name.substring(1);
m=s.getServiceClass().getMethod(name,parameters);
}
else if(s.getFields().get(k).getType().toString().equals("double"))
{
Class []parameters={double.class};
name="set"+name.substring(0,1).toUpperCase()+name.substring(1);
m=s.getServiceClass().getMethod(name,parameters);
}
else if(s.getFields().get(k).getType().toString().equals("float"))
{
Class []parameters={float.class};
name="set"+name.substring(0,1).toUpperCase()+name.substring(1);
m=s.getServiceClass().getMethod(name,parameters);
}
else if(s.getFields().get(k).getType().toString().equals("char"))
{
Class []parameters={char.class};
name="set"+name.substring(0,1).toUpperCase()+name.substring(1);
m=s.getServiceClass().getMethod(name,parameters);
}
else if(s.getFields().get(k).getType().toString().equals("boolean"))
{
Class []parameters={boolean.class};
name="set"+name.substring(0,1).toUpperCase()+name.substring(1);
m=s.getServiceClass().getMethod(name,parameters);
}
else
{
Class []parameters={String.class};
name="set"+name.substring(0,1).toUpperCase()+name.substring(1);
m=s.getServiceClass().getMethod(name,parameters);
}
if(m!=null)
{
Object o=s.getServiceClass().newInstance();
value=Integer.parseInt(value.toString());
Object arguments[]={value};
m.invoke(obj,arguments);
}
}
}
}
}
if((requestMethod.equalsIgnoreCase("/GET")==true&& s.getIsGetAllowed()==true)||(requestMethod.equalsIgnoreCase("/POST")==true&& s.getIsPostAllowed()==true))
{
if(resultFromService!=null && s.getForwardTo()!=null)  //on reture value from method starts here
{
Class parameterClass=s.getParameters().get(0).getType();
if(parameterClass.getClass().isInstance(resultFromService.getClass()))
{
Object[] arguments={resultFromService};
resultFromService=s.getService().invoke(obj,arguments);
}
else
{
throw new ServiceException("Parameter are not type of previous service class parameter");
}
}else if(resultFromService!=null)
{
resultFromService=null;
}//on reture value from method ends here

else if(s.getParameters().size()>0)
{
Object[] arguments=new Object[s.getParameters().size()];
String parameterName=null;
boolean found=false;
Parameter parameter=null;
Object value=null;
for(int k=0;k<s.getParameters().size();k++)
{
parameter=s.getParameters().get(k);

if(parameter.getDeclaredAnnotations().length>0)
{
parameterName=((RequestParameter)parameter.getDeclaredAnnotations()[0]).value();
found=false;
value=request.getParameter(parameterName);
if(value!=null)found=true;
if(found==false)
{
value=request.getAttribute(parameterName);
if(value!=null)found=true;
}
if(found==false)
{
value=request.getSession().getAttribute(parameterName);
if(value!=null)found=true;
}
if(found==false)
{
value=request.getServletContext().getAttribute(parameterName);
if(value!=null)found=true;
}
if(found==true)
{
if(parameter.getType().getClass().isInstance(value))
{
arguments[k]=value;
}
else
{
if(parameter.getType().getName().equals("long"))
{
arguments[k]=Long.parseLong(value.toString());
}
else if(parameter.getType().getName().equals("int"))
{
arguments[k]=Integer.parseInt(value.toString());
}
else if(parameter.getType().getName().equals("short"))
{
arguments[k]=Short.parseShort(value.toString());
}
else if(parameter.getType().getName().equals("byte"))
{
arguments[k]=Byte.parseByte(value.toString());
}
else if(parameter.getType().getName().equals("double"))
{
arguments[k]=Double.parseDouble(value.toString());
}
else if(parameter.getType().getName().equals("float"))
{
arguments[k]=Float.parseFloat(value.toString());
}
else if(parameter.getType().getName().equals("char"))
{
arguments[k]=value.toString().charAt(0);
}
else if(parameter.getType().getName().equals("boolean"))
{
arguments[k]=Boolean.parseBoolean(value.toString());
}
}
}
else
{
throw new ServiceException("invalid request Parameter type ");
}
}// if condition ends here (parameter.getDeclaredAnnotations().length>0)
else
{
if(parameter.getType().getName().equals("com.thinking.machines.webrock.pojo.RequestScope"))
{
RequestScope rs=new RequestScope(request);
arguments[k]=rs;
}
else if(parameter.getType().getName().equals("com.thinking.machines.webrock.pojo.SessionScope"))
{
SessionScope ss=new SessionScope(request.getSession());
arguments[k]=ss;
}
else if(parameter.getType().getName().equals("com.thinking.machines.webrock.pojo.ApplicationDirectory"))
{
ApplicationDirectory ad=new ApplicationDirectory(new File(path));
arguments[k]=ad;
}
else if(parameter.getType().getName().equals("com.thinking.machines.webrock.pojo.ApplicationScope"))
{
ApplicationScope as=new ApplicationScope(request.getServletContext());
arguments[k]=as;
}//request contains json part starts here
else if(jsonString!=null)
{
//request containing JSON part starts here
Gson gson=new Gson();
arguments[k]=gson.fromJson(jsonString,parameter.getType());
jsonString=null;
//request containing JSON part ends here
}
else
{
try
{
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}catch(Exception e){}
//throw new ServiceException("Use of JSON for multiple time/Invalid parameter type");
}
}
}//for loop ends here 

resultFromService=s.getService().invoke(obj,arguments);
jsonString=new Gson().toJson(resultFromService);
PrintWriter pw=response.getWriter();
pw.print(jsonString);

}//else if ends here
else
{
resultFromService=s.getService().invoke(obj);

jsonString=new Gson().toJson(resultFromService);
PrintWriter pw=response.getWriter();
pw.print(jsonString);

}
}
else
{
try
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
}catch(Exception e){}
}

if(s.getForwardTo()!=null)
{
RequestDispatcher requestDispatcher=request.getRequestDispatcher(s.getForwardTo());
requestDispatcher.forward(request,response);
}

}catch(Exception exception)
{
System.out.println(exception);
try
{
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}catch(Exception e){}
}
}


else
{
try
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
return;
}catch(Exception e){}
}
}
public void doPost(HttpServletRequest request,HttpServletResponse response)
{
doGet(request,response);
}
}