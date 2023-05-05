package ru.zakirov.Project3.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.zakirov.Project3.dto.MeasurementDTO;
import ru.zakirov.Project3.dto.MeasurementsResponse;
import ru.zakirov.Project3.models.Measurement;
import ru.zakirov.Project3.services.MeasurementsService;
import ru.zakirov.Project3.util.IsThereASensorValidator;
import ru.zakirov.Project3.util.MeasurementErrorResponse;
import ru.zakirov.Project3.util.MeasurementNotAddException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;
    private final ModelMapper modelMapper;
    private final IsThereASensorValidator isThereASensorValidator;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, ModelMapper modelMapper, IsThereASensorValidator isThereASensorValidator) {
        this.measurementsService = measurementsService;
        this.modelMapper = modelMapper;
        this.isThereASensorValidator = isThereASensorValidator;
    }

    @GetMapping()
    public MeasurementsResponse allMeasurements(){
        return new MeasurementsResponse(measurementsService.findAll().stream().map(this::convertToMeasurementDTO).toList());
    }

    @GetMapping("/rainyDaysCount")
    public long rainyDaysCount(){
        return measurementsService.findCountRainyDays();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                          BindingResult bindingResult){
        isThereASensorValidator.validate(measurementDTO.getSensor(), bindingResult);

        if (bindingResult.hasErrors()){
            StringBuilder stringBuilder = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors) {
                stringBuilder.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new MeasurementNotAddException(stringBuilder.toString());
        }

        measurementsService.save(convertToMeasurement(measurementDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<MeasurementErrorResponse> handlerException(MeasurementNotAddException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO){
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement){
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
