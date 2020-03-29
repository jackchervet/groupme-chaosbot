package helpers;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client("https://api.groupme.com/v3")
public interface GroupMeClient {

    @Get("/groups")
    String getGroups();

    @Post("/groups/{groupId}/members/{memberId}/remove")
    void removeMember(@NotBlank String groupId, @NotBlank String memberId);

    @Post("/bots/post")
    void postMessage(@Body @Valid String message);
}
