package repositories;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import models.GroupModel;

public class ApiGroupMeRepository implements GroupMeRepository {
    private static final String BASE_API_URL = "https://api.groupme.com/v3";
    private static final String GROUPS_ENDPOINT = "/groups";
    private static final String REMOVE_USER_ENDPOINT = "/groups/%s/members/%s/remove";
    private final Supplier<String> authTokenSupplier;

    @Client(BASE_API_URL)
    @Inject RxHttpClient httpClient;
    public ApiGroupMeRepository(Supplier<String> authTokenSupplier) {
        this.authTokenSupplier = authTokenSupplier;
    }

    @Override
    public List<GroupModel> listGroups() {
        Gson gson = new Gson();
        JsonObject response = gson.fromJson(
            httpClient.toBlocking().retrieve(GROUPS_ENDPOINT), JsonObject.class);
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
            httpClient.toBlocking().retrieve(GROUPS_ENDPOINT), JsonObject.class);
        JsonArray allGroups = response.get("response").getAsJsonArray();

        return IntStream.range(0, allGroups.size())
            .mapToObj(i -> allGroups.get(i).getAsJsonObject())
            .map(jsonObject -> gson.fromJson(jsonObject.toString(), GroupModel.class))
            .filter(g -> groupId.equals(g.getGroupId()))
            .findFirst();
    }

    @Override
    public void removeUserFromGroup(String groupId, String membershipId) {
        httpClient.exchange(
            HttpRequest.POST(String.format(REMOVE_USER_ENDPOINT, groupId, membershipId), ""));
    }
}
