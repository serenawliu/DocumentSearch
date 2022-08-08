package org.casestudy;
import java.io.IOException;

public class SearchMain {
    public static void main(String[] args) throws IOException {
        SearchEngine engine = new SearchEngine();

        engine.addTextFile("french_armed_forces.txt");
        engine.addTextFile("hitchhikers.txt");
        engine.addTextFile("warp_drive.txt");

        engine.printMap(engine.search("ranco-Prussian", SearchEngine.searchMethod.STRING_SEARCH));
        engine.printMap(engine.search("Resurgent", SearchEngine.searchMethod.REGEX_SEARCH));
        engine.printMap(engine.search("Resurgent", SearchEngine.searchMethod.PREPROCESS_SEARCH));
    }
}