package br.edu.atitus.geopoint.services;

import br.edu.atitus.geopoint.dtos.PointRequest;
import br.edu.atitus.geopoint.entities.PointEntity;
import br.edu.atitus.geopoint.repositories.PointRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class PointService {
    private final PointRepository repository;

    public PointService(PointRepository repository) {
        this.repository = repository;
    }

    public PointEntity insert(PointRequest pointRequest) {
        if (pointRequest == null)
            throw new RuntimeException("Objeto Nulo");
        if (pointRequest.name() == null || pointRequest.name().isBlank())
            throw new RuntimeException("Nome inválido");
        if (pointRequest.latitude() == null
                || pointRequest.latitude().compareTo(BigDecimal.valueOf(-90)) < 0
                || pointRequest.latitude().compareTo(BigDecimal.valueOf(90)) > 0)
            throw new RuntimeException("Latitude inválida - Intervalo aceito: -90 a 90");
        if (pointRequest.longitude() == null
                || pointRequest.longitude().compareTo(BigDecimal.valueOf(-180)) < 0
                || pointRequest.longitude().compareTo(BigDecimal.valueOf(180)) > 0)
            throw new RuntimeException("Longitude inválida - Intervalo aceito: -180 a 180");

        PointEntity point = new PointEntity();

        point.setId(UUID.randomUUID());
        point.setName(pointRequest.name().trim());
        point.setDescription(pointRequest.description() == null ? null : pointRequest.description().trim());
        point.setLatitude(pointRequest.latitude());
        point.setLongitude(pointRequest.longitude());

        return repository.insert(point);
    }

    public List<PointEntity> findAll() {
        return repository.findAll();
    }
}