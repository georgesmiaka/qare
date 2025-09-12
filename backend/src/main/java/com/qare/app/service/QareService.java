package com.qare.app.service;

import com.qare.app.config.DBConfig;
import com.qare.app.model.MedicalSupply;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QareService {
    private final DBConfig db;
    public QareService(DBConfig db) { this.db = db; }

    public MedicalSupply add(MedicalSupply medicalSupply) {
        var s = normalize(medicalSupply);
        db.create(s);
        return s;
    }

    @Transactional(readOnly = true)
    public List<MedicalSupply> readAll() {
        return db.readAll();
    }

    @Transactional(readOnly = true)
    public Optional<MedicalSupply> read(String name) {
        return db.read(normalizeName(name));
    }

    public boolean update(MedicalSupply supply) {
        return db.update(normalize(supply));
    }

    public boolean delete(String name) {
        return db.delete(normalizeName(name));
    }

    private static MedicalSupply normalize(MedicalSupply s) {
        if (s == null) throw new IllegalArgumentException("MedicalSupply must not be null");
        return new MedicalSupply(
                s.name() == null ? null : s.name().strip(),
                s.amount(),
                s.unitName() == null ? null : s.unitName().strip()
        );
    }
    private static String normalizeName(String name) {
        if (name == null) throw new IllegalArgumentException("name must not be null");
        return name.strip();
    }
}