package org.elasticsearch.index.analysis;

import fi.seco.lucene.MorphologicalAnalyzer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LASAnalysisAnalyzerProvider extends AbstractIndexAnalyzerProvider<MorphologicalAnalyzer> {

    public static final String NAME = "las_analysis";

    private final MorphologicalAnalyzer analyzer;

    @Inject
    @SuppressWarnings("unused")
    public LASAnalysisAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                       Settings settings) {
        super(indexSettings, name, settings);
        Locale locale = new Locale(settings.get("locale","fi"));
        List<String> inflections = settings.getAsList("inflections_to_produce",Collections.emptyList());
        boolean segmentBaseform = settings.getAsBoolean("produce_segmentation_of_baseforms",false);
        boolean guessUnknown = settings.getAsBoolean("guess_analyses_for_unknown_words", true);
        boolean segmentUnknown = settings.getAsBoolean("produce_segmentation_for_unknown_words",false);
        int maxEditDistance = settings.getAsInt("ocr_corr_max_edit_distance",0);
        int depth = settings.getAsInt("analysis_depth",1);
        analyzer = new MorphologicalAnalyzer(locale,inflections,segmentBaseform,guessUnknown,segmentUnknown,maxEditDistance,depth);
    }

    @Override
    public MorphologicalAnalyzer get() {
        return this.analyzer;
    }
}
