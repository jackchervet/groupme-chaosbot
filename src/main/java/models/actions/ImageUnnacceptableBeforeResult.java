package models.actions;

public class ImageUnnacceptableBeforeResult extends BeforeResult {
    private final Boolean isUnnacceptable;

    public ImageUnnacceptableBeforeResult(Boolean isUnnacceptable) {
        this.isUnnacceptable = isUnnacceptable;
    }

    public Boolean isUnnacceptable() {
        return isUnnacceptable;
    }
}
