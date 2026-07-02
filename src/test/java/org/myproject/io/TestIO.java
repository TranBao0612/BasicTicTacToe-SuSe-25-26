package org.myproject.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TestIO implements IOService {
    private final Queue<String> simulatedInputs = new LinkedList<>();
    private final List<String> capturedOutputs = new ArrayList<>();

    public void addSimulatedInputs(String... inputs) {
        for (String input : inputs) {
            simulatedInputs.add(input);
        }
    }

    public List<String> getCapturedOutputs() {
        return capturedOutputs;
    }

    public List<String> getRemainingSimulatedInputs() {
        return new ArrayList<>(simulatedInputs);
    }

    public boolean hasOutputsContaining(String keyword) {
        return capturedOutputs.stream().anyMatch(line -> line.contains(keyword));
    }

    public int hasOutputsContainingWithCount(String keyword) {
        return (int) capturedOutputs.stream().filter(line -> line.contains(keyword)).count();
    }

    @Override
    public String nextLine() {
        if (simulatedInputs.isEmpty()) {
            // Safe fallback to prevent game loop locks during test exceptions
            return "q"; 
        }
        return simulatedInputs.poll();
    }

    @Override
    public void print(String message) {
        capturedOutputs.add(message);
    }

    @Override
    public void println(String message) {
        capturedOutputs.add(message);
    }
}