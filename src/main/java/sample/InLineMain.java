package sample;

import javafx.util.Pair;

import java.io.Console;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InLineMain {

    static String source = "-source";
    static String spareSource = "big.txt";
    static String mongo = "-mongo";
    static String spareMongo = "localhost:27017";
    /*
        java –Xmx8192m -jar challenge.jar –source dump.xml –mongo [hostname]:[port]
    */
    public static void main(String[] args){
        Service service = new Service();
        String fileName = getArg(args, source, spareSource);
        String server = getArg(args, mongo, spareMongo);
        if(!service.startConnection(server)) return;
        boolean done = service.processFile(new File(fileName));
        if(done){
            Map<Boolean, List<Pair<Long, String>>> results = service.getResults(Integer.parseInt(args[1]));
            if (results == null) {
                return;
            }
            printResults(results);
        }
    }

    private static String getArg(String[] args, String keyWord, String spare) {
        return Arrays.stream(args).filter(keyWord::equalsIgnoreCase).findFirst().orElse(spare);
    }

    private static void printResults(Map<Boolean, List<Pair<Long, String>>> results){
        Console console = System.console();
        PrintWriter writer = console.writer();
        writer.println();
        writer.print("Most 50 used words");
        results.get(true).forEach(pair -> writer.print(pair.getKey()+" : "+pair.getValue()));
        writer.println();
        writer.println();
        writer.print("Least 50 used words");
        results.get(false).forEach(pair -> writer.print(pair.getKey()+" : "+pair.getValue()));
    }
}
