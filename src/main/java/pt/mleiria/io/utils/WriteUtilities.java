package pt.mleiria.io.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Logger;

public class WriteUtilities {

    private static final Logger LOG = Logger.getLogger(WriteUtilities.class.getName());

    private WriteUtilities(){}

    /**
     *
     * @param fileName
     * @param contents
     */
    public static void writeToFile(final String fileName, final List<String> contents) {
        try (final FileWriter myWriter = new FileWriter(fileName)) {
            contents.forEach(elem -> {
                try {
                    myWriter.write(elem + "\n");
                } catch (IOException e) {
                    LOG.severe("An error occurred.");
                    e.printStackTrace();
                }
            });
            LOG.info("Successfully wrote to the file.");
        } catch (IOException e) {
            LOG.severe("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     *
     * @param fileName
     * @param contents
     */
    public static void writeToFile(final String fileName, final String contents) {
        try (final FileWriter myWriter = new FileWriter(fileName)) {
            myWriter.write(contents);
            LOG.info("Successfully wrote to the file.");
        } catch (IOException e) {
            LOG.severe("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Append to file
     *
     * @param fileName
     * @param contents
     */
    public static void appendToFile(final String fileName, final String contents) {
        try {
            Files.write(Paths.get(fileName), contents.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOG.severe("An error occurred.");
            e.printStackTrace();
        }
    }
}
