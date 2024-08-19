package clients;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client("https://api.openai.com/v1")
public interface GPT4oClient {

    @Post("/chat/completions")
    String isImagePolitical(@Header("Authorization") String apiKey, @Body String message);
}
