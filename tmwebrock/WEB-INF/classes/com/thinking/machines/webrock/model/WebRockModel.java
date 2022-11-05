package com.thinking.machines.webrock.model;
import java.util.*;
import com.thinking.machines.webrock.pojo.*;
import com.google.gson.*;
public class WebRockModel
{
private static Map<String,Services> serviceMap=new HashMap<>();
public WebRockModel()
{
} 
public void addServiceMap(String fullPath,Services services)
{
serviceMap.put(fullPath,services);
}
public Map<String,Services> getServiceMap()
{
return this.serviceMap;
}
public int sizeOfServiceMap()
{
return this.serviceMap.size();
}

}