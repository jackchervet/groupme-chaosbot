package models.actions;

import java.util.Optional;

import clients.GPT4oClient;
import clients.GroupMeClient;
import clients.HiRezClient;
import com.google.common.cache.Cache;

public interface Before {
    Optional<BeforeResult> performBefore(GroupMeClient groupMeClient, HiRezClient hiRezClient, GPT4oClient gptClient, Cache<String, Object> cache);
}
