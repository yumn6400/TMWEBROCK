package bobby.test;
import com.thinking.machines.webrock.annotation.*;
import java.util.*;
import java.sql.*;
@Path("/studentService")
public class StudentService
{
private Connection getConnection()
{
Connection connection=null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/hrdb","hr","hr");
}catch(Exception e)
{}
return connection;
}
@Path("/add")
public void add(Student student)
{
int rollNumber=student.getRollNumber();
String name=student.getName();
String gender=String.valueOf(student.getGender());
System.out.println(rollNumber+","+name+","+gender);
try
{
Connection connection=getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("insert into student(rollNumber,name,gender)values(?,?,?)");
preparedStatement.setInt(1,rollNumber);
preparedStatement.setString(2,name);
preparedStatement.setString(3,gender);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
System.out.println("Student added");
}catch(Exception e)
{
System.out.println(e);
}
}
@Path("/update")
public void update(Student student)
{
int rollNumber=student.getRollNumber();
String name=student.getName();
String gender=String.valueOf(student.getGender());
try
{
Connection connection=getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("update student set name=?,gender=? where rollNumber=?");
preparedStatement.setInt(3,rollNumber);
preparedStatement.setString(1,name);
preparedStatement.setString(2,gender);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
System.out.println("Student updated");
}catch(Exception e)
{
System.out.println(e);
}
}
@Path("/delete")
public void delete(Student student)
{
int rollNumber=student.getRollNumber();
try
{
Connection connection=getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("delete from student where rollNumber=?");
preparedStatement.setInt(1,rollNumber);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
System.out.println("Student deleted");
}catch(Exception e)
{
System.out.println(e);
}
}
@Path("/getByCode")
public Student getByCode(Student student)
{
int rollNumber=student.getRollNumber();
try
{
Connection connection=getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select * from student where rollNumber=?");
preparedStatement.setInt(1,rollNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
student.setRollNumber(resultSet.getInt("rollNumber"));
student.setName(resultSet.getString("name"));
student.setGender(resultSet.getString("gender").charAt(0));
}
resultSet.close();
preparedStatement.close();
connection.close();
}catch(Exception e)
{
System.out.println(e);
}
return student;
}
@Path("/getAll")
public List<Student> getAll()
{
List<Student> list=new ArrayList<>();
Student student=null;
try
{
Connection connection=getConnection();
Statement statement=connection.createStatement();
ResultSet resultSet=statement.executeQuery("select * from student");
while(resultSet.next())
{
student=new Student();
student.setRollNumber(resultSet.getInt("rollNumber"));
student.setName(resultSet.getString("name"));
student.setGender(resultSet.getString("gender").charAt(0));
list.add(student);
}
resultSet.close();
statement.close();
connection.close();
System.out.println("Students list created ");
}catch(Exception e)
{
System.out.println(e);
}
return list;
}
}