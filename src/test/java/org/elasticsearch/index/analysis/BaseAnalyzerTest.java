package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.assertj.core.api.Assertions;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.plugin.analysis.las.LASAnalysisPlugin;
import org.elasticsearch.test.ESTestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAnalyzerTest extends ESTestCase {

    public static void testAnalyzer(String analyzerName, Settings settings, String source, String... expectedTokens) throws IOException {
        testAnalyzer(analyzerName, settings, source, true, expectedTokens);
    }

    public static void testAnalyzerIgnoringPosition(String analyzerName, Settings settings, String source, String... expectedTokens)
            throws IOException {

        testAnalyzer(analyzerName, settings, source, false, expectedTokens);
    }

    private static void testAnalyzer(String analyzerName, Settings settings, String source, boolean checkPosition, String... expectedTokens) throws IOException {
        Analyzer analyzer = createTestAnalyzer(analyzerName, settings);
        List<String> actualTokens = runAnalysis(source, analyzer, checkPosition);

        // Due to poor imports of ESTestCase...
        Assertions.assertThat(actualTokens).containsExactly(expectedTokens);
    }

    private static Analyzer createTestAnalyzer(String analyzerName, Settings settings) throws IOException {
        TestAnalysis analysis = createTestAnalysis(new Index("test", "_na_"),
                                                   settings,
                                                   new LASAnalysisPlugin());
        return analysis.indexAnalyzers.get(analyzerName).analyzer();
    }

    private static List<String> runAnalysis(String source, Analyzer analyzer, boolean checkPosition) throws IOException {
        TokenStream ts = analyzer.tokenStream("test", source);
        CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
        PositionIncrementAttribute pos = ts.addAttribute(PositionIncrementAttribute.class);
        ts.reset();

        List<String> actualTokens = new ArrayList<>();
        boolean first = true;

        while (ts.incrementToken()) {
            if (!first && checkPosition) {
                Assertions.assertThat(pos.getPositionIncrement()).isEqualTo(1);
            }
            first = false;

            actualTokens.add(term.toString());
        }

        return actualTokens;
    }
}
