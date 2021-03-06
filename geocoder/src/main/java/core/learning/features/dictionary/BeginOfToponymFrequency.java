package core.learning.features.dictionary;

import core.language.dictionary.Dictionary;
import core.language.word.Word;
import core.learning.features.Feature;

public class BeginOfToponymFrequency extends Feature {

    public BeginOfToponymFrequency(Word word, Dictionary dictionary) {
        this.value = dictionary.getBeginOfToponymCount(word.getText());
    }

    @Override
    public String getName() {
        return "BeginOfToponymFrequency";
    }
}
