import java.util.ArrayList;
import java.util.List;

public class WordSearchThread extends Thread{
    private List<String> lines;
    private String searchWord;
    private List<String> resultLines = new ArrayList<>();

    public WordSearchThread(List<String> lines, String searchWord) {
        this.lines = lines;
        this.searchWord = searchWord;
    }

    @Override
    public void run() {
        for (String line : lines) { // Επεξεργασία κάθε γραμμής και προσθήκη στη λίστα αποτελεσμάτων
            if (line.contains(searchWord)) {
                resultLines.add(line);
            }
        }
    }

    public List<String> getResultLines() {
        return resultLines;
    }

}
