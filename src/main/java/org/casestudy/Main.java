package org.casestudy;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SearchEngine engine = new SearchEngine();

        engine.addTextFile("french_armed_forces.txt");
        engine.addTextFile("hitchhikers.txt");
        engine.addTextFile("warp_drive.txt");

        engine.textFileToString("french_armed_forces.txt");

        engine.printMap(engine.search("and", SearchEngine.searchMethod.STRING_SEARCH));
        engine.printMap(engine.search("AND", SearchEngine.searchMethod.REGEX_SEARCH));
    }
}