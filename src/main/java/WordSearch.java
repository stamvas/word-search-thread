import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;



public class WordSearch {

    public static void main(String[] args) {
        String inputFolderPath = "data_ypoergasia3";
        String searchWord = "the";

        // Λήψη των ονομάτων των αρχείων σε λίστα
        List<String> fileNames = getFileNames(inputFolderPath);

        // επανάληψη για διαφορετικό αριθμό νημάτων (1,2,4,8)
        for (int numThreads : new int[]{1, 2, 4, 8}) {
            List<String> linesWithFileNames  = new ArrayList<>();

            // Διάβασμα των γραμμών από κάθε αρχείο
            for (String fileName : fileNames) {
                try {
                    List<String> fileLines = Files.readAllLines(Paths.get(inputFolderPath, fileName));
                    for (String line : fileLines) {
                        linesWithFileNames.add(line + " " + fileName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            long start = System.currentTimeMillis();

            // Δημιουργία των νημάτων
            List<WordSearchThread> threads = new ArrayList<>();
            int chunkSize = linesWithFileNames.size() / numThreads;
            int startIndex = 0;

            // διαχωρισμός των γραμμών σε τμήματα
            for (int i = 0; i < numThreads; i++) {
                int endIndex = (i == numThreads - 1) ? linesWithFileNames.size() : startIndex + chunkSize;
                List<String> subList = linesWithFileNames.subList(startIndex, endIndex);
                WordSearchThread thread = new WordSearchThread(subList, searchWord);
                threads.add(thread);
                startIndex = endIndex;
            }

            // Εκκίνηση των νημάτων
            for (WordSearchThread thread : threads) {
                thread.start();
            }

            // Αναμονή για την ολοκλήρωση όλων των νημάτων
            for (WordSearchThread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long finish = System.currentTimeMillis();
            long executionTime = finish - start;

            // Εκτύπωση των αποτελεσμάτων
            List<String> resultLines = new ArrayList<>();
            for (WordSearchThread thread : threads) {
                resultLines.addAll(thread.getResultLines());
            }

            // Εκτύπωση των χρόνων
            System.out.println("Execution Time for " + numThreads + " threads: " + executionTime + " ms");


            // Εγγραφή των αποτελεσμάτων στο αρχείο output.txt
            String outputFilePath = "output.txt";
            writeResultsToFile(resultLines, outputFilePath);
        }
    }

    // Λήψη των ονομάτων των αρχείων σε λίστα
    private static List<String> getFileNames(String folderPath) {
        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folderPath))) {
            for (Path path : stream) {
                if (path.toFile().isFile()) {
                    fileNames.add(path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    // // Εγγραφή των αποτελεσμάτων σε αρχείο
    private static void writeResultsToFile(List<String> lines, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
