package design_patterns.behavioral.strategy_pattern;

interface TextFormatter {
    String format(String text);
}

class UpperCaseFormatter implements TextFormatter {
    @Override
    public String format(String text) {
        return text.toUpperCase();
    }
}

class LowerCaseFormatter implements TextFormatter {
    @Override
    public String format(String text) {
        return text.toLowerCase();
    }
}

class TitleCaseFormatter implements TextFormatter {
    @Override
    public String format(String text) {
        String[] words = text.split("\\s+");
        StringBuilder ans = new StringBuilder();
        for(String word : words) {
            ans.append(String.valueOf(word.charAt(0)).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
        }
        String answer = ans.toString();
        return answer.trim();
    }
}

class TextEditor {
    private TextFormatter formatter;

    public TextEditor(TextFormatter formatter) {
        this.formatter = formatter;
    }

    public void setFormatter(TextFormatter formatter) {
        this.formatter = formatter;
    }

    public void publishText(String text) {
        System.out.println(formatter.format(text));
    }
}

public class TextEditorDemo {
    public static void main(String[] args) {
         TextEditor editor = new TextEditor(new UpperCaseFormatter());
         editor.publishText("hello world from strategy pattern");

         editor.setFormatter(new LowerCaseFormatter());
         editor.publishText("Hello World From Strategy Pattern");

         editor.setFormatter(new TitleCaseFormatter());
         editor.publishText("hello world from strategy pattern");
    }
}
