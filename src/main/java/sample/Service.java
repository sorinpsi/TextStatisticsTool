package sample;

import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class Service {

    private DBConnection dbConnection;
    boolean processFile(File file) {
        //clear DB
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            Queue<String> queue = new PriorityQueue<>();
            queue = new FileProcess(bReader, queue).compute();
            dbConnection = new DBConnection();
            dbConnection.persistQueue(queue);
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }


    public Map<Boolean,List<Pair<Long,String>>> getResults(Long amount) {
        return dbConnection.getResultsFromDB(amount);
    }
}

class FileProcess extends RecursiveTask<Queue<String>> {

    private BufferedReader reader;
    private Queue<String> queue;
    FileProcess(BufferedReader reader, Queue<String> queue) {
        this.reader = reader;
        this.queue = queue;
    }

    @Override
    protected Queue<String> compute() {
        List<RecursiveTask<Queue<String>>> actions = new ArrayList<>();
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                String[] words = line.split(" ");
                for(String word: words){
                    queue.add(word);
                    FileProcess newProcess = new FileProcess(reader, queue);
                    newProcess.fork();
                    actions.add(newProcess);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(RecursiveTask<Queue<String>> action : actions){
            action.join();
        }
        return queue;
    }
}