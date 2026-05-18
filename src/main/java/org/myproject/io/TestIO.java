package org.myproject.io;

import java.util.Queue;
import java.util.LinkedList;

/**
 * This class is a concrete implementation of the IOService interface 
 *      that can be used for testing purpose that require fake input and checking output functionality 
 *      without relying on the console.
 */
public class TestIO implements IOService {
    protected Queue<String> inputs;
    protected Queue<String> printed_messages;

    public TestIO() {
        this.inputs = new LinkedList<>();
        this.printed_messages = new LinkedList<>();
    }

    /**
     * Get the next line of input. Simulates readLine() method by removing any trailing newline characters from the input string.
     * @return the next line of input, or empty string if there are no more inputs.
     */
    @Override
    public String nextLine() {
        String input = inputs.poll().replaceAll("\\R$", "");
        return input != null ? input : "";
    }
    /**
     * Simulates the print() method by adding the message to the printed_messages queue.
     * @param message the message to print
     */
    @Override
    public void print(String message) {
        printed_messages.offer(message);
    }
    /**
     * Simulates the println() method by adding the message with a newline character to the printed_messages queue.
     * @param message the message to print
     */
    @Override
    public void println(String message) {
        printed_messages.offer(message + System.lineSeparator());
    }


    /**
     * Add an input string to the inputs queue. This method can be used to simulate user input for testing purposes.
     * @param input the input string to add
     */
    public void addInput(String input) {
        this.inputs.offer(input);
    }
    /**
     * Get the next printed message. This method can be used to check the output of the print and println methods for testing purposes.
     * @return the next printed message, or an empty string if none has been printed.
     */
    public String getPrintedMessage() {
        String message = printed_messages.poll();
        return message != null ? message : "";
    }

    /**
     * Clear all inputs. This method can be used to reset the state of the TestIO instance between tests.
     */
    public void clearInputs() {
        inputs.clear();
    }
    /**
     * Clear all printed messages. This method can be used to reset the state of the TestIO instance between tests.
     */
    public void clearPrintedMessages() {
        printed_messages.clear();
    }
}
