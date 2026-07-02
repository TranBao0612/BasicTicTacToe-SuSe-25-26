package org.myproject.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A predictable, memory-only test double for IOService.
 * Perfectly substitutes ConsoleIO, TcpIO or other IO implementations during testing.
 */
public class TestIO implements IOService {
    private final Queue<String> simulatedInputs = new LinkedList<>();
    private final List<String> capturedOutputs = new ArrayList<>();

    /**
     * Pre-load inputs that the game will consume when calling nextLine()
     */
    public void addSimulatedInput(String input) {
        simulatedInputs.add(input);
    }

    public List<String> getCapturedOutputs() {
        return capturedOutputs;
    }

    @Override
    public String nextLine() {
        if (simulatedInputs.isEmpty()) {
            throw new IllegalStateException("Game requested input, but no simulated inputs are left!");
        }
        return simulatedInputs.poll();
    }

    @Override
    public void print(String message) {
        capturedOutputs.add(message);
    }

    @Override
    public void println(String message) {
        capturedOutputs.add(message + "\n");
    }
}