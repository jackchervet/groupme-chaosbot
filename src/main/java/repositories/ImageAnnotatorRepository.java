package repositories;

import java.util.List;

public interface ImageAnnotatorRepository {
    Boolean isUnacceptedImage(List<String> imageUris);

    static ImageAnnotatorRepository get() { return new VisionApiImageAnnotatorRepository(); }
}
