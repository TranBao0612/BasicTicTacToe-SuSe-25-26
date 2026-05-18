package org.myproject.io;

import java.util.Scanner;

/**
 * ConsoleIO is a concrete implementation of the IOService interface that uses the console for input and output.
 */
public class ConsoleIO implements IOService {
    private Scanner scanner;

    /**
     * Constructs a new ConsoleIO instance and initializes the scanner for reading input from the console.
     */
    public ConsoleIO() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next line of input from the console.
     * @return the next line of input from the console.
     */
    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    /**
     * Prints the specified format string to the console without a newline at the end.
     * @param message the string to be printed to the console.
     */
    @Override
    public void print(String message) {
        System.out.print(message);
    }

    /**
     * Prints the specified message to the console with a newline at the end.
     * @param message the string to be printed to the console.
     */
    @Override
    public void println(String message) {
        System.out.println(message);
    }
}
