package com.mediscreen.Mediscreen.Services;


import com.mediscreen.Mediscreen.Repository.IPatientRepository;
import com.mediscreen.Mediscreen.Service.Implementation.IPatientServiceImpl;
import com.mediscreen.Mediscreen.model.PatientEntity;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PatientServiceTest {

    @Mock
    private IPatientRepository patientRepository;

    @InjectMocks
    private IPatientServiceImpl patientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetAllPatientEntity() {
        // Arrange
        List<PatientEntity> expectedPatientList = Arrays.asList(
                new PatientEntity(1, "Jose", "Cardona", LocalDate.now(), "M", "101-Ave", "1551-5485"),
                new PatientEntity(2, "Luis", "Trujillo", LocalDate.now(), "M", "101-Ave", "1551-5485")
        );

        when(patientRepository.findAll()).thenReturn(expectedPatientList);

        // Act
        Iterable<PatientEntity> result = patientService.getAllPatientEntity();

        // Assert
        assertThat(result).isNotNull();
        assertEquals(expectedPatientList, result);

        verify(patientRepository, times(1)).findAll();
    }


    @Test
    public void testGetPatientsWhenAnyPatientsInDB() {

        List<PatientEntity> patientEntities = new ArrayList<>();
        // Given
        when(patientRepository.findAll()).thenReturn(patientEntities);

        // When
        Iterable<PatientEntity> patients = patientService.getAllPatientEntity();

        List<PatientEntity> list = new ArrayList<>();
        for (PatientEntity p : patients) {
            list.add(p);
        }

        // Then

        assertThat(list.size()).isEqualTo(0);

    }

    @Test
    public void testDeletePatientEntityById_ExistingId_Success() {
        // Arrange
        Integer id = 1;
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(id);

        // Mocking repository behavior
        when(patientRepository.findById(id)).thenReturn(Optional.of(patientEntity));

        // Act
        patientService.deletePatientEntityById(id);

        // Assert
        verify(patientRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeletePatientEntityById_NonExistingId_ExceptionThrown() {
        // Arrange
        Integer id = 1;

        // Mocking repository behavior
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            patientService.deletePatientEntityById(id);
        });

        // Verify that deleteById was not called
        verify(patientRepository, never()).deleteById(id);
    }

    @Test
    public void testFindPatientById_ExistingId_Success() {
        // Arrange
        Integer id = 1;
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(id);

        // Mocking repository behavior
        when(patientRepository.findById(id)).thenReturn(Optional.of(patientEntity));

        // Act
        PatientEntity result = patientService.findPatientById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void testFindPatientById_NonExistingId_ExceptionThrown() {
        // Arrange
        Integer id = 1;

        // Mocking repository behavior
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            patientService.findPatientById(id);
        });
    }

    @Test
    public void testFindPatientByLastName_ExistingLastName_Success() {
        // Arrange
        String lastName = "Doe";
        List<PatientEntity> patientList = new ArrayList<>();
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setLastName(lastName);
        patientList.add(patientEntity);

        // Mocking repository behavior
        when(patientRepository.findPatientByLastName(lastName)).thenReturn(patientList);

        // Act
        List<PatientEntity> result = patientService.findPatientByLastName(lastName);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(lastName, result.get(0).getLastName());
    }

    @Test
    public void testFindPatientByLastName_NonExistingLastName_ExceptionThrown() {
        // Arrange
        String lastName = "Doe";

        // Mocking repository behavior
        when(patientRepository.findPatientByLastName(lastName)).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            patientService.findPatientByLastName(lastName);
        });
    }

    @Test
    public void testSavePatientEntity_Success() {
        // Arrange
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(1);
        patientEntity.setName("John");
        patientEntity.setLastName("Doe");

        // Mocking repository behavior
        when(patientRepository.save(patientEntity)).thenReturn(patientEntity);

        // Act
        PatientEntity result = patientService.savePatientEntity(patientEntity);

        // Assert
        assertNotNull(result);
        assertEquals(patientEntity.getId(), result.getId());
        assertEquals(patientEntity.getName(), result.getName());
        assertEquals(patientEntity.getLastName(), result.getLastName());
    }

    @Test
    public void testUpdatedPatientEntity_Success() {
        // Arrange
        Integer id = 1;
        PatientEntity existingPatient = new PatientEntity();
        existingPatient.setId(id);
        existingPatient.setName("John");
        existingPatient.setLastName("Doe");

        PatientEntity updatedPatient = new PatientEntity();
        updatedPatient.setId(id);
        updatedPatient.setName("Jane");
        updatedPatient.setLastName("Smith");

        // Mocking repository behavior
        when(patientRepository.findById(id)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(existingPatient)).thenReturn(existingPatient);

        // Act
        PatientEntity result = patientService.updatedPatientEntity(id, updatedPatient);

        // Assert
        assertNotNull(result);
        assertEquals(updatedPatient.getId(), result.getId());
        assertEquals(updatedPatient.getName(), result.getName());
        assertEquals(updatedPatient.getLastName(), result.getLastName());
    }

    @Test
    public void testUpdatedPatientEntity_Failure() {
        // Arrange
        Integer id = 1;
        PatientEntity existingPatient = new PatientEntity();
        existingPatient.setId(id);
        existingPatient.setName("John");
        existingPatient.setLastName("Doe");

        PatientEntity updatedPatient = new PatientEntity();
        updatedPatient.setId(id);
        updatedPatient.setName("Jane");
        updatedPatient.setLastName("Smith");

        // Mocking repository behavior
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            patientService.updatedPatientEntity(id, updatedPatient);
        });
    }
}
