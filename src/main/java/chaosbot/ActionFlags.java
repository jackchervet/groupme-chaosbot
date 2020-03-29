package chaosbot;

import javax.inject.Singleton;

@Singleton
public class ActionFlags {
    private boolean peppers = false;
    private boolean tomDelonge = false;

    public boolean peppersOn() {
        return peppers;
    }

    public void setPeppers(boolean peppers) {
        this.peppers = peppers;
    }

    public boolean tomDelongeOn() {
        return tomDelonge;
    }

    public void setTomDelonge(boolean tomDelonge) {
        this.tomDelonge = tomDelonge;
    }
}
