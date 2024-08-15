package com.wzy.demo.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlStringHandle {
    public static String htmlStringHandle(String htmlString) {

        String regex = "path=([^\\s\"']*?\\.*_temp)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlString);
        while (matcher.find()) {
            // 提取匹配的部分
            AppFileUtils.renameFile(matcher.group(1));
        }
        return htmlString.replace("_temp", "");
    }
    public static String htmlStringHandle(String oldStr, String newStr) {
        oldStr = htmlStringHandle(oldStr);
        newStr = htmlStringHandle(newStr);
        List<String> oldPaths = extractPaths(oldStr);
        List<String> newPaths = extractPaths(newStr);
        System.out.println(oldPaths);
        System.out.println(newPaths);
        Set<String> newPathSet = new HashSet<>(newPaths);
        List<String> missingPaths = new ArrayList<>();
        
        for (String path : oldPaths) {
            if (!newPathSet.contains(path)) {
                missingPaths.add(path);
            }
        }
        for (String path : missingPaths) {
            AppFileUtils.removeFileByPath(path);
        }
        return newStr;
    }
    public static List<String> extractPaths(String html) {
        List<String> paths = new ArrayList<>();
        String regex = "path=([^\"&\\s]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        
        while (matcher.find()) {
            paths.add(matcher.group(1));
        }
        
        return paths;
    }

}
