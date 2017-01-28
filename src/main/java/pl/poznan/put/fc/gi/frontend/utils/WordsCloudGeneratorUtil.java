package pl.poznan.put.fc.gi.frontend.utils;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.wordstart.CenterWordStart;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public class WordsCloudGeneratorUtil {

    public static void main(String[] args) throws IOException {
        generateSummaryCloud();
        generateYearsClouds();
    }

    private static void generateSummaryCloud() throws IOException {
        List<WordFrequency> wordFrequencies = DatabaseHandlerUtil.getSummaryWordsFrequencyList();
        generateWordsCloudImage(wordFrequencies, "summary.png");
    }

    private static void generateYearsClouds() throws IOException {
        List<Integer> years = DatabaseHandlerUtil.getYearsWithArticles();
        for(int year: years) {
            List<WordFrequency> wordFrequencies = DatabaseHandlerUtil.getWordsFrequencyListFromYear(15, year);
            generateWordsCloudImage(wordFrequencies.subList(0, 5), year + "_mini.png");
            generateWordsCloudImage(wordFrequencies, year + ".png");
        }
    }

    private static void generateWordsCloudImage(List<WordFrequency> wordFrequencies, String filename) throws IOException {
        final Dimension dimension = new Dimension(750, 750);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(10);
        wordCloud.setWordStartStrategy(new CenterWordStart());
        wordCloud.setBackgroundColor(new Color(0xf4f4f4));
        wordCloud.setColorPalette(new ColorPalette(
                new Color(0xFA7E05),
                new Color(0xFA8F27),
                new Color(0xFAA149),
                new Color(0xFAB26B),
                new Color(0xFAC48E)
        ));
        wordCloud.setKumoFont(new KumoFont(ClassLoader.class.getResourceAsStream("/font/Roboto-Regular.ttf")));
        wordCloud.setFontScalar(new LinearFontScalar(10, 100 + (15 - wordFrequencies.size()) * 5));
        wordCloud.build(wordFrequencies.subList(0, wordFrequencies.size()));
        wordCloud.writeToFile("output/" + filename);
    }
}
