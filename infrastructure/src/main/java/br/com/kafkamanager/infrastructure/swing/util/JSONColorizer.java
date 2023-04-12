package br.com.kafkamanager.infrastructure.swing.util;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class JSONColorizer {

    private static final Color DEFAULT_JSON_KEY_COLOR = new Color(0, 188, 18);

    private final JTextPane editorPane;

    private final StyledDocument document;

    private final StyleContext styleContext = StyleContext.getDefaultStyleContext();
    private final AttributeSet jsonBtacketsAttributeSet;
    private final AttributeSet normalAttributeSet;
    private final AttributeSet jsonKeyAttributeSet;

    private final Highlighter highlighter;
    private final Highlighter.HighlightPainter painter;

    private Pattern bracketsPattern = Pattern.compile("(?<!\")(?!\")(\\{|\\}|\\[|\\])");
    private Pattern jsonKeyPattern = Pattern.compile("\"([^\"]*)\":");

    private Object last;

    public JSONColorizer(JTextPane editorPane, Color jsonKeyColor, Color bracketsColor,
        Pattern bracketsPattern, Pattern jsonKeyPattern) {
        this.bracketsPattern = bracketsPattern;
        this.jsonKeyPattern = jsonKeyPattern;
        this.editorPane = editorPane;
        document = editorPane.getStyledDocument();

        Color currentColor = document.getForeground(editorPane.getInputAttributes());

        jsonBtacketsAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(),
            StyleConstants.Foreground, bracketsColor);
        normalAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(),
            StyleConstants.Foreground, currentColor);
        jsonKeyAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(),
            StyleConstants.Foreground, jsonKeyColor);

        highlighter = editorPane.getHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
    }

    public JSONColorizer(JTextPane editorPane, Color jsonKeyColor, Color bracketsColor) {
        this.editorPane = editorPane;
        document = editorPane.getStyledDocument();

        Color currentColor = document.getForeground(editorPane.getInputAttributes());

        jsonBtacketsAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(),
            StyleConstants.Foreground, bracketsColor);
        normalAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(),
            StyleConstants.Foreground, currentColor);
        jsonKeyAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(),
            StyleConstants.Foreground, jsonKeyColor);

        highlighter = editorPane.getHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
    }

    public JSONColorizer(JTextPane editorPane) {
        this(editorPane, DEFAULT_JSON_KEY_COLOR, Color.RED);
    }

    public JSONColorizer(JTextPane editorPane, Pattern bracketsPattern, Pattern jsonKeyPattern) {
        this(editorPane, DEFAULT_JSON_KEY_COLOR, Color.RED, bracketsPattern, jsonKeyPattern);
    }

    public void colorize() {
        document.setCharacterAttributes(0, editorPane.getText().length(), normalAttributeSet, true);
        Matcher matcher = bracketsPattern.matcher(editorPane.getText());
        while (matcher.find()) {
            document.setCharacterAttributes(matcher.start(),
                matcher.end() - matcher.start(), jsonBtacketsAttributeSet, false);
        }

        Matcher keysMatcher = jsonKeyPattern.matcher(editorPane.getText());
        while (keysMatcher.find()) {
            document.setCharacterAttributes(keysMatcher.start(),
                keysMatcher.end() - keysMatcher.start(),
                jsonKeyAttributeSet, false);
        }
    }

    public void clearErrorHighLight() {
        if (last != null) {
            highlighter.removeHighlight(last);
            last = null;
        }
    }

    public void highlightError(int line, int character) {
        Element root = document.getDefaultRootElement();
        int startOfLineOffset = root.getElement(line - 1).getStartOffset() + (character - 1);
        try {
            if (last != null) {
                highlighter.removeHighlight(last);
                last = null;
            }
            last = highlighter.addHighlight(startOfLineOffset - 1, startOfLineOffset, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
