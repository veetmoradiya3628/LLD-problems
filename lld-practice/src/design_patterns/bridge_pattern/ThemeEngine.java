package design_patterns.bridge_pattern;

interface Theme {
    void applyButtonStyle(String label);
    void applyTextFieldStyle(String placeholder);
    void applyCheckboxStyle(String label, boolean checked);
}

class DarkTheme implements Theme {
    @Override
    public void applyButtonStyle(String label) {
        System.out.println("[Dark] Button: \"" + label + "\" | bg=#1a1a2e, text=#ffffff");
    }

    @Override
    public void applyTextFieldStyle(String placeholder) {
        System.out.println("[Dark] TextField: \"" + placeholder + "\" | bg=#1a1a2e, text=#ffffff");
    }

    @Override
    public void applyCheckboxStyle(String label, boolean checked) {
        String mark = checked ? "x" : " ";
        System.out.println("[Dark] Checkbox: \"" + label + "\" [" + mark + "] | bg=#1a1a2e, text=#ffffff");
    }
}

class LightTheme implements Theme {
    @Override
    public void applyButtonStyle(String label) {
        System.out.println("[Light] Button: \"" + label + "\" | bg=#ffffff, text=#1a1a2e");
    }

    @Override
    public void applyTextFieldStyle(String placeholder) {
        System.out.println("[Light] TextField: \"" + placeholder + "\" | bg=#ffffff, text=#1a1a2e");
    }

    @Override
    public void applyCheckboxStyle(String label, boolean checked) {
        String mark = checked ? "x" : " ";
        System.out.println("[Light] Checkbox: \"" + label + "\" [" + mark + "] | bg=#ffffff, text=#1a1a2e");
    }
}

abstract class Widget {
    protected Theme theme;

    public Widget(Theme theme) {
        this.theme = theme;
    }

    public abstract void render();
}

class Button extends Widget {
    private String label;

    public Button(Theme theme, String label) {
        super(theme);
        this.label = label;
    }

    @Override
    public void render() {
        theme.applyButtonStyle(label);
    }
}

class TextField extends Widget {
    private String placeholder;

    public TextField(Theme theme, String placeholder) {
        super(theme);
        this.placeholder = placeholder;
    }

    @Override
    public void render() {
        theme.applyTextFieldStyle(placeholder);
    }
}

class Checkbox extends Widget {
    private String label;
    private boolean checked;

    public Checkbox(Theme theme, String label, boolean checked) {
        super(theme);
        this.label = label;
        this.checked = checked;
    }

    @Override
    public void render() {
        theme.applyCheckboxStyle(label, checked);
    }
}

public class ThemeEngine {
    public static void main(String[] args) {
        Theme dark = new DarkTheme();
        Theme light = new LightTheme();
        Widget btn = new Button(dark, "Submit");
        Widget txt = new TextField(light, "Enter name...");
        Widget chk = new Checkbox(dark, "Remember me", true);
        btn.render();
        txt.render();
        chk.render();
    }
}
