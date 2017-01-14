package pl.poznan.put.fc.gi.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * @author Kamil Walkowiak
 */
public class SingleWordsCloudController {
    @FXML
    private ImageView wordsCloudImageView;
    @FXML
    private Label numOfArticlesLabel;

    public void setWordsCloudImage(Image wordsCloudImage) {
        wordsCloudImageView.setImage(wordsCloudImage);
    }
}
