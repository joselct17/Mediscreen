package com.mediscreen.Mediscreen.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.Mediscreen.Repository.IPatientRepository;
import com.mediscreen.Mediscreen.Service.Implementation.IPatientServiceImpl;
import com.mediscreen.Mediscreen.model.PatientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;


import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @MockBean
    private IPatientRepository patientRepository;

    @InjectMocks
    private PatientController patientController;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private IPatientServiceImpl patientService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }


    @Test
    public void testGetPatients() throws Exception {
        // Arrange
        List<PatientEntity> patientList = new ArrayList<>();
        PatientEntity patient1 = new PatientEntity(1, "John", "Doe", LocalDate.now(), "M", "123 Main St", "555-1234");
        PatientEntity patient2 = new PatientEntity(2, "Jane", "Doe", LocalDate.now(), "F", "456 Oak Ave", "555-5678");
        patientList.add(patient1);
        patientList.add(patient2);

        // Créez une page à partir de la liste de patients
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<PatientEntity> patientPage = new PageImpl<>(patientList, pageable, patientList.size());

        when(patientService.getPaginatedPatients(pageable)).thenReturn(patientPage);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("Jane"))
                .andReturn();
    }


    @Test
    void PostRegistrationForm_shouldSucceed() throws Exception {

        PatientEntity patientEntityNew = new PatientEntity(1,"Jose", "Cardona", LocalDate.now(),"M", "101 Avenue", "525-556");

        when(patientService.savePatientEntity(patientEntityNew)).thenReturn(patientEntityNew);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientEntityNew)))
                .andExpect(status().isCreated())
        .andReturn();

        verify(patientService).savePatientEntity(any(PatientEntity.class));
    }



    @Test
    void testGetPatientById() throws Exception {
        Integer id = 1;
        PatientEntity patient = new PatientEntity(1,"Jose", "Cardona", LocalDate.now(),"M", "101 Avenue", "525-556");
        patient.setId(id);

        when(patientService.findPatientById(id)).thenReturn(patient);

        mockMvc.perform(get("/api/patients/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", is("Cardona")));

    }


    @Test
    void testUpdatePatient() throws Exception {

        PatientEntity patientToUdpate = new PatientEntity(1,"Jose", "Cardona", LocalDate.now(),"M", "101 Avenue", "525-556");
        Integer idPatient = patientToUdpate.getId();

        PatientEntity patientUpdated = new PatientEntity(1,"Jose", "Cardona", LocalDate.now(),"M", "200 rue Manal", "525-556");


        when(patientService.findPatientById(idPatient)).thenReturn(patientToUdpate);
        when(patientService.savePatientEntity(patientToUdpate)).thenReturn(patientUpdated);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/{id}", idPatient)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientToUdpate)))
                .andExpect(status().isOk())
                .andReturn();
        verify(patientService).updatedPatientEntity(anyInt(), any(PatientEntity.class));
    }

    @Test
    void testDeletePatient() throws Exception {
        Integer patientId = 1;
        PatientEntity patient = new PatientEntity();
        patient.setId(patientId);

        when(patientService.findPatientById(patientId)).thenReturn(patient);
        doNothing().when(patientService).deletePatientEntityById(patientId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patients/{id}", patientId))
                .andExpect(status().isOk());

        verify(patientService).deletePatientEntityById(anyInt());
    }

    @Test
    void testFindPatientByLastName() throws Exception {
        // Créez une liste de patients avec le même nom de famille
        List<PatientEntity> patientList = new ArrayList<>();
        PatientEntity patient1 = new PatientEntity(1, "John", "Doe", LocalDate.now(), "M", "123 Main St", "555-1234");
        PatientEntity patient2 = new PatientEntity(2, "Jane", "Doe", LocalDate.now(), "F", "456 Oak Ave", "555-5678");
        patientList.add(patient1);
        patientList.add(patient2);

        // Créez une page à partir de la liste de patients
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<PatientEntity> patientPage = new PageImpl<>(patientList, pageable, patientList.size());

        // Définissez le comportement attendu du service patientService
        String lastName = "Doe";
        when(patientService.findPatientByLastName(lastName, pageable)).thenReturn(patientPage);

        // Effectuez une requête GET sur le point de terminaison
        mockMvc.perform(get("/api/patients/by-lastName/{lastName}", lastName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].lastName", is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name", is("Jane")));

        // Vérifiez que la méthode du service patientService a été appelée avec les bons paramètres
        verify(patientService).findPatientByLastName(lastName, pageable);
    }



}
