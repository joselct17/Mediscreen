package com.mediscreen.Mediscreen.Controller;


import com.mediscreen.Mediscreen.Repository.IPatientRepository;
import com.mediscreen.Mediscreen.Service.Implementation.IPatientServiceImpl;
import com.mediscreen.Mediscreen.model.PatientEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PatientController {

    Logger logger = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    private IPatientServiceImpl iPatientService;

    @GetMapping("/patients/list")
    public String getPatients(Model model) {

        model.addAttribute("patients", iPatientService.getAllPatientEntity());
        logger.info("REQUEST:/patients/list");
        return "patients/list";
    }

    @GetMapping("/patients/add")
    public String addUser(PatientEntity bid, Model model) {

        logger.info("GET:/patients/add");
        model.addAttribute("patients", bid);
        return "patients/add";
    }

    @PostMapping("/patients/validate")
    public String validate(@Validated PatientEntity patientEntity, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            iPatientService.savePatientEntity(patientEntity);
            model.addAttribute("patients", iPatientService.getAllPatientEntity());
            logger.info("redirect:/patients/list");
            return "redirect:/patients/list";
        }
        logger.info("POST:/patients/validate");
        return "patients/add";
    }

    @GetMapping("/patients/info/{id}")
    public String getPatientInfo(@PathVariable("id") Integer id, Model model) {
        PatientEntity patients = iPatientService.findPatientById(id);
        model.addAttribute("patients", patients);
        logger.info("REQUEST:/patients/info");
        return "patients/info";
    }

    @GetMapping("/patients/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        PatientEntity patients = iPatientService.findPatientById(id);
        model.addAttribute("patients", patients);
        logger.info("GET:/patients/update");
        return "patients/update";
    }

    @PostMapping("/patients/update/{id}")
    public String updatePatient(@PathVariable("id") Integer id, @Validated PatientEntity patients,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("POST:/patients/update");
            return "patients/update";
        }
        patients.setId(id);
        iPatientService.savePatientEntity(patients);
        model.addAttribute("patients", iPatientService.getAllPatientEntity());
        logger.info("redirect:/patients/list");
        return "redirect:/patients/list";
    }

    @GetMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable("id") Integer id, Model model) {
        iPatientService.deletePatientEntityById(id);

        model.addAttribute("patients", iPatientService.getAllPatientEntity());
        logger.info("GET:/patients/delete");
        return "redirect:/patients/list";
    }

    @GetMapping("/patients/list/{lastName}")
    public String findByLastName(@PathVariable("lastName") String lastName, Model model) {
        PatientEntity patientsByLastName = iPatientService.findPatientByLastName(lastName);

        model.addAttribute("patients", iPatientService.getAllPatientEntity());
        model.addAttribute("patientsByLastNames", patientsByLastName);

        return "patients/list";
    }


}
