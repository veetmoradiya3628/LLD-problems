package design_patterns.structural.decorator_pattern;

interface TextView {
    void render();
}

class PlainTextView implements TextView {
    private final String text;

    public PlainTextView(String text) {
        this.text = text;
    }

    @Override
    public void render() {
        System.out.print(text);
    }
}

abstract class TextDecorator implements TextView {
    protected final TextView inner;

    public TextDecorator(TextView textView) {
        this.inner = textView;
    }
}

class BoldDecorator extends TextDecorator {
    public BoldDecorator(TextView inner) {
        super(inner);
    }

    @Override
    public void render() {
        System.out.print("<b>");
        inner.render();
        System.out.print("</b>");
    }
}

class ItalicDecorator extends TextDecorator {
    public ItalicDecorator(TextView inner) {
        super(inner);
    }

    @Override
    public void render() {
        System.out.print("<i>");
        inner.render();
        System.out.print("</i>");
    }
}

class UnderlineDecorator extends TextDecorator {
    public UnderlineDecorator(TextView inner) {
        super(inner);
    }

    @Override
    public void render() {
        System.out.print("<u>");
        inner.render();
        System.out.print("</u>");
    }
}

public class DynamicTextRenderer {
    public static void main(String[] args) {
        TextView text = new PlainTextView("Hello, World");

        // Plain text
        System.out.print("Plain : ");
        text.render();
        System.out.println();

        // Bold decorator
        TextView boldText = new BoldDecorator(text);
        boldText.render();
        System.out.println();

        // Italic + Underlying
        TextView italicUnderlying = new UnderlineDecorator(new ItalicDecorator(text));
        italicUnderlying.render();
        System.out.println();

        // Bold + Italic + Underlying
        TextView allStyles = new UnderlineDecorator(
                new ItalicDecorator(
                        new BoldDecorator(text)
                )
        );
        allStyles.render();
        System.out.println();
    }
}
