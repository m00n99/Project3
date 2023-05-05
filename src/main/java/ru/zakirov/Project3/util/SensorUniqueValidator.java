package ru.zakirov.Project3.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zakirov.Project3.dto.SensorDTO;
import ru.zakirov.Project3.services.SensorsService;

@Component
public class SensorUniqueValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public SensorUniqueValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(SensorDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensor = (SensorDTO) target;

        if (sensorsService.findByName(sensor.getName()).isPresent()){
            errors.rejectValue("name", "","сенсор с таким именем уже существует");
        }
    }
}
