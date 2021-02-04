package models.hirez;

public class Session {
    private final String ret_msg;
    private final String session_id;
    private final String timestamp;

    public Session(String ret_msg, String session_id, String timestamp) {
        this.ret_msg = ret_msg;
        this.session_id = session_id;
        this.timestamp = timestamp;
    }

    public String getSession_id() {
        return session_id;
    }
}
