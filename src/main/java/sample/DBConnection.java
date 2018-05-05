package sample;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.util.Pair;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;

class DBConnection {
    private MongoClient mongoClient;
    private MongoCollection<Document> wordCollection;
    private Bson condition = new Document();

    void persistWords(List<Document> documents) {
        wordCollection.insertMany(documents);
    }

    Map<Boolean, List<Pair<Long, String>>> getResultsFromDB(Long amount) {
        return null;
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
