package e.edit;

import java.awt.event.*;
import e.util.*;

/**
The ETextArea action that inserts a newline and performs auto-indentation.
*/
public class InsertNewlineAction extends ETextAction {
    public static final String ACTION_NAME = "insert-newline-and-auto-indent";

    public InsertNewlineAction() {
        super(ACTION_NAME);
    }

    public void actionPerformed(ActionEvent e) {
        // FIXME
        /*
        ETextArea target = getTextArea();
        CompoundEdit entireEdit = new CompoundEdit();
        target.getUndoManager().addEdit(entireEdit);
        try {
            final int position = target.getCaretPosition();
            
            // Should we try to insert matching brace pairs?
            String line = target.getLineTextAtOffset(position);
            if (target.getIndenter().isElectric('}') && position > 0 && target.charSequence().charAt(position - 1) == '{' && hasUnbalancedBraces(target.getText())) {
                insertMatchingBrace(target);
            } else if (line.endsWith("/*") || line.endsWith("/**")) {
                insertMatchingCloseComment(target);
            } else {
                target.replaceSelection("\n");
                target.autoIndent();
            }
        } catch (BadLocationException ex) {
            Log.warn("Problem inserting newline.", ex);
        } finally {
            entireEdit.end();
        }
        */
    }
    
    /*
    public void insertMatchingBrace(ETextArea target) {
        try {
            ETextWindow textWindow = (ETextWindow) SwingUtilities.getAncestorOfClass(ETextWindow.class, target);
            boolean mightNeedSemicolon = textWindow != null && textWindow.isCPlusPlus();
            
            final int position = target.getCaretPosition();
            String line = target.getLineTextAtOffset(position);
            String whitespace = target.getIndentationOfLineAtOffset(position);
            String prefix = "\n" + whitespace + target.getIndentationString();
            String suffix = "\n" + whitespace + "}";
            if (mightNeedSemicolon && line.matches(".*\\b(class|enum|struct|union)\\b.*")) {
                // These C constructs need a semicolon after the closing brace.
                suffix += ";";
            }
            target.getDocument().insertString(position, prefix + suffix, null);
            target.setCaretPosition(position + prefix.length());
        } catch (BadLocationException ex) {
            Log.warn("Problem inserting brace pair.", ex);
        }
    }
    */
    
    public void insertMatchingCloseComment(ETextArea target) {
        // FIXME - selection?
        final int position = target.getSelectionStart();
        String line = target.getLineTextAtOffset(position);
        String whitespace = target.getIndentationOfLineAtOffset(position);
        String prefix = "\n" + whitespace + " * ";
        String suffix = "\n" + whitespace + " */";
        target.insert(prefix + suffix);
        // FIXME: this is automatic in PTextArea, no?
        //target.setCaretPosition(position + prefix.length());
    }
    
    public boolean hasUnbalancedBraces(final String text) {
        return (calculateBraceNesting(text) != 0);
    }

    /**
     * Returns how many more opening braces there are than closing
     * braces in the given String. Returns 0 if there are equal
     * numbers of opening and closing braces; a negative number if
     * there are more closing braces than opening braces.
     */
    public int calculateBraceNesting(final String initialText) {
        String text = initialText.replaceAll("\\\\.", "_"); // Remove escaped characters.
        text = text.replaceAll("'.'", "_"); // Remove character literals.
        text = text.replaceAll("\"([^\\n]*?)\"", "_"); // Remove string literals.
        text = text.replaceAll("/\\*(?s).*?\\*/", "_"); // Remove C comments.
        text = text.replaceAll("//[^\\n]*", "_"); // Remove C++ comments.
        int braceNesting = 0;
        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch == '{') {
                ++braceNesting;
            } else if (ch == '}') {
                --braceNesting;
            }
        }
        return braceNesting;
    }

    public static void main(String[] filenames) {
        InsertNewlineAction action = new InsertNewlineAction();
        for (int i = 0; i < filenames.length; ++i) {
            String filename = filenames[i];
            String text = StringUtilities.readFile(filename);
            int nesting = action.calculateBraceNesting(text);
            System.err.println(filename + " nesting = " + nesting);
        }
    }
}
