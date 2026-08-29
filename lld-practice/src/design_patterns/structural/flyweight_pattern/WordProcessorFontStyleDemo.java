package design_patterns.structural.flyweight_pattern;

import java.util.HashMap;
import java.util.Map;

interface FontStyle {
    void format(int line, int column, char character);
}

class ConcreteFontStyle implements FontStyle {
    private final String fontFamily;
    private final int fontSize;
    private final boolean bold;
    private final boolean italic;

    public ConcreteFontStyle(String fontFamily, int fontSize, boolean bold, boolean italic) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.bold = bold;
        this.italic = italic;
    }

    @Override
    public void format(int line, int column, char character) {
        System.out.println("[" + fontFamily + "," + fontSize + ", " + (italic ? "italic" : (bold ? "bold" : "normal")) + "] '" + character + "' at " + line + ":" + column);
    }
}

class FontStyleFactory {
    private final Map<String, FontStyle> cache = new HashMap<>();

    public FontStyle getFontStyle(String fontFamily, int fontSize, boolean bold, boolean italic) {
        String key = fontFamily + "_" + fontSize + "_" + bold + "_" + italic;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        ConcreteFontStyle cf = new ConcreteFontStyle(fontFamily, fontSize, bold, italic);
        cache.put(key, cf);
        return cf;
    }

    public int getStyleCount() {
        return cache.size();
    }
}

public class WordProcessorFontStyleDemo {
    public static void main(String[] args) {
         FontStyleFactory factory = new FontStyleFactory();
         FontStyle arial12 = factory.getFontStyle("Arial", 12, false, false);
         FontStyle arial12Bold = factory.getFontStyle("Arial", 12, true, false);
         FontStyle arial12Again = factory.getFontStyle("Arial", 12, false, false);

         arial12.format(1, 1, 'H');
         arial12Bold.format(1, 2, 'e');
         arial12Again.format(1, 3, 'l');

         System.out.println("Same instance? " + (arial12 == arial12Again));
         System.out.println("Total styles: " + factory.getStyleCount());
    }
}
