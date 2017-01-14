package pl.poznan.put.fc.gi.frontend.utils;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kamil Walkowiak
 */
public abstract class WordsCloudGeneratorUtil {
    public static Image generateWordsCloudImage(int wordsCount) {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(wordsCount);

        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(Arrays.asList("Buenos Aires", "Buenos Aires", "Córdoba", "La Plata"));
        final Dimension dimension = new Dimension(700, 700);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        return SwingFXUtils.toFXImage(wordCloud.getBufferedImage(), null);
    }
}
