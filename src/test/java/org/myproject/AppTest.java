package org.myproject;

import org.myproject.constant.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    // Save original streams
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    // Test streams
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Redirect output stream
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // // Redirect input stream
        // String simulatedInput = "";
        // ByteArrayInputStream inputStream =
        //         new ByteArrayInputStream(simulatedInput.getBytes());

        // System.setIn(inputStream);
    }

    @AfterEach
    void tearDown() {
        // Restore original streams
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    /**
     * Execute program with no CLI argument. <br>
     * Verify terminal prints exactly 'Please, input a valid option [1-2]' and exits without starting game loop.
     */
    @Test
    public void startupWithoutArgument() {
        App.main(new String[] {});
        String output = outputStream.toString();
        assertEquals(Message.INVALID_ARGS, output.trim());
    }

    /**
     * Execute with invalid argument values (for example: 0, 3, -1, abc). <br>
     * Verify exact message Please, input a valid option [1-2].
     */
    @Test
    public void startupWithInvalidArgument() {
        App.main(new String[] {"abc"});
        String output = outputStream.toString();
        assertEquals(Message.INVALID_ARGS, output.trim());
    }

    /**
     * Execute with more than one argument (for example: 1 extra). <br>
     * Verify expected handling (reject or ignore extras).
     */
    @Test
    public void startupWithExtraArguments() {
        App.main(new String[] {"1", "extra"});
        String output = outputStream.toString();
        assertEquals(Message.INVALID_ARGS, output.trim());
    }

    /**
     * Execute with valid start option (1 then 2). <br>
     * Verify output order is: Hello!, initial 3x3 board of zeros, then Player#<n>'s turn.
     */
    @Test
    public void startupMessageAndOrder() {
        String[][] test_Strings = {{"1"}, {"2"}};
        for (String[] args : test_Strings) {
            outputStream.reset(); // Clear output stream before each test case
            String simulatedInput = "q\n"; // Simulate user input to quit immediately
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            // Test
            App.main(args);
            String[] outputLines = outputStream.toString().trim().split("\\R");
            assertEquals(Message.WELCOME_MESSAGE, outputLines[0]);
            assertEquals("| 0 | 0 | 0 |\n" +
                        "| 0 | 0 | 0 |\n" +
                        "| 0 | 0 | 0 |", String.join("\n", outputLines[1], outputLines[2], outputLines[3]));
            assertEquals(Message.getPlayersTurnMessage(1), outputLines[4]);
        }
    }
}
