package models.gpt;

import com.google.common.collect.Lists;

import java.util.List;

public class CompletionRequest {
    private final String model = "gpt-4o-mini";
    private final Integer max_tokens = 1;

    private List<PromptMessage> messages;

    public CompletionRequest(String imageUrl) {
        Content textPrompt = new Content.Builder()
            .setType("text")
            .setText("Is this image likely to contain political content? Respond with 1 for yes or 0 for no.")
            .build();
        Content imagePrompt = new Content.Builder()
            .setType("image_url")
            .setImage(new ImageObject(imageUrl))
            .build();
        this.messages = Lists.newArrayList(
            new PromptMessage(Lists.newArrayList(textPrompt, imagePrompt))
        );
    }
}
