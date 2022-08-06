package org.casestudy;

import java.io.*;

public class SearchEngine {

    public enum SearchMethod{
        STRING_SEARCH,
        REGEX_SEARCH,
        PREPROCESS_SEARCH
    }

    public int Search(String stringToMatch, File f, SearchMethod s) throws IOException {
        switch(s){
            case STRING_SEARCH:
                return StringSearch(stringToMatch, f);
            case REGEX_SEARCH:
            case PREPROCESS_SEARCH:
            default:
                break;
        }
        return 0;
    }
/*
Method to parse through file and search for a string
@param: stringToMatch - target string to find in file
@param: fileToSearch - file to search for matching string
@return: int indicating occurrences of exact string match
 */
    public int StringSearch(String stringToMatch, File fileToSearch) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileToSearch));
        String currentLine;

        int matching_string_count = 0;
        while ((currentLine = br.readLine()) != null)
        {
            if (currentLine.contains(stringToMatch)){
                matching_string_count++;
            }
        }

        return matching_string_count;
    }


}
