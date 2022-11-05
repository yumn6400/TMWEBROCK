package com.thinking.machines.webrock.pojo;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
import com.google.gson.*;
public class AutowiredProperty
{
private Class propertyClass; //property class name
private String name; // autowired contains name
public AutowiredProperty()
{
this.propertyClass=null;
this.name=null;
}
public void setPropertyClass(Class propertyClass)
{
this.propertyClass=propertyClass;
}
public Class getPropertyClass()
{
return this.propertyClass;
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
}