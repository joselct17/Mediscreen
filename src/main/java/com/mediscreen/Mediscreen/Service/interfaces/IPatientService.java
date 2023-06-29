package com.mediscreen.Mediscreen.Service.interfaces;

import com.mediscreen.Mediscreen.model.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IPatientService {

    Iterable<PatientEntity> getAllPatientEntity();

    void deletePatientEntityById(Integer id);

    PatientEntity findPatientById(Integer id);

    Page<PatientEntity> findPatientByLastName(String lastName, Pageable pageable);


    PatientEntity savePatientEntity(PatientEntity patientEntity);


    PatientEntity updatedPatientEntity(Integer id, PatientEntity patientEntity);

    Page<PatientEntity> getPaginatedPatients(Pageable pageable);
}
