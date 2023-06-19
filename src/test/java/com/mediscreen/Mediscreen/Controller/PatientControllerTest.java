package com.mediscreen.Mediscreen.Controller;


import com.mediscreen.Mediscreen.Repository.IPatientRepository;
import com.mediscreen.Mediscreen.model.PatientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @MockBean
    private IPatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void PostRegistrationForm_shouldSucceedAndRedirected() throws Exception {
        mockMvc.perform(post("/patients/validate")
                        .param("name", "John")
                        .param("lastName", "Doe")
                        .param("birthDate", "2000-11-20")
                        .param("sex", "M")
                        .param("address", "101 NY")
                        .param("phone", "110-258")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients/list"))
        ;
    }


    @Test
    void getPatientsList() throws Exception {
        mockMvc.perform(get("/patients/list")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("patients/list"))
        ;
    }


    @Test
    void testAddUser() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("patients/add"))
                ;
    }

    @Test
    void testGetPatientInfo() throws Exception {
        Integer id = 1;
        PatientEntity patient = new PatientEntity();
        patient.setId(id);

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/patients/info/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("patients/info"))
                .andExpect(MockMvcResultMatchers.model().attribute("patients", patient));
    }

    @Test
    void testShowUpdateForm() throws Exception {
        Integer id = 1;
        PatientEntity patient = new PatientEntity();
        patient.setId(id);

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/update/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("patients/update"))
                .andExpect(MockMvcResultMatchers.model().attribute("patients", patient));
    }

    @Test
    void testUpdatePatient() throws Exception {
        Integer id = 1;
        PatientEntity patient = new PatientEntity();
        patient.setId(id);

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.post("/patients/update/{id}", id)
                .param("name", "John")
                .param("lastName", "Doe")
                .param("birthDate", "2000-11-20")
                .param("sex", "M")
                .param("address", "101 NY")
                .param("phone", "110-258")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/patients/list"));
    }

    @Test
    void testDeletePatient() throws Exception {
        Integer id = 1;
        PatientEntity patient = new PatientEntity();
        patient.setId(id);

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/delete/{id}", id))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/patients/list"));
    }

}
