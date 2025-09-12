package com.qare.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.qare.app.model.MedicalSupply;
import com.qare.app.service.QareService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QareController.class)
@AutoConfigureMockMvc(addFilters = false)
class QareControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    QareService service;

    @Test
    void create_success_returns201() throws Exception {
        var body = new MedicalSupply("Flour", 2, "kg");
        given(service.add(any(MedicalSupply.class))).willReturn(body);

        mockMvc.perform(post("/api/supplies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(status().is(201))
                .andExpect(header().string("Location", "http://localhost/api/supplies/Flour"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Flour"))
                .andExpect(jsonPath("$.amount").value(2))
                .andExpect(jsonPath("$.unitName").value("kg"));

        verify(service).add(any(MedicalSupply.class));
    }

    @Test
    void create_withInvalidPayload_returns400() throws Exception {
        var invalid = new MedicalSupply("Flour", -1, "kg");

        mockMvc.perform(post("/api/supplies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().is(400))
                .andExpect(status().isBadRequest());

        verify(service, never()).add(any(MedicalSupply.class));
    }

    @Test
    void readAll_success_returns200_withList() throws Exception {
        var list = List.of(
                new MedicalSupply("Alcohol", 1, "bottle"),
                new MedicalSupply("Bandage", 5, "pack")
        );
        given(service.readAll()).willReturn(list);

        mockMvc.perform(get("/api/supplies"))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Alcohol"))
                .andExpect(jsonPath("$[1].name").value("Bandage"));

        verify(service).readAll();
    }

    @Test
    void read_one_returns200_withBody() throws Exception {
        given(service.read("Flour")).willReturn(Optional.of(new MedicalSupply("Flour", 2, "kg")));

        mockMvc.perform(get("/api/supplies/Flour"))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value("Flour"))
                .andExpect(jsonPath("$.amount").value(2))
                .andExpect(jsonPath("$.unitName").value("kg"));

        verify(service).read("Flour");
    }

    @Test
    void read_missing_returns404() throws Exception {
        given(service.read("Missing")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/supplies/Missing"))
                .andExpect(status().is(404))
                .andExpect(status().isNotFound());

        verify(service).read("Missing");
    }

    @Test
    void update_existing_returns200_withUpdatedBody() throws Exception {
        var incoming = new MedicalSupply("IGNORED", 3, "box");
        given(service.update(new MedicalSupply("Syringe", 3, "box"))).willReturn(true);

        mockMvc.perform(put("/api/supplies/Syringe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Syringe"))
                .andExpect(jsonPath("$.amount").value(3))
                .andExpect(jsonPath("$.unitName").value("box"));

        verify(service).update(eq(new MedicalSupply("Syringe", 3, "box")));
    }

    @Test
    void update_missing_returns404() throws Exception {
        var incoming = new MedicalSupply("IGNORED", 1, "unit");
        given(service.update(new MedicalSupply("Nope", 1, "unit"))).willReturn(false);

        mockMvc.perform(put("/api/supplies/Nope")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isNotFound());

        verify(service).update(eq(new MedicalSupply("Nope", 1, "unit")));
    }

    @Test
    void update_withInvalidPayload_returns400() throws Exception {
        var invalid = new MedicalSupply("x", -5, "u");

        mockMvc.perform(put("/api/supplies/Anything")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().is(400))
                .andExpect(status().isBadRequest());

        verify(service, never()).update(any(MedicalSupply.class));
    }


    @Test
    void delete_existing_returns204() throws Exception {
        given(service.delete("Mask")).willReturn(true);

        mockMvc.perform(delete("/api/supplies/Mask"))
                .andExpect(status().is(204))
                .andExpect(status().isNoContent());

        verify(service).delete("Mask");
    }

    @Test
    void delete_missing_returns404() throws Exception {
        given(service.delete("Ghost")).willReturn(false);

        mockMvc.perform(delete("/api/supplies/Ghost"))
                .andExpect(status().isNotFound());

        verify(service).delete("Ghost");
    }
}
