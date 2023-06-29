package com.mediscreen.Mediscreen.Repository;

import com.mediscreen.Mediscreen.model.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IPatientRepository extends CrudRepository<PatientEntity, Integer> {

    Page<PatientEntity> findPatientByLastName(String lastName, Pageable pageable);

    Page<PatientEntity> findAll(Pageable pageable);
}
