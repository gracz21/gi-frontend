package pl.poznan.put.fc.gi.frontend.controllers;

import com.kennycason.kumo.WordFrequency;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import pl.poznan.put.fc.gi.frontend.models.Article;

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
    private TableView articlesTableView;
    @FXML
    private TableColumn<Article, String> titleTableColumn;
    @FXML
    private TableColumn<Article, String> authorsTableColumn;
    @FXML
    private TableView wordsStatisticsTableView;
    @FXML
    private TableColumn<WordFrequency, String> wordTableColumn;
    @FXML
    private TableColumn<WordFrequency, Integer> countTableColumn;

    private List<WordFrequency> wordFrequencyList;

    @FXML
    private void initialize() {
        titleTableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        titleTableColumn.setCellFactory(param -> {
            TableCell<Article, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(titleTableColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        authorsTableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAuthors()));

        wordTableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getWord()));
        countTableColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        countTableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getFrequency()));
    }

    public void setNumOfArticlesLabel(String label) {
        numOfArticlesLabel.setText(label);
    }

    public void setWordsCloudImage(Image wordsCloudImage) {
        wordsCloudImageView.setImage(wordsCloudImage);
    }

    public void setWordFrequencyList(List<WordFrequency> wordFrequencyList) {
        this.wordFrequencyList = wordFrequencyList;
        wordsStatisticsTableView.setItems(new ObservableListWrapper<>(wordFrequencyList));
    }

    public void setArticlesList(List<Article> articlesList) {
        articlesTableView.setItems(new ObservableListWrapper<>(articlesList));
    }
}
