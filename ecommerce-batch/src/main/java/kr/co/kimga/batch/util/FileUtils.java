package kr.co.kimga.batch.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

    public static List<File> splitCsv(File csvFile, long fileCount) {
        return splitFileAfterLineCount(csvFile, fileCount, true, ".csv");
    }

    private static List<File> splitFileAfterLineCount(File csvFile, long fileCount, boolean ignoreFirstLine, String suffix) {
        long lineCount;

        try (Stream<String> stream = Files.lines(csvFile.toPath(), StandardCharsets.UTF_8)) {
            lineCount = stream.count();
            long linesPerFile = (long) Math.ceil((double) lineCount / fileCount);

            return splitFiles(csvFile, linesPerFile, ignoreFirstLine, suffix);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<File> splitFiles(File csvFile, long linePerFile, boolean ignoreFirstLine, String suffix) throws IOException {
        List<File> splitFiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean firstLine = true;
            BufferedWriter writer = null;
            int lineCount = 0;
            boolean shouldCreateFile = true;
            File splitFile;
            int fileIndex = 0;

            while ((line = reader.readLine()) != null) {
                if (ignoreFirstLine && firstLine) {
                    firstLine = false;
                    continue;
                }
                if (shouldCreateFile) {
                    splitFile = createTempFile("split_" + (fileIndex++) + "_", suffix);
                    writer = new BufferedWriter(new FileWriter(splitFile));
                    splitFiles.add(splitFile);
                    lineCount = 0;
                }

                writer.write(line);
                writer.newLine();
                lineCount++;

                if (lineCount >= linePerFile) {
                    writer.close();
                    shouldCreateFile = true;
                }
            }
            writer.close();
        }

        return splitFiles;
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static List<File> splitLog(File logFile, long fileCount) {
        return splitFileAfterLineCount(logFile, fileCount, true, ".csv");
    }

}
