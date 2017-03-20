package com.jojos.path.input;

import com.jojos.path.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Iterates over a provided list of {@link InputLine}s.
 *
 * @author karanikasg@gmail.com
 */
public class InputLinesIterator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final List<InputLine> inputLines;

    public InputLinesIterator(List<InputLine> inputLines) {
        this.inputLines = inputLines;
    }

    public void forEach(InputLinesHandler handler) {
        int totalLines = inputLines.size();
        String startMessage = String.format("Processing %d lines", totalLines);
        log.info(startMessage);

        long start = System.currentTimeMillis();
        for (int row = 0; row < totalLines; row++) {
            InputLine inputLine = inputLines.get(row);
            try {
                handler.handle(inputLine);
            } catch (RuntimeException e) {
                log.error(String.format("Handling parameter '%s' on row %d failed. Skipping...", inputLine, row), e);
            }
        }

        String time = Util.longDuration(start);
        String processingCompleted = String.format("Processed all %d lines in %s", totalLines, time);
        log.info(processingCompleted);

    }
}
