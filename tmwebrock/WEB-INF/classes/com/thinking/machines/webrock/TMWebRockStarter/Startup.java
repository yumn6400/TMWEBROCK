package com.thinking.machines.webrock.TMWebRockStarter;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import com.thinking.machines.webrock.annotation.*;
import java.lang.reflect.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.model.*;
import java.util.*;
import java.lang.annotation.*;
public class Startup extends HttpServlet
{
public void init() 
{ 
String val=getServletContext().getInitParameter("SERVICE_PACKAGE_PREFIX");
try
{
java.nio.file.Path path=java.nio.file.Paths.get("");
String realPath=path.toAbsolutePath().toString();
realPath=realPath.substring(0,realPath.length()-3)+"webapps\\tmwebrock\\WEB-INF\\classes\\"+val;
File[] files=new File(realPath).listFiles();

processOnFiles(files,val);

WebRockModel model=new WebRockModel();
List<Services> list;
list= new LinkedList<>();
for(Services services:model.getServiceMap().values())
{
if(services.getPriority()<=0)continue;
list.add(services);
}
Collections.sort(list,(i1,i2)->{return i1.getPriority()-i2.getPriority();});
for(Services s:list)
{
Object obj=s.getServiceClass().newInstance();
s.getService().invoke(obj);
}
}catch(Exception exception)
{
System.out.println(exception);	
}
}


public void processOnFiles(File []files,String name)
{
for(File file:files)
{
if(file.isDirectory())
{
name=name+"."+file.getName();
processOnFiles(file.listFiles(),name);
}
else
{
if(file.getName().substring(file.getName().length()-6).equals(".class"))
{
try
{
String newName=name+"."+file.getName().substring(0,file.getName().length()-6);
Class c=Class.forName(newName);
WebRockModel model=new WebRockModel();
Path pathOnType;
Path pathOnMethod;
Method methods[];
Field fields[];
String fullPath;
int priority;
pathOnType=(Path)c.getAnnotation(Path.class);
if(pathOnType!=null)
{
fields=c.getDeclaredFields();
if(fields!=null)
{
for(Field f:fields)
{
if((Autowired)f.getAnnotation(Autowired.class)!=null)
{
AutowiredProperty autowiredProperty=new AutowiredProperty();
autowiredProperty.setPropertyClass(Class.forName(f.getType().getName()));
autowiredProperty.setName(((Autowired)f.getAnnotation(Autowired.class)).name());
Services.addAutowiredMap(c,autowiredProperty);
}
}
}


methods=c.getMethods();
if(methods!=null)
{
for(Method method:methods)
{
pathOnMethod=(Path)method.getAnnotation(Path.class);
if(pathOnMethod!=null)
{
fullPath=pathOnType.value()+pathOnMethod.value();
Services services=new Services();
services.setServiceClass(c);
services.setService(method);
services.setPath(fullPath);

if((MethodType)c.getAnnotation(MethodType.class)==null && (MethodType)method.getAnnotation(MethodType.class)==null)
{
services.setIsGetAllowed(true);
services.setIsPostAllowed(true);
}
else if((MethodType)method.getAnnotation(MethodType.class)!=null)
{
String value=((MethodType)method.getAnnotation(MethodType.class)).value();
if(value.equalsIgnoreCase("/GET"))services.setIsGetAllowed(true);
else if(value.equalsIgnoreCase("/POST"))services.setIsPostAllowed(true);
}
else 
{
String value=((MethodType)c.getAnnotation(MethodType.class)).value();
if(value.equalsIgnoreCase("/GET"))services.setIsGetAllowed(true);
else if(value.equalsIgnoreCase("/POST"))services.setIsPostAllowed(true);
}
if((Forward)method.getAnnotation(Forward.class)!=null)services.setForwardTo(((Forward)method.getAnnotation(Forward.class)).value());
if((OnStartup)method.getAnnotation(OnStartup.class)!=null)services.setPriority(((OnStartup)method.getAnnotation(OnStartup.class)).Priority());
if((InjectApplicationScope)c.getAnnotation(InjectApplicationScope.class)!=null)services.setInjectApplicationScope(true);
if((InjectSessionScope)c.getAnnotation(InjectSessionScope.class)!=null)services.setInjectSessionScope(true);
if((InjectRequestScope)c.getAnnotation(InjectRequestScope.class)!=null)services.setInjectRequestScope(true);
if((InjectApplicationDirectory)c.getAnnotation(InjectApplicationDirectory.class)!=null)services.setInjectApplicationDirectory(true);
for(Parameter parameter:method.getParameters())
{
services.addParameters(parameter);
}  
for(Field field:c.getDeclaredFields())
{
services.addFields(field);
}   

if((SecuredAccess)c.getAnnotation(SecuredAccess.class)!=null)services.setSecuredAccess(true);                         
model.addServiceMap(fullPath,services);
}
}
}
}
}catch(Exception e)
{
System.out.println(e);
}
}
}
}
}
}