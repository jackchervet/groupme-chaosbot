package repositories;

import java.util.List;
import java.util.Optional;

import helpers.AuthTokenSupplier;
import models.GroupModel;

public interface GroupMeRepository {
    List<GroupModel> listGroups();
    Optional<GroupModel> getGroupByGroupId(String groupId);

    void removeUserFromGroup(String groupId, String membershipId);

    static GroupMeRepository getDefault() {
        return new ApiGroupMeRepository(AuthTokenSupplier.get());
    }
}
