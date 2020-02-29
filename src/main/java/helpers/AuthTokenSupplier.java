package helpers;

import java.util.function.Supplier;

public class AuthTokenSupplier {
    private static final Supplier<String> AUTH_TOKEN_SUPPLIER = () -> "";

    public static Supplier<String> get() {
        return AUTH_TOKEN_SUPPLIER;
    }
}
