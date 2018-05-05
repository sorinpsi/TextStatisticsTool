package sample;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersistTextTask implements Runnable {
    private Map<String, Long> wordMap;

    PersistTextTask(Map<String, Long> wordMap) {
        this.wordMap = wordMap;
    }

    @Override
    public void run() {
        DBConnection dbConnection = new DBConnection();
        dbConnection.startConnection();
        List<Document> documents = new ArrayList<>();
        wordMap.forEach((word, num) -> {
            Document newDoc = new Document();
            newDoc.put("word", word);
            newDoc.put("amount", num);
            documents.add(newDoc);
        });
        dbConnection.persistWords(documents);
    }
}
