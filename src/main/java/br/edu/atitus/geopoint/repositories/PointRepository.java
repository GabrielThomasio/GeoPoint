package br.edu.atitus.geopoint.repositories;

import br.edu.atitus.geopoint.entities.PointEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.Optional;

import java.util.List;
import java.util.UUID;

public class PointRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PointEntity> rowMapper = (rs, rowNum) -> {
        PointEntity point = new PointEntity();

        point.setId(rs.getObject("id", UUID.class));
        point.setName(rs.getString("name"));
        point.setDescription(rs.getString("description"));
        point.setLatitude(rs.getBigDecimal("latitude"));
        point.setLongitude(rs.getBigDecimal("longitude"));

        return point;
    };

    public PointRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PointEntity insert(PointEntity point) {

        String sql = """
        INSERT INTO points (
            id, name, description, latitude, longitude
        )
        VALUES (?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                point.getId(),
                point.getName(),
                point.getDescription(),
                point.getLatitude(),
                point.getLongitude()
        );

        return point;
    }

    public List<PointEntity> findAll() {

        String sql = """
        SELECT id, name, description, latitude, longitude
        FROM points
        ORDER BY name
        """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<PointEntity> findById(UUID id) {

        String sql = """
        SELECT id, name, description, latitude, longitude
        FROM points
        WHERE id = ?
        """;
        try {
            PointEntity point = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(point);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM points WHERE LOWER(name) = LOWER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public boolean existsByNameAndIdNot(String name, UUID id) {
        String sql = "SELECT COUNT(*) FROM points WHERE LOWER(name) = LOWER(?) AND id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, id);
        return count != null && count > 0;
    }

    public PointEntity update(PointEntity point) {
        String sql = """
        UPDATE points
        SET name = ?, description = ?, latitude = ?, longitude = ?
        WHERE id = ?
        """;

        jdbcTemplate.update(
                sql,
                point.getName(),
                point.getDescription(),
                point.getLatitude(),
                point.getLongitude(),
                point.getId()
        );

        return point;
    }

    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE FROM points WHERE id = ?", id);
    }
}