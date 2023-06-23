package com.mediscreen.Mediscreen.Service.interfaces;

import com.mediscreen.Mediscreen.model.PatientEntity;

import java.util.List;

public interface IPatientService {

    Iterable<PatientEntity> getAllPatientEntity();

    void deletePatientEntityById(Integer id);

    PatientEntity findPatientById(Integer id);

    List<PatientEntity> findPatientByLastName(String lastName);


    PatientEntity savePatientEntity(PatientEntity patientEntity);


    PatientEntity updatedPatientEntity(Integer id, PatientEntity patientEntity);
}
