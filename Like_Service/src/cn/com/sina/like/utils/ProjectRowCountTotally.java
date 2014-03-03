package cn.com.sina.like.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ProjectRowCountTotally {

 /** 普通行数 */
 private long normalLines = 0;
 /** 注释行数 */
 private long commentLines = 0;
 /** 空白行数 */
 private long spaceLines = 0;
 /** 总行数 */
 private long totalLines = 0;
 
 /** 普通行数 */
 long normalLinesTotal = 0;
 /** 注释行数 */
 long commentLinesTotal = 0;
 /** 空白行数 */
 long spaceLinesTotal = 0;
 /** 总行数 */
 long totalLinesTotal = 0;

 /***
  * 通过java文件路径构造该对象
  *
  * @param filePath
  *            java文件路径
  */
 public ProjectRowCountTotally(String filePath) {
  long[] array = tree(filePath);
  System.err.println("最终普通代码行数:" + array[0]);
  System.err.println("最终空白代码行数:" + array[1]);
  System.err.println("最终注释代码行数:" + array[2]);
  System.err.println("最终代码总行数:" + array[3]);
 }

 /**
  * 处理文件的方法
  *
  * @param filePath
  *            文件路径
  */
 private long[] tree(String filePath) {
  File file = new File(filePath);
  File[] childs = file.listFiles();
  if (childs == null) {
   parse(file);
  } else {
   for (int i = 0; i < childs.length; i++) {
    System.out.println("path:" + childs[i].getPath());
    if (childs[i].isDirectory()) {
     tree(childs[i].getPath());
    } else {
     childs[i].getName().matches(".*\\.java$");
     System.out.println("当前" + childs[i].getName() + "代码行数:");
     parse(childs[i]);
     long[] temp = getCodeCounter();
     normalLinesTotal += temp[0];
     commentLinesTotal += temp[1];
     spaceLinesTotal += temp[2];
     totalLinesTotal += temp[3];
    }
   }
  }
  long[] array = new long[4];
  array[0] = normalLinesTotal;
  array[1] = commentLinesTotal;
  array[2] = spaceLinesTotal;
  array[3] = totalLinesTotal;
  return array;
 }

 /**
  * 解析文件
  *
  * @param file
  *            文件对象
  */
 private void parse(File file) {
  BufferedReader br = null;
  boolean comment = false;
  try {
   br = new BufferedReader(new FileReader(file));
   String line = "";
   while ((line = br.readLine()) != null) {
    line = line.trim();// 去除空格
    if (line.matches("^[\\s&&[^\\n]]*$")) {
     spaceLines++;
    } else if ((line.startsWith("/*")) && !line.endsWith("*/")) {
     commentLines++;
     comment = true;
    } else if (true == comment) {
     commentLines++;
     if (line.endsWith("*/")) {
      comment = false;
     }
    } else if (line.startsWith("//")) {
     commentLines++;
    } else {
     normalLines++;
    }
   }

  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 /**
  * 得到Java文件的代码行数
  */
 private long[] getCodeCounter() {
  totalLines = normalLines + spaceLines + commentLines;
  System.out.println("普通代码行数:" + normalLines);
  System.out.println("空白代码行数:" + spaceLines);
  System.out.println("注释代码行数:" + commentLines);
  System.out.println("代码总行数:" + totalLines);
  long[] array = new long[4];
  array[0] = normalLines;
  array[1] = spaceLines;
  array[2] = commentLines;
  array[3] = totalLines;
  normalLines = 0;
  spaceLines = 0;
  commentLines = 0;
  totalLines = 0;
  return array;
 }

// public static void main(String args[]) {
//  ProjectRowCountTotally counter = new ProjectRowCountTotally("/home/su/workspace/Like/Like_Service/src");
// }

}
