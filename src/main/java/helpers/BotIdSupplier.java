package helpers;

import java.util.function.Supplier;

public class BotIdSupplier {
    private static final Supplier<String> BOT_ID_SUPPLIER = () -> "";

    public static Supplier<String> get() {
        return BOT_ID_SUPPLIER;
    }
}
