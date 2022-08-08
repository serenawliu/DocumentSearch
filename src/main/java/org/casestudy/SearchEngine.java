package org.casestudy;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Paths;

public class SearchEngine {
    private static Set<String> textFileNames = new HashSet<>();
    private static HashMap<String, String> textFilesAsStrings = new HashMap<>();

    private static HashMap<String, HashMap<String, Integer>> indexedFileMapping = new HashMap<>();
    public enum searchMethod {
        STRING_SEARCH, REGEX_SEARCH, PREPROCESS_SEARCH
    }
    private static String fileDirectory = "data";

    public void addTextFile(String fileName) throws IOException {

        if (new File(fileDirectory + '/' + fileName).isFile()) {
            textFileNames.add(fileName);
            textFileToString(fileName);
            System.out.println(textFilesAsStrings.get(fileName));
        } else {
            System.out.println("WARNING: File " + fileName + " could not be found. Check the file path.");
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

    public LinkedHashMap<String, Integer> search(String stringToMatch, searchMethod searchType) {
        LinkedHashMap<String, Integer> hm = new LinkedHashMap<>();

        switch (searchType) {
            case STRING_SEARCH:
                for (String fileName : textFileNames) {
                    hm.put(fileName, stringSearch(stringToMatch.toLowerCase(), fileName));
                }
                break;
            case REGEX_SEARCH:
                for (String fileName : textFileNames) {
                    hm.put(fileName, regexSearch(stringToMatch.toLowerCase(), fileName));
                }
                break;
            case PREPROCESS_SEARCH:
                for (String fileName : textFileNames) {
                    hm.put(fileName, preprocessSearch(stringToMatch.toLowerCase(), fileName));
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
    public int stringSearch(String stringToMatch, String filePath){
        int matching_string_count = 0;
        if (textFilesAsStrings.containsKey(filePath)) {
            String targetText = textFilesAsStrings.get(filePath);
            matching_string_count = targetText.split(stringToMatch, -1).length-1;
        }
        return matching_string_count;
    }
    /*
    Method to parse through file and search for a target string using regex.
    @param: stringToMatch - target string to find in file
    @param: fileToSearch - file to search for matching string
    @return: int indicating occurrences of exact string match
     */
    public int regexSearch(String stringToMatch, String filePath) {

        Pattern pattern = Pattern.compile(stringToMatch, Pattern.LITERAL);
        Matcher matcher;
        int matching_string_count = 0;

        if (textFilesAsStrings.containsKey(filePath)) {
            String targetText = textFilesAsStrings.get(filePath);
            matcher = pattern.matcher(targetText);
            while(matcher.find()) {
                ++matching_string_count;
            }
        }
        return matching_string_count;
    }

    public int preprocessSearch(String stringToMatch, String filePath) {
        int matching_string_count = 0;
        return matching_string_count;
    }

    /*
    Helper method to convert a File to a String.


    @return: HashMap containing a string and the index of appearance.
     */
    public void textFileToString(String fileName) throws IOException {

        // Create a string from text file.
        List<String> lines = Files.readAllLines(Paths.get(fileDirectory + '/' + fileName), StandardCharsets.UTF_8);
        String fileString = String.join("", lines);
        textFilesAsStrings.put(fileName, fileString);

    }

    public void indexTextFile(String fileName){
        HashMap<String, Integer> wordFrequencies= new HashMap();

        String text = textFilesAsStrings.get(fileName);
        for (String word: text.split("\\s+")){
            if(!wordFrequencies.containsKey(word)) {
                wordFrequencies.put(word, 1);
            }
            else{
                wordFrequencies.put(word, wordFrequencies.get(word)+1);
            }
        }
        indexedFileMapping.put(fileName, wordFrequencies);
    }
}
