package models.gpt;

public class CompletionResponseChoice {
    private final CompletionResponseMessage message;

    public CompletionResponseChoice(CompletionResponseMessage message) {
        this.message = message;
    }

    public CompletionResponseMessage getMessage() {
        return message;
    }
}
