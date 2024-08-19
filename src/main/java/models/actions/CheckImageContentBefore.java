package models.actions;

import clients.GPT4oClient;
import clients.GroupMeClient;
import clients.HiRezClient;
import com.google.common.cache.Cache;
import repositories.ImageAnnotatorRepository;

import java.util.List;
import java.util.Optional;

public class CheckImageContentBefore implements Before {
    private final List<String> imageUrls;

    public CheckImageContentBefore(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    @Override
    public Optional<BeforeResult> performBefore(GroupMeClient groupMeClient, HiRezClient hiRezClient, GPT4oClient gpt4oClient, Cache<String, Object> cache) {
        ImageAnnotatorRepository repo = ImageAnnotatorRepository.get(gpt4oClient);
        System.out.println("Checking image content:\n");
        imageUrls.forEach(System.out::println);
        return Optional.of(new ImageUnnacceptableBeforeResult(repo.isUnacceptedImage(imageUrls)));
    }
}
