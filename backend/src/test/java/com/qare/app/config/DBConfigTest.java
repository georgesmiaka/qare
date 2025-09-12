package com.qare.app.config;

import com.qare.app.model.MedicalSupply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import(DBConfig.class)
class DBConfigTest {

    @Autowired
    DBConfig db;

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void verifyDBandTableWereCreated() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SUPPLIES'",
                Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void create_and_read_item() {
        var s = new MedicalSupply("Gauze", 10, "pack");
        db.create(s);

        Optional<MedicalSupply> back = db.read("Gauze");
        assertThat(back).isPresent();
        assertThat(back.get()).isEqualTo(s);
    }

    @Test
    void readAll_returnsAlphabeticalByName() {
        db.create(new MedicalSupply("Bandage", 5, "pack"));
        db.create(new MedicalSupply("Alcohol", 2, "bottle"));
        db.create(new MedicalSupply("Cotton", 7, "bag"));

        List<MedicalSupply> all = db.readAll();
        assertThat(all).extracting(MedicalSupply::name)
                .containsExactly("Alcohol", "Bandage", "Cotton"); // order by name
    }

    @Test
    void update_existing_returnsTrue() {
        db.create(new MedicalSupply("Syringe", 1, "box"));

        boolean updated = db.update(new MedicalSupply("Syringe", 3, "box"));
        assertThat(updated).isTrue();
    }

    @Test
    void update_missing_returnsFalse() {
        boolean updated = db.update(new MedicalSupply("Nope", 1, "unit"));
        assertThat(updated).isFalse();
    }

    @Test
    void delete_existing_returnsTrue_andRemovesRow() {
        db.create(new MedicalSupply("Thermometer", 2, "pcs"));
        boolean deleted = db.delete("Thermometer");
        assertThat(deleted).isTrue();
        assertThat(db.read("Thermometer")).isEmpty();
    }

    @Test
    void delete_missing_returnsFalse() {
        assertThat(db.delete("Missing")).isFalse();
    }

    @Test
    void create_duplicateName_throwsDataIntegrityViolation() {
        db.create(new MedicalSupply("Mask", 10, "box"));
        assertThatThrownBy(() -> db.create(new MedicalSupply("Mask", 5, "box")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void validate_rejectsNegativeAmount_beforeHittingDB() {
        assertThatThrownBy(() -> db.create(new MedicalSupply("Bad", -1, "unit")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-negative");
    }

    @Test
    void db_enforces_check_constraint_amount_nonNegative() {
        // Bypass DBConfig.validate to prove the DB constraint exists.
        assertThatThrownBy(() ->
                jdbc.update("INSERT INTO supplies (name, amount, \"unitName\") VALUES (?,?,?)",
                        "DBBad", -5, "unit"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
