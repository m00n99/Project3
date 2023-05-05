package ru.zakirov.Project3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zakirov.Project3.models.Measurement;
import ru.zakirov.Project3.repositroies.MeasurementsRepository;
import ru.zakirov.Project3.repositroies.SensorsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {

    private final MeasurementsRepository measurementsRepository;
    private final SensorsRepository sensorsRepository;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsRepository sensorsRepository) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsRepository = sensorsRepository;
    }

    public List<Measurement> findAll(){
        return measurementsRepository.findAll();
    }

    public long findCountRainyDays(){
        return measurementsRepository.findAll().stream().filter(Measurement::isRaining).count();
    }

    @Transactional
    public void save(Measurement measurement){
        measurement.setCreatedAt(LocalDateTime.now());

        measurement.getSensor().setId(sensorsRepository.findSensorByName(measurement.getSensor().getName())
                .get().getId());

        measurementsRepository.save(measurement);
    }
}