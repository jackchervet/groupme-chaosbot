package models.actions;

import java.util.Optional;

import helpers.GroupMeClient;

public interface Before {
    Optional<BeforeResult> performBefore(GroupMeClient client);
}
