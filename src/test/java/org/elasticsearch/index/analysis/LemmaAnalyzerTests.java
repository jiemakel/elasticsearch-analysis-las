package org.elasticsearch.index.analysis;

import org.elasticsearch.common.settings.Settings;

public class LemmaAnalyzerTests extends BaseAnalyzerTest {

    /* Locale locale = new Locale(settings.get("locale", "fi"));
    boolean originalWords = settings.getAsBoolean("include_original_words",true);
    boolean allLemmas = settings.getAsBoolean("include_nonprimary_lemmas",false);
    boolean guessUnknown = settings.getAsBoolean("guess_lemmas_for_unknown_words",true);
    int maxEditDistance = settings.getAsInt("ocr_cor_max_edit_distance",0);
    int depth = settings.getAsInt("analysis_depth",1); */


    public void testLemmaAnalyzer() throws Exception {
        testAnalyzerIgnoringPosition(LASLemmaAnalyzerProvider.NAME, Settings.EMPTY,
                "Juoksin, läpi yön",
                "juoksin", "juosta", "läpi", "yön", "yö");
    }

    public void testOnlyLemmas() throws Exception {
        Settings settings = Settings.builder()
                .put("index.analysis.analyzer.test.type", LASLemmaAnalyzerProvider.NAME)
                .put("index.analysis.analyzer.test.include_original_words",false)
                .build();
        testAnalyzerIgnoringPosition("test", settings,
                "juoksin, läpi yön",
                "juosta", "läpi", "yö");
        testAnalyzerIgnoringPosition("test", settings,
                "olin kuin hullu",
                "olla", "kuin", "hullu");
    }

    public void testNoLowercasing() throws Exception {
        Settings settings = Settings.builder()
                .put("index.analysis.analyzer.test.type", LASLemmaAnalyzerProvider.NAME)
                .put("index.analysis.analyzer.test.lowercase",false)
                .build();
        testAnalyzerIgnoringPosition("test", settings,
                "Juoksin, läpi yön",
                "Juoksin", "juosta", "läpi", "yön", "yö");
    }

    public void testAllLemmas() throws Exception {
        Settings settings = Settings.builder()
                .put("index.analysis.analyzer.test.type", LASLemmaAnalyzerProvider.NAME)
                .put("index.analysis.analyzer.test.include_original_words",false)
                .put("index.analysis.analyzer.test.include_nonprimary_lemmas",true)
                .build();
        testAnalyzerIgnoringPosition("test", settings,
                "olin kuin hullu",
                "olla",
                "olka",
                "kuin",
                "kui",
                "kuu",
                "kuti",
                "hullu");
    }

}
