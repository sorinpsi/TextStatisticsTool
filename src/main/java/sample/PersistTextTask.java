package sample;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersistTextTask implements Runnable {
    private Map<String, Long> wordMap;
    private String server;

    PersistTextTask(Map<String, Long> wordMap, String server) {
        this.wordMap = wordMap;
        this.server = server;
    }

    @Override
    public void run() {
        DBConnection dbConnection = new DBConnection();
        if(!dbConnection.startConnection(this.server)) return;
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
