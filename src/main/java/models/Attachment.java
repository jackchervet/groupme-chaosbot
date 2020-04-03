package models;

import java.util.ArrayList;
import java.util.List;

public class Attachment {
    private String type;
    private String url;
    private List<List<Integer>> loci;
    private List<String> user_ids;

    private Attachment(
        String type,
        String url,
        List<List<Integer>> loci,
        List<String> userIds)
    {
        this.type = type;
        this.url = url;
        this.loci = loci.isEmpty() ? null : loci;
        this.user_ids = userIds.isEmpty() ? null : userIds;
    }

    public String getType() {
        return this.type;
    }

    public List<String> getUserIds() {
        return user_ids;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String type;
        private String url;
        private List<List<Integer>> loci = new ArrayList<>();
        private List<String> user_ids = new ArrayList<>();

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setLoci(List<List<Integer>> loci) {
            this.loci = loci;
            return this;
        }

        public Builder addLoci(List<Integer> loci) {
            this.loci.add(loci);
            return this;
        }

        public Builder setUserIds(List<String> userIds) {
            this.user_ids = userIds;
            return this;
        }

        public Builder addUserId(String userId) {
            this.user_ids.add(userId);
            return this;
        }

        public Attachment build() {
            return new Attachment(this.type, this.url, this.loci, this.user_ids);
        }
    }
}
