package org.myproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private final PrintStream originalOut = System.out;
    private PipedOutputStream outputStream;

    

   @BeforeEach
   void setUp() { 
       
    outputStream = new PipedOutputStream();
    try {
        PipedInputStream inputStream = new PipedInputStream(outputStream);
    } catch (IOException ex) {
        throw new RuntimeException("Failed to set up piped streams", ex);
    }

    System.setOut(new PrintStream(outputStream)); }

    @AfterEach
    void tearDown() { System.setOut(originalOut); }

    /**
     * Execute program with no CLI argument. <br>
     * Verify terminal prints exactly 'Please, input a valid option [1-2]' and exits without starting game loop.
     */
    @Test
    public void startupWithoutArgument() {
        App.main(new String[] {});
        String output = outputStream.toString();
        assertEquals("Please, input a valid option [1-2]", output.trim());
    }

    /**
     * Execute with invalid argument values (for example: 0, 3, -1, abc). <br>
     * Verify exact message Please, input a valid option [1-2].
     */
    @Test
    public void startupWithInvalidArgument() {
        App.main(new String[] {"abc"});
        String output = outputStream.toString();
        assertEquals("Please, input a valid option [1-2]", output.trim());
    }

    /**
     * Execute with more than one argument (for example: 1 extra). <br>
     * Verify expected handling (reject or ignore extras).
     */
    @Test
    public void startupWithExtraArguments() {
        App.main(new String[] {"1", "extra"});
        String output = outputStream.toString();
        assertEquals("Please, input a valid option [1-2]", output.trim());
    }

    /**
     * Execute with valid start option (1 then 2). <br>
     * Verify output order is: Hello!, initial 3x3 board of zeros, then Player#<n>'s turn.
     */
    @Test
    public void startupMessageAndOrder() throws IOException {
        String[][] test_Strings = {{"1"}, {"2"}};
        for (String[] args : test_Strings) {
            // Test with user goes first
            App.main(args);
            assertEquals("Hello!", outputStream.toString().trim());
            assertEquals("| 0 | 0 | 0 |\n" +
                        "| 0 | 0 | 0 |\n" +
                        "| 0 | 0 | 0 |", outputStream.toString().trim());
            assertEquals("Player#" + args[0] + "'s turn", outputStream.toString().trim());
        }
    }
}
