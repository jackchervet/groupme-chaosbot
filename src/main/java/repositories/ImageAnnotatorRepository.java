package repositories;

import clients.GPT4oClient;
import helpers.GPT4oApiKeySupplier;

import java.util.List;

public interface ImageAnnotatorRepository {
    Boolean isUnacceptedImage(List<String> imageUris);

    static ImageAnnotatorRepository get(GPT4oClient client) {
        return new Gpt4oMiniImageAnnotatorRepository(GPT4oApiKeySupplier.get(), client);
    }
}
