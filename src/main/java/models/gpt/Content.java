package models.gpt;


public class Content {
    private String type;
    private String text;
    private ImageObject image_url;

    public Content(Builder builder) {
        this.type = builder.type;
        this.text = builder.text;
        this.image_url = builder.image_url;
    }

    public static class Builder {
        private String type;
        private String text;
        private ImageObject image_url;

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setImage(ImageObject image_url) {
            this.image_url = image_url;
            return this;
        }

        public Content build() {
            return new Content(this);
        }
    }
}
