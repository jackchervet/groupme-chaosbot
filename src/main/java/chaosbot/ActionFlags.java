package chaosbot;

import javax.inject.Singleton;

@Singleton
public class ActionFlags {
    private boolean peppers = false;

    public boolean peppersOn() {
        return peppers;
    }

    public void setPeppers(boolean peppers) {
        this.peppers = peppers;
    }
}
