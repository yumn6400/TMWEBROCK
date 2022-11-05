package com.thinking.machines.webrock.pojo;
import java.io.*;
public class ApplicationDirectory
{
private static File directory;
public ApplicationDirectory(File directory)
{
this.directory=directory;
}
public static File getDirectory()
{
return directory;
}
}