package com.mediscreen.Mediscreen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;




@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PATIENT")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "patient_id")
    private  Integer id;

    @NotNull(message = "Name is mandatory")
    private String name;

    @NotNull(message = "LastName is mandatory")
    private String lastName;

    @NotNull(message = "BirthDate is mandatory")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "Gender is mandatory")
    private String sex;

    @NotNull(message = "Address is mandatory")
    private String address;

    @NotNull(message = "Phone is mandatory")
    private String phone;


}
