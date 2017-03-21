package com.jojos.path.calculation;

import com.jojos.path.input.ParsedObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author karanikasg@gmail.com
 */
public class ParseJobTest {

    private static final String SUCCESS_PATH = "test_success_input" + File.separator;
    private static final String UNSUCCESSFUL_PATH = "test_wrong_input" + File.separator;

    @Test
    public void testSuccessResourceFiles() throws URISyntaxException, IOException {
        String[] files = getResourceListing(getClass(), SUCCESS_PATH);

        for (String file : files) {
            ParseJob parseJob = new ParseJob(file);
            ParsedObject parsedObject = parseJob.runAndCreateGraph(ParseJob.RunMode.WITH_ASSERTIONS);

            Assert.assertNotNull(parsedObject);
            Assert.assertNotNull(parsedObject.getAssertions());
            Assert.assertNotNull(parsedObject.getGraph());

            // we assume that if no ApplicationException is thrown then everything were validate OK
            ShortestPathJob.withAssertions(parsedObject).run();
        }
    }

    @Test
    public void testUnsuccessfulResourceFiles() throws URISyntaxException, IOException {
        String[] files = getResourceListing(getClass(), UNSUCCESSFUL_PATH);

        for (String file : files) {
            ParseJob parseJob = new ParseJob(file);
            ParsedObject parsedObject = parseJob.runAndCreateGraph(ParseJob.RunMode.WITH_ASSERTIONS);

            Assert.assertNotNull(parsedObject);

            // we assume that if no ApplicationException is thrown then everything were validate OK
            ShortestPathJob.withAssertions(parsedObject).run();
        }
    }

    /**
     * Special thanks to Greg Briggs as seen under
     * @see <a href="http://www.uofr.net/~greg/java/get-resource-listing.html">Greg's source code</a>
     *
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     *
     * @param clazz Any java class that lives in the same place as the resources you want.
     * @param path Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException if this URL is not formatted strictly according to to RFC2396 and cannot be converted to a URI.
     * @throws IOException If character encoding needs to be consulted, but named character encoding is not supported
     */
    @SuppressWarnings("SameParameterValue")
    private String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
        URL dirURL = clazz.getClassLoader().getResource(path);

        if (Objects.nonNull(dirURL) && dirURL.getProtocol().equals("file")) {
            File parentDirectory = new File(dirURL.toURI());
            String parentDirectoryAbsolutePath = parentDirectory.getAbsolutePath();
            /* A file path: easy enough */
            List<String> list = Arrays.asList(parentDirectory.list());
            return list.stream()
                    .map(string -> parentDirectoryAbsolutePath + File.separator + string)
                    .collect(Collectors.toList())
                    .toArray(new String[list.size()]);
        }

        if (Objects.isNull(dirURL)) {
        /*
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
         */
            String me = clazz.getName().replace(".", "/")+".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }

        if (Objects.nonNull(dirURL) && dirURL.getProtocol().equals("jar")) {
        /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
            while(entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(path)) { //filter according to the path
                    String entry = name.substring(path.length());
                    int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0) {
                        // if it is a subdirectory, we just return the directory name
                        entry = entry.substring(0, checkSubdir);
                    }
                    result.add(entry);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
    }

}
