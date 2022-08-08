package org.casestudy

import junit.framework.TestCase


class SearchEngineTest extends TestCase {

    SearchEngine engine = new SearchEngine()
    String[] textFiles = ["hitchhikers.txt", "french_armed_forces.txt", "warp_drive.txt"]

    private ByteArrayOutputStream outContent = new ByteArrayOutputStream()
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream()
    private final PrintStream originalOut = System.out

    void setUp() {
        super.setUp()
        System.setOut(new PrintStream(outContent))
    }

    void tearDown() {
        engine = null
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    void testSearch() {
        engine.addTextFile("warp_drive.txt")
        engine.printMap(engine.search("warp", SearchEngine.searchMethod.STRING_SEARCH))
        assertEquals(outContent.toString(), "warp_drive.txt 6\n")
        outContent.reset()
        engine.printMap(engine.search("warp", SearchEngine.searchMethod.REGEX_SEARCH))
        assertEquals(outContent.toString(), "warp_drive.txt 6\n")
        outContent.reset()
        engine.printMap(engine.search("warp", SearchEngine.searchMethod.PREPROCESS_SEARCH))
        assertEquals(outContent.toString(), "warp_drive.txt 6\n")

        /* Test Cases:
        Happy Path: text is in file
        Alpha characters in file
        numbers in file
        string encompassed in other word
        space delimited?
        Negative: no files
        none present words
         */

    }

    void testAddTextFile() {
        engine.addTextFile("hitchhikers.txt")
        engine.addTextFile("invalid.txt")

        HashSet<String> fileNames = engine.getTextFileNames()
        assert(fileNames.contains("hitchhikers.txt"))
        assertFalse(fileNames.contains("invalid.txt"))
    }

    void testPrintMap() {
        engine.addTextFile("hitchhikers.txt")
        engine.printMap(engine.search("guIDE", SearchEngine.searchMethod.STRING_SEARCH))
        assertEquals(outContent.toString(), "hitchhikers.txt 8\n")
    }

}
