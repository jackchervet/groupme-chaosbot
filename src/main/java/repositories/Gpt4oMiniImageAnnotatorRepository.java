package repositories;

import clients.GPT4oClient;
import com.google.gson.Gson;
import models.gpt.CompletionRequest;
import models.gpt.CompletionResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class Gpt4oMiniImageAnnotatorRepository implements ImageAnnotatorRepository {
    private final Supplier<String> apiKeySupplier;
    private final GPT4oClient httpClient;

    public Gpt4oMiniImageAnnotatorRepository(Supplier<String> apiKeySupplier, GPT4oClient client) {
        this.apiKeySupplier = apiKeySupplier;
        this.httpClient = client;
    }

    private static String encodeImageToBase64(String imageUrl) throws Exception {
        // Open a connection to the URL
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Read the image data into a byte array
        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Convert byte array to base64 encoded string
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }

    @Override
    public Boolean isUnacceptedImage(List<String> imageUris) {
        Gson gson = new Gson();
        String key = apiKeySupplier.get();
        try {
            return imageUris.stream()
                    .filter(Objects::nonNull)
                    .map(CompletionRequest::new)
                    .map(gson::toJson)
                    .peek(System.out::println)
                    .map(r -> httpClient.isImagePolitical("Bearer " + key, r))
                    .peek(System.out::println)
                    .map(r -> gson.fromJson(r, CompletionResponse.class))
                    .peek(System.out::println)
                    .flatMap(cr -> cr.getChoices().stream())
                    .anyMatch(c -> c.getMessage().getContent().equals("1"));
        } catch (Exception e) {
            System.out.println("Error checking image content: " + e.getMessage());
            return false;
        }
    }
}
