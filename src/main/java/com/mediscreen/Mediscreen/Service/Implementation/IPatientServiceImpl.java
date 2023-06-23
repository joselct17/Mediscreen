package com.mediscreen.Mediscreen.Service.Implementation;


import com.mediscreen.Mediscreen.Repository.IPatientRepository;
import com.mediscreen.Mediscreen.Service.interfaces.IPatientService;
import com.mediscreen.Mediscreen.model.PatientEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IPatientServiceImpl implements IPatientService {

    Logger logger = LoggerFactory.getLogger(IPatientServiceImpl.class);

    @Autowired
    IPatientRepository patientRepository;

    @Override
    public Iterable<PatientEntity> getAllPatientEntity() {
        logger.info("Get all patients call, IPatientServiceImpl");
        Iterable<PatientEntity> patientEntityList = patientRepository.findAll();
        logger.info("return patientEntityList, IPatientServiceImpl");
        return patientEntityList;
    }

    @Override
    public void deletePatientEntityById(Integer id) {
        logger.info("Delete patient by Id{} call, IPatientServiceImpl", id);
        Optional<PatientEntity> patientById = patientRepository.findById(id);
        if (patientById.isPresent()) {
            logger.info("Patient with id:{} has been successfully deleted", id);
            patientRepository.deleteById(id);
        } else {
            logger.debug("Patient with id:{} doesn't exist in DB!", id);
            throw new RuntimeException("Patient with id:{%s} doesn't exist in DB!");
        }
    }

    @Override
    public PatientEntity findPatientById(Integer id) {
        logger.info("find Patient by Id{} call in IPatientServiceImpl", id);
        Optional<PatientEntity> patientById = patientRepository.findById(id);
        if (patientById.isPresent()) {
            logger.info("Patient with id:{} has been found in IPatientServiceImpl", id);
            return patientById.get();
        } else {
            logger.info("Patient with id{} is not found in IPatientServiceImpl,", id);
            throw new RuntimeException("Patient with id:{} doesn't exist in DB!");
        }
    }


    @Override
    public List<PatientEntity> findPatientByLastName(String lastName) {
        logger.debug("getPatientByLastName from PatientServiceImpl starts here");
        List<PatientEntity>patient = patientRepository.findPatientByLastName(lastName);

        if (patient.isEmpty()) {
            logger.error("Patient doesn't exist in DB with lastName:{{}}", lastName);
            throw new RuntimeException("Patient with lastName:{%s} doesn't exist in DB!".formatted(lastName));
        }
        logger.info("Patient has been retrieved successfully by lastName:{{}}, from PatientServiceImpl", lastName);
        return patient;
    }


    @Override
    public PatientEntity savePatientEntity(PatientEntity patientEntity) {
        logger.debug("savePatientEntity method starts here, from IPatientServiceImpl");
        PatientEntity patientSaved = patientRepository.save(patientEntity);
        logger.info("Patient with Id:{} has been successfully save", patientEntity.getId());
        return patientSaved;
    }

    @Override
    public PatientEntity updatedPatientEntity(Integer id, PatientEntity patientEntity) {
        logger.debug("updatedPatientEntityById method starts here, from IPatientServiceImpl");
        Optional<PatientEntity> patientById = patientRepository.findById(id);
        if (patientById.isPresent()) {
            PatientEntity patientUpdated = patientById.get();
            patientById.get().setId(patientEntity.getId());
            patientById.get().setName(patientEntity.getName());
            patientById.get().setPhone(patientEntity.getPhone());
            patientById.get().setSex(patientEntity.getSex());
            patientById.get().setBirthDate(patientEntity.getBirthDate());
            patientById.get().setLastName(patientEntity.getLastName());
            patientById.get().setAddress(patientEntity.getAddress());
            patientRepository.save(patientById.get());
            return patientUpdated;
        } else {
            logger.debug("Any patient  exist with id:{} in DB!", id);
            throw new RuntimeException("Any patient  exist with id:{%s}".formatted(id));
        }
    }
}

