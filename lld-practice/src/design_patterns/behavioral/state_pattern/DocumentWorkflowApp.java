package design_patterns.behavioral.state_pattern;

interface DocumentState {
    void edit(Document context, String content);
    void submitForReview(Document context);
    void approve(Document context);
    void reject(Document context);
    void unpublish(Document context);
}

class DraftState implements DocumentState {
    @Override
    public void edit(Document context, String content) {
        System.out.println("Editing document: " + content);
        context.setContent(content);
    }

    @Override
    public void submitForReview(Document context) {
        System.out.println("Document submitted for review.");
        context.setState(new UnderReviewState());
    }

    @Override
    public void approve(Document context) {
        System.out.println("Cannot approve a draft. Submit for review first.");
    }

    @Override
    public void reject(Document context) {
        System.out.println("Cannot reject a draft. Submit for review first.");
    }

    @Override
    public void unpublish(Document context) {
        System.out.println("Document is already a draft.");
    }
}

class UnderReviewState implements DocumentState {
    @Override
    public void edit(Document context, String content) {
        System.out.println("Cannot edit while under review.");
    }

    @Override
    public void submitForReview(Document context) {
        System.out.println("Document is already under review.");
    }

    @Override
    public void approve(Document context) {
        System.out.println("Document approved and published.");
        context.setState(new PublishedState());
    }

    @Override
    public void reject(Document context) {
        System.out.println("Document rejected. Returning to draft.");
        context.setState(new DraftState());
    }

    @Override
    public void unpublish(Document context) {
        System.out.println("Document is not published yet.");
    }
}

class PublishedState implements DocumentState {
    @Override
    public void edit(Document context, String content) {
        System.out.println("Cannot edit a published document. Unpublish first.");
    }

    @Override
    public void submitForReview(Document context) {
        System.out.println("Document is already published.");
    }

    @Override
    public void approve(Document context) {
        System.out.println("Document is already published.");
    }

    @Override
    public void reject(Document context) {
        System.out.println("Cannot reject a published document.");
    }

    @Override
    public void unpublish(Document context) {
        System.out.println("Document unpublished. Returning to draft.");
        context.setState(new DraftState());
    }
}

class Document {
    private DocumentState currentState;
    private String content;

    public Document() {
        this.currentState = new DraftState();
        this.content = "";
    }

    public void setState(DocumentState state) { this.currentState = state; }
    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public void edit(String content) { currentState.edit(this, content); }
    public void submitForReview() { currentState.submitForReview(this); }
    public void approve() { currentState.approve(this); }
    public void reject() { currentState.reject(this); }
    public void unpublish() { currentState.unpublish(this); }
}

public class DocumentWorkflowApp {
    public static void main(String[] args) {
        Document doc = new Document();

        doc.edit("First draft of the article.");
        doc.approve();              // Rejected: cannot approve a draft
        doc.submitForReview();
        doc.edit("Trying to edit");  // Rejected: under review
        doc.reject();                // Back to draft
        doc.edit("Revised draft.");
        doc.submitForReview();
        doc.approve();               // Published
        doc.edit("Trying to edit");  // Rejected: published
        doc.unpublish();             // Back to draft
    }
}
