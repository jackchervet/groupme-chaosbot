package models.gpt;

import java.util.List;

public class CompletionResponse {
    private final List<CompletionResponseChoice> choices;

    public CompletionResponse(List<CompletionResponseChoice> choices) {
        this.choices = choices;
    }

    public List<CompletionResponseChoice> getChoices() {
        return choices;
    }
}
