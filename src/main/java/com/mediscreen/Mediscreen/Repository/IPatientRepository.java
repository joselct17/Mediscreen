package com.mediscreen.Mediscreen.Repository;

import com.mediscreen.Mediscreen.model.PatientEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IPatientRepository extends CrudRepository<PatientEntity, Integer> {

    Optional<PatientEntity> findPatientByLastName(String lastName);
}
