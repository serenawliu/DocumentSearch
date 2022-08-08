package org.casestudy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine {
    private static final String fileDirectory = "data";
    private static final Set<String> textFileNames = new HashSet<>();
    private static final HashMap<String, String> textFilesAsStrings = new HashMap<>();
    private static final HashMap<String, HashMap<String, Integer>> indexedFileMapping = new HashMap<>();

    public enum searchMethod {
        STRING_SEARCH, REGEX_SEARCH, PREPROCESS_SEARCH
    }

    /*
    Method to call each search method
    @param: stringToMatch - string to search
    @param: searchType - enum indicating method used to search
    @returns - LinkedHashMap containing each file name and
     */
    public LinkedHashMap<String, Integer> search(String stringToMatch, searchMethod searchType) {

        LinkedHashMap<String, Integer> hm = new LinkedHashMap<>();
        int searchResult;

        switch (searchType) {
            case STRING_SEARCH:
                for (String fileName : textFileNames) {
                    searchResult = stringSearch(stringToMatch.toLowerCase(), fileName);
                    if (0 < searchResult) {
                        hm.put(fileName, searchResult);
                    }
                }
                break;
            case REGEX_SEARCH:
                for (String fileName : textFileNames) {
                    searchResult = regexSearch(stringToMatch.toLowerCase(), fileName);
                    if (0 < searchResult) {
                        hm.put(fileName, searchResult);
                    }
                }
                break;
            case PREPROCESS_SEARCH:
                for (String fileName : textFileNames) {
                    searchResult = preprocessSearch(stringToMatch.toLowerCase(), fileName);
                    if (0 < searchResult) {
                        hm.put(fileName, searchResult);
                    }
                }
                break;
            default:
                break;
        }
        return sortMapByRelevance(hm);
    }

    /*
    Method to parse through a text file string and search for a target string.
    @param: stringToMatch - target string to find in file
    @param: fileToSearch - file to search for matching string
    @return: int indicating occurrences of exact string match
     */
    private int stringSearch(String stringToMatch, String fileName) {

        if (0 == stringToMatch.length()) return 0;
        int matching_string_count = 0;
        if (textFilesAsStrings.containsKey(fileName)) {
            String targetText = textFilesAsStrings.get(fileName);
            matching_string_count = targetText.split(stringToMatch, -1).length - 1;
        }
        return matching_string_count;
    }

    /*
    Method to parse through a text file string and search for a target string using regex.
    @param: stringToMatch - target string to find in file
    @param: fileName - file to search for matching string
    @return: int indicating occurrences of exact string match
    */
    private int regexSearch(String stringToMatch, String fileName) {

        if (0 == stringToMatch.length()) return 0;
        Pattern pattern = Pattern.compile(stringToMatch, Pattern.LITERAL);
        Matcher matcher;
        int matching_string_count = 0;

        if (textFilesAsStrings.containsKey(fileName)) {
            String targetText = textFilesAsStrings.get(fileName);
            matcher = pattern.matcher(targetText);
            while (matcher.find()) {
                ++matching_string_count;
            }
        }
        return matching_string_count;
    }

    /*
    Method to parse through a text file string and search for a target string using an indexed search.
    @param: stringToMatch - target string to find in file
    @param: fileName - file to search for matching string
    @return: int indicating occurrences of exact string match
     */
    private int preprocessSearch(String stringToMatch, String fileName) {

        if (0 == stringToMatch.length()) return 0;
        if (indexedFileMapping.containsKey(fileName)) {
            if (indexedFileMapping.get(fileName).containsKey(stringToMatch)) {
                return indexedFileMapping.get(fileName).get(stringToMatch);
            }
        }
        return 0;
    }

    /*
    Helper method to index a text file for the indexed search method.
    @param: fileName - file to search for matching string
     */
    private void indexTextFile(String fileName) {

        HashMap<String, Integer> wordFrequencies = new HashMap<>();

        String text = textFilesAsStrings.get(fileName);
        for (String word : text.split("\\s+")) {
            String wordLower = word.toLowerCase();
            if (!wordFrequencies.containsKey(wordLower)) {
                wordFrequencies.put(wordLower, 1);
            } else {
                wordFrequencies.put(wordLower, wordFrequencies.get(wordLower) + 1);
            }
        }
        indexedFileMapping.put(fileName, wordFrequencies);
    }

    /*
    Method to add a text file to list of text files to be queried.
    @param: fileName - Name of file to add.
     */
    public void addTextFile(String fileName) throws IOException {

        if (new File(fileDirectory + '/' + fileName).isFile()) {
            textFileNames.add(fileName);
            storeTextFileToString(fileName);
            indexTextFile(fileName);
        } else {
            System.out.println("WARNING: File " + fileName + " could not be found. Check the file path.");
        }
    }

    /*
    Helper method to convert a File to a String and store it in a shared map.
    @param: fileName - Name of file to convert to a string
     */
    private void storeTextFileToString(String fileName) throws IOException {

        if (!textFilesAsStrings.containsKey(fileName)) {
            // Create a string from text file.
            List<String> lines = Files.readAllLines(Paths.get(fileDirectory + '/' + fileName), StandardCharsets.UTF_8);
            String fileString = String.join(" ", lines);
            textFilesAsStrings.put(fileName, fileString.toLowerCase());
        }
    }
    /*
    Helper method to sort a LinkedHashMap by highest value (relevance).
    @param: unsortedMap - map to be sorted by relevance.
    @return: sorted map by relevance.
     */
    private LinkedHashMap<String, Integer> sortMapByRelevance(Map<String, Integer> unsortedMap) {

        LinkedHashMap<String, Integer> relevanceSortedMap = new LinkedHashMap<>();
        unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> relevanceSortedMap.put(x.getKey(), x.getValue()));

        return relevanceSortedMap;
    }

    public void printMap(Map<String, Integer> map) {

        for (String fileName : map.keySet()) {
            System.out.println(fileName + " " + map.get(fileName));
        }
    }

    public String getFileDirectory() {

        return fileDirectory;

    }

    public Set<String> getTextFileNames() {

        return textFileNames;

    }

    public HashMap<String, String> getTextFilesAsStrings() {

        return textFilesAsStrings;

    }
}
