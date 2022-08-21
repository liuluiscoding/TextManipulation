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

public class MyLibTest {

    /*
     * TEST FILE CONTENT
     */
    private final Charset charset = StandardCharsets.UTF_8;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;
    private EdTextInterface edtextImpl;


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
        edtextImpl = new EdText();
    }

    /*
     *  TEST UTILITIES
     */

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
        edtextImpl = null;
    }

    // Create File Utility
    private File createTmpFile() throws Exception {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    // Write File Utility
    private File createInputFile(String input) throws Exception {
        File file = createTmpFile();

        OutputStreamWriter fileWriter =
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

        fileWriter.write(input);

        fileWriter.close();
        return file;
    }

    //Read File Utility
    private String getFileContent(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /*
     *   TEST CASES
     */

    // Instructor Example 1 - test the -r flag
    @Test
    public void mainTest1() throws Exception {
        String input = "alphanumeric123foobar123" + System.lineSeparator();
        String expected = "alphanumeric456foobar123" + System.lineSeparator();

        File inputFile = createInputFile(input);
        edtextImpl.setFilepath(inputFile.getPath());
        edtextImpl.setReplaceString("123", "456");
        edtextImpl.edtext();
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }
}
