package org.casestudy;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine {
    private static Set<String> textFilePaths = new HashSet<>();

    public enum searchMethod {
        STRING_SEARCH, REGEX_SEARCH, PREPROCESS_SEARCH
    }

    public void addTextFilePath(String filePath) {

        if (new File(filePath).isFile()) {
            textFilePaths.add(filePath);
        } else {
            System.out.println("WARNING: File " + filePath + " could not be found. Check the file path.");
        }
    }

    public void printMap(LinkedHashMap<String, Integer> map) {

        for (String fileName : map.keySet()) {
            System.out.println(fileName + " " + map.get(fileName));
        }
    }

    public LinkedHashMap<String, Integer> sortByRelevance(Map<String, Integer> unsortedMap) {

        LinkedHashMap<String, Integer> relevanceSortedMap = new LinkedHashMap<>();
        unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> relevanceSortedMap.put(x.getKey(), x.getValue()));

        return relevanceSortedMap;
    }

    public LinkedHashMap<String, Integer> search(String stringToMatch, searchMethod s) throws IOException {
        LinkedHashMap<String, Integer> hm = new LinkedHashMap<>();

        switch (s) {
            case STRING_SEARCH:
                for (String filePath : textFilePaths) {
                    String[] paths = filePath.split("/");
                    hm.put(paths[paths.length - 1], stringSearch(stringToMatch.toLowerCase(), filePath));
                }
                break;
            case REGEX_SEARCH:
                for (String filePath : textFilePaths) {
                    String[] paths = filePath.split("/");
                    hm.put(paths[paths.length - 1], regexSearch(stringToMatch.toLowerCase(), filePath));
                }
                break;
            case PREPROCESS_SEARCH:
                for (String filePath : textFilePaths) {
                    String[] paths = filePath.split("/");
                    hm.put(paths[paths.length - 1], preprocessSearch(stringToMatch.toLowerCase(), filePath));
                }
                break;
            default:
                break;
        }
        return sortByRelevance(hm);
    }

    /*
    Method to parse through file and search for a target string.
    @param: stringToMatch - target string to find in file
    @param: fileToSearch - file to search for matching string
    @return: int indicating occurrences of exact string match
     */
    public int stringSearch(String stringToMatch, String filePath) throws IOException {
        File textFile = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(textFile));
        String currentLine;
        int matching_string_count = 0;

        while ((currentLine = br.readLine()) != null) {
            if (currentLine.contains(stringToMatch)) {
                ++matching_string_count;
            }
        }
        return matching_string_count;
    }
    /*
    Method to parse through file and search for a target string using regex.
    @param: stringToMatch - target string to find in file
    @param: fileToSearch - file to search for matching string
    @return: int indicating occurrences of exact string match
     */
    public int regexSearch(String stringToMatch, String filePath) throws IOException {
        File textFile = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(textFile));
        String currentLine;

        Pattern pattern = Pattern.compile(stringToMatch, Pattern.LITERAL);
        Matcher matcher;
        int matching_string_count = 0;

        while ((currentLine = br.readLine()) != null) {
            matcher = pattern.matcher(currentLine);
            if (matcher.find()) {
                ++matching_string_count;
            }
        }
        return matching_string_count;
    }

    public int preprocessSearch(String stringToMatch, String filePath) {
        int matching_string_count = 0;
        String[] directories = filePath.split("/");
        String fileName = directories[directories.length-1];
        return matching_string_count;
    }
}
