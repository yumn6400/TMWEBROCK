package com.thinking.machines.webrock.pojo;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
import com.google.gson.*;
public class RequestParameters
{
private Class requestParameterClass; //request parameter type  in class
private String annotationName; // if requestParameter contains annotation then annotationAnnotationName
public RequestParameters()
{
this.requestParameterClass=null;
this.annotationName=null;
}
public void setRequestParameterClass(Class requestParameterClass)
{
this.requestParameterClass=requestParameterClass;
}
public Class getRequestParameterClass()
{
return this.requestParameterClass;
}
public void setAnnotationName(String annotationName)
{
this.annotationName=annotationName;
}
public String getAnnotationName()
{
return this.annotationName;
}
}