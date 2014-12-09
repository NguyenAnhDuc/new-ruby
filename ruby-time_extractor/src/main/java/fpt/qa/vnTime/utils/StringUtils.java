package fpt.qa.vnTime.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StringUtils
{
  private static Scanner sc;

public static String textFileToString(String filePath)
    throws FileNotFoundException
  {
    StringBuilder s = new StringBuilder();
    File file = new File(filePath);
    sc = new Scanner(new FileInputStream(file));
    while (sc.hasNextLine()) {
      s.append(sc.nextLine()).append("\n");
    }
    String result = s.toString();
    return result;
  }
}