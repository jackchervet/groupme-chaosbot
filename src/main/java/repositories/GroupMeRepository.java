package repositories;

import java.util.List;
import java.util.Optional;

import helpers.AuthTokenSupplier;
import clients.GroupMeClient;
import models.BotPostModel;
import models.GroupModel;
import models.MessageCallbackModel;

public interface GroupMeRepository {
    List<GroupModel> listGroups();
    Optional<GroupModel> getGroupByGroupId(String groupId);

    MessageCallbackModel getLikedMessagesForPeriod(String groupId, String period);

    void removeUserFromGroup(String groupId, String membershipId);
    void sendMessageToGroup(BotPostModel message);

    static GroupMeRepository get(GroupMeClient client) {
        return new ApiGroupMeRepository(AuthTokenSupplier.get(), client);
    }
}
