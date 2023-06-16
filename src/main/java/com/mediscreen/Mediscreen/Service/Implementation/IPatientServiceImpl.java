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
            throw new RuntimeException("Patient with id:{%s} doesn't exist in DB!");
        }
    }

    @Override
    public PatientEntity findPatientByLastName(String lastName) {
        logger.info("find Patient by lastName{} call in IPatientServiceImpl", lastName);
        Optional<PatientEntity> patientEntityByLastName = patientRepository.findPatientByLastName(lastName);
       logger.info("the patient with de lastName{} has been find", lastName);
        return patientEntityByLastName.get();
    }

    @Override
    public PatientEntity savePatientEntity(PatientEntity patientEntity) {
        logger.debug("savePatientEntity method starts here, from IPatientEntity");
        PatientEntity patientSaved = patientRepository.save(patientEntity);
        logger.info("Patient with Id:{} has been successfully save", patientEntity.getId());
        return patientSaved;
    }
}
