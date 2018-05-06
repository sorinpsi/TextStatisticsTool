package sample;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.util.Pair;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class DBConnection {
    private MongoClient mongoClient;
    private MongoCollection<Document> wordCollection;
    private Bson condition = new Document();

    void persistWords(List<Document> documents) {
        wordCollection.insertMany(documents);
    }

    Map<Boolean, List<Pair<Long, String>>> getResultsFromDB(int amount) {
        FindIterable<Document> cursor = wordCollection.find();
        Document mostDoc = new Document();
        Document leastDoc = new Document();
        mostDoc.put("amount", -1);
        leastDoc.put("amount", 1);
        List<Pair<Long, String>> mostPairList = cursor.sort(mostDoc).limit(amount).into(new ArrayList<>()).stream()
                .map(e -> new Pair<>((Long) e.get("amount"), (String) e.get("word"))).collect(Collectors.toList());
        List<Pair<Long, String>> leastPairList = cursor.sort(leastDoc).limit(amount).into(new ArrayList<>()).stream()
                .map(e -> new Pair<>((Long) e.get("amount"), (String) e.get("word"))).collect(Collectors.toList());
        Map<Boolean, List<Pair<Long, String>>> resultsMap = new HashMap<>();
        resultsMap.put(true, mostPairList);
        resultsMap.put(false, leastPairList);
        return resultsMap;
    }

    void startConnection() {
        mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase db = mongoClient.getDatabase("wordDB");
        wordCollection = db.getCollection("wordCollection");
    }

    void closeConnection() {
        mongoClient.close();
    }

    void cleanCollection() {
        wordCollection.deleteMany(condition);
    }
}
