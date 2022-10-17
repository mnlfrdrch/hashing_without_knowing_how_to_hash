package localhost.hashing_without_knowing_how_to_hash.dao;

import de.uni_leipzig.dbs.pprl.primat.common.extraction.FeatureExtractor;
import de.uni_leipzig.dbs.pprl.primat.common.extraction.qgram.BigramExtractor;
import de.uni_leipzig.dbs.pprl.primat.common.extraction.qgram.QGramExtractor;
import de.uni_leipzig.dbs.pprl.primat.common.extraction.qgram.TrigramExtractor;
import de.uni_leipzig.dbs.pprl.primat.common.extraction.qgram.UnigramExtractor;
import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.constants.ProjectPaths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PRIMATParserTest {

    private PRIMATParser parser;

    private boolean WITH_PADDING=true;

    @BeforeEach
    public void setUp(){
        parser=new PRIMATParser(ModelMetaData.PATH, ModelMetaData.RECORD_SCHEMA_CONFIGURATION,2,true);
    }

    @AfterEach
    public void tearDown(){
        parser=null;
    }


    @Test
    public void testInstantiateFeatureExtractorShouldInstantiateUnigramExtractor(){
        //given
        int unigramExtractorQ=1;

        //when
        FeatureExtractor shouldBeUnigramExtractor=parser.instantiateFeatureExtractor(unigramExtractorQ, WITH_PADDING);

        //then
        assertInstanceOf(UnigramExtractor.class, shouldBeUnigramExtractor);
    }

    @Test
    public void testInstantiateFeatureExtractorShouldInstantiateBigramExtractor(){
        //given
        int bigramExtractorQ=2;

        //when
        FeatureExtractor shouldBeBigramExtractor=parser.instantiateFeatureExtractor(bigramExtractorQ, WITH_PADDING);

        //then
        assertInstanceOf(BigramExtractor.class, shouldBeBigramExtractor);
    }

    @Test
    public void testInstantiateFeatureExtractorShouldInstantiateTrigramExtractor(){
        //given
        int trigramExtractorQ=3;
        //when
        FeatureExtractor shouldBeTrigramExtractor=parser.instantiateFeatureExtractor(trigramExtractorQ, WITH_PADDING);

        //then
        assertInstanceOf(TrigramExtractor.class, shouldBeTrigramExtractor);
    }

    @Test
    public void testInstantiateFeatureExtractorShouldInstantiate4GramExtractor(){
        //given
        int qgramExtractorQ=4;

        //when
        FeatureExtractor shouldBeQgramExtractor=parser.instantiateFeatureExtractor(qgramExtractorQ, WITH_PADDING);

        //then
        assertInstanceOf(QGramExtractor.class, shouldBeQgramExtractor);
    }

    @Test
    public void testInstantiateFeatureExtractorShouldInstantiateQGramExtractorShouldHandleOverflowNumbers(){
        //given
        int overflowQ=5;

        //when
        FeatureExtractor shouldBeQGramExtractorEvenIfOverflow=parser.instantiateFeatureExtractor(overflowQ, WITH_PADDING);

        //then
        assertInstanceOf(QGramExtractor.class, shouldBeQGramExtractorEvenIfOverflow);

    }

    @Test
    public void testInstantiateFeatureExtractorShouldInstantiateQGramExtractorShouldHandleUnderflowNumbers(){
        //given
        int underflowQ=-1;

        //when
        FeatureExtractor shouldBeQGramExtractorEvenIfUnderflow=parser.instantiateFeatureExtractor(underflowQ, WITH_PADDING);

        //then
        assertInstanceOf(QGramExtractor.class, shouldBeQGramExtractorEvenIfUnderflow);
    }

    @Test
    public void testInstantiateFeatureExtractorShouldInstantiateQGramExtractorShouldHandleZero() {
        //given
        int zeroQ = 0;

        //when
        FeatureExtractor shouldBeQGramExtractorEvenIfZero = parser.instantiateFeatureExtractor(zeroQ, WITH_PADDING);

        //then
        assertInstanceOf(QGramExtractor.class, shouldBeQGramExtractorEvenIfZero);
    }


    @Test
    public void testLoadRecordsShouldBeAbleToLoadsSomeRecordsFromFile(){
        //given
        parser=new PRIMATParser(ModelMetaData.PATH,ModelMetaData.RECORD_SCHEMA_CONFIGURATION,3,true);

        //when
        parser.loadRecords();

        //then
        int recordListSize=parser.getQGramsOfRecords().size();

        assertTrue(recordListSize>0);
    }

    @Test
    public void testCollectRecordsShouldBeAbleToExtractThe115BigramsFromGivenFile(){
        //given
        parser=new PRIMATParser(ProjectPaths.RESOURCES_PATH+ "/dataset.csv", ModelMetaData.RECORD_SCHEMA_CONFIGURATION,2,true);
        parser.loadRecords();
        int expectedNumBigrams=115;

        //when
        int actualNumBigrams=parser.collectRequiredQGrams().size();

        //then
        assertEquals(expectedNumBigrams, actualNumBigrams);
    }

    @Test
    public void testAlternativeConstructorShouldAlsoBeAbleToExtractTheQGrams(){
        //given
        File file=new File(ProjectPaths.RESOURCES_PATH+ "/dataset.csv");

        //when
        parser=new PRIMATParser(file,ModelMetaData.RECORD_SCHEMA_CONFIGURATION,2,true);
        parser.loadRecords();

        //then
        assertTrue(parser.collectRequiredQGrams().size()>0);
    }

}