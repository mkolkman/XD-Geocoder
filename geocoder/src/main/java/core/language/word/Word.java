package core.language.word;

import core.language.pos.PosTag;
import core.learning.label.Label;

import java.io.Serializable;

public class Word implements Serializable {

    private PosTag posTag = PosTag.UNKNOWN;
    private int start;
    private int end;
    private String text;

    private Label label;

    public Word() {}

    public Word(String text) {
        this.text = text;
    }

    public Word(int start, int end, String text) {
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public Word(String text, PosTag posTag) {
        this.text = text;
        this.posTag = posTag;
    }

    public Word(int start, int end, String text, Label label) {
        this.start = start;
        this.end = end;
        this.text = text;
        this.label = label;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public PosTag getPosTag() {
        return posTag;
    }

    public void setPosTag(PosTag posTag) {
        this.posTag = posTag;
    }

    public String toString() {
        return text;
    }
}
