package chaosbot;

import com.google.gson.Gson;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import models.MessageCallbackModel;

@Controller("/bot")
public class BotController {

    @Post("/messageAdded")
    public void messageAdded(@Body String body) {
        Gson gson = new Gson();
        MessageCallbackModel message = gson.fromJson(body, MessageCallbackModel.class);
        System.out.println("Message received from Bot");
        System.out.println(message.getText());
    }
}
