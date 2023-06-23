package com.mediscreen.Mediscreen.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.Mediscreen.Repository.IPatientRepository;
import com.mediscreen.Mediscreen.Service.Implementation.IPatientServiceImpl;
import com.mediscreen.Mediscreen.Service.interfaces.IPatientService;
import com.mediscreen.Mediscreen.model.PatientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetPatients() throws Exception {
        // Arrange
        List<PatientEntity> expectedPatients;
        PatientEntity patient1 = new PatientEntity(1,"Jose", "Cardona", LocalDate.now(),"M", "101 Avenue", "525-556" );
        PatientEntity patient2 = new PatientEntity(2,"Luis", "Trujillo", LocalDate.now(),"M", "101 Avenue", "525-556" );

        expectedPatients = List.of(patient1, patient2);

        Mockito.when(patientService.getAllPatientEntity()).thenReturn(expectedPatients);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Cardona"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Luis"))
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
    void testGetPatientByLastName() throws Exception {
        List<PatientEntity> patientEntityList;

        PatientEntity patient = new PatientEntity(1,"Jose", "Cardona", LocalDate.now(),"M", "101 Avenue", "525-556");
        PatientEntity patient2 = new PatientEntity(2,"Luis", "Cardona", LocalDate.now(),"M", "101 Avenue", "525-556");

        patientEntityList = List.of(patient, patient2);

        String lastName = patient.getLastName();

        when(patientService.findPatientByLastName(lastName)).thenReturn(patientEntityList);

        mockMvc.perform(get("/api/patients/by-lastName/{lastName}", lastName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", is("Cardona")));



    }

}
