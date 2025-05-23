class Logger { 
    private static Logger unique;

    private Logger() { }

    public static Logger getInstance() {
        if (unique == null) {
            synchronized (unique) {
                if (unique == null) {
                    unique = new Logger();
                }
            }
        }
        return unique;
    }

    public void log(String message) {
        System.out.println(message);
    }
}

abstract class DocumentState {
    public abstract void handleInput(String input);
}

class DraftState extends DocumentState {
    @Override
    public void handleInput(String input) {
        System.out.println("Draft State: Handling input - " + input);
    }
}

class ReviewState extends DocumentState {
    @Override
    public void handleInput(String input) {
        System.out.println("Review State: Handling input - " + input);
    }
}

class FinalState extends DocumentState {
    @Override
    public void handleInput(String input) {
        System.out.println("Final State: Handling input - " + input);
    }
}

class Document {
    private DocumentState currentState;

    public Document(DocumentState initialState) {
        this.currentState = initialState;
    }

    public void changeState(DocumentState newState) {
        this.currentState = newState;
        System.out.println("Document state changed to: " + newState.getClass().getSimpleName());
    }

    public void handleInput(String input) {
        currentState.handleInput(input);
    }
}

abstract class DocumentPrototype {
    public abstract DocumentPrototype clone();
}

class ReportType extends DocumentPrototype {
    private String content;

    public ReportType(String content) {
        this.content = content;
    }

    @Override
    public DocumentPrototype clone() {
        Logger.getInstance().log("Cloning ReportType document");
        return new ReportType(this.content);
    }

    public String getContent() {
        return content;
    }
}

class InvoiceType extends DocumentPrototype {
    private String content;

    public InvoiceType(String content) {
        this.content = content;
    }

    @Override
    public DocumentPrototype clone() {
        Logger.getInstance().log("Cloning InvoiceType document.");
        return new InvoiceType(this.content);
    }

    public String getContent() {
        return content;
    }
}

public class Singleton_State_Prototype {
    public static void main(String[] args) {
        DocumentPrototype reportPrototype = new ReportType("Default Report Content");
        DocumentPrototype invoicePrototype = new InvoiceType("Default Invoice Content");

        DocumentPrototype clonedReport = reportPrototype.clone();
        DocumentPrototype clonedInvoice = invoicePrototype.clone();

        Logger.getInstance().log("Created a new report document with content: " + ((ReportType) clonedReport).getContent());
        Logger.getInstance().log("Created a new invoice document with content: " + ((InvoiceType) clonedInvoice).getContent());

        DocumentState draft = new DraftState();
        DocumentState review = new ReviewState();
        DocumentState finalState = new FinalState();

        Document document1 = new Document(draft);
        document1.handleInput("Initial draft input for document 1");
        document1.changeState(review);
        document1.handleInput("Review input for document 1");
        document1.changeState(finalState);
        document1.handleInput("Final input for document 1");

        Document document2 = new Document(draft);
        document2.handleInput("Initial draft input for document 2");
        document2.changeState(review);
        document2.handleInput("Review input for document 2");
        document2.changeState(finalState);
        document2.handleInput("Final input for document 2");
    }
}