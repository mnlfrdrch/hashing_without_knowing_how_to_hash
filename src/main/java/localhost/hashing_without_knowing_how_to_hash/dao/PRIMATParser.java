package localhost.hashing_without_knowing_how_to_hash.dao;

import de.uni_leipzig.dbs.pprl.primat.common.extraction.FeatureExtractor;
import de.uni_leipzig.dbs.pprl.primat.common.extraction.qgram.BigramExtractor;
import de.uni_leipzig.dbs.pprl.primat.common.extraction.qgram.QGramExtractor;
import de.uni_leipzig.dbs.pprl.primat.common.extraction.qgram.TrigramExtractor;
import de.uni_leipzig.dbs.pprl.primat.common.extraction.qgram.UnigramExtractor;
import de.uni_leipzig.dbs.pprl.primat.common.model.NamedRecordSchemaConfiguration;
import de.uni_leipzig.dbs.pprl.primat.common.model.attributes.QidAttribute;
import de.uni_leipzig.dbs.pprl.primat.common.utils.DatasetReader;
import de.uni_leipzig.dbs.pprl.primat.common.model.Record;
import localhost.hashing_without_knowing_how_to_hash.dto.parser.QGramRecordDto;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Accesses csv file which contains the records that should be encoded
 * Uses the 'PRIMAT' toolkit to get the job done
 * Provides setting options for the q-grams to be used
 * Extracts the required q-grams record wise
 * Collects q-grams of each records and computes union
 * This way all necessary (q-gram, hash-value_secret) pairs which are required to encode all records in the file can be evaluated in first place
 */
public class PRIMATParser {

    private final String pathToCsv;
    private final NamedRecordSchemaConfiguration recordSchema;
    private final FeatureExtractor featureExtractor;
    @Getter
    private final Set<QGramRecordDto> qGramsOfRecords;

    public PRIMATParser(String pathToCsv, NamedRecordSchemaConfiguration recordSchema, int qOfQGram, boolean isWithPadding){
        this.pathToCsv=pathToCsv;
        this.featureExtractor= instantiateFeatureExtractor(qOfQGram,isWithPadding);
        this.recordSchema = recordSchema;
        qGramsOfRecords =new HashSet<>();
    }

    public PRIMATParser(File csvFile, NamedRecordSchemaConfiguration recordSchema, int qOfQGram, boolean isWithPadding){
        this.pathToCsv=csvFile.getAbsolutePath();
        this.featureExtractor= instantiateFeatureExtractor(qOfQGram,isWithPadding);
        this.recordSchema = recordSchema;
        qGramsOfRecords =new HashSet<>();
    }

    /**
     * Factory method for the feature extractor, which specifies the type of q-gram
     * Allows unigram, bigrams, trigrams and 4-grams each with or without padding
     * This way type of q-gram can be set flexibly even within runtime of the sofware
     * @param qOfQGram 1,2,3 or 4 for (h), (h,e), (h,e,l) or (h,e,l,l)
     * @param isWithPadding if *true* q-grams look like (#,#,h),(#,h,e),... and if *false* they look like (h,e,l),(e,l,l),...
     * @return feature extractor instance which determines the 'shape' of parsed q-grams
     */
    public FeatureExtractor instantiateFeatureExtractor(int qOfQGram, boolean isWithPadding){
        FeatureExtractor featureExtractor=null;
        switch (qOfQGram) {
            case 1 -> featureExtractor = new UnigramExtractor(isWithPadding);
            case 2 -> featureExtractor = new BigramExtractor(isWithPadding);
            case 3 -> featureExtractor = new TrigramExtractor(isWithPadding);
            default -> {
                int qBetween1And4 = (Math.abs(qOfQGram) % 4) + 1;
                featureExtractor = new QGramExtractor(qBetween1And4, isWithPadding);
            }
        }
        return featureExtractor;
    }

    /**
     * Reads records from specified file and parses them, so they are accessible from variables
     */
    public void loadRecords(){
        DatasetReader reader=new DatasetReader(pathToCsv, recordSchema);
        List<Record> records=null;
        try {
            records = reader.read();
        }
        catch (IOException ioException){
            records=new ArrayList<>();
            ioException.printStackTrace();
        }

        for(Record record: records){
            QGramRecordDto ngRecord= convertRecordToQGramRecordDto(record);
            ngRecord.preprocess();
            qGramsOfRecords.add(ngRecord);
        }
    }

    /**
     * Compute all the q-grams specified by constructor for given record from csv file
     * @param record the q-grams should be extracted from
     * @return Set of all q-grams
     */
    private Set<String> extractQGramsFromRecord(Record record){
        Set<String> qGrams=new HashSet<>();

        for(QidAttribute attribute:record.getAttributes())
        {
            qGrams.addAll(featureExtractor.extract(attribute));
        }
        return qGrams;
    }

    /**
     * Extracts required data from Record object to create QGramRecordDto object from it
     * Custom class QGramRecordDto is more suited to store and manipulate data within this software
     * @param record
     * @return
     */
    private QGramRecordDto convertRecordToQGramRecordDto(Record record){
        String id=record.getId();
        Set<String> qgrams=extractQGramsFromRecord(record);
        return new QGramRecordDto(id, qgrams);
    }

    /**
     * Union of q-grams of each record
     * All q-grams that all record within the file collectively contain
     * Is required to determine which (q-gram, hash_value_secret) pairs need to be evaluated in order to encode all records of the csv file
     * @return Set of all q-grams
     */
    public Set<String> collectRequiredQGrams(){
        Set<String> requiredQGrams=new HashSet<>();

        for(QGramRecordDto qGramRecordDto : qGramsOfRecords){
            requiredQGrams.addAll(qGramRecordDto.getQgrams());
        }

        return requiredQGrams;
    }

}