package pl.poznan.put.fc.gi.frontend.utils;

import com.kennycason.kumo.WordFrequency;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            "LIMIT 15";
    private static final String wordsYearQuery = "SELECT term, SUM(count*weight) AS val " +
            "FROM articles " +
            "JOIN terms ON articles.id = terms.article " +
            "JOIN weights ON weights.type = terms.type " +
            "WHERE date = ? " +
            "GROUP BY term " +
            "ORDER BY val DESC " +
            "LIMIT ?";
    private static final String yearsWithArticlesQuery = "SELECT DISTINCT date FROM articles ORDER BY date";
    private static final String allArticlesQuery = "SELECT COUNT(*) FROM articles ORDER BY date";
    private static final String yearArticlesQuery = "SELECT COUNT(*) FROM articles WHERE date = ? ORDER BY date";

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

    public static List<WordFrequency> getWordsFrequencyListFromYear(int numOfWords, int year) {
        List<WordFrequency> wordFrequencies = null;

        try {
            Connection connection = setupConnection();
            PreparedStatement statement = connection.prepareStatement(wordsYearQuery);
            statement.setInt(1, year);
            statement.setInt(2, numOfWords);
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

    public static int getAllArticlesCount() {
        int result = 0;

        try {
            Connection connection = setupConnection();
            PreparedStatement statement = connection.prepareStatement(allArticlesQuery);
            result = executeArticlesCountStatement(statement);
            connection.close();
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static int getYearArticlesCount(int year) {
        int result = 0;

        try {
            Connection connection = setupConnection();
            PreparedStatement statement = connection.prepareStatement(yearArticlesQuery);
            statement.setInt(1, year);
            result = executeArticlesCountStatement(statement);
            connection.close();
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
        }

        return result;
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

    private static int executeArticlesCountStatement(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.executeQuery();
        return rs.getInt(1);
    }
}
