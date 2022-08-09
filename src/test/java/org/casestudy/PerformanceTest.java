package org.casestudy;

import java.io.IOException;
import java.util.Random;

public class PerformanceTest {
    private static final SearchEngine engine = new SearchEngine();
    public static void main(String[] args) throws IOException {
        int NUM_ITERATIONS = 2000000;

        engine.addTextFile("hitchhikers.txt");
        engine.addTextFile("warp_drive.txt");
        engine.addTextFile("french_armed_forces.txt");

        String[] randomWords = generateWords(NUM_ITERATIONS);

        timeSearch(SearchEngine.searchMethod.STRING_SEARCH, randomWords);
        timeSearch(SearchEngine.searchMethod.REGEX_SEARCH, randomWords);
        timeSearch(SearchEngine.searchMethod.PREPROCESS_SEARCH, randomWords);
    }

    private static void timeSearch(SearchEngine.searchMethod searchType, String[] randomWords) {

        long startTime = System.currentTimeMillis();
        for (String word: randomWords) {
            engine.search(word, searchType);
        }
        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - startTime;

        System.out.println("Run time for " + searchType.toString().replace("_", " ").toLowerCase() + " " +
                "in milliseconds: " +  " " + runTime + " ms");
    }

    public static String[] generateWords(int numberOfWords)
    {
        String[] randomWords = new String[numberOfWords];
        Random random = new Random();
        for(int i = 0; i < numberOfWords; i++)
        {
            char[] word = new char[random.nextInt(8)+3];
            for(int j = 0; j < word.length; j++)
            {
                word[j] = (char)('a' + random.nextInt(26));
            }
            randomWords[i] = new String(word);
        }
        return randomWords;
    }
}
