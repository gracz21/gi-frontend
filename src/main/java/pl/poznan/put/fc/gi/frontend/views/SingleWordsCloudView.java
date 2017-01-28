package pl.poznan.put.fc.gi.frontend.views;

import com.kennycason.kumo.WordFrequency;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import pl.poznan.put.fc.gi.frontend.controllers.SingleWordsCloudController;

import java.io.IOException;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public class SingleWordsCloudView {
    private HBox layout;

    public SingleWordsCloudView(String imageName, List<WordFrequency> wordFrequencies, String articlesLabel) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SingleWordsCloudLayout.fxml"));
        layout = loader.load();
        Image wordsCloudImage = new Image(getClass().getResourceAsStream("/wordsClouds/" + imageName));
        SingleWordsCloudController controller = loader.getController();
        controller.setWordsCloudImage(wordsCloudImage);
        controller.setWordFrequencyList(wordFrequencies);
        controller.setNumOfArticlesLabel(articlesLabel);
    }

    public HBox getLayout() {
        return layout;
    }
}
