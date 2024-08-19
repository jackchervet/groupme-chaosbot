package models.gpt;

public class CompletionResponseMessage {
    private final String content;

    public CompletionResponseMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
