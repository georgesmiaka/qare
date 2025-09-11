package com.qare.app.service;

import com.qare.app.config.DBConfig;
import com.qare.app.model.MedicalSupply;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QareService {
    private final DBConfig db;
    public QareService(DBConfig db) {
        this.db = db;
    }

    public MedicalSupply add(MedicalSupply medicalSupply) {
        try {
            MedicalSupply normalized = new MedicalSupply(
                    medicalSupply.name().trim(),
                    medicalSupply.amount(),
                    medicalSupply.unitName().trim()
            );
            db.create(normalized);
            return normalized;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MedicalSupply> readAll() {
        try {
            return db.readAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<MedicalSupply> read(String name) {
        try {
            return db.read(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(MedicalSupply supply) {
        try {
            return db.update(supply);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String name) {
        try {
            return db.delete(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
