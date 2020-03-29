package repositories;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import helpers.GroupMeClient;
import models.BotPostModel;
import models.GroupModel;
import models.MessageCallbackModel;

public class ApiGroupMeRepository implements GroupMeRepository {
    private final Supplier<String> authTokenSupplier;
    private final GroupMeClient httpClient;

    public ApiGroupMeRepository(Supplier<String> authTokenSupplier, GroupMeClient client) {
        this.authTokenSupplier = authTokenSupplier;
        this.httpClient = client;
    }

    @Override
    public List<GroupModel> listGroups() {
        Gson gson = new Gson();
        JsonObject response = gson.fromJson(
            httpClient.getGroups(), JsonObject.class);
        JsonArray allGroups = response.get("response").getAsJsonArray();

        return IntStream.range(0, allGroups.size())
            .mapToObj(i -> allGroups.get(i).getAsJsonObject())
            .map(jsonObject -> gson.fromJson(jsonObject.toString(), GroupModel.class))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<GroupModel> getGroupByGroupId(String groupId) {
        Gson gson = new Gson();
        JsonObject response = gson.fromJson(
            httpClient.getGroups(), JsonObject.class);
        JsonArray allGroups = response.get("response").getAsJsonArray();

        return IntStream.range(0, allGroups.size())
            .mapToObj(i -> allGroups.get(i).getAsJsonObject())
            .map(jsonObject -> gson.fromJson(jsonObject.toString(), GroupModel.class))
            .filter(g -> groupId.equals(g.getGroupId()))
            .findFirst();
    }

    @Override
    public MessageCallbackModel getLikedMessagesForPeriod(String groupId, String period) {
        String response = httpClient.getLikesForPeriod(groupId, period, authTokenSupplier.get());
        return new Gson().fromJson(response, MessageCallbackModel.class);
    }

    @Override
    public void removeUserFromGroup(String groupId, String membershipId) {
        httpClient.removeMember(groupId, membershipId);
    }

    @Override
    public void sendMessageToGroup(BotPostModel message) {
        Gson gson = new Gson();
        String m = gson.toJson(message);
        System.out.println("message: " + m);
        httpClient.postMessage(m);
    }
}
