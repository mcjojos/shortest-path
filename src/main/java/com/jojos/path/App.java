package com.jojos.path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Main entry point of the application
 *
 * @author gkaranikas
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    // a file can either be a single data file or a directory, in which case the program will
    // execute consecutive for all files contained under that directory
    private final File dataFile;

    public App(File dataFile) {
        this.dataFile = dataFile;
    }

    public void start() {
        log.info("Application starting");
    }

    public static void main(String[] args) {
        File file = loadInputOrThrow(args);
        log.info("Loading file {}", file);
        App main = new App(file);
        main.start();
    }

    /**
     * parse the command line usage of something like
     * -input example.csv
     */
    private static File loadInputOrThrow(String[] commandLineArguments) {
        String errorMsg = null;
        if (commandLineArguments != null) {
            for (int i = 0; i < commandLineArguments.length - 1; i++) {
                if (commandLineArguments[i] == null ||
                        commandLineArguments[i].length() == 0 ||
                        commandLineArguments[i].charAt(0) != '-' ||
                        commandLineArguments[i + 1] == null) {
                    continue;
                }
                String arg = commandLineArguments[i].substring(1);
                if (arg.equalsIgnoreCase("input")) {
                    String input = commandLineArguments[i + 1];
                    if (input != null && !input.equals("")) {
                        File file = new File(input);
                        if (file.exists()) {
                            return file;
                        }
                        errorMsg = "File " + file.getAbsolutePath() + " not found.";
                    }
                } else {
                    errorMsg = "Wrong usage. Please start the application with command line arguments -input example.csv";
                }
            }
        } else {
            errorMsg = "Wrong usage. Please start the application with command line arguments -input example.csv";
        }
        throw new ApplicationException(errorMsg);
    }
}
