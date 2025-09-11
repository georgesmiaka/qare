package com.qare.app.config;

import com.qare.app.model.MedicalSupply;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component // singleton
public class DBConfig {

    private final JdbcTemplate jdbc;

    public DBConfig(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostConstruct
    public void initDB() {
        // name is the primary key here (string); amount must be non-negative
        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS supplies (
                name VARCHAR(255) PRIMARY KEY,
                amount INT NOT NULL CHECK (amount >= 0),
                "unitName" VARCHAR(255) NOT NULL
            )
        """);
    }

    public void create(MedicalSupply s) {
        validate(s);
        // will throw if name already exists (PK violation)
        jdbc.update("""
            INSERT INTO supplies (name, amount, "unitName") VALUES (?,?,?)
        """, s.name(), s.amount(), s.unitName());
    }

    public Optional<MedicalSupply> read(String name) {
        var list = jdbc.query("""
            SELECT name, amount, "unitName" FROM supplies WHERE name = ?
        """, rowMapper(), name);
        return list.stream().findFirst();
    }

    public List<MedicalSupply> readAll() {
        return jdbc.query("""
            SELECT name, amount, "unitName" FROM supplies ORDER BY name
        """, rowMapper());
    }

    public boolean update(MedicalSupply s) {
        validate(s);
        int rows = jdbc.update("""
            UPDATE supplies SET amount = ?, "unitName" = ? WHERE name = ?
        """, s.amount(), s.unitName(), s.name());
        return rows == 1;
    }

    public boolean delete(String name) {
        int rows = jdbc.update("""
            DELETE FROM supplies WHERE name = ?
        """, name);
        return rows == 1;
    }


    private static void validate(MedicalSupply s) {
        if (s == null) throw new IllegalArgumentException("Supply must not be null");
        if (s.name() == null || s.name().isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (s.amount() < 0) {
            throw new IllegalArgumentException("amount must be non-negative");
        }
        if (s.unitName() == null || s.unitName().isBlank()) {
            throw new IllegalArgumentException("unitName must not be blank");
        }
    }

    private static RowMapper<MedicalSupply> rowMapper() {
        return new RowMapper<>() {
            @Override public MedicalSupply mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MedicalSupply(
                        rs.getString("name"),
                        rs.getInt("amount"),
                        rs.getString("unitName")); // quoted in DDL, readable without quotes
            }
        };
    }
}

