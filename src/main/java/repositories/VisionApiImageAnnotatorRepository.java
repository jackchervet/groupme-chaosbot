package repositories;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class VisionApiImageAnnotatorRepository implements ImageAnnotatorRepository {

    private static AnnotateImageRequest buildRequest(String imageUri) {
        return AnnotateImageRequest.newBuilder()
            .setImage(Image.newBuilder()
                .setSource(ImageSource.newBuilder()
                    .setImageUri(imageUri)
                    .build()))
            .addFeatures(Feature.newBuilder()
                .setType(Feature.Type.SAFE_SEARCH_DETECTION)
                .build())
            .build();
    }

    @Override
    public Boolean isUnacceptedImage(List<String> imageUris) {
        List<AnnotateImageRequest> requests = imageUris.stream().map(VisionApiImageAnnotatorRepository::buildRequest).collect(Collectors.toList());
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            AnnotateImageResponse res = vision.batchAnnotateImages(requests)
                .getResponsesList().get(0);

            if (res.hasError()) {
                System.out.format("Error when fetching image annotations: %s%n", res.getError().getMessage());
                return false;
            }

            return res.getSafeSearchAnnotation().getMedical().getNumber() >= 4 ||
                res.getSafeSearchAnnotation().getViolence().getNumber() >= 4;

        } catch (IOException ex) {
            System.out.println("Error interacting with Google Vision API: " + ex);
            return false;
        }
    }
}
