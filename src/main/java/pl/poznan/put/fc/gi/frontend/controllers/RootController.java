package pl.poznan.put.fc.gi.frontend.controllers;

import com.kennycason.kumo.WordFrequency;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import pl.poznan.put.fc.gi.frontend.utils.DatabaseHandlerUtil;
import pl.poznan.put.fc.gi.frontend.views.SingleWordsCloudView;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public class RootController {
    @FXML
    private Tab summaryTab;
    @FXML
    private ArrayList<ImageView> wordsCloudImageViews;
    @FXML
    private HBox pane;

    private List<Integer> yearsWithArticles;

    @FXML
    private void initialize() throws IOException {
        yearsWithArticles = DatabaseHandlerUtil.getYearsWithArticles();

        setupSummaryTab();
        setupYearsSummaryTab();
        setupGraphTab();
    }

    private void setupSummaryTab() throws IOException {
        List<WordFrequency> wordFrequencies = DatabaseHandlerUtil.getSummaryWordsFrequencyList();
        SingleWordsCloudView singleWordsCloudView = new SingleWordsCloudView("summary.png", wordFrequencies);
        BorderPane borderPane = (BorderPane)summaryTab.getContent();
        HBox layout = singleWordsCloudView.getLayout();
        borderPane.setCenter(layout);
        layout.prefWidthProperty().bind(borderPane.widthProperty());
        layout.prefHeightProperty().bind(borderPane.heightProperty());
    }

    private void setupYearsSummaryTab() {
        for(ImageView imageView: wordsCloudImageViews) {
            int index = wordsCloudImageViews.indexOf(imageView);
            InputStream image = getClass().getResourceAsStream("/wordsClouds/" +
                    yearsWithArticles.get(index) + "_mini.png");
            imageView.setImage(new Image(image));
            imageView.setOnMouseClicked(event -> {
                try {
                    showYearDetails(yearsWithArticles.get(index));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void setupGraphTab() {
        SwingNode graphNode = new SwingNode();
        SwingUtilities.invokeLater(() -> graphNode.setContent(setupGraph()));
        pane.getChildren().add(0, graphNode);
        HBox.setHgrow(graphNode, Priority.ALWAYS);
    }

    private void showYearDetails(int year) throws IOException {
        List<WordFrequency> wordFrequencies = DatabaseHandlerUtil.getWordsFrequencyListFromYear(15, year);
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("Podsumowanie roku");
        SingleWordsCloudView singleWordsCloudView = new SingleWordsCloudView(year + ".png", wordFrequencies);
        stage.setScene(new Scene(singleWordsCloudView.getLayout()));
        stage.show();
    }

    private mxGraphComponent setupGraph() {
        String color1 = "#0E812E";
        String color2 = "#2B9A4A";
        String color3 = "#49B367";
        String color4 = "#67CC83";
        String color5 = "#85E5A0";

        String vertexStyle = "rounded=1;fontSize=15;fontColor=#000000;fillColor=#eeeeee;strokeColor=#000000";
        String edgeStyle = "strokeWidth=3";

        mxGraph graph = new mxGraph();
        graph.setCellsEditable(false);
        graph.setCellsMovable(false);
        graph.setCellsResizable(false);
        graph.setCellsSelectable(false);
        Object defaultParent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        Object v1 = graph.insertVertex(defaultParent, null, "Using valued closeness relation in\n" +
                "classification support of new objects\n2006", 100, 300, 300, 65, vertexStyle);
        Object v2 = graph.insertVertex(defaultParent, null, "The bagging and n2-classifiers based on\n" +
                "rules induced by MODLEM\n2004", 500, 100, 300, 65, vertexStyle);
        Object v3 = graph.insertVertex(defaultParent, null, "Classification of Polish Email Messages:\n" +
                "Experiments with Various Data Representations\n2006", 1000, 100, 350, 65, vertexStyle);
        Object v4 = graph.insertVertex(defaultParent, null, "Overlapping, Rare Examples and Class\n" +
                "Decomposition in Learning Classifiers\nfrom Imbalanced Data\n2011", 1200, 300, 300, 90, vertexStyle);
        Object v5 = graph.insertVertex(defaultParent, null, "Improving Bagging by Feature Selection with\n" +
                "Dynamic Integration of Sub-classifiers\n2005", 1000, 700, 300, 65, vertexStyle);
        Object v6 = graph.insertVertex(defaultParent, null, "Multiple and hybrid classifiers\n2000",
                500, 700, 300, 65, vertexStyle);

        graph.insertEdge(defaultParent, null, null, v1, v2, edgeStyle + ";strokeColor=" + color1);
        graph.insertEdge(defaultParent, null, null, v1, v4, edgeStyle + ";strokeColor=" + color5);
        graph.insertEdge(defaultParent, null, null, v2, v5, edgeStyle + ";strokeColor=" + color2);
        graph.insertEdge(defaultParent, null, null, v3, v4, edgeStyle + ";strokeColor=" + color3);
        graph.insertEdge(defaultParent, null, null, v6, v5, edgeStyle + ";strokeColor=" + color4);
        graph.insertEdge(defaultParent, null, null, v5, v4, edgeStyle + ";strokeColor=" + color2);

        graph.getModel().endUpdate();
        return new mxGraphComponent(graph);
    }
}
