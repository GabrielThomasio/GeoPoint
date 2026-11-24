package br.edu.atitus.geopoint.services;

import br.edu.atitus.geopoint.dtos.PointRequest;
import br.edu.atitus.geopoint.entities.PointEntity;
import br.edu.atitus.geopoint.repositories.PointRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class PointService {
    private final PointRepository repository;

    public PointService(PointRepository repository) {
        this.repository = repository;
    }

    public PointEntity insert(PointRequest pointRequest) {
        validate(pointRequest);

        String name = pointRequest.name().trim();
        if (repository.existsByName(name))
            throw new RuntimeException("Já existe um ponto cadastrado com o nome: " + name);

        PointEntity point = new PointEntity();
        point.setId(UUID.randomUUID());
        fill(point, pointRequest);

        return repository.insert(point);
    }

    public List<PointEntity> findAll() {
        return repository.findAll();
    }

    public PointEntity findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ponto não localizado com o ID: " + id));
    }

    public PointEntity update(UUID id, PointRequest pointRequest) {
        PointEntity point = findById(id);

        validate(pointRequest);

        String name = pointRequest.name().trim();
        if (repository.existsByNameAndIdNot(name, id))
            throw new RuntimeException("Já existe outro ponto cadastrado com o nome: " + name);

        fill(point, pointRequest);

        return repository.update(point);
    }

    public void delete(UUID id) {
        findById(id);
        repository.deleteById(id);
    }

    private void validate(PointRequest request) {
        if (request == null)
            throw new RuntimeException("Objeto nulo");
        if (request.name() == null || request.name().isBlank())
            throw new RuntimeException("Nome inválido");
        if (request.name().trim().length() > 100)
            throw new RuntimeException("Nome deve ter no máximo 100 caracteres");
        if (request.description() != null && request.description().trim().length() > 255)
            throw new RuntimeException("Descrição deve ter no máximo 255 caracteres");
        if (request.latitude() == null
                || request.latitude().compareTo(BigDecimal.valueOf(-90)) < 0
                || request.latitude().compareTo(BigDecimal.valueOf(90)) > 0)
            throw new RuntimeException("Latitude inválida - Intervalo aceito: -90 a 90");
        if (request.longitude() == null
                || request.longitude().compareTo(BigDecimal.valueOf(-180)) < 0
                || request.longitude().compareTo(BigDecimal.valueOf(180)) > 0)
            throw new RuntimeException("Longitude inválida - Intervalo aceito: -180 a 180");
    }

    private void fill(PointEntity point, PointRequest request) {
        point.setName(request.name().trim());
        point.setDescription(request.description() == null ? null : request.description().trim());
        point.setLatitude(request.latitude());
        point.setLongitude(request.longitude());
    }
}