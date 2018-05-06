package sample;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class WordCount {
    private final SimpleLongProperty amount;
    private final SimpleStringProperty word;

    WordCount(Long amount, String word) {
        this.amount = new SimpleLongProperty(amount);
        this.word = new SimpleStringProperty(word);
    }

    public long getAmount() {
        return amount.get();
    }

    public SimpleLongProperty amountProperty() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount.set(amount);
    }

    public String getWord() {
        return word.get();
    }

    public SimpleStringProperty wordProperty() {
        return word;
    }

    public void setWord(String word) {
        this.word.set(word);
    }
}