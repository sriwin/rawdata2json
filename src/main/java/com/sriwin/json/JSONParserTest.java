package com.sriwin.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONParserTest {
  public static void main(String[] args) {
    try {
      String folderPath = System.getProperty("user.dir") + File.separator + "etc" + File.separator;
      String metaFileContent = FileUtils.readFileToString(new File(folderPath + "given.meta"), "UTF-8");
      String dataFileContent = FileUtils.readFileToString(new File(folderPath + "given.dat"), "UTF-8");

      // get col names
      Map<Integer, String> namesMap = getColNamesMap(metaFileContent);
      System.out.println(namesMap);

      // get col names
      List<Map<String, String>> list = getDataList(namesMap, dataFileContent);
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
      String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
      System.out.println(json);


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static Map<Integer, String> getColNamesMap(String metaFileContent) {
    Map<Integer, String> map = new HashMap<Integer, String>();
    String[] metaLineTokens = metaFileContent.split("\\n");
    for (int i = 0; i < metaLineTokens.length; i++) {
      if (i > 0) {
        String[] tokens = metaLineTokens[i].split("\\|");
        int colId = Integer.parseInt(tokens[9]);
        String colName = tokens[3];
        map.put(colId, colName);
      }
    }
    return map;
  }

  private static List<Map<String, String>> getDataList(Map<Integer, String> colNamesMap,
                                                       String dataFileContent) {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    try {
      Map<String, String> dataMap = new HashMap<String, String>();
      String[] dataLineTokens = dataFileContent.split("\\n");
      for (int i = 0; i < dataLineTokens.length; i++) {
        if (i > 0) {
          String[] tokens = dataLineTokens[i].split(",");
          for (int j = 1; j <= tokens.length; j++) {
            String colName = colNamesMap.get(j);
            dataMap.put(colName, tokens[j-1].trim());
          }
          list.add(dataMap);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }
}