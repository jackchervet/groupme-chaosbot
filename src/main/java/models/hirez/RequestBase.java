package models.hirez;

public class RequestBase {
    private final String signature;
    private final String timestamp;

    public RequestBase(String signature, String timestamp) {
        this.signature = signature;
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
