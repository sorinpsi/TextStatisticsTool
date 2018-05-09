package sample;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

class Service {

    private DBConnection dbConnection;
    private String server;

    boolean processFile(File file) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<String> wordCount = new ArrayList<>();

        try {
            List<Future<List<String>>> futures = executor.invokeAll(decodeFile(file));
            futures.forEach(f -> {
                try {
                    wordCount.addAll(f.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            return false;
        }

        Map<String, Long> wordMap = wordCount.stream().map(String::toLowerCase).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        wordMap.remove("");
        List<PersistTextTask> runnableList = persistText(wordMap);
        runnableList.forEach(executor::submit);

        executor.shutdown();
        return true;
    }


    Map<Boolean, List<Pair<Long, String>>> getResults(int amount) {
        return dbConnection.getResultsFromDB(amount);
    }

    void startConnection(String server) {
        if (dbConnection == null) {
            this.server = server;
            dbConnection = new DBConnection();
            dbConnection.startConnection(server);
        }
    }

    void closeConnection() {
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }

    void cleanCollection() {
        dbConnection.cleanCollection();
    }

    private List<ParseTextTask> decodeFile(File file) {
        List<ParseTextTask> parseTextTaskList = new ArrayList<>();
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            List<String> stringList = new ArrayList<>();
            for (String line; (line = bReader.readLine()) != null; ) {
                stringList.add(line);
                if (stringList.size() > 100) {
                    parseTextTaskList.add(new ParseTextTask(stringList));
                    stringList = new ArrayList<>();
                }
            }
            if (stringList.size() > 0) {
                parseTextTaskList.add(new ParseTextTask(stringList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseTextTaskList;
    }

    private List<PersistTextTask> persistText(Map<String, Long> wordMap) {
        List<PersistTextTask> runnableList = new ArrayList<>();
        TreeMap<String, Long> treeMap = new TreeMap<>(wordMap);
        for (int i = 0, j = 100; i < treeMap.size(); i = i + j + 1) {
            int lastIndex = treeMap.size() < i + j ? treeMap.size() - 1 : i + j;
            runnableList.add(new PersistTextTask(treeMap.subMap((String) treeMap.keySet().toArray()[i], true,
                    (String) treeMap.keySet().toArray()[lastIndex], true), server));
        }
        return runnableList;
    }

}
