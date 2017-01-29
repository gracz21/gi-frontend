package pl.poznan.put.fc.gi.frontend.utils;

import com.kennycason.kumo.WordFrequency;
import org.apache.commons.lang.WordUtils;
import pl.poznan.put.fc.gi.frontend.models.Article;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kamil Walkowiak
 */
public abstract class DatabaseHandlerUtil {
    private static final String wordsSummaryQuery = "SELECT term, SUM(count*weight) AS val " +
            "FROM articles " +
            "JOIN terms ON articles.id = terms.article " +
            "JOIN weights ON weights.type = terms.type " +
            "GROUP BY term " +
            "ORDER BY val DESC " +
            "LIMIT 20";
    private static final String wordsYearQuery = "SELECT term, SUM(count*weight) AS val " +
            "FROM articles " +
            "JOIN terms ON articles.id = terms.article " +
            "JOIN weights ON weights.type = terms.type " +
            "WHERE date = ? " +
            "GROUP BY term " +
            "ORDER BY val DESC " +
            "LIMIT 20";
    private static final String yearsWithArticlesQuery = "SELECT DISTINCT date FROM articles ORDER BY date";
    private static final String allArticlesQuery = "SELECT id, title, authors, date FROM articles ORDER BY date";
    private static final String yearArticlesQuery = "SELECT id, title, authors, date " +
            "FROM articles WHERE date = ? ORDER BY date";
    private static final String similarityQuery = "SELECT article1, article2, (similarity - 0.4)*10 as similarity " +
            "FROM similarities WHERE similarity > 0.4 " +
            "ORDER BY article1, article2";

    public static List<WordFrequency> getSummaryWordsFrequencyList() {
        List<WordFrequency> wordFrequencies = null;

        try {
            Connection connection = setupConnection();
            PreparedStatement statement = connection.prepareStatement(wordsSummaryQuery);
            wordFrequencies = executeWordsFrequencyStatement(statement);
            connection.close();
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
        return wordFrequencies;
    }

    public static List<WordFrequency> getWordsFrequencyListFromYear(int year) {
        List<WordFrequency> wordFrequencies = null;

        try {
            Connection connection = setupConnection();
            PreparedStatement statement = connection.prepareStatement(wordsYearQuery);
            statement.setInt(1, year);
            wordFrequencies = executeWordsFrequencyStatement(statement);
            connection.close();
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
        return wordFrequencies;
    }

    public static List<Integer> getYearsWithArticles() {
        List<Integer> years = null;

        try {
            Connection connection = setupConnection();
            PreparedStatement statement = connection.prepareStatement(yearsWithArticlesQuery);
            ResultSet rs = statement.executeQuery();
            years = new ArrayList<>();
            while (rs.next()) {
                years.add(rs.getInt("date"));
            }
            connection.close();
        } catch (ClassNotFoundException | URISyntaxException | SQLException e) {
            e.printStackTrace();
        }

        return years;
    }

    public static List<Article> getAllArticles() {
        List<Article> articles = null;

        try {
            Connection connection = setupConnection();
            PreparedStatement statement = connection.prepareStatement(allArticlesQuery);
            articles = executeArticlesStatement(statement);
            connection.close();
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
        }

        return articles;
    }

    public static List<Article> getYearArticles(int year) {
        List<Article> articles = null;

        try {
            Connection connection = setupConnection();
            PreparedStatement statement = connection.prepareStatement(yearArticlesQuery);
            statement.setInt(1, year);
            articles = executeArticlesStatement(statement);
            connection.close();
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
        }

        return articles;
    }

    public static Map<Object, Map<Object, Integer>> getSimilaritiesMap(List<Object> vertices) {
        Map<Object, Map<Object, Integer>> similarityMap = null;

        try {
            Connection connection = setupConnection();
            PreparedStatement statement = connection.prepareStatement(similarityQuery);
            ResultSet rs = statement.executeQuery();
            similarityMap = new HashMap<>();
            int currentVertexIndex = 1;
            Map<Object, Integer> currentVertexSimilarityMap = new HashMap<>();
            while (rs.next()) {
                int article1 = rs.getInt("article1");
                int article2 = rs.getInt("article2");
                if(currentVertexIndex != article1) {
                    similarityMap.put(vertices.get(currentVertexIndex-1), currentVertexSimilarityMap);
                    currentVertexIndex = article1;
                    currentVertexSimilarityMap = new HashMap<>();
                }
                if(article2 > currentVertexIndex) {
                    currentVertexSimilarityMap.put(vertices.get(article2-1), rs.getInt("similarity"));
                }
            }
            connection.close();
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
        }

        return similarityMap;
    }

    private static Connection setupConnection() throws ClassNotFoundException, SQLException, URISyntaxException {
        String dbLocation =
                Paths.get(DatabaseHandlerUtil.class.getResource("/data/database.sqlite").toURI()).toString();
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
    }

    private static List<WordFrequency> executeWordsFrequencyStatement(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.executeQuery();
        List<WordFrequency> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new WordFrequency(rs.getString("term"), rs.getInt("val")));
        }

        return result;
    }

    private static List<Article> executeArticlesStatement(PreparedStatement statement) throws SQLException {
        List<Article> articles = new ArrayList<>();
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            title = title.substring(0, 1).toUpperCase() + title.substring(1);
            int date = rs.getInt("date");
            String authors = WordUtils.capitalize(rs.getString("authors"));
            articles.add(new Article(id, title, date, authors));
        }
        return articles;
    }
}
