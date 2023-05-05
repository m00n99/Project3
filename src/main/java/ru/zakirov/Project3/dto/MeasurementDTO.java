package ru.zakirov.Project3.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.zakirov.Project3.models.Sensor;

public class MeasurementDTO {
    @Min(value = -100, message = "значение температуры воздуха не должен быть меньше чем -100")
    @Max(value = 100, message = "значение температуры воздуха не должно быть больше чем 100")
    @NotNull(message = "значение температуры воздуха не может быть нулевым")
    private float value;
    @NotNull(message = "значение регистрации дождя не может быть нулевым")
    private boolean raining;
    private SensorDTO sensor;

    public double getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
