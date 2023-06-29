package com.mediscreen.Mediscreen.Controller;


import com.mediscreen.Mediscreen.Service.Implementation.IPatientServiceImpl;
import com.mediscreen.Mediscreen.model.PatientEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    Logger logger = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    private IPatientServiceImpl iPatientService;



    @GetMapping
    public Page<PatientEntity> getPatients(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        logger.debug("getPatients starts, PatientController");
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientEntity> patientPage = iPatientService.getPaginatedPatients(pageable);
        logger.info("getPatients Paginated patients list success");
        return patientPage;
    }


    @PostMapping
    public ResponseEntity<PatientEntity> validate(@RequestBody @Validated PatientEntity patientEntity) {
        logger.debug("validate starts here, from PatientController");
        PatientEntity patientNew = iPatientService.savePatientEntity(patientEntity);
        logger.info("POST:/patients/validate, Validate new patient success");
        return new ResponseEntity <>(patientNew, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientEntity> getPatientById(@PathVariable Integer id) {
        logger.debug("getPatientById  starts here, from PatientController");
        PatientEntity patientById = iPatientService.findPatientById(id);
        logger.info("REQUEST:/patients/info, Patient width id:{} successfully retrieved", id);
        return ResponseEntity.ok(patientById);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PatientEntity> updatePatientById(@PathVariable Integer id, @RequestBody @Validated PatientEntity patientEntity) {
        logger.debug("updatePatientById starts here, from PatientController");
        PatientEntity patientEntityUpdated = iPatientService.updatedPatientEntity(id, patientEntity);
        logger.info("Patient with id:{} has been successfully updated, from PatientController", id);
        return ResponseEntity.ok(patientEntityUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePatient(@PathVariable Integer id) {

        try {
            iPatientService.deletePatientEntityById(id);
            logger.info("Patient with id:{} has been successfully deleted from PatientController", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Patient with id:{} not found DB from PatientController", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/by-lastName/{lastName}")
    public ResponseEntity<Page<PatientEntity>> findPatientByLastName(@PathVariable  String lastName, @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) {
        logger.debug("findPatientByLastName starts here from PatientController");
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientEntity> patientsByLastNameList = iPatientService.findPatientByLastName(lastName,pageable);
        logger.info("Patient with lastName:{} has been found from PatientController", lastName);
        return ResponseEntity.ok(patientsByLastNameList);
    }


}
