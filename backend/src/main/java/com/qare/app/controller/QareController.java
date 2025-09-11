package com.qare.app.controller;

import com.qare.app.model.MedicalSupply;
import com.qare.app.service.QareService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/supplies")
public class QareController {

    private final QareService service;

    public QareController(QareService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<MedicalSupply> create(@RequestBody @Valid MedicalSupply supply,
                                                       UriComponentsBuilder uri) {
        MedicalSupply created = service.add(supply);
        URI location = uri.path("/api/supplies/{name}")
                .buildAndExpand(created.name())
                .toUri();
        return ResponseEntity.created(location).body(created); // 201
    }

    @GetMapping
    public List<MedicalSupply> readAll() {
        return service.readAll(); // 200
    }

    @GetMapping("/{name}")
    public ResponseEntity<MedicalSupply> read(@PathVariable String name) {
        return service.read(name)
                .map(ResponseEntity::ok)         // 200
                .orElse(ResponseEntity.notFound().build()); // 404
    }

    @PutMapping("/{name}")
    public ResponseEntity<MedicalSupply> update(@PathVariable String name,
                                         @RequestBody @Valid MedicalSupply body) {

        MedicalSupply toUpdate = new MedicalSupply(name, body.amount(), body.unitName());
        boolean updated = service.update(toUpdate);
        return updated ? ResponseEntity.ok(toUpdate)           // 200
                : ResponseEntity.notFound().build();    // 404
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name) {
        boolean deleted = service.delete(name);
        return deleted ? ResponseEntity.noContent().build()    // 204
                : ResponseEntity.notFound().build();    // 404
    }
}
