package main.java.hashing_without_knowing_how_to_hash.config;

import de.uni_leipzig.dbs.pprl.primat.common.model.NamedRecordSchemaConfiguration;
import de.uni_leipzig.dbs.pprl.primat.common.model.attributes.NonQidAttributeType;
import de.uni_leipzig.dbs.pprl.primat.common.model.attributes.QidAttributeType;
import localhost.hashing_without_knowing_how_to_hash.constants.CharacterSets;
import localhost.hashing_without_knowing_how_to_hash.constants.ProjectPaths;
import localhost.hashing_without_knowing_how_to_hash.model.formula.FormulaOptimisationStrategy;
import localhost.hashing_without_knowing_how_to_hash.model.formula.NegationOptimisationStrategy;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithmType;
import localhost.hashing_without_knowing_how_to_hash.util.SetUnionUtil;

import java.util.HashSet;
import java.util.Set;

public interface ModelMetaData {
    String PATH= ProjectPaths.RESOURCES_PATH+"/dataset.csv";
    NamedRecordSchemaConfiguration RECORD_SCHEMA_CONFIGURATION =new NamedRecordSchemaConfiguration.Builder()
            .add(0, NonQidAttributeType.ID)
            .add(1, NonQidAttributeType.PARTY)
            .add(2, QidAttributeType.STRING, "GN")
            .add(3, QidAttributeType.STRING, "LN")
            .add(4, QidAttributeType.NUMERIC, "D")
            .add(5, QidAttributeType.NUMERIC, "M")
            .add(6, QidAttributeType.NUMERIC, "Y")
            .build();
    int Q =2;
    boolean WITH_PADDING=true;
    Set<Character> CHARACTERS =new SetUnionUtil<Character>()
            .union(
                    WITH_PADDING ? CharacterSets.EMPTY_ELEMENT : new HashSet<>(),
                    CharacterSets.NUMERICAL);/*,
                    CharacterSets.UPPERCASE_LETTERS,
                    CharacterSets.PUNCTUATION);*/

    HashingAlgorithmType ALGORITHM_TYPE =HashingAlgorithmType.SHA256_SHORTENED_12BIT;
    FormulaOptimisationStrategy OPTIMISATION_STRATEGY=new NegationOptimisationStrategy();
}
