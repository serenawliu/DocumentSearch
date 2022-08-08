package org.casestudy;

import java.io.IOException;

public class PerformanceTest {
    private static final SearchEngine engine = new SearchEngine();

    public static void main(String[] args) throws IOException {
        engine.addTextFile("hitchhikers.txt");
        engine.addTextFile("warp_drive.txt");
        engine.addTextFile("french_armed_forces.txt");

        timeSearch(SearchEngine.searchMethod.STRING_SEARCH, 2000000);
        timeSearch(SearchEngine.searchMethod.REGEX_SEARCH, 2000000);
        timeSearch(SearchEngine.searchMethod.PREPROCESS_SEARCH, 2000000);
    }

    private static void timeSearch(SearchEngine.searchMethod searchType, int numIterations) {
        String word = "new";
        long startTime = System.currentTimeMillis();
        for (int i = 0; i<numIterations; i++) {
            engine.search(word, searchType);
        }
        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - startTime;
        System.out.println("Run time for " + searchType.toString().replace("_", " ").toLowerCase() + " " +
                "in milliseconds: " +  " " + runTime + " ms");
    }

}
