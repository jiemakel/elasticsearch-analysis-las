# elasticsearch-analysis-las

An Elasticsearch plugin for morphological analysis / lemmatization using [LAS](http://github.com/jiemakel/las).

The most probable use for this is to index lemmas for Finnish text so queries will not need to specify the exact inflected form as it appears in the indexed documents (e.g. a query for `kauppa` will match also `kaupassa`, `kaupan`, `kauppoja`, `kaupoilla` [etc](https://www.ling.helsinki.fi/~fkarlsso/genkau2.html).)
 
To install the plugin, run `bin/elasticsearch-plugin install https://github.com/jiemakel/elasticsearch-analysis-las/releases/download/v1.0/elasticsearch-analysis-las-1.0-plugin.zip` (or a smaller variant from the [releases page](https://github.com/jiemakel/elasticsearch-analysis-las/releases), see https://github.com/jiemakel/las#installation-and-running for a discussion on the variants).

To use it, configure your index or particular fields to use the `lemma_las`analyzer. 

## Testing
 
```
curl -X POST 'http://localhost:9200/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "analyzer": "las_lemma",
  "text": "Juoksin kuin hullu."
}'
```

should produce:
```
{
  "tokens" : [
    {
      "token" : "juoksin",
      "start_offset" : 0,
      "end_offset" : 7,
      "type" : "word",
      "position" : 0
    },
    {
      "token" : "juosta",
      "start_offset" : 0,
      "end_offset" : 7,
      "type" : "word",
      "position" : 0
    },
    {
      "token" : "kuin",
      "start_offset" : 8,
      "end_offset" : 12,
      "type" : "word",
      "position" : 1
    },
    {
      "token" : "hullu",
      "start_offset" : 13,
      "end_offset" : 18,
      "type" : "word",
      "position" : 2
    }
  ]
}
```

## Configuration

For fine-tuning, the lemma analyzer accepts the following configuration options:
 * `locale`: the language/locale to use for lemmatisation (default: `"fi"`)
 * `lowercase`: include a lowercasing filter in the analyzer (default: `true`)
 * `include_original_words`: include the original words in the index in addition to the lemmas (guarding against when the lemmatizer makes a wrong call for example. Default: `true`) 
 * `include_nonprimary_lemmas`: include not only the automatically deduced best guess lemmas but also all other possible lemmas as output tokens (e.g. the word `kuin` will produce not only `kuin` but also `kuu`, `kuti` and `kui`. Default: `false`)
 * `guess_lemmas_for_unknown_words`: utilise an algorithm to guess the lemmas for out of vocabulary words (default: `true`. If `false`, the original form of the word is produced)
 * `include_punctuation`:  Include punctuation (e.g. `,.:?`) as tokens in the output (default: `false`)
 * `include_multiple_copies`: sometimes, the analyzer can produce the same lemma multiple times. So for example `kuin` can be `kuin` as an adverbial, `kuin` as a comparative and `kuin` as a general conjunction). If true, these multiple copies of the same word are returned (possibly useful for e.g. weighting in conjunction with including all lemmas, default: `false`) 
 * `ocr_corr_max_edit_distance`: for text that arises from an OCR process, try to overcome OCR errors by searching for vocabulary words with at most this number of corrections (default: `0`). This can be used to e.g. lemmatize `Leh>tim»ehen` to `lehtimies`.
 * `analysis_depth`: `0-1`, `0`=apply only rule-based, and not machine learned best lemma guessing (default: `1`)
 
Example:
```
curl -X PUT "localhost:9200/my_index" -H 'Content-Type: application/json' -d'
{
 "settings": {
    "analysis": {
      "analyzer": {
        "all_lemmas": {
          "type": "las_lemma",
          "include_nonprimary_lemmas": true
        }
      }
    }
  },
  "mappings": {
    "_doc": {
      "properties": {
        "text": { 
          "type": "text",
          "analyzer": "las_lemma",
          "fields": {
            "all_lemmas": {
              "type": "text",
              "analyzer": "all_lemmas"
            }
          }
        }
      }
    }
  }
}
'
curl -X GET "localhost:9200/my_index/_analyze?pretty" -H 'Content-Type: application/json' -d'
{
  "field": "text",
  "text": "Juoksin kuin hullu."
}
'
curl -X GET "localhost:9200/my_index/_analyze?pretty" -H 'Content-Type: application/json' -d'
{
  "field": "text.all_lemmas",
  "text": "Juoksin kuin hullu."
}
'
```