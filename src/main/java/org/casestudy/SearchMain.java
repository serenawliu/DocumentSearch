package org.casestudy;
import java.io.IOException;
import java.util.Scanner;

public class SearchMain {
    public static void main(String[] args) throws IOException {
        SearchEngine engine = new SearchEngine();

        engine.addTextFile("french_armed_forces.txt");
        engine.addTextFile("hitchhikers.txt");
        engine.addTextFile("warp_drive.txt");

        //TODO Error checking for invalid input.
        Scanner scan = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Enter a search term. No spaces, single term: ");
        String searchTerm = scan.next();
        System.out.println("\nEnter an integer for a searchMethod 0: String Search, 1: RegEx Search, 2: Indexed Search:");
        int searchSelection = scan.nextInt();  // Read user input
        scan.close();
        engine.printMap(engine.search(searchTerm, SearchEngine.searchMethod.values()[searchSelection]));
    }
}