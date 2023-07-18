package com.mediscreen.Mediscreen.Controller;


import com.mediscreen.Mediscreen.Service.Implementation.IPatientServiceImpl;
import com.mediscreen.Mediscreen.model.PatientEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PatientController handles all HTTP requests related to patients.
 * It exposes the patient-related APIs to the clients.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    Logger logger = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    private IPatientServiceImpl iPatientService;


    @Operation(summary = "Obtenir la liste paginée des patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste paginée des patients récupérée avec succès")
    })
    @GetMapping
    public Page<PatientEntity> getPatients( @Parameter(description = "Numéro de page") @RequestParam(defaultValue = "0") int page,
                                            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {
        logger.debug("getPatients starts, PatientController");
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientEntity> patientPage = iPatientService.getPaginatedPatients(pageable);
        logger.info("getPatients Paginated patients list success");
        return patientPage;
    }

    @Operation(summary = "Valider un nouveau patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nouveau patient validé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
    })
    @PostMapping
    public ResponseEntity<PatientEntity> validate(@RequestBody @Validated PatientEntity patientEntity) {
        logger.debug("validate starts here, from PatientController");
        PatientEntity patientNew = iPatientService.savePatientEntity(patientEntity);
        logger.info("POST:/patients/validate, Validate new patient success");
        return new ResponseEntity <>(patientNew, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtenir un patient par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientEntity> getPatientById( @Parameter(description = "ID du patient") @PathVariable Integer id) {
        logger.debug("getPatientById  starts here, from PatientController");
        PatientEntity patientById = iPatientService.findPatientById(id);
        logger.info("REQUEST:/patients/info, Patient width id:{} successfully retrieved", id);
        return ResponseEntity.ok(patientById);
    }

    @Operation(summary = "Mettre à jour un patient par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatientEntity> updatePatientById( @Parameter(description = "ID du patient") @PathVariable Integer id, @RequestBody @Validated PatientEntity patientEntity) {
        logger.debug("updatePatientById starts here, from PatientController");
        PatientEntity patientEntityUpdated = iPatientService.updatedPatientEntity(id, patientEntity);
        logger.info("Patient with id:{} has been successfully updated, from PatientController", id);
        return ResponseEntity.ok(patientEntityUpdated);
    }

    @Operation(summary = "Supprimer un patient par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient supprimé avec succès"),
            @ApiResponse(responseCode = "204", description = "Patient non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePatient( @Parameter(description = "ID du patient") @PathVariable Integer id) {

        try {
            iPatientService.deletePatientEntityById(id);
            logger.info("Patient with id:{} has been successfully deleted from PatientController", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Patient with id:{} not found DB from PatientController", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Trouver un patient par son nom de famille")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient(s) récupéré(s) avec succès")
    })
    @GetMapping("/by-lastName/{lastName}")
    public ResponseEntity<Page<PatientEntity>> findPatientByLastName( @Parameter(description = "Nom de famille du patient") @PathVariable  String lastName, @Parameter(description = "Numéro de page") @RequestParam(defaultValue = "0") int page,
                                                                      @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {
        logger.debug("findPatientByLastName starts here from PatientController");
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientEntity> patientsByLastNameList = iPatientService.findPatientByLastName(lastName,pageable);
        logger.info("Patient with lastName:{} has been found from PatientController", lastName);
        return ResponseEntity.ok(patientsByLastNameList);
    }


}
