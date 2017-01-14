package pl.poznan.put.fc.gi.frontend.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import pl.poznan.put.fc.gi.frontend.controllers.SingleWordsCloudController;

import java.io.IOException;

/**
 * @author Kamil Walkowiak
 */
public class SingleWordsCloudView {
    private AnchorPane layout;

    public SingleWordsCloudView(Image wordsCloudImage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SingleWordsCloudLayout.fxml"));
        layout = loader.load();
        ((SingleWordsCloudController)loader.getController()).setWordsCloudImage(wordsCloudImage);
    }

    public Pane getLayout() {
        return layout;
    }
}
