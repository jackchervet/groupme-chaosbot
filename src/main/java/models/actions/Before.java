package models.actions;

import java.util.Optional;

import cache.Cache;
import clients.GroupMeClient;
import clients.HiRezClient;

public interface Before {
    Optional<BeforeResult> performBefore(GroupMeClient groupMeClient, HiRezClient hiRezClient, Cache cache);
}
