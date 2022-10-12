package com.ingemark.patientvitals.pojo;


public class HeartRate {
    private String timestamp;
    private Integer pulsePerMinute;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getPulsePerMinute() {
        return pulsePerMinute;
    }

    public void setPulsePerMinute(Integer pulsePerMinute) {
        this.pulsePerMinute = pulsePerMinute;
    }

    @Override
    public String toString() {
        return "HeartRate{" +
                "timestamp='" + timestamp + '\'' +
                ", pulsePerMinute=" + pulsePerMinute +
                '}';
    }
}

