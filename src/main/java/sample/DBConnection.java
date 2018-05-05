package sample;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.util.Pair;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public class DBConnection {

    MongoCollection<Document> collection;
    public void persistQueue(Queue<String> queue) {
    }

    public Map<Boolean,List<Pair<Long,String>>> getResultsFromDB(Long amount) {
        return null;
    }

    public boolean startConnection(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase db = mongoClient.getDatabase("pairs");
        collection = db.getCollection("pairCollection");
        return true;
    }
}
