package org.elasticsearch.index.analysis;

import fi.seco.lucene.LemmaTokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

import java.util.Locale;

public class LASLemmaTokenizerProvider extends AbstractTokenizerFactory {

    public static final String NAME = "las_lemma";

    private final LemmaTokenizer tokenizer;

    @Inject
    @SuppressWarnings("unused")
    public LASLemmaTokenizerProvider(IndexSettings indexSettings, Environment env, String name,
                                     Settings settings) {
        super(indexSettings, name, settings);
        Locale locale = new Locale(settings.get("locale", "fi"));
        boolean originalWords = settings.getAsBoolean("include_original_words",true);
        boolean allLemmas = settings.getAsBoolean("include_nonprimary_lemmas",false);
        boolean guessUnknown = settings.getAsBoolean("guess_lemmas_for_unknown_words",true);
        int maxEditDistance = settings.getAsInt("ocr_corr_max_edit_distance",0);
        int depth = settings.getAsInt("analysis_depth",1);
        boolean nonWords = settings.getAsBoolean("include_punctuation", false);
        boolean unique = !settings.getAsBoolean("include_multiple_copies", false);
        tokenizer = new LemmaTokenizer(locale,originalWords,allLemmas,guessUnknown,maxEditDistance,depth,nonWords, unique);
    }

    @Override
    public LemmaTokenizer create() {
        return this.tokenizer;
    }
}
