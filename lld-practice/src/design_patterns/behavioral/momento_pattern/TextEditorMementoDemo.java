package design_patterns.behavioral.momento_pattern;

import java.util.Stack;

class TextEditorMemento {
    private final String state;
    private final int cursorPosition;

    public TextEditorMemento(String state, int cursorPosition){
        this.state = state;
        this.cursorPosition = cursorPosition;
    }

    public String getState() {
        return state;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }
}

class TextEditor {
    private String content = "";
    private int cursorPosition = 0;

    public void type(String newText) {
        content += newText;
        System.out.println("Typed: \"" + newText + "\"");
        cursorPosition = content.length();
    }

    public String getContent() {
        return content;
    }

    public TextEditorMemento save() {
        System.out.println("Saving state: \"" + content + "\"");
        return new TextEditorMemento(content, cursorPosition);
    }

    public void restore(TextEditorMemento memento) {
        content = memento.getState();
        System.out.println("Restored state to : \"" + content + "\"");
        cursorPosition = memento.getCursorPosition();
    }
}

class TextEditorUndoManager {
    private final Stack<TextEditorMemento> history = new Stack<>();

    public void save(TextEditor editor) {
        history.push(editor.save());
    }

    public void undo(TextEditor editor) {
        if (!history.isEmpty()) {
            editor.restore(history.pop());
        } else {
            System.out.println("Nothing to undo.");
        }
    }

    public int historySize() {
        return history.size();
    }
}

public class TextEditorMementoDemo {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        TextEditorUndoManager undoManager = new TextEditorUndoManager();

        editor.type("Hello");
        undoManager.save(editor);

        editor.type("World");
        undoManager.save(editor);

        editor.type("!");
        System.out.println("Current: " + editor.getContent());

        System.out.println("\n---- Undo 1 ------");
        undoManager.undo(editor);
        System.out.println("Content: " + editor.getContent());

        System.out.println("\n---- Undo 2 ------");
        undoManager.undo(editor);
        System.out.println("Content: " + editor.getContent());

        System.out.println("\n---- Undo 3 ------");
        undoManager.undo(editor);
    }
}
