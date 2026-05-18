package org.myproject.io;

/**
 * This class represents an input/output service for a game.
 */
public interface IOService {
    public String nextLine();
    public void print(String message);
    public void println(String message);
}
