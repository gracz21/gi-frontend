package pl.poznan.put.fc.gi.frontend.controllers;

import com.kennycason.kumo.WordFrequency;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;


/**
 * @author Kamil Walkowiak
 */
public class SingleWordsCloudController {
    @FXML
    private ImageView wordsCloudImageView;
    @FXML
    private Label numOfArticlesLabel;
    @FXML
    private TableView wordsStatisticsTableView;
    @FXML
    private TableColumn<WordFrequency, Integer> rankTableColumn;
    @FXML
    private TableColumn<WordFrequency, String> wordTableColumn;
    @FXML
    private TableColumn<WordFrequency, Integer> countTableColumn;

    private List<WordFrequency> wordFrequencyList;

    @FXML
    private void initialize() {
        rankTableColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(wordFrequencyList.indexOf(param.getValue()) + 1));
        wordTableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getWord()));
        countTableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getFrequency()));
    }

    public void setWordsCloudImage(Image wordsCloudImage) {
        wordsCloudImageView.setImage(wordsCloudImage);
    }

    public void setWordFrequencyList(List<WordFrequency> wordFrequencyList) {
        this.wordFrequencyList = wordFrequencyList;
        wordsStatisticsTableView.setItems(new ObservableListWrapper<>(wordFrequencyList));
    }
}
