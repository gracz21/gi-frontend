package pl.poznan.put.fc.gi.frontend.controllers;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.poznan.put.fc.gi.frontend.utils.WordsCloudGeneratorUtil;
import pl.poznan.put.fc.gi.frontend.views.SingleWordsCloudView;

import javax.swing.*;
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
    @FXML
    private AnchorPane graphAnchorPane;

    private Image summaryWordsCloudImage;
    private Image miniWordsCloudImage;

    @FXML
    private void initialize() throws IOException {
        summaryWordsCloudImage = WordsCloudGeneratorUtil.getInstance().generateWordsCloudImage(15);
        miniWordsCloudImage = WordsCloudGeneratorUtil.getInstance().generateWordsCloudImage(5);

        setupSummaryTab();
        setupYearsSummaryTab();
        setupGraphTab();
    }

    private void setupSummaryTab() throws IOException {
        SingleWordsCloudView singleWordsCloudView = new SingleWordsCloudView(summaryWordsCloudImage);
        BorderPane borderPane = (BorderPane)summaryTab.getContent();
        HBox layout = singleWordsCloudView.getLayout();
        borderPane.setCenter(layout);
        layout.prefWidthProperty().bind(borderPane.widthProperty());
        layout.prefHeightProperty().bind(borderPane.heightProperty());
    }

    private void setupYearsSummaryTab() {
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

    private void setupGraphTab() {
        SwingNode graphNode = new SwingNode();
        SwingUtilities.invokeLater(() -> graphNode.setContent(setupGraph()));
        graphAnchorPane.getChildren().add(graphNode);
        AnchorPane.setBottomAnchor(graphNode, 0.0);
        AnchorPane.setTopAnchor(graphNode, 0.0);
        AnchorPane.setRightAnchor(graphNode, 0.0);
        AnchorPane.setLeftAnchor(graphNode, 0.0);
    }

    private void showYearDetails() throws IOException {
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("Podsumowanie roku");
        SingleWordsCloudView singleWordsCloudView = new SingleWordsCloudView(summaryWordsCloudImage);
        stage.setScene(new Scene(singleWordsCloudView.getLayout()));
        stage.show();
    }

    private mxGraphComponent setupGraph() {
        mxGraph graph = new mxGraph();
        graph.setCellsEditable(false);
        graph.setCellsMovable(false);
        graph.setCellsResizable(false);
        graph.setCellsSelectable(false);

        Object defaultParent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        Object v1 = graph.insertVertex(defaultParent, null, "USING VALUED CLOSENESS RELATION IN\n" +
                "CLASSIFICATION SUPPORT OF NEW OBJECTS", 20, 20, 300, 50);
        Object v2 = graph.insertVertex(defaultParent, null, "World", 240, 150, 150, 100);
        graph.insertEdge(defaultParent, null, null, v1, v2, "strokeColor=green;strokeWidth=3");
        graph.getModel().endUpdate();

        return new mxGraphComponent(graph);
    }
}
