package com.ingemark.patientvitals.pojo;

public class PatientVitals {

    private int heartRate;
    private Double temp;
    private String unit;

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
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
        return "PatientVitals{" +
                "heartRate=" + heartRate +
                ", temp=" + temp +
                ", unit='" + unit + '\'' +
                '}';
    }


    public static final class PatientVitalsBuilder {
        private int heartRate;
        private Double temp;
        private String unit;

        private PatientVitalsBuilder() {
        }

        public static PatientVitalsBuilder aPatientVitals() {
            return new PatientVitalsBuilder();
        }

        public PatientVitalsBuilder withHeartRate(int heartRate) {
            this.heartRate = heartRate;
            return this;
        }

        public PatientVitalsBuilder withTemp(Double temp) {
            this.temp = temp;
            return this;
        }

        public PatientVitalsBuilder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public PatientVitals build() {
            PatientVitals patientVitals = new PatientVitals();
            patientVitals.setHeartRate(heartRate);
            patientVitals.setTemp(temp);
            patientVitals.setUnit(unit);
            return patientVitals;
        }
    }
}
