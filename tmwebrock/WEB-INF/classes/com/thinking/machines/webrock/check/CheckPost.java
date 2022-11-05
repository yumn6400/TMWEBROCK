package com.thinking.machines.webrock.check;
import com.thinking.machines.webrock.pojo.*;
public class CheckPost
{
private Object token;
public void init(Object token)
{
this.token=token;
System.out.println(this.token);
}
public boolean main()
{
Object id=SessionScope.getAttribute("uniqueID");
if(id!=null)
{
String uniqueId=id.toString();
System.out.println(uniqueId+","+this.token.toString());
if(uniqueId.equals(this.token.toString())==true)
{
System.out.println("YES");
return true;
}
}
else
{
System.out.println("NO");
return false;
}
return false;
}
}