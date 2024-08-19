package models.gpt;

import java.util.List;

public class PromptMessage {
    private final String role = "user";
    private List<Content> content;

    public PromptMessage(List<Content> content) {
        this.content = content;
    }
}

