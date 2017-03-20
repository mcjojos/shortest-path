package com.jojos.path.input;

/**
 * Handles an instance of {@link InputLine}. Used with {@link InputLinesIterator}.
 *
 * @author karanikasg@gmail.com
 *
 * @see InputLine
 * @see InputLinesIterator
 */
public interface InputLinesHandler {
    void handle(InputLine inputLine);
}
