package com.qare.app.service;

import com.qare.app.config.DBConfig;
import com.qare.app.model.MedicalSupply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class QareServiceTest {

    @Mock DBConfig db;
    @InjectMocks QareService service;

    @Captor ArgumentCaptor<MedicalSupply> supplyCaptor;

    @BeforeEach
    void setup() {}

    @Test
    void add_normalizes_and_returns_created() {
        var input = new MedicalSupply("  Flour  ", 2, "  kg  ");

        service.add(input);

        verify(db).create(supplyCaptor.capture());
        var passed = supplyCaptor.getValue();

        // Expect leading or trailing spaces removed e.g., "  kg  "
        assertThat(passed.name()).isEqualTo("Flour");
        assertThat(passed.amount()).isEqualTo(2);
        assertThat(passed.unitName()).isEqualTo("kg");
    }

    @Test
    void add_duplicate_exception_from_db() {
        var input = new MedicalSupply("Flour", 2, "kg");
        willThrow(new DataIntegrityViolationException("dup"))
                .given(db).create(any(MedicalSupply.class));
        assertThatThrownBy(() -> service.add(input))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void readAll_delegates_to_db() {
        var list = List.of(new MedicalSupply("A", 1, "u"));
        given(db.readAll()).willReturn(list);

        assertThat(service.readAll()).isSameAs(list);
        verify(db).readAll();
    }

    @Test
    void read_normalizes_name() {
        given(db.read("Bandage")).willReturn(Optional.of(new MedicalSupply("Bandage", 1, "pack")));

        var result = service.read("  Bandage  ");

        assertThat(result).isPresent();
        verify(db).read("Bandage");
    }

    @Test
    void update_normalizes_then_returning_boolean() {
        var input = new MedicalSupply("  Syringe  ", 3, "  box ");
        given(db.update(any())).willReturn(true);

        boolean updated = service.update(input);

        assertThat(updated).isTrue();
        verify(db).update(supplyCaptor.capture());
        var passed = supplyCaptor.getValue();
        assertThat(passed.name()).isEqualTo("Syringe");
        assertThat(passed.unitName()).isEqualTo("box");
    }

    @Test
    void delete_normalizes_name() {
        given(db.delete("Mask")).willReturn(true);

        boolean deleted = service.delete("  Mask  ");

        assertThat(deleted).isTrue();
        verify(db).delete("Mask");
    }
}
