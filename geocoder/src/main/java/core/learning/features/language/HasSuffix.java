package core.learning.features.language;

import core.learning.features.Feature;
import core.language.word.Word;

public class HasSuffix extends Feature {

    private final String suffix;

    public HasSuffix(Word word, String suffix) {
        this.suffix = suffix;
        this.value = (float) (word.getText().endsWith(suffix) ? 1.0 : 0.0);
    }

    @Override
    public String getName() {
        return "HasSuffix_" + suffix;
    }
}
