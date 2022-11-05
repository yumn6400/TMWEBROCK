package bobby.test;
import java.util.*;
import com.thinking.machines.webrock.annotation.*;
import com.thinking.machines.webrock.pojo.*;
import java.lang.annotation.*;
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
}