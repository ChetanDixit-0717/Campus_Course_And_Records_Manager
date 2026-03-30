package edu.ccrm.domain;

import java.time.LocalDate;

public class Instructor extends Person {
    private String department;
    private String specialization;

    public Instructor(String fullName, String email, LocalDate dateOfBirth, String department, String specialization) {
        super(fullName, email, dateOfBirth);
        this.department = department;
        this.specialization = specialization;
    }

    public String getRole() {
        return "Instructor";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String toString() {
        return "Instructor [Department: " + department + ", Specialization: " + specialization + ", " + super.toString() + "]";
    }
}