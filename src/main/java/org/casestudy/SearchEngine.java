package org.casestudy;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SearchEngine {
    private static Set<String> textFileNames = new HashSet<>();
    private static HashMap<String, String> textFilesAsStrings = new HashMap<>();
    public enum searchMethod {
        STRING_SEARCH, REGEX_SEARCH, PREPROCESS_SEARCH
    }
    private static String fileDirectory = "data";

    public void addTextFile(String fileName) {

        if (new File(fileDirectory + '/' + fileName).isFile()) {
            textFileNames.add(fileName);
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

    public LinkedHashMap<String, Integer> search(String stringToMatch, searchMethod s) throws IOException {
        LinkedHashMap<String, Integer> hm = new LinkedHashMap<>();

        switch (s) {
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
    public int stringSearch(String stringToMatch, String filePath) throws IOException {
        File textFile = new File(fileDirectory + '/' + filePath);
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
        File textFile = new File(fileDirectory + '/' + filePath);
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
        //TODO: Find a method for grabbing multiple searches
        int matching_string_count = 0;
        return matching_string_count;
    }

    /*
    Helper method to process a file.


    @return: HashMap containing a string and the index of appearance.
     */
    public void textFileToString(String fileName) throws IOException {

        // Create a string from text file.
        List<String> lines = Files.readAllLines(Paths.get(fileDirectory + '/' + fileName), StandardCharsets.US_ASCII);
        String fileString = String.join("", lines);
        textFilesAsStrings.put(fileName, fileString);

    }

    public void indexTextFile(){

    }
}
