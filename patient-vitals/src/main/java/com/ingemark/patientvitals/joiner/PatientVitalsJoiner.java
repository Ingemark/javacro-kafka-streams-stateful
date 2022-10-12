package com.ingemark.patientvitals.joiner;

import com.ingemark.patientvitals.pojo.BodyTemp;
import com.ingemark.patientvitals.pojo.HeartRate;
import com.ingemark.patientvitals.pojo.PatientVitals;
import org.apache.kafka.streams.kstream.ValueJoiner;

public class PatientVitalsJoiner implements ValueJoiner<HeartRate, BodyTemp, PatientVitals> {

    @Override
    public PatientVitals apply(HeartRate heartRate, BodyTemp bodyTemp) {

        return PatientVitals.PatientVitalsBuilder.aPatientVitals().
                withHeartRate(heartRate.getPulsePerMinute())
                .withTemp(bodyTemp.getTemp())
                .withUnit(bodyTemp.getUnit())
                .build();
    }
}
