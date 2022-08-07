package org.casestudy;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SearchEngine engine = new SearchEngine();

        engine.addTextFilePath("data/french_armed_forces.txt");
        engine.addTextFilePath("data/hitchhikers.txt");
        engine.addTextFilePath("data/warp_drive.txt");

        engine.printMap(engine.search("and", SearchEngine.searchMethod.STRING_SEARCH));
        engine.printMap(engine.search("AND", SearchEngine.searchMethod.REGEX_SEARCH));
    }
}