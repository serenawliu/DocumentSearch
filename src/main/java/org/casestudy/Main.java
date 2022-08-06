package org.casestudy;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SearchEngine engine = new SearchEngine();
        File f = new File("sample_text/french_armed_forces.txt");
        System.out.println("Matching: " + engine.Search("defeat", f, SearchEngine.SearchMethod.STRING_SEARCH));
    }
}