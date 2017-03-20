package com.jojos.path;

import com.jojos.path.calculation.Computations;
import com.jojos.path.calculation.DimensionsWeight;
import com.jojos.path.calculation.ParseJob;
import com.jojos.path.calculation.ShortestPathJob;
import com.jojos.path.input.ParsedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Main entry point of the application.
 * the program is run with the following parameter
 *
 * -input file_name
 *
 * where file_name can either be a single file with definition of a your friend network
 * or a directory in which case the program will execute consecutive for all files contained under that directory
 *
 * @author karanikasg@gmail.com
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    // a file can either be a single data file or a directory
    private final String inputDataFilePath;
    private final String outputFile;
    private final String source;
    private final String target;
    private final DimensionsWeight dimensionsWeight;
    private final ParseJob.RunMode runMode;

    private App(String[] args) {
        this.inputDataFilePath = loadInputOrThrow(args);
        this.outputFile = getOutputFile(args);
        this.source = getSource(args);
        this.target = getTarget(args);
        this.dimensionsWeight = getDimensionsAndWeight(args);
        this.runMode = getRunMode(args);

        logParameters();

    }

    private void logParameters() {
        log.info("Application started with ");
        log.info("input file: \"{}\"", inputDataFilePath);
        log.info("output file: \"{}\"", outputFile);
        if (source != null) {
            log.info("source: \"{}\"", source);
        }
        if (target != null) {
            log.info("target: \"{}\"", target);
        }
        if (dimensionsWeight != null) {
            log.info("dimensions: \"{}\"", dimensionsWeight);
        }
        log.info("running {} mode", runMode);

    }

    private void start() {
        log.info("Application starting");
        long start = System.currentTimeMillis();
        ParsedObject parsedObject = startParseJob();
        startShortestPathJob(parsedObject);

        String time = Util.longDuration(start);
        String processingCompleted = String.format("Application completed in %s", time);
        log.info(processingCompleted);
    }

    private ParsedObject startParseJob() {
        ParseJob parseJob = new ParseJob(inputDataFilePath);
        return parseJob.runAndCreateGraph(runMode);
    }

    private void startShortestPathJob(ParsedObject parsedObject) {
        switch (runMode) {
            case WITHOUT_ASSERTIONS:
                ShortestPathJob
                        .withoutAssertions(parsedObject.getGraph(), outputFile, source, target, dimensionsWeight)
                        .run();
                break;
            case WITH_ASSERTIONS:
                ShortestPathJob
                        .withAssertions(parsedObject)
                        .run();
                break;
        }
    }

    public static void main(String[] args) {
        App main = new App(args);
        main.start();
    }

    private static ParseJob.RunMode getRunMode(String[] commandLineArguments) {
        return isPrimaryArgumentPresent(commandLineArguments, "assert")
                ? ParseJob.RunMode.WITH_ASSERTIONS : ParseJob.RunMode.WITHOUT_ASSERTIONS;
    }

    /**
     * parse the command line usage of something like
     * -source name
     */
    private static String getSource(String[] commandLineArguments) {
        return getArgument(commandLineArguments, "source");
    }

    /**
     * parse the command line usage of something like
     * -target name
     */
    private static String getTarget(String[] commandLineArguments) {
        return getArgument(commandLineArguments, "target");
    }

    /**
     * parse the command line usage of something like
     * -dimension WxLxHxWeight (in grams)
     */
    private static DimensionsWeight getDimensionsAndWeight(String[] commandLineArguments) {
        String dimensionWeight = getArgument(commandLineArguments, "dimensions");

        return dimensionWeight == null ? null : Computations.parseDimensions(dimensionWeight);
    }

    /**
     * parse the command line usage of something like
     * -output output.csv
     * If no input is found the default output file is named output.csv
     */
    private static String getOutputFile(String[] commandLineArguments) {
        String output = getArgument(commandLineArguments, "output");
        if (Objects.isNull(output)) {
            output = "output.csv";
        }
        return output;
    }


    /**
     * parse the command line usage of something like
     * -input example.csv
     * @throws ApplicationException if no input is found
     */
    private static String loadInputOrThrow(String[] commandLineArguments) {
        String input = getArgument(commandLineArguments, "input");
        if (Objects.isNull(input)) {
            String errorMsg = "Wrong usage. Please start the application with command line arguments -input example.csv";
            throw new ApplicationException(errorMsg);
        }
        return input;
    }

    private static String getArgument(String[] commandLineArguments, String arg) {

        if (commandLineArguments != null) {
            for (int i = 0; i < commandLineArguments.length - 1; i++) {
                if (commandLineArguments[i] == null ||
                        commandLineArguments[i].length() == 0 ||
                        commandLineArguments[i].charAt(0) != '-' ||
                        commandLineArguments[i + 1] == null) {
                    continue;
                }
                String tmpArg = commandLineArguments[i].substring(1);
                if (tmpArg.equalsIgnoreCase(arg)) {
                    String input = commandLineArguments[i + 1];
                    if (input != null && !input.equals("")) {
                        return input;
                    }
                }
            }
        }
        return null;
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean isPrimaryArgumentPresent(String[] commandLineArguments, String arg) {

        if (commandLineArguments != null) {
            for (String commandLineArgument : commandLineArguments) {
                if (commandLineArgument == null ||
                        commandLineArgument.length() == 0 ||
                        commandLineArgument.charAt(0) != '-') {
                    continue;
                }
                String tmpArg = commandLineArgument.substring(1);
                if (tmpArg.equalsIgnoreCase(arg)) {
                    return true;
                }
            }
        }
        return false;
    }

}
