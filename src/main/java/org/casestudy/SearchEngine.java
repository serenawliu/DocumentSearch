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
    private static final HashMap<String, HashMap<String, List<Integer>>> wordIndexMapping = new HashMap<>();
    private static final HashMap<String, HashMap<String, Integer>> wordFrequencyMapping = new HashMap<>();

    public enum searchMethod {
        STRING_SEARCH, REGEX_SEARCH, PREPROCESS_SEARCH
    }

    /**
     * Method to call a search method based on user input.
     *
     * @param stringToMatch - string to search
     * @param searchType    - enum indicating method used to search
     * @return - LinkedHashMap with filename and int indicating number of search term appearances
     */
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
        return sortMapByRelevance(hm);
    }

    /**
     * Method to parse through a text file string and search for a target string.
     *
     * @param stringToMatch - target string
     * @param fileName      - file to search
     * @return int indicating occurrences of exact string match
     */
    private int stringSearch(String stringToMatch, String fileName) {

        if (!textFilesAsStrings.containsKey(fileName)) return 0;

        String targetText = textFilesAsStrings.get(fileName);

        return targetText.split(stringToMatch, -1).length - 1;
    }

    /**
     * Method to parse through a text file string and search for a target string using regex.
     *
     * @param stringToMatch - target string
     * @param fileName      - file to search
     * @return int indicating occurrences of exact string match
     */
    private int regexSearch(String stringToMatch, String fileName) {

        if (!textFilesAsStrings.containsKey(fileName)) return 0;

        String targetText = textFilesAsStrings.get(fileName);
        Pattern pattern = Pattern.compile(stringToMatch, Pattern.LITERAL);
        Matcher matcher;

        int matching_string_count = 0;
        matcher = pattern.matcher(targetText);

        while (matcher.find()) {
            ++matching_string_count;
        }

        return matching_string_count;
    }

    /**
     * Method to parse through a text file string and search for a target string using an indexed search.
     *
     * @param stringToMatch - target string
     * @param fileName      - file to search
     * @return int indicating occurrences of exact string match
     */
    private int preprocessSearch(String stringToMatch, String fileName) {

        if (!wordFrequencyMapping.containsKey(fileName)) return 0;

        Map<String, Integer> tempMap = wordFrequencyMapping.get(fileName);
        String[] wordsToQuery = stringToMatch.split(" ");

        if (1 == wordsToQuery.length) {
            if (tempMap.containsKey(stringToMatch)) {
                return tempMap.get(stringToMatch);
            }
        } else {
            for (String word : wordsToQuery) {
                if (!tempMap.containsKey(word)) {
                    return 0;
                }
            }
            return indexSearch(wordsToQuery, fileName);
        }
        return 0;
    }

    /**
     * Method for a search query containing multiple words.
     *
     * @param wordsToQuery - array of words in search term.
     * @param fileName     - fileName to perform indexSearch.
     * @return int indicating number of search term matches in file.
     */
    private int indexSearch(String[] wordsToQuery, String fileName) {
        Map<String, List<Integer>> tempMap = wordIndexMapping.get(fileName);
        String currentWord;
        Integer currentIndex;
        Queue<Integer> indicesToCheck = new LinkedList<>(tempMap.get(wordsToQuery[0]));
        int sizeOfIndicesToCheck;

        for (int i = 1; i < wordsToQuery.length; i++) {
            currentWord = wordsToQuery[i];
            sizeOfIndicesToCheck = indicesToCheck.size();

            for (int j = 0; j < sizeOfIndicesToCheck; j++) {
                currentIndex = indicesToCheck.remove();
                for (Integer temp : tempMap.get(currentWord)) {
                    if (temp == currentIndex + 1) {
                        indicesToCheck.add(temp);
                        break;
                    }
                }
            }
        }
        return indicesToCheck.size();
    }

    /**
     * Method to index a text file for the indexed search method.
     *
     * @param fileName - file to search
     */
    private void indexTextFile(String fileName) {

        HashMap<String, Integer> wordFrequencies = new HashMap<>();
        HashMap<String, List<Integer>> wordIndices = new HashMap<>();

        Integer index = 0;
        List<Integer> tempIndexList;
        String text = textFilesAsStrings.get(fileName);
        String wordLower, wordAlphaNumeric;

        for (String word : text.split("\\s+")) {

            wordLower = word.toLowerCase();
            wordAlphaNumeric = wordLower.replaceAll("[^A-Za-z0-9]", "");

            if (!wordFrequencies.containsKey(wordLower)) {
                wordFrequencies.put(wordLower, 1);
            } else {
                wordFrequencies.put(wordLower, wordFrequencies.get(wordLower) + 1);
            }

            if (!wordLower.equals(wordAlphaNumeric)) {
                if (!wordFrequencies.containsKey(wordAlphaNumeric)) {
                    wordFrequencies.put(wordAlphaNumeric, 1);
                } else {
                    wordFrequencies.put(wordAlphaNumeric, wordFrequencies.get(wordLower) + 1);
                }
            }

            if (!wordIndices.containsKey(wordLower)) {
                tempIndexList = new ArrayList<>();
            } else {
                tempIndexList = wordIndices.get(wordLower);
            }
            tempIndexList.add(index);
            wordIndices.put(wordLower, tempIndexList);

            if (!wordLower.equals(wordAlphaNumeric)) {
                if (!wordIndices.containsKey(wordAlphaNumeric)) {
                    tempIndexList = new ArrayList<>();
                } else {
                    tempIndexList = wordIndices.get(wordAlphaNumeric);
                }
                tempIndexList.add(index);
                wordIndices.put(wordAlphaNumeric, tempIndexList);
            }

            index++;
        }
        wordFrequencyMapping.put(fileName, wordFrequencies);
        wordIndexMapping.put(fileName, wordIndices);
    }

    /**
     * Method to add a text file to list of text files to be queried.
     *
     * @param fileName - Name of file to add.
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

    /**
     * Helper method to convert a File to a String and store it in a shared map.
     *
     * @param fileName - Name of file to convert to a string
     */
    private void storeTextFileToString(String fileName) throws IOException {

        if (!textFilesAsStrings.containsKey(fileName)) {
            // Create a string from text file.
            List<String> lines = Files.readAllLines(Paths.get(fileDirectory + '/' + fileName), StandardCharsets.UTF_8);
            String fileString = String.join(" ", lines);
            textFilesAsStrings.put(fileName, fileString.toLowerCase());
        }
    }

    /**
     * Helper method to sort a LinkedHashMap by highest value (relevance).
     *
     * @param unsortedMap - map to be sorted by relevance.
     * @return sorted map by relevance.
     */
    private LinkedHashMap<String, Integer> sortMapByRelevance(Map<String, Integer> unsortedMap) {

        LinkedHashMap<String, Integer> relevanceSortedMap = new LinkedHashMap<>();
        unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> relevanceSortedMap.put(x.getKey(), x.getValue()));

        return relevanceSortedMap;
    }

    /**
     * Method to print search result details to the console.
     *
     * @param map - map to print.
     */
    public void printMap(Map<String, Integer> map) {

        System.out.println("Search Results:\n");
        for (String fileName : map.keySet()) {
            System.out.println(fileName + " " + map.get(fileName));
        }
    }

    /**
     * Method to get loaded and processed text file names.
     *
     * @return Set of text file names.
     */
    public Set<String> getTextFileNames() {

        return textFileNames;

    }
}
