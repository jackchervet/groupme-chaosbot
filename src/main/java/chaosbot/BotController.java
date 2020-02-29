package chaosbot;

import java.util.Random;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;

import helpers.BotIdSupplier;
import helpers.GroupMeClient;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import models.BotPostModel;
import models.BotPostModel.Attachment;
import models.MessageCallbackModel;
import repositories.GroupMeRepository;

@Controller("/bot")
public class BotController {
    @Inject
    private GroupMeClient client;

    private static String GEPPO_ID = "64855034";
    private static ImmutableList<String> PEPPERS = new ImmutableList.Builder<String>()
        .add("https://www.almanac.com/sites/default/files/styles/primary_image_in_article/public/image_nodes/bell-peppers-assorted-crop.jpg?itok=fchiyU7L")
        .add("https://images-gmi-pmc.edge-generalmills.com/5d03cd07-b034-47cb-8f58-17a1837b73a3.jpg")
        .add("https://www.chilipeppermadness.com/wp-content/uploads/2013/09/Chimayo-Pepper.jpg")
        .add("https://images.food52.com/CvFzQSRN7dVEfsZk0D-hINrCO98=/1000x1000/38daf73e-324a-4402-956a-2d2cc7a190a0--roasted-red-peppers-9.jpg")
        .add("https://i.insider.com/5df90ffc79d75713692d7c02?width=1100&format=jpeg&auto=webp")
        .add("https://www.friedas.com/wp-content/uploads/2012/02/Friedas_Mini_SweetPeppers_Group.jpg")
        .add("https://toriavey.com/images/2010/02/MG_0568-1.jpg")
        .add("https://s11284.pcdn.co/wp-content/uploads/2020/02/mccormick-ground-black-pepper.jpg")
        .add("https://thenypost.files.wordpress.com/2015/08/shutterstock_220826371.jpg?quality=80&strip=all&w=618&h=410&crop=1")
        .add("https://images2.minutemediacdn.com/image/upload/c_fill,g_auto,h_1248,w_2220/f_auto,q_auto,w_1100/v1555303330/shape/mentalfloss/istock_4043660_small.jpg")
        .build();


    @Post("/messageAdded")
    public void messageAdded(@Body String body) {
        Gson gson = new Gson();
        MessageCallbackModel message = gson.fromJson(body, MessageCallbackModel.class);

        if (GEPPO_ID.equals(message.getSenderId())) {
            BotPostModel req = new BotPostModel.Builder()
                .setBotId(BotIdSupplier.get().get())
                .addAttachment(new Attachment.Builder()
                    .setType("image")
                    .setUrl(getPepperImage())
                    .build())
                .build();

            GroupMeRepository repo = GroupMeRepository.get(client);
            repo.sendMessageToGroup(req);
        }
    }

    private static String getPepperImage() {
        Random rand = new Random();
        return PEPPERS.get(rand.nextInt(PEPPERS.size()));
    }
}
