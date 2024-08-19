package clients;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client("https://api.groupme.com/v3")
public interface GroupMeClient {

    @Get("/groups")
    String getGroups();

    @Get("/groups/{groupId}/likes?period={period}&token={token}")
    String getLikesForPeriod(@NotBlank String groupId, @NotBlank String period, @NotBlank String token);

    @Post("/groups/{groupId}/members/{memberId}/remove?token={token}")
    void removeMember(@NotBlank String groupId, @NotBlank String memberId, @NotBlank String token);

    @Post("/bots/post")
    void postMessage(@Body @Valid String message);

    @Delete("/conversations/{groupId}/messages/{messageId}?token={token}")
    void deleteMessage(@NotBlank String groupId, @NotBlank String messageId, @NotBlank String token);
}
