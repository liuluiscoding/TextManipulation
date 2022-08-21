package edu.gatech.seclass.edtext;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyMainTest {

    // Place all  of your tests in this class, optionally using MainTest.java as an example.


    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();
    private final Charset charset = StandardCharsets.UTF_8;
    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    /*
     *  TEST UTILITIES
     */

    // Create File Utility
    private File createTmpFile() throws Exception {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    // Write File Utility
    private File createInputFile(String input) throws Exception {
        File file = createTmpFile();
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        fileWriter.write(input);
        fileWriter.close();
        return file;
    }

    private String getFileContent(String filename) {
        String content = null;
        try {
            content = Files.readString(Paths.get(filename), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /*
     *   TEST CASES
     */
    
    // Test for non printable
    @Test
    public void Test71() throws Exception {
        char nprt = 0xD2;
        String input = nprt + System.lineSeparator();
        String expected = nprt + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }
  
    //test case for no flag
    @Test
    public void Test72() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"9", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());

    }
    //test case for empty r new string
    @Test
    public void Test73() throws Exception {

        String[] args = {"-r", "e"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }
    
    //test case for invalid input of -n
    @Test
    public void Test74() throws Exception {
       String input = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n","e", inputFile.getPath()};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }


//    //test case for new string "-g"
//    @Test
//    public void Test75() throws Exception {
//       String input = "Dobby0501Meow!" + System.lineSeparator();
//        String expected = "-gobby0501Meow!" + System.lineSeparator();
//
//        File inputFile = createInputFile(input);
//        String[] args = {"-r","D","-g", inputFile.getPath()};
//        Main.main(args);
//        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
//        assertEquals("Output differs from expected", expected, outStream.toString());
//        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
//    }
//
//    //test case for filename not as the last one
//    @Test
//    public void Test76() throws Exception {
//       String input = "Dobby0501Meow!" + System.lineSeparator();
//
//        File inputFile = createInputFile(input);
//        String[] args = {"-n","1", inputFile.getPath(),"-a"};
//        Main.main(args);
//        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
//        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
//    }
    
    
    // test for duplicate n flag
    @Test
    public void Test69() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator();

        String expected = "01 alphanumeric123foobar" + System.lineSeparator() +
                "02 alphanumeric123foobar" + System.lineSeparator() +
                "03 alphanumeric123foobar" + System.lineSeparator() +
                "04 alphanumeric123foobar" + System.lineSeparator() +
                "05 alphanumeric123foobar" + System.lineSeparator() +
                "06 alphanumeric123foobar" + System.lineSeparator() +
                "07 alphanumeric123foobar" + System.lineSeparator() +
                "08 alphanumeric123foobar" + System.lineSeparator() +
                "09 alphanumeric123foobar" + System.lineSeparator() +
                "10 alphanumeric123foobar" + System.lineSeparator() +
                "11 alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n", "4","-n", "2", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }
    
//    //test case for empty r old string
//    @Test
//    public void Test70() throws Exception {
//
//        String[] args = {"-r", "", "3"};
//        Main.main(args);
//        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
//        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
//    }

    // Frame 1: <test case 1 in file catpart.txt.tsl>
    @Test
    public void edtextTest1() throws Exception {
        String[] args = new String[0];
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame 2: <test case 2 in file catpart.txt.tsl>
    @Test
    public void edtextTest2() throws Exception {
        String[] args = {"-k"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame 3: <test case 3 in file catpart.txt.tsl>
    @Test
    public void edtextTest3() throws Exception {
        String[] args = {"-f"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame 4: <test case 4 in file catpart.txt.tsl>
    @Test
    public void edtextTest4() throws Exception {

        String[] args = {"-f", "random"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame 5: <test case 5 in file catpart.txt.tsl>
//    @Test
//    public void edtextTest5() throws Exception {
//        String input = "" + System.lineSeparator();
//        String expected = "" + System.lineSeparator();
//
//        File inputFile = createInputFile(input);
//        String[] args = {"-f", inputFile.getPath()};
//        Main.main(args);
//        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
//        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
//        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
//    }

    // Frame 6: <test case 6 in file catpart.txt.tsl>
    @Test
    public void edtextTest6() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();
        String expected = "Dobby0101Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "1", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame 7: <test case 7 in file catpart.txt.tsl>
    @Test
    public void edtextTest7() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();
        String expected = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame 8: <test case 8 in file catpart.txt.tsl>
    @Test
    public void edtextTest8() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();
        File inputFile = createInputFile(input);
        String[] args = {"-r", inputFile.getPath()};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame 9: <test case 9 in file catpart.txt.tsl>
    @Test
    public void edtextTest9() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();
        String expected = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "cute", "wow", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame 10: <test case 10 in file catpart.txt.tsl>
   @Test
   public void edtextTest10() throws Exception {
       String input = "Dobby0501Meow!" + System.lineSeparator();
       String expected = "Dobby0501!" + System.lineSeparator();

       File inputFile = createInputFile(input);
       String[] args = {"-r", "Meow","", inputFile.getPath()};
       Main.main(args);

       assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
       assertEquals("Output differs from expected", expected, outStream.toString());
       assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

   }

    // Frame 11: <test case 11 in file catpart.txt.tsl>
    @Test
    public void edtextTest11() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();
        String expected = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "Meow", "Meow", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 12: <test case 12 in file catpart.txt.tsl>
    @Test
    public void edtextTest12() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();
        File inputFile = createInputFile(input);
        String[] args = {"-g", inputFile.getPath()};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame 13: <test case 13 in file catpart.txt.tsl>
    @Test
    public void edtextTest13() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();
        String expected = "~~Dobby0501wow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "Meow", "wow","-p", "~~", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 14: <test case 14 in file catpart.txt.tsl>
    @Test
    public void edtextTest14() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-p","-r", "Meow", "wow", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());

    }

    // Frame 15: <test case 15 in file catpart.txt.tsl>
    @Test
    public void edtextTest15() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-d","-1", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());

    }

    // Frame 16: <test case 16 in file catpart.txt.tsl>
    @Test
    public void edtextTest16() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-d", "13", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());

    }

    // Frame 17: <test case 17 in file catpart.txt.tsl>
    @Test
    public void edtextTest17() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-d", "0.39", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());

    }

    // Frame 18: <test case 18 in file catpart.txt.tsl>
    @Test
    public void edtextTest18() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n", "-23", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());

    }

    // Frame 19: <test case 19 in file catpart.txt.tsl>
    @Test
    public void edtextTest19() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n", "7", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());

    }

    // Frame 20: <test case 20 in file catpart.txt.tsl>
    @Test
    public void edtextTest20() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n", "1.32", inputFile.getPath()};
        Main.main(args);

        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());

    }




    // Frame 21: <test case 21 in file catpart.txt.tsl>
    @Test
    public void edtextTest21() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                      +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"002 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"003 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"004 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"005 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"006 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"007 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"008 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-a", "-p","~", "-d", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }
    
    // Frame 21: <test case 21 in file catpart.txt.tsl>
    @Test
    public void edtextTest21_2() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                      +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"002 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"003 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"004 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"005 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"006 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"007 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"008 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-f", "-a", "-p","~", "-d", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));

    }

    // Frame 22: <test case 22 in file catpart.txt.tsl>
    @Test
    public void edtextTest22() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-a", "-p","~", "-d", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    
    // Frame 23: <test case 23 in file catpart.txt.tsl>
    @Test
    public void edtextTest23() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"002 ~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-a", "-p","~", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 24: <test case 24 in file catpart.txt.tsl>
    @Test
    public void edtextTest24() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-a", "-p","~", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 25: <test case 25 in file catpart.txt.tsl>
    @Test
    public void edtextTest25() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"002 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"003 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"004 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"005 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"006 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"007 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"008 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-a", "-d", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 26: <test case 22 in file catpart.txt.tsl>
    @Test
    public void edtextTest26() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-a", "-d", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 27: <test case 27 in file catpart.txt.tsl>
    @Test
    public void edtextTest27() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"002 68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-a", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 28: <test case 24 in file catpart.txt.tsl>
    @Test
    public void edtextTest28() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 33 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-a", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 29: <test case 29 in file catpart.txt.tsl>
    @Test
    public void edtextTest29() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 ~Dobby0!01Meow!" + System.lineSeparator()
                +"002 ~Dobby0!01Meow!" + System.lineSeparator()
                +"003 ~Dobby0!01Meow!" + System.lineSeparator()
                +"004 ~Dobby0!01Meow!" + System.lineSeparator()
                +"005 ~Dobby0!01Meow!" + System.lineSeparator()
                +"006 ~Dobby0!01Meow!" + System.lineSeparator()
                +"007 ~Dobby0!01Meow!" + System.lineSeparator()
                +"008 ~Dobby0!01Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-p","~", "-d", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 30: <test case 30 in file catpart.txt.tsl>
    @Test
    public void edtextTest30() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "~Dobby0!01Meow!" + System.lineSeparator()
                +"~Dobby0!01Meow!" + System.lineSeparator()
                +"~Dobby0!01Meow!" + System.lineSeparator()
                +"~Dobby0!01Meow!" + System.lineSeparator()
                +"~Dobby0!01Meow!" + System.lineSeparator()
                +"~Dobby0!01Meow!" + System.lineSeparator()
                +"~Dobby0!01Meow!" + System.lineSeparator()
                +"~Dobby0!01Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-p","~", "-d", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 31: <test case 31 in file catpart.txt.tsl>
    @Test
    public void edtextTest31() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 ~Dobby0!01Meow!" + System.lineSeparator()
                +"002 ~Dobby0!01Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-p","~", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 32: <test case 32 in file catpart.txt.tsl>
    @Test
    public void edtextTest32() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "~Dobby0!01Meow!" + System.lineSeparator()
                +"~Dobby0!01Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-p","~", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }



    // Frame 33: <test case 33 in file catpart.txt.tsl>
    @Test
    public void edtextTest33() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 Dobby0!01Meow!" + System.lineSeparator()
                +"002 Dobby0!01Meow!" + System.lineSeparator()
                +"003 Dobby0!01Meow!" + System.lineSeparator()
                +"004 Dobby0!01Meow!" + System.lineSeparator()
                +"005 Dobby0!01Meow!" + System.lineSeparator()
                +"006 Dobby0!01Meow!" + System.lineSeparator()
                +"007 Dobby0!01Meow!" + System.lineSeparator()
                +"008 Dobby0!01Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-d", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }


    // Frame 34: <test case 34 in file catpart.txt.tsl>
    @Test
    public void edtextTest34() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "Dobby0!01Meow!" + System.lineSeparator()
                +"Dobby0!01Meow!" + System.lineSeparator()
                +"Dobby0!01Meow!" + System.lineSeparator()
                +"Dobby0!01Meow!" + System.lineSeparator()
                +"Dobby0!01Meow!" + System.lineSeparator()
                +"Dobby0!01Meow!" + System.lineSeparator()
                +"Dobby0!01Meow!" + System.lineSeparator()
                +"Dobby0!01Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g","-d", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }


    // Frame 35: <test case 35 in file catpart.txt.tsl>
    @Test
    public void edtextTest35() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 Dobby0!01Meow!" + System.lineSeparator()
                +"002 Dobby0!01Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 36: <test case 36 in file catpart.txt.tsl>
    @Test
    public void edtextTest36() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "Dobby0!01Meow!" + System.lineSeparator()
                +"Dobby0!01Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "5", "!","-g", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }



    // Frame 37: <test case 37 in file catpart.txt.tsl>
    @Test
    public void edtextTest37() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"002 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"003 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"004 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"005 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"006 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"007 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"008 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", "-p","~", "-d", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 38: <test case 38 in file catpart.txt.tsl>
    @Test
    public void edtextTest38() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", "-p","~", "-d", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }



    // Frame 39: <test case 39 in file catpart.txt.tsl>
    @Test
    public void edtextTest39() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"002 ~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", "-p","~", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 40: <test case 40 in file catpart.txt.tsl>
    @Test
    public void edtextTest40() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"~68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", "-p","~", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 41: <test case 41 in file catpart.txt.tsl>
    @Test
    public void edtextTest41() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"002 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"003 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"004 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"005 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"006 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"007 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"008 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", "-d", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 42: <test case 42 in file catpart.txt.tsl>
    @Test
    public void edtextTest42() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", "-d", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 43: <test case 43 in file catpart.txt.tsl>
    @Test
    public void edtextTest43() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"002 68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = { "-a", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 44: <test case 44 in file catpart.txt.tsl>
    @Test
    public void edtextTest44() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator()
                +"68 111 98 98 121 48 53 48 49 77 101 111 119 33 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 45: <test case 45 in file catpart.txt.tsl>
    @Test
    public void edtextTest45() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 ~Dobby0501Meow!" + System.lineSeparator()
                +"002 ~Dobby0501Meow!" + System.lineSeparator()
                +"003 ~Dobby0501Meow!" + System.lineSeparator()
                +"004 ~Dobby0501Meow!" + System.lineSeparator()
                +"005 ~Dobby0501Meow!" + System.lineSeparator()
                +"006 ~Dobby0501Meow!" + System.lineSeparator()
                +"007 ~Dobby0501Meow!" + System.lineSeparator()
                +"008 ~Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-p","~", "-d", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 46: <test case 46 in file catpart.txt.tsl>
    @Test
    public void edtextTest46() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "~Dobby0501Meow!" + System.lineSeparator()
                +"~Dobby0501Meow!" + System.lineSeparator()
                +"~Dobby0501Meow!" + System.lineSeparator()
                +"~Dobby0501Meow!" + System.lineSeparator()
                +"~Dobby0501Meow!" + System.lineSeparator()
                +"~Dobby0501Meow!" + System.lineSeparator()
                +"~Dobby0501Meow!" + System.lineSeparator()
                +"~Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-p","~", "-d", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 47: <test case 47 in file catpart.txt.tsl>
    @Test
    public void edtextTest47() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 ~Dobby0501Meow!" + System.lineSeparator()
                +"002 ~Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-p","~", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 48: <test case 48 in file catpart.txt.tsl>
    @Test
    public void edtextTest48() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "~Dobby0501Meow!" + System.lineSeparator()
                +"~Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = { "-p","~", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }



    // Frame 49: <test case 49 in file catpart.txt.tsl>
    @Test
    public void edtextTest49() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 Dobby0501Meow!" + System.lineSeparator()
                +"002 Dobby0501Meow!" + System.lineSeparator()
                +"003 Dobby0501Meow!" + System.lineSeparator()
                +"004 Dobby0501Meow!" + System.lineSeparator()
                +"005 Dobby0501Meow!" + System.lineSeparator()
                +"006 Dobby0501Meow!" + System.lineSeparator()
                +"007 Dobby0501Meow!" + System.lineSeparator()
                +"008 Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-d", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }


    // Frame 50: <test case 50 in file catpart.txt.tsl>
    @Test
    public void edtextTest50() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-d", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }


    // Frame 51: <test case 51 in file catpart.txt.tsl>
    @Test
    public void edtextTest51() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator()
                +"Dobby0501Meow!" + System.lineSeparator();
        String expected = "001 Dobby0501Meow!" + System.lineSeparator()
                +"002 Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n", "3", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    // Frame 52: <test case 52 in file catpart.txt.tsl>
//    @Test
//    public void edtextTest52() throws Exception {
//        String input = "Dobby0501Meow!" + System.lineSeparator()
//                +"Dobby0501Meow!" + System.lineSeparator();
//        String expected = "Dobby0501Meow!" + System.lineSeparator()
//                +"Dobby0501Meow!" + System.lineSeparator();
//
//        File inputFile = createInputFile(input);
//        String[] args = {inputFile.getPath()};
//        Main.main(args);
//
//        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
//        assertEquals("Output differs from expected", expected, outStream.toString());
//        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
//
//    }


    //start of the new tests

    // Instructor Example 1 - test the -f flag
    @Test
    public void mainTest1() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 2 - test the -r flag
    @Test
    public void mainTest2() throws Exception {
        String input = "alphanumeric123foobar123" + System.lineSeparator();
        String expected = "alphanumeric456foobar123" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "123", "456", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 3 - test the -g flag with -r
    @Test
    public void mainTest3() throws Exception {
        String input = "alphanumeric123foobar123" + System.lineSeparator();
        String expected = "alphanumeric456foobar456" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "123", "456", "-g", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 4 - test the -a flag
    @Test
    public void mainTest4() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "97 108 112 104 97 110 117 109 101 114 105 99 49 50 51 102 111 111 98 97 114 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 5 - test the -p flag
    @Test
    public void mainTest5() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "##alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-p", "##", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 6 - test the -d flag
    @Test
    public void mainTest6() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-d", "3", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 7 - test the -n flag
    @Test
    public void mainTest7() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator();

        String expected = "01 alphanumeric123foobar" + System.lineSeparator() +
                "02 alphanumeric123foobar" + System.lineSeparator() +
                "03 alphanumeric123foobar" + System.lineSeparator() +
                "04 alphanumeric123foobar" + System.lineSeparator() +
                "05 alphanumeric123foobar" + System.lineSeparator() +
                "06 alphanumeric123foobar" + System.lineSeparator() +
                "07 alphanumeric123foobar" + System.lineSeparator() +
                "08 alphanumeric123foobar" + System.lineSeparator() +
                "09 alphanumeric123foobar" + System.lineSeparator() +
                "10 alphanumeric123foobar" + System.lineSeparator() +
                "11 alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n", "2", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }
// Instructor Example 8 - test a valid combination of all flags
    @Test
    public void mainTest8() throws Exception {
        String input = "foobar0Foobar1" + System.lineSeparator() +
                "foobar2foobar3" + System.lineSeparator() +
                "foobar4Foobar5" + System.lineSeparator() +
                "foobar6foobar7" + System.lineSeparator() +
                "foobar8Foobar9" + System.lineSeparator();

        String expected = "001 !!!70 79 79 98 97 114 48 70 111 111 98 97 114 49 " + System.lineSeparator() +
                "002 !!!70 79 79 98 97 114 48 70 111 111 98 97 114 49 " + System.lineSeparator() +
                "003 !!!70 79 79 98 97 114 50 70 79 79 98 97 114 51 " + System.lineSeparator() +
                "004 !!!70 79 79 98 97 114 50 70 79 79 98 97 114 51 " + System.lineSeparator() +
                "005 !!!70 79 79 98 97 114 52 70 111 111 98 97 114 53 " + System.lineSeparator() +
                "006 !!!70 79 79 98 97 114 52 70 111 111 98 97 114 53 " + System.lineSeparator() +
                "007 !!!70 79 79 98 97 114 54 70 79 79 98 97 114 55 " + System.lineSeparator() +
                "008 !!!70 79 79 98 97 114 54 70 79 79 98 97 114 55 " + System.lineSeparator() +
                "009 !!!70 79 79 98 97 114 56 70 111 111 98 97 114 57 " + System.lineSeparator() +
                "010 !!!70 79 79 98 97 114 56 70 111 111 98 97 114 57 " + System.lineSeparator();


        File inputFile = createInputFile(input);
        String[] args = {"-n", "3", "-r", "foo", "FOO", "-f", "-g", "-p", "!!!", "-d", "1", "-a", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 9 - test a valid combination of duplicate flags
    @Test
    public void mainTest9() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "01 ##alphanumeric123foobar" + System.lineSeparator() +
                "02 ##alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n", "3", "-d", "5", "-d", "1", "-n", "2", "-p", "!!!", "-p", "##", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 10 - test an invalid flag scenario
    @Test
    public void mainTest10() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).
        String[] args = new String[0];
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Test for invalid -f input
    @Test
    public void Test61() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).

        String[] args = {"-f", "1", "-f"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Test for invalid -g input
    @Test
    public void Test62() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).

        String[] args = {"-r", "1", "3", "-g", "12"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Test for invalid -a input
    @Test
    public void Test63() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).

        String[] args = {"-a", "1"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    //test case for more than 2 args after flags
    @Test
    public void Test65() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).

        String[] args = {"-d", "1", "3"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    //test case for padding overflow
    @Test
    public void Test66() throws Exception {
        String input = "Dobby0501Meow!" + System.lineSeparator();
        String expected = "1 Dobby0501Meow!" + System.lineSeparator()
                +"2 Dobby0501Meow!" + System.lineSeparator()
                +"3 Dobby0501Meow!" + System.lineSeparator()
                +"4 Dobby0501Meow!" + System.lineSeparator()
                +"5 Dobby0501Meow!" + System.lineSeparator()
                +"6 Dobby0501Meow!" + System.lineSeparator()
                +"7 Dobby0501Meow!" + System.lineSeparator()
                +"8 Dobby0501Meow!" + System.lineSeparator()
                +"9 Dobby0501Meow!" + System.lineSeparator()
                +"10 Dobby0501Meow!" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-d", "9" ,"-n", "1", inputFile.getPath()};
        Main.main(args);

        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));

    }

    //test case for more than 2 args after flags
    @Test
    public void Test67() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).

        String[] args = {"-n", "1", "3"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    //test case for non printable
    public void Test68() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).

        String[] args = {"-r", "o", "酷"};
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }
}
