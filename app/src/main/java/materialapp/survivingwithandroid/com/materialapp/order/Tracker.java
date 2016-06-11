package materialapp.survivingwithandroid.com.materialapp.order;

/**
 * Created by dganeshappa on 11/10/2015.
 */
public class Tracker {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String status;
    String reason;
    String time;
}
