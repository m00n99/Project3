package ru.zakirov.Project3.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zakirov.Project3.dto.MeasurementDTO;
import ru.zakirov.Project3.dto.SensorDTO;
import ru.zakirov.Project3.services.SensorsService;

@Component
public class IsThereASensorValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public IsThereASensorValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(SensorDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensorDTO = (SensorDTO) target;

        if (sensorsService.findByName(sensorDTO.getName()).isEmpty())
            errors.rejectValue("sensor", "","такого сенсора не существует");
    }
}
