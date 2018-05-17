package org.elasticsearch.index.analysis;

import fi.seco.lucene.LemmaAnalyzer;
import fi.seco.lucene.MorphologicalAnalyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LASLemmaAnalyzerProvider extends AbstractIndexAnalyzerProvider<LemmaAnalyzer> {

    public static final String NAME = "las_lemma";

    private final LemmaAnalyzer analyzer;

    @Inject
    @SuppressWarnings("unused")
    public LASLemmaAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                    Settings settings) {
        super(indexSettings, name, settings);
        Locale locale = new Locale(settings.get("locale", "fi"));
        boolean originalWords = settings.getAsBoolean("include_original_words",true);
        boolean allLemmas = settings.getAsBoolean("include_nonprimary_lemmas",false);
        boolean guessUnknown = settings.getAsBoolean("guess_lemmas_for_unknown_words",true);
        int maxEditDistance = settings.getAsInt("ocr_cor_max_edit_distance",0);
        int depth = settings.getAsInt("analysis_depth",1);
        boolean lowercase = settings.getAsBoolean("lowercase", true);
        analyzer = new LemmaAnalyzer(locale,originalWords,allLemmas,guessUnknown,maxEditDistance,depth,lowercase);
    }

    @Override
    public LemmaAnalyzer get() {
        return this.analyzer;
    }
}
