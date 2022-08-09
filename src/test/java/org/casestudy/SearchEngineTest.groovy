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
    }

    void testSearchOneWord() {
        engine.addTextFile(textFiles[0])
        engine.addTextFile(textFiles[1])
        engine.addTextFile(textFiles[2])
        engine.printMap(engine.search("warp", SearchEngine.searchMethod.STRING_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nwarp_drive.txt 6\nhitchhikers.txt 0\n" +
                "french_armed_forces.txt 0\n")
        outContent.reset()
        engine.printMap(engine.search("warp", SearchEngine.searchMethod.REGEX_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nwarp_drive.txt 6\nhitchhikers.txt 0\n" +
                "french_armed_forces.txt 0\n")
        outContent.reset()
        engine.printMap(engine.search("warp", SearchEngine.searchMethod.PREPROCESS_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nwarp_drive.txt 6\nhitchhikers.txt 0\n" +
                "french_armed_forces.txt 0\n")

    }
    void testSearchMultipleWords() {
        engine.addTextFile(textFiles[0])
        engine.addTextFile(textFiles[1])
        engine.addTextFile(textFiles[2])
        engine.printMap(engine.search("hello hi", SearchEngine.searchMethod.STRING_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nhitchhikers.txt 4\nfrench_armed_forces.txt 0\n" +
                "warp_drive.txt 0\n")
        outContent.reset()
        engine.printMap(engine.search("hello hi", SearchEngine.searchMethod.REGEX_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nhitchhikers.txt 4\nfrench_armed_forces.txt 0\n" +
                "warp_drive.txt 0\n")
        outContent.reset()
        engine.printMap(engine.search("hello hi", SearchEngine.searchMethod.PREPROCESS_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nhitchhikers.txt 4\nfrench_armed_forces.txt 0\n" +
                "warp_drive.txt 0\n")
    }

    void testSearchSpecialCharacters() {
        engine.addTextFile(textFiles[0])
        engine.addTextFile(textFiles[1])
        engine.addTextFile(textFiles[2])
        engine.printMap(engine.search("!!!!!", SearchEngine.searchMethod.STRING_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nhitchhikers.txt 1\nfrench_armed_forces.txt 0\n" +
                "warp_drive.txt 0\n")
        outContent.reset()
        engine.printMap(engine.search("!!!!!", SearchEngine.searchMethod.REGEX_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nhitchhikers.txt 1\nfrench_armed_forces.txt 0\n" +
                "warp_drive.txt 0\n")
        outContent.reset()
        engine.printMap(engine.search("!!!!!", SearchEngine.searchMethod.PREPROCESS_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nhitchhikers.txt 1\nfrench_armed_forces.txt 0\n" +
                "warp_drive.txt 0\n")
    }

    void testSearchWordOnNewLine() {
        engine.addTextFile(textFiles[0])
        engine.addTextFile(textFiles[1])
        engine.addTextFile(textFiles[2])
        engine.printMap(engine.search("Resurgent French armies", SearchEngine.searchMethod.STRING_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nfrench_armed_forces.txt 1\nhitchhikers.txt 0\n" +
                "warp_drive.txt 0\n")
        outContent.reset()
        engine.printMap(engine.search("Resurgent French armies", SearchEngine.searchMethod.REGEX_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nfrench_armed_forces.txt 1\nhitchhikers.txt 0\n" +
                "warp_drive.txt 0\n")
        outContent.reset()
        engine.printMap(engine.search("Resurgent French armies", SearchEngine.searchMethod.PREPROCESS_SEARCH))
        assertEquals(outContent.toString(), "Search Results:\n\nfrench_armed_forces.txt 1\nhitchhikers.txt 0\n" +
                "warp_drive.txt 0\n")
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
        assertEquals(outContent.toString(), "Search Results:\n\nhitchhikers.txt 8\nfrench_armed_forces.txt 0\n" +
                "warp_drive.txt 0\n")
    }

}
