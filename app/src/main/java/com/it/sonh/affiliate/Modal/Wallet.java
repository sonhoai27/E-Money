package com.it.sonh.affiliate.Modal;

/**
 * Created by sonho on 3/21/2018.
 */

public class Wallet {
    private String from;
    private String message;
    private String status;
    private String id;

    public Wallet() {
    }

    public Wallet(String from, String message, String status, String id) {
        this.from = from;
        this.message = message;
        this.status = status;
        this.id = id;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "from='" + from + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
