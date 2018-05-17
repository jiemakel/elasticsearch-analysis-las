package org.elasticsearch.plugin.analysis.las;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;

public class LASAnalysisPlugin extends Plugin implements AnalysisPlugin {


    private static final Map<String,AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> analyzers = new HashMap<>();
    private static final Map<String,AnalysisProvider<TokenizerFactory>> tokenizers = new HashMap<>();

    static {
        analyzers.put(LASAnalysisAnalyzerProvider.NAME, LASAnalysisAnalyzerProvider::new);
        analyzers.put(LASLemmaAnalyzerProvider.NAME, LASLemmaAnalyzerProvider::new);
        tokenizers.put(LASAnalysisTokenizerProvider.NAME, LASAnalysisTokenizerProvider::new);
        tokenizers.put(LASLemmaTokenizerProvider.NAME, LASLemmaTokenizerProvider::new);
    }

    @Override
    public Map<String, AnalysisProvider<TokenizerFactory>> getTokenizers() {
        return tokenizers;
    }

    @Override
    public Map<String, AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        return analyzers;
    }
}
