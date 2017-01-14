package pl.poznan.put.fc.gi.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.poznan.put.fc.gi.frontend.utils.WordsCloudGeneratorUtil;
import pl.poznan.put.fc.gi.frontend.views.SingleWordsCloudView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Kamil Walkowiak
 */
public class RootController {
    @FXML
    private Tab summaryTab;
    @FXML
    private ArrayList<ImageView> wordsCloudImageViews;

    private Image summaryWordsCloudImage;
    private Image miniWordsCloudImage;

    @FXML
    private void initialize() throws IOException {
        summaryWordsCloudImage = WordsCloudGeneratorUtil.getInstance().generateWordsCloudImage(15);
        miniWordsCloudImage = WordsCloudGeneratorUtil.getInstance().generateWordsCloudImage(5);

        SingleWordsCloudView singleWordsCloudView = new SingleWordsCloudView(summaryWordsCloudImage);
        BorderPane borderPane = (BorderPane)summaryTab.getContent();
        HBox layout = singleWordsCloudView.getLayout();
        borderPane.setCenter(layout);
        layout.prefWidthProperty().bind(borderPane.widthProperty());
        layout.prefHeightProperty().bind(borderPane.heightProperty());

        setupImageViews();
    }

    private void setupImageViews() {
        for(ImageView imageView: wordsCloudImageViews) {
            imageView.setImage(miniWordsCloudImage);
            imageView.setOnMouseClicked(event -> {
                try {
                    showYearDetails();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void showYearDetails() throws IOException {
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("Podsumowanie roku");
        SingleWordsCloudView singleWordsCloudView = new SingleWordsCloudView(summaryWordsCloudImage);
        stage.setScene(new Scene(singleWordsCloudView.getLayout()));
        stage.show();
    }
}
