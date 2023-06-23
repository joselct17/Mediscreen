package com.mediscreen.Mediscreen.Repository;

import com.mediscreen.Mediscreen.model.PatientEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPatientRepository extends CrudRepository<PatientEntity, Integer> {

    List<PatientEntity> findPatientByLastName(String lastName);
}
