# TMWEBROCK-FRAMEWORK
A J2EE Web service Framework are a holistic set of guidelines and specifications that provide platforms, tools, and programming
environments for addressing the design, integration, performance, security, and reliability of distributed and multi-tiered
applications. An application framework includes the presentation services, server-side processing, session management,business logic framework, 
application data caching, application logic, persistence, transactions, security, and logging services for applications. 

TMWEBROCK framework Features:

1. On Server  Startup :
Developer set param name/value pair in web.xml  by using SERVICE_PACKAGE_PREFIX can name and pass folder name as value. Statup Service scan all folder inside value pass by developer and get all class file having Path annotation . All this classes having annotation as path are loaded during startup of server and create  data Structure of it.

2. GET/ POST request :
 Developer can pass annotation as GET/POST on class/method for a request to process related to it ( default GET annotation applied).
 
 3. Priority of Method :
 Developer also specify priority of method to load during startup of server using OnStartup annotation and pass value as int and Priority as key. Method having
 less int value or higher priority loads first.
 
 4. Scope :
 Developer also use scope as RequestScope, SessionScope, ApplicationScope,ApplicationDirectory for storing cookie/information used during application .
 For this feature developer has to specified injectSessionScope/ injectApplicationScope / injectRequestScope /injectApplicationDirectory above class
 and write setAttribute / getAttribute method inside class .
 
 5. Autowired :
 Developer can also use autowired feature on property used in application . By Passing Autowired attribute as annotation , key as name and value as string type. During startup server can find value along all scope and set along property . If not found then set null as value along property.
 
 6. Forwarding :
 Developer can also forward request to another service using Forward as annotation and pass path of required service to forward .
 During forwarding if first service return any value then second service can use of it by setting parameter related to it.
 
 7. RequestParameter :
 Developer can also use feature of Request Parameter through which on creating a service along class and pass parameter to it . If he wanted to add 
 value along parameter then  he has to specify value along scope or along  queryString.
 
 8. Scope along service  :
 Developer can use scope feature without injecting annotation on class . By creating service along class and pass scope as parameter he can get feature
 of scope.
 
 9. InjectRequestParameter :
 Developer can inject value to a field using InjectRequestParameter as annotation declared on respective field and pass string to it . On Loaded of class
 server can find string along scope and along query String if found then set along property otherwise set null value to it.
 
 10. Request containing JSON as queryString :
 Developer can use feature of Request containing JSON  as queryString through it by passing parameter along service related to JSON .
 
 11.Security :
 Developer can use feature of Security by using SecuredAccess annotation and pass checkpost and guard value to it.
 
 12. PDF file :
 Developer can also create PDF file of all classes containing information as path,class name,methods,parameters,return type and annotation applied on it.
 For getting PDF File :
 Java -classpath :-->jar file path<--:  ServicesDoc :-->Path to classe Folder<--:  :-->pdf file path<--:
 
 
 Examples of using Framework:
 
 Example1:
 
 @Path("/student")
public class Student
{
@Path("/add")
@MethodType("/POST")
@Forward("/student/process")
public void add()
{
System.out.println("God is great");	
}
 
 Example 2:
 
@Path("/manager")
@InjectApplicationScope
public class Manager
{
private ApplicationScope applicationScope;
public void setApplicationScope(ApplicationScope applicationScope)
{
this.applicationScope=applicationScope;
}
}

Example 3:

@Path("/autowiredTest")
public class AutowiredTest
{
@Autowired(name="xyz")
private Employee employee;
public void setEmployee(Employee employee)
{
this.employee=employee;
}
public Employee getEmployee()
{
return this.employee;
}
}

Example 4: 

@Path("/forwardRequest")
public class ForwardRequest
{
@Path("/process1")
@Forward("/forwardRequest/process2")
public int process1()
{
System.out.println("Process1 got called");
return 100;
}
@Path("/process2")
public void process2(int x)
{
System.out.println("Process2 got called and value of parameter "+x);
}
}

Example 5 :

@Path("/injectRequestParameterTest")
public class InjectRequestParameterTest
{
@InjectRequestParameter("value")
private float x;
public void setX(float x)
{
this.x=x;
}
public float getX()
{
return x;
}
@Path("/process1")
public void process1()
{
System.out.println("Process1 got called");
System.out.println(this.x);
}
@Path("/process2")
public void process2()
{
System.out.println("Process2 got called");
System.out.println(this.x);
}
}

Example 6:
@SecuredAccess(checkPost="com.thinking.machines.webrock.check.CheckPost",guard="main")
@Path("/requestJSONTest")
public class RequestJSONTest
{
@Path("/process1")
@Forward("/main/requestJSONTest/process2")
public void process1(@RequestParameter("value1")int x,@RequestParameter("value2")int y,Student student,RequestScope rs)
{
System.out.println("Process1 got called");
System.out.println("Total of value1 :"+x+",value2: "+y+" is "+(x+y));
System.out.println("Student detail: "+student.getRollNumber()+","+student.getName());
System.out.println(rs);
}
@Path("/process2")
public void process2()
{
System.out.println("God is great ");
}
}

Example 7 :
 
@SecuredAccess(checkPost="com.thinking.machines.webrock.check.CheckPost",guard="main")
@Path("/requestJSONTest")
public class RequestJSONTest
{
@Path("/process1")
@Forward("/main/requestJSONTest/process2")
public void process1(@RequestParameter("value1")int x,@RequestParameter("value2")int y,Student student,RequestScope rs)
{
System.out.println("Process1 got called");
System.out.println("Total of value1 :"+x+",value2: "+y+" is "+(x+y));
System.out.println("Student detail: "+student.getRollNumber()+","+student.getName());
System.out.println(rs);
}
@Path("/process2")
public int  process2()
{
System.out.println("Process 2 got called");
System.out.println("God is great ");
return 100;
}


PDF :

![Screenshot (310)](https://user-images.githubusercontent.com/82231622/200102221-e4625d62-e9b1-499f-ab63-3bac035bd2a3.png)

