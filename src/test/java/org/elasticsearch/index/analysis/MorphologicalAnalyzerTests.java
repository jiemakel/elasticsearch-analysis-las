package org.elasticsearch.index.analysis;

import org.elasticsearch.common.settings.Settings;

public class MorphologicalAnalyzerTests extends BaseAnalyzerTest {

    public void testMorphologicalAnalyzer() throws Exception {
        Settings settings = Settings.builder()
                .put("settings.analysis.analyzer.test","fi")
                .put("analysis_depth", 3)
                .build();
/*        Locale locale = new Locale(settings.get("locale","fi"));
        List inflections = settings.getAsList("inflections_to_produce",Collections.EMPTY_LIST);
        boolean segmentBaseform = settings.getAsBoolean("produce_segmentation_of_baseforms",false);
        boolean guessUnknown = settings.getAsBoolean("guess_analyses_for_unknown_words", true);
        boolean segmentUnknown = settings.getAsBoolean("produce_segmentation_for_unknown_words",false);
        int maxEditDistance = settings.getAsInt("ocr_corr_max_edit_distance",0);
        int depth = settings.getAsInt("analysis_depth",1);*/


        testAnalyzerIgnoringPosition(LASAnalysisAnalyzerProvider.NAME, settings,
                "juoksin, läpi yön",
                "W=juoksin",
                "BFIRST_IN_SENTENCE=TRUE",
                "BL=juosta",
                "BVOICE=ACT",
                "BMOOD=INDV",
                "BPERS=SG1",
                "BUPOS=VERB",
                "BTENSE=PAST",
                "BFIRST_IN_SENTENCE=TRUE",
                "BL=juosta",
                "BVOICE=ACT",
                "BMOOD=INDV",
                "BPERS=SG1",
                "BUPOS=VERB",
                "BTENSE=PRESENT",
                "W=,",
                "BSEGMENT=,",
                "BSUBCAT=COMMA",
                "BL=,",
                "BUPOS=PUNCT",
                "OL=,",
                "OUPOS=SYM",
                "W=läpi",
                "BL=läpi",
                "BKAV=E",
                "BKTN=7",
                "BNUM=SG",
                "BUPOS=NOUN",
                "BCASE=NOM",
                "OL=läpi",
                "OUPOS=ADV",
                "W=yön",
                "BL=yö",
                "BKTN=19",
                "BNUM=SG",
                "BUPOS=NOUN",
                "BCASE=GEN");
    }


}
