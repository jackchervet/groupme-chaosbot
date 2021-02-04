package cache;

import java.util.Optional;

public interface Cache {
    void add(String key, Object value, long periodInMillis);
    void remove(String key);
    Optional<Object> get(String key);
    void clear();
    long size();

    static Cache get() {
        return new InMemoryCache();
    }
}
