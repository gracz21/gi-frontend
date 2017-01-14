package pl.poznan.put.fc.gi.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import pl.poznan.put.fc.gi.frontend.utils.WordsCloudGeneratorUtil;
import pl.poznan.put.fc.gi.frontend.views.SingleWordsCloudView;

import java.io.IOException;

/**
 * @author Kamil Walkowiak
 */
public class RootController {
    @FXML
    private Tab summaryTab;

    @FXML
    private void initialize() throws IOException {
        Image wordsCloudImage = WordsCloudGeneratorUtil.generateWordsCloudImage(15);
        SingleWordsCloudView singleWordsCloudView = new SingleWordsCloudView(wordsCloudImage);
        BorderPane borderPane = (BorderPane)summaryTab.getContent();
        borderPane.setCenter(singleWordsCloudView.getLayout());
    }
}
