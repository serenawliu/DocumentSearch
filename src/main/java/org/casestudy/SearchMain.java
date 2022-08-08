package org.casestudy;

import java.io.IOException;
import java.util.Scanner;

public class SearchMain {
    public static void main(String[] args) throws IOException {
        SearchEngine engine = new SearchEngine();

        engine.addTextFile("french_armed_forces.txt");
        engine.addTextFile("hitchhikers.txt");
        engine.addTextFile("warp_drive.txt");

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a search term: ");
        String searchTerm = scan.next();
        System.out.println("\nEnter an integer for a searchMethod 0: String Search, 1: RegEx Search, 2: Indexed Search:");
        while (true) {
            if (!scan.hasNextInt()) {
                System.out.println("Please enter a valid value.");
            } else {
                int searchSelection = scan.nextInt();
                if (searchSelection > 2 || searchSelection < 0) {
                    System.out.println("Please enter a valid value.");
                } else {
                    scan.close();
                    engine.printMap(engine.search(searchTerm, SearchEngine.searchMethod.values()[searchSelection]));
                    break;
                }
            }
        }
    }
}