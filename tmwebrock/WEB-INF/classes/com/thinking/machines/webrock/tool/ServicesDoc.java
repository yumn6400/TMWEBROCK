package com.thinking.machines.webrock.tool;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.lang.annotation.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.image.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.geom.*;
class Doc
{
public String fullPath;
public Class c;
public Method method;
public Parameter[] parameters;
public String returnType;
public Annotation[] annotations;
}
public class ServicesDoc
{
private static java.util.Map<Class,java.util.List<Doc>> doc_list=new java.util.LinkedHashMap<>();

public static void main(String gg[])
{ 
String pathToClassesFolder=gg[0];
String pdfFilePath=gg[1];
try
{
java.nio.file.Path path=java.nio.file.Paths.get(gg[0]);
String realPath=path.toString();
File[] files=new File(realPath).listFiles();
processOnFiles(files,realPath);
File pdfFile=new File(gg[1]);
if(pdfFile.isDirectory())
{
File file=new File("pdf.pdf");
if(file.exists())file.delete();
exportToPDF(file);
}else
{
exportToPDF(pdfFile);
}
}catch(Exception exception)
{
System.out.println(exception);	
}
}
public static void processOnFiles(File []files,String name)
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
String className=c.getName().toString();
int i=0;
Doc doc;
java.util.List<Doc> list=new java.util.ArrayList<>();
for(Method m:c.getDeclaredMethods())
{
doc=new Doc();
doc.fullPath=newName;
doc.c=c;
doc.method=m;
doc.parameters=m.getParameters();
doc.returnType=m.getReturnType().getName().toString();
doc.annotations=m.getAnnotations();
list.add(doc);
}
doc_list.put(c,list);
}catch(Exception e)
{
System.out.println(e);
}
}
}
}
}
public static void exportToPDF(File file)
{
try
{
PdfWriter pdfWriter=new PdfWriter(file);
PdfDocument pdfDocument=new PdfDocument(pdfWriter);
Document document=new Document(pdfDocument,PageSize.A4);
document.setMargins(15,15,15,15);
float columnWidths[]={1,2,4,5};
PdfFont titleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
PdfFont dataFont=PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
Table table=new Table(UnitValue.createPercentArray(columnWidths));
table.setWidth(UnitValue.createPercentValue(100));
table.setFixedLayout();
Cell headingCell=new Cell(1,4).add(new Paragraph("Services Documentation"));
headingCell.setFont(titleFont);
headingCell.setFontSize(30);
headingCell.setTextAlignment(TextAlignment.CENTER);
table.addHeaderCell(headingCell);
Paragraph title1=new Paragraph("S.no");
title1.setFont(titleFont);
title1.setFontSize(8);
Paragraph title2=new Paragraph("Name");
title2.setFont(titleFont);
title2.setFontSize(8);
Paragraph title3=new Paragraph("Value1");
title3.setFont(titleFont);
title3.setFontSize(8);
Paragraph title4=new Paragraph("Value2");
title4.setFont(titleFont);
title4.setFontSize(8);
table.addHeaderCell(new Cell().add(title1));
table.addHeaderCell(new Cell().add(title2));
table.addHeaderCell(new Cell().add(title3));
table.addHeaderCell(new Cell().add(title4));
Paragraph cellPara;
Cell cell;
Integer i=1;
for(Class c:doc_list.keySet())
{
java.util.List<Doc> list=doc_list.get(c);
cellPara=new Paragraph(i.toString());
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
cell=new Cell().add(cellPara);
cell.setTextAlignment(TextAlignment.RIGHT);
table.addCell(cell);
cellPara=new Paragraph("CLASS");
cellPara.setFont(titleFont);
cellPara.setFontSize(25);
cell=new Cell().add(cellPara);
cell.setTextAlignment(TextAlignment.LEFT);
table.addCell(cell);
cellPara=new Paragraph(list.get(0).fullPath);
cellPara.setFont(titleFont);
cellPara.setFontSize(25);
cell=new Cell(1,2).add(cellPara);
cell.setTextAlignment(TextAlignment.CENTER);
table.addCell(cell);
i++;

cellPara=new Paragraph("PATH");
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
cell=new Cell(1,2).add(cellPara);
cell.setTextAlignment(TextAlignment.CENTER);
table.addCell(cell);
cellPara=new Paragraph(c.getName().toString());
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
cell=new Cell(1,2).add(cellPara);
cell.setTextAlignment(TextAlignment.CENTER);
table.addCell(cell);

cellPara=new Paragraph("METHOD");
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
int size=0;
for(Doc dd:list)
{
size+=1; //method
size+=1; //return type
if(dd.parameters.length>0)size+=dd.parameters.length; //parameters
if(dd.annotations.length>0)size+=dd.annotations.length; //annotations
}
cell=new Cell(size,2).add(cellPara);
cell.setTextAlignment(TextAlignment.CENTER);
table.addCell(cell);

for(Doc d:list)
{
cellPara=new Paragraph(d.method.getName().toString());
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
cell=new Cell(1,2).add(cellPara);
cell.setTextAlignment(TextAlignment.CENTER);
table.addCell(cell);

cellPara=new Paragraph("RETURN TYPE");
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
cell=new Cell(1,1).add(cellPara);
//cell.setTextAlignment(TextAlignment.CENTER);
table.addCell(cell);
cellPara=new Paragraph(d.returnType.toString());
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
cell=new Cell(1,1).add(cellPara);
//cell.setTextAlignment(TextAlignment.CENTER);
table.addCell(cell);
if(d.parameters.length>0)
{
cellPara=new Paragraph("PARAMETERS");
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
if(d.parameters.length==0)cell=new Cell(1,1).add(cellPara);
else cell=new Cell(d.parameters.length,1).add(cellPara);
//cell.setTextAlignment(TextAlignment.CENTER);
table.addCell(cell);

for(int k=0;k<d.parameters.length;k++)
{
cellPara=new Paragraph(d.parameters[k].getType().getName().toString());
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
cell=new Cell(1,1).add(cellPara);
cell.setTextAlignment(TextAlignment.LEFT);
table.addCell(cell);
}
}

if(d.annotations.length>0)
{
cellPara=new Paragraph("ANNOTATIONS");
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
if(d.annotations.length==0)cell=new Cell(1,1).add(cellPara);
else cell=new Cell(d.annotations.length,1).add(cellPara);
//cell.setTextAlignment(TextAlignment.CENTER);
table.addCell(cell);
for(int k=0;k<d.annotations.length;k++)
{
cellPara=new Paragraph(d.annotations[k].toString());
cellPara.setFont(dataFont);
cellPara.setFontSize(20);
cell=new Cell(1,1).add(cellPara);
cell.setTextAlignment(TextAlignment.LEFT);
table.addCell(cell);
}
}
}
//document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
}
document.add(table);
document.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}