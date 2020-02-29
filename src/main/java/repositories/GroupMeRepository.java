package repositories;

import java.util.List;
import java.util.Optional;

import helpers.AuthTokenSupplier;
import helpers.GroupMeClient;
import models.BotPostModel;
import models.GroupModel;

public interface GroupMeRepository {
    List<GroupModel> listGroups();
    Optional<GroupModel> getGroupByGroupId(String groupId);

    void removeUserFromGroup(String groupId, String membershipId);
    void sendMessageToGroup(BotPostModel message);

    static GroupMeRepository get(GroupMeClient client) {
        return new ApiGroupMeRepository(AuthTokenSupplier.get(), client);
    }
}
