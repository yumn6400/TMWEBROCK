package com.thinking.machines.webrock.pojo;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
import com.google.gson.*;
public class Services
{
private Class serviceClass;
private String path;
private Method service;
private boolean isGetAllowed;
private boolean isPostAllowed;
private String forwardTo;
private int priority;
private boolean injectSessionScope;
private boolean injectApplicationScope;
private boolean injectRequestScope;
private boolean injectApplicationDirectory;
private boolean securedAccess;
private static Map<Class,List<AutowiredProperty>> autowiredMap=new HashMap<>();
private  List<Parameter> parameters=new ArrayList<>();
private  List<Field> fields=new ArrayList<>();
public Services()
{
this.serviceClass=null;
this.path=null;
this.service=null;
this.isGetAllowed=false;
this.isPostAllowed=false;
this.priority=0;
this.injectSessionScope=false;
this.injectRequestScope=false;
this.injectApplicationScope=false;
this.injectApplicationDirectory=false;
}
public void setServiceClass(Class serviceClass)
{
this.serviceClass=serviceClass;
}
public Class getServiceClass()
{
return this.serviceClass;
}
public void setPath(String path)
{
this.path=path;
}
public String getPath()
{
return this.path;
}
public void setService(Method service)
{
this.service=service;
}
public Method getService()
{
return this.service;
}
public void setIsGetAllowed(boolean isGetAllowed)
{
this.isGetAllowed=isGetAllowed;
}
public boolean getIsGetAllowed()
{
return this.isGetAllowed;
}
public void setIsPostAllowed(boolean isPostAllowed)
{
this.isPostAllowed=isPostAllowed;
}
public boolean getIsPostAllowed()
{
return this.isPostAllowed;
}
public void setForwardTo(String forwardTo)
{
this.forwardTo=forwardTo;
}
public String getForwardTo()
{
return this.forwardTo;
}
public void setPriority(int priority)
{
this.priority=priority;
}
public int getPriority()
{
return this.priority;
}
public void setInjectSessionScope(boolean injectSessionScope)
{
this.injectSessionScope=injectSessionScope;
}
public boolean getInjectSessionScope()
{
return this.injectSessionScope;
}
public void setInjectApplicationScope(boolean injectApplicationScope)
{
this.injectApplicationScope=injectApplicationScope;
}
public boolean getInjectApplicationScope()
{
return this.injectApplicationScope;
}
public void setInjectRequestScope(boolean injectRequestScope)
{
this.injectRequestScope=injectRequestScope;
}
public boolean getInjectRequestScope()
{
return this.injectRequestScope;
}
public void setInjectApplicationDirectory(boolean injectApplicationDirectory)
{
this.injectApplicationDirectory=injectApplicationDirectory;
}
public boolean getInjectApplicationDirectory()
{
return this.injectApplicationDirectory;
}
public static void addAutowiredMap(Class c,AutowiredProperty autowiredProperty)
{
if(autowiredMap.containsKey(c))
{
List<AutowiredProperty> list=autowiredMap.get(c);
list.add(autowiredProperty);
autowiredMap.put(c,list);
}
else
{
List<AutowiredProperty> list=new ArrayList<>();
list.add(autowiredProperty);
autowiredMap.put(c,list);
}
}
public static Map<Class,List<AutowiredProperty>> getAutowiredMap()
{
return autowiredMap;
}
public void addParameters(Parameter parameter)
{
this.parameters.add(parameter);
}
public List<Parameter> getParameters()
{
List<Parameter>p=new ArrayList<>();
for(Parameter parameter:parameters)
{
p.add(parameter);
}
return p;
}
public void addFields(Field field)
{
this.fields.add(field);
}
public List<Field> getFields()
{
List<Field> f=new ArrayList<>();
for(Field field:fields)
{
f.add(field);
}
return f;
}
public void setSecuredAccess(boolean securedAccess)
{
this.securedAccess=securedAccess;
}
public boolean getSecuredAccess()
{
return this.securedAccess;
}
}