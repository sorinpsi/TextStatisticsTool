package sample;

import java.util.*;
import java.util.concurrent.Callable;

public class ParseTextTask implements Callable<List<String>> {

    private List<String> stringList;

    ParseTextTask(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public List<String> call() {
        List<String> wordCount = new ArrayList<>();
        for (String line : stringList) {
            String[] words = line.split("[^\\w]");
            wordCount.addAll(Arrays.asList(words));
        }
        return wordCount;
    }
}