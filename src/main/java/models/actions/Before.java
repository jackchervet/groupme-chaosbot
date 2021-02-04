package models.actions;

import java.util.Optional;

import clients.GroupMeClient;
import clients.HiRezClient;
import com.google.common.cache.Cache;

public interface Before {
    Optional<BeforeResult> performBefore(GroupMeClient groupMeClient, HiRezClient hiRezClient, Cache<String, Object> cache);
}
