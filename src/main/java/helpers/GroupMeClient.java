package helpers;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client("https://api.groupme.com/v3")
public interface GroupMeClient {

    @Get("/groups")
    String getGroups();

    @Post("/groups/{groupId}/members/{memberId}/remove")
    void removeMember(String groupId, String memberId);

    @Post("/bots/post")
    void postMessage(@Body String message);
}
