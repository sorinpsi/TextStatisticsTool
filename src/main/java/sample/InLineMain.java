package sample;

import javafx.util.Pair;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InLineMain {

    private static String source = "-source";
    private static String defaultSource = "big.txt";
    private static String mongo = "-mongo";
    private static String defaultMongo = "localhost:27017";
    private static String amount = "-amount";
    private static String defaultAmount = "100";
    private static String loadFile = "-load";
    private static String defaultloadFile = "true";

    public static void main(String[] args) {
        Service service = new Service();
        String finalSource = getArg(args, source, defaultSource);
        String finalMongo = getArg(args, mongo, defaultMongo);
        String stringAmount = getArg(args, amount, defaultAmount);
        boolean finalLoadFile = Boolean.valueOf(getArg(args, loadFile, defaultloadFile));
        int finalAmount = 0;
        try {
            finalAmount = Integer.parseInt(stringAmount);
        } catch (NumberFormatException ignored) {
        }
        service.startConnection(finalMongo.trim());
        boolean done = !finalLoadFile || service.processFile(new File(finalSource));
        if (done) {
            Map<Boolean, List<Pair<Long, String>>> results = service.getResults(finalAmount);
            printResults(results, finalAmount);
        }
    }

    private static String getArg(String[] args, String keyWord, String spare) {
        List<String> argsArray = Arrays.asList(args);
        if (argsArray.indexOf(keyWord) > -1) {
            return argsArray.get(argsArray.indexOf(keyWord) + 1);
        } else {
            return spare;
        }
    }

    private static void printResults(Map<Boolean, List<Pair<Long, String>>> results, int amount) {
        System.out.println();
        System.out.println("Most " + amount + " used words");
        results.get(true).forEach(pair -> System.out.println(pair.getKey() + " : " + pair.getValue()));
        System.out.println();
        System.out.println();
        System.out.println("Least " + amount + " used words");
        results.get(false).forEach(pair -> System.out.println(pair.getKey() + " : " + pair.getValue()));
    }
}
