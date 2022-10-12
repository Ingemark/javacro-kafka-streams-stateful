package com.ingemark.patientvitals.pojo;

public class BodyTemp {
    private String timestamp;
    private Double temp;
    private String unit;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "BodyTemp{" +
                "timestamp='" + timestamp + '\'' +
                ", temp=" + temp +
                ", unit='" + unit + '\'' +
                '}';
    }
}
