package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller extends Application {

    private TextField textField;
    private TableView most;
    private TableView least;
    private Service service = new Service();
    private Button run;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        service.startConnection();
        primaryStage.setTitle("Challenge");
        BorderPane borderPane = loadBorderPane();
        Scene scene = new Scene(borderPane, 410, 480);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> service.closeConnection());
        primaryStage.show();
    }

    private BorderPane loadBorderPane() {
        Button open = new Button("Open");
        run = new Button("Run");
        run.setDisable(true);
        open.setOnAction(o -> showSingleFileChooser());
        run.setOnAction(r -> getStatistics());
        BorderPane borderPane = new BorderPane();
        Label label = new Label("No. of results:");
        textField = new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*")) textField.setText(oldValue);
        });
        HBox hBox = new HBox();
        hBox.getChildren().addAll(open, run, label, textField);
        borderPane.setTop(hBox);
        GridPane gridPane = new GridPane();
        borderPane.setCenter(gridPane);

        loadGridPane(gridPane);
        return borderPane;
    }

    private void loadGridPane(GridPane gridPane) {
        ScrollPane mostScroll = new ScrollPane();
        ScrollPane leastScroll = new ScrollPane();
        most = new TableView();
        least = new TableView();
        mostScroll.setContent(most);
        leastScroll.setContent(least);
        most.setEditable(false);
        least.setEditable(false);
        most.setMaxSize(200, 450);
        least.setMaxSize(200, 450);
        TableColumn<WordCount, Long> mostNumber = new TableColumn<>("No of Times");
        TableColumn<WordCount, Long> leastNumber = new TableColumn<>("No of Times");
        TableColumn<WordCount, String> mostWord = new TableColumn<>("Most Word");
        TableColumn<WordCount, String> leastWord = new TableColumn<>("Least Word");
        mostNumber.setCellValueFactory(new PropertyValueFactory<>("amount"));
        leastNumber.setCellValueFactory(new PropertyValueFactory<>("amount"));
        mostWord.setCellValueFactory(new PropertyValueFactory<>("word"));
        leastWord.setCellValueFactory(new PropertyValueFactory<>("word"));
        most.getColumns().addAll(mostNumber, mostWord);
        least.getColumns().addAll(leastNumber, leastWord);
        gridPane.add(mostScroll, 0, 0);
        gridPane.add(leastScroll, 1, 0);
    }

    private void showSingleFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            service.cleanCollection();
            boolean effect = service.processFile(selectedFile);
            run.setDisable(!effect);
        }
    }

    private void getStatistics() {
        String text = textField.getText();
        int amount = Integer.parseInt(text);
        most.getItems().clear();
        least.getItems().clear();
        Map<Boolean, List<Pair<Long, String>>> results = service.getResults(amount);
        if (results == null) {
            return;
        }

        List<WordCount> mostWords = results.get(true).stream().map(pair -> new WordCount(pair.getKey(), pair.getValue())).collect(Collectors.toList());
        List<WordCount> leastWords = results.get(false).stream().map(pair -> new WordCount(pair.getKey(), pair.getValue())).collect(Collectors.toList());

        ObservableList<WordCount> mostWordList = FXCollections.observableArrayList(mostWords);
        ObservableList<WordCount> leastWordList = FXCollections.observableArrayList(leastWords);
        most.setItems(mostWordList);
        least.setItems(leastWordList);
        most.refresh();
        least.refresh();
    }
}
