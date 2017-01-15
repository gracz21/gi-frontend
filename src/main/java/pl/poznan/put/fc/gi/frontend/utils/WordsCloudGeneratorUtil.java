package pl.poznan.put.fc.gi.frontend.utils;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.wordstart.CenterWordStart;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public class WordsCloudGeneratorUtil {
    private static WordsCloudGeneratorUtil instance;
    private List<WordFrequency> wordFrequencies;

    private WordsCloudGeneratorUtil() throws IOException {
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        wordFrequencies = frequencyAnalyzer.load(ClassLoader.class.getResourceAsStream("/showcase/ExampleData.txt"));
    }

    public static WordsCloudGeneratorUtil getInstance() throws IOException {
        if(instance == null) {
            instance = new WordsCloudGeneratorUtil();
        }

        return instance;
    }

    public Image generateWordsCloudImage(int wordsCount) throws IOException {
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
        wordCloud.setKumoFont(new KumoFont(ClassLoader.class.getResourceAsStream("/showcase/Roboto-Regular.ttf")));
        wordCloud.setFontScalar(new LinearFontScalar(10, 100 + (15 - wordsCount)*5));
        wordCloud.build(wordFrequencies.subList(0, wordsCount));
        return SwingFXUtils.toFXImage(wordCloud.getBufferedImage(), null);
    }
}