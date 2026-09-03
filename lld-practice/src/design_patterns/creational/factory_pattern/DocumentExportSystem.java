package design_patterns.creational.factory_pattern;

// Product interface
interface Document {
    String getHeader();
    String formatRow(String[] data);
    String getFooter();
    String getFileExtension();
}

// Concrete Products
class PdfDocument implements Document {
    @Override
    public String getHeader() {
        return "--- PDF DOCUMENT START ---";
    }

    @Override
    public String formatRow(String[] data) {
        return "| " + String.join(" | ", data) + " |";
    }

    @Override
    public String getFooter() {
        return "--- PDF DOCUMENT END ---";
    }

    @Override
    public String getFileExtension() {
        return ".pdf";
    }
}

class HtmlDocument implements Document {
    @Override
    public String getHeader() {
        return "<html><body><table>";
    }

    @Override
    public String formatRow(String[] data) {
        StringBuilder sb = new StringBuilder("<tr>");
        for (String cell : data) {
            sb.append("<td>").append(cell).append("</td>");
        }
        sb.append("</tr>");
        return sb.toString();
    }

    @Override
    public String getFooter() {
        return "</table></body></html>";
    }

    @Override
    public String getFileExtension() {
        return ".html";
    }
}

class CsvDocument implements Document {
    @Override
    public String getHeader() {
        return ""; // CSV has no header wrapper
    }

    @Override
    public String formatRow(String[] data) {
        return String.join(",", data);
    }

    @Override
    public String getFooter() {
        return ""; // CSV has no footer
    }

    @Override
    public String getFileExtension() {
        return ".csv";
    }
}

// Abstract Creator
abstract class ExportCreator {
    // Factory Method
    public abstract Document createDocument();

    // Shared export logic
    public void export(String[][] data) {
        Document doc = createDocument();
        System.out.println("Exporting to " + doc.getFileExtension() + " format...");

        String header = doc.getHeader();
        if (!header.isEmpty()) {
            System.out.println(header);
        }

        for (String[] row : data) {
            System.out.println(doc.formatRow(row));
        }

        String footer = doc.getFooter();
        if (!footer.isEmpty()) {
            System.out.println(footer);
        }

        System.out.println("Export complete.\n");
    }
}

// Concrete Creators
class PdfExportCreator extends ExportCreator {
    @Override
    public Document createDocument() {
        return new PdfDocument();
    }
}

class HtmlExportCreator extends ExportCreator {
    @Override
    public Document createDocument() {
        return new HtmlDocument();
    }
}

class CsvExportCreator extends ExportCreator {
    @Override
    public Document createDocument() {
        return new CsvDocument();
    }
}

// Client
public class DocumentExportSystem {
    public static void main(String[] args) {
        String[][] reportData = {
                {"Name", "Department", "Salary"},
                {"Alice", "Engineering", "120000"},
                {"Bob", "Marketing", "95000"},
                {"Charlie", "Design", "105000"}
        };

        ExportCreator pdfExporter = new PdfExportCreator();
        pdfExporter.export(reportData);

        ExportCreator htmlExporter = new HtmlExportCreator();
        htmlExporter.export(reportData);

        ExportCreator csvExporter = new CsvExportCreator();
        csvExporter.export(reportData);
    }
}
