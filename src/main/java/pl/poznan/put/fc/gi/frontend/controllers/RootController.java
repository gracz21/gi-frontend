package pl.poznan.put.fc.gi.frontend.controllers;

import com.kennycason.kumo.WordFrequency;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphSelectionModel;
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
import pl.poznan.put.fc.gi.frontend.models.Article;
import pl.poznan.put.fc.gi.frontend.utils.DatabaseHandlerUtil;
import pl.poznan.put.fc.gi.frontend.views.SingleWordsCloudView;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

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
    private List<Article> articles;

    @FXML
    private void initialize() throws IOException {
        yearsWithArticles = DatabaseHandlerUtil.getYearsWithArticles();
        articles = DatabaseHandlerUtil.getAllArticles();

        setupSummaryTab();
        setupYearsSummaryTab();
        setupGraphTab();
    }

    private void setupSummaryTab() throws IOException {
        List<WordFrequency> wordFrequencies = DatabaseHandlerUtil.getSummaryWordsFrequencyList();
        int articlesCount = articles.size();
        String label = articlesCount + " artykułów z lat " + yearsWithArticles.get(0)
                + "-" + yearsWithArticles.get(yearsWithArticles.size() - 1);
        SingleWordsCloudView singleWordsCloudView = new SingleWordsCloudView("summary.png", wordFrequencies, articles, label);
        BorderPane borderPane = (BorderPane) summaryTab.getContent();
        HBox layout = singleWordsCloudView.getLayout();
        borderPane.setCenter(layout);
        layout.prefWidthProperty().bind(borderPane.widthProperty());
        layout.prefHeightProperty().bind(borderPane.heightProperty());
    }

    private void setupYearsSummaryTab() {
        for (ImageView imageView : wordsCloudImageViews) {
            int index = wordsCloudImageViews.indexOf(imageView);
            Image image = new Image(getClass().getResourceAsStream("/wordsClouds/" +
                    yearsWithArticles.get(index) + "_mini.png"));
            imageView.setImage(image);
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
        List<WordFrequency> wordFrequencies = DatabaseHandlerUtil.getWordsFrequencyListFromYear(year);
        List<Article> articles = DatabaseHandlerUtil.getYearArticles(year);
        int articlesCount = articles.size();
        String label = articlesCount + " artykuł(-y) z roku " + year;
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("Podsumowanie roku");
        SingleWordsCloudView singleWordsCloudView = new SingleWordsCloudView(year + ".png", wordFrequencies, articles, label);
        stage.setScene(new Scene(singleWordsCloudView.getLayout()));
        stage.show();
    }

    private mxGraphComponent setupGraph() {
        mxGraph graph = new mxGraph(){
            public boolean isCellSelectable(Object cell) {
                return !model.isEdge(cell) && super.isCellSelectable(cell);
            }
        };
        graph.setCellsLocked(true);
        mxConstants.DEFAULT_MARKERSIZE = 0;
        Object defaultParent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        graph.getSelectionModel().setSingleSelection(true);

        List<Object> vertices = setupVertices(articles, graph, defaultParent);
        Map<Object, Map<Object, Integer>> similarityMap = DatabaseHandlerUtil.getSimilaritiesMap(vertices);
        setupEdges(similarityMap, graph, defaultParent);

        graph.getModel().endUpdate();
        final mxCell[] lastSelectedCell = {null};
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setDragEnabled(false);
        graph.getSelectionModel().addListener(mxEvent.CHANGE, (o, mxEventObject) -> {
            Object[] objects = ((mxGraphSelectionModel) o).getCells();
            mxCell cell = null;
            if(objects.length > 0) {
                cell = (mxCell)objects[0];
                if (!cell.equals(lastSelectedCell[0])) {
                    for (int i = 0; i < cell.getEdgeCount(); i++) {
                        String newStyle = "";
                        mxICell edge = cell.getEdgeAt(i);
                        String[] elements = edge.getStyle().split(";");
                        for (String element : elements) {
                            if (!element.contains("opacity")) {
                                newStyle += element + ";";
                            }
                        }
                        newStyle = newStyle.substring(0, newStyle.length() - 1);
                        edge.setStyle(newStyle);
                    }
                }
            }
            if (lastSelectedCell[0] != null) {
                for (int i = 0; i < lastSelectedCell[0].getEdgeCount(); i++) {
                    mxICell edge = lastSelectedCell[0].getEdgeAt(i);
                    edge.setStyle(edge.getStyle() + ";opacity=30");
                }
            }
            lastSelectedCell[0] = cell;
            graphComponent.refresh();
        });
        return graphComponent;
    }

    private List<Object> setupVertices(List<Article> articles, mxGraph graph, Object defaultParent) {
        String vertexStyle = "rounded=1;fontSize=15;fontColor=#000000;fillColor=#eeeeee;strokeColor=#000000";
        List<Object> vertices = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            Article article = articles.get(i);
            String text = article.getTitle().replaceAll("((?:\\S+\\s+){4}\\S+[^\\S\\n])", "$1\n")
                    + "\n" + article.getDate();
            vertices.add(graph.insertVertex(defaultParent, null, text,
                    10 + 320*i, 10 + (abs((2 - i)%3))*70, 300, 110, vertexStyle));
        }

        for(int i = 5; i < 7; i++) {
            Article article = articles.get(i);
            String text = article.getTitle().replaceAll("((?:\\S+\\s+){4}\\S+[^\\S\\n])", "$1\n")
                    + "\n" + article.getDate();
            vertices.add(graph.insertVertex(defaultParent, null, text,
                    1320 + ((i - 1)%2)*100, 330 + (i%5)*200, 300, 110, vertexStyle));
        }

        for(int i = 7; i < 12; i++) {
            Article article = articles.get(i);
            String text = article.getTitle().replaceAll("((?:\\S+\\s+){4}\\S+[^\\S\\n])", "$1\n")
                    + "\n" + article.getDate();
            vertices.add(graph.insertVertex(defaultParent, null, text,
                    1280 - 320*(i%7), 850 - (abs((9 - i)%3))*70, 300, 110, vertexStyle));
        }

        for(int i = 12; i < articles.size(); i++) {
            Article article = articles.get(i);
            String text = article.getTitle().replaceAll("((?:\\S+\\s+){4}\\S+[^\\S\\n])", "$1\n")
                    + "\n" + article.getDate();
            vertices.add(graph.insertVertex(defaultParent, null, text,
                    10 + (i%2)*100, 530 - (i%12)*200, 300, 110, vertexStyle));
        }

        return vertices;
    }

    private void setupEdges(Map<Object, Map<Object, Integer>> similaritiesMap, mxGraph graph, Object defaultParent) {
        String edgeStyle = "strokeWidth=3;opacity=30";
        List<String> gradient = Arrays.asList("#0E812E", "#386B28", "#625623", "#8C401E", "#B62B19", "#E11614");

        for(Object vertex: similaritiesMap.keySet()) {
            for(Map.Entry<Object, Integer> edgeData: similaritiesMap.get(vertex).entrySet()) {
                graph.insertEdge(defaultParent, null, null, vertex, edgeData.getKey(),
                        edgeStyle + ";strokeColor=" + gradient.get(edgeData.getValue()));
            }
        }
    }
}
