package localhost.hashing_without_knowing_how_to_hash.constants;

import java.util.Set;

public interface CharacterSets {
    Set<Character> NUMERICAL = Set.of('0','1','2','3','4','5','6','7','8','9');
    Set<Character> UPPERCASE_LETTERS = Set.of('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
    Set<Character> EMPTY_ELEMENT = Set.of('#');
    Set<Character> HEXADECIMAL = Set.of('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f');
    Set<Character> PUNCTUATION = Set.of(' '/*space*/ , '\''/*apostroph*/ ,'-', '.',':','<');
}