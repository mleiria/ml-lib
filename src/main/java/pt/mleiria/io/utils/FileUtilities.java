package pt.mleiria.io.utils;

import pt.mleiria.collections.immutable.CollectionUtilities;
import pt.mleiria.core.StopWatch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;

public class FileUtilities {

    private static final Logger LOG = Logger.getLogger(FileUtilities.class.getName());

    public static final Function<String, String[]> strToStrArr = x -> x.split(",");

    public static final Function<String[], double[]> strArrToDbl = x -> Arrays.stream(x)
            .mapToDouble(Double::valueOf)
            .toArray();

    private FileUtilities() {
    }


    /**
     * Loads a file into a List of Strings
     *
     * @param path
     * @return a list of strings
     */
    public static List<String> load(String path) {
        final Path file = get(path);
        final List<String> data = new ArrayList<>();
        try (final InputStream in = newInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    data.add(line);
                }
            }
        } catch (final IOException x) {
            LOG.log(Level.SEVERE, x.getMessage(), x);
        }
        return CollectionUtilities.list(data);
    }

    public static <R> List<R> load(final String path, final Function<String, R> function) {
        final Path file = get(path);
        final List<R> data = new ArrayList<>();
        try (final InputStream in = newInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    data.add(function.apply(line));
                }
            }
        } catch (final IOException x) {
            LOG.severe(x.getMessage());
        }
        return CollectionUtilities.list(data);
    }

    /**
     * @param path
     * @param skipFirstRow
     * @return
     */
    public static List<String> load(final String path, final boolean skipFirstRow) {
        final Path file = get(path);
        final List<String> data = new ArrayList<>();
        try (final InputStream in = newInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            if (skipFirstRow) {
                reader.readLine();
            }
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (final IOException x) {
            LOG.severe(x.getMessage());
        }
        return CollectionUtilities.list(data);
    }

    /**
     * @param dir
     * @return
     */
    public static File[] loadAllFilesInDir(final String dir) {
        final File source = new File(dir);
        return source.listFiles();
    }

    /**
     * @param dir
     * @return a Set with all file names of the dir (no recursion)
     */
    public static Set<String> loadAllFileNamesInDir(final String dir) {
        final File source = new File(dir);
        final Set<String> res = new HashSet<>();
        Arrays.stream(Objects.requireNonNull(source.listFiles())).sequential().forEach(elem -> res.add(elem.getName()));
        return res;
    }

    /**
     * @param sDir
     * @return a Set of all jpg files in dir and sub dirs
     */
    public static File[] loadJPG(String sDir) {
        final StopWatch sw = new StopWatch();
        final Set<File> fileSet = new HashSet<>();
        try (final Stream<Path> stream = Files.find(Paths.get(sDir), 999,
                (p, bfa) -> bfa.isRegularFile() && p.getFileName().toString().matches(".*\\.(?i)jpg"))) {
            stream.parallel().forEach(elem -> fileSet.add(elem.toFile()));

        } catch (IOException e) {
            LOG.severe(() -> String.valueOf(e));
        }
        LOG.info(String.format("Loaded %d in %s", fileSet.size(), sw.elapsedTime()));
        return fileSet.toArray(new File[0]);
    }

    public String loadFileToString(final String filePath){
        return
                processFiles((BufferedReader br) -> br.lines().collect(Collectors.joining()), filePath);
    }

    public static String loadFileFromResourceToString(final String filePath){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return
                processFiles((BufferedReader br) -> br.lines().collect(Collectors.joining()),
                        Objects.requireNonNull(classLoader.getResource(filePath)).getFile());
    }


    private static String processFiles(final BufferedReaderProcessor p, final String filePath) {
        try (final BufferedReader br = new BufferedReader((new FileReader(filePath)))) {
            return p.process(br);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }
        return "";
    }


}
