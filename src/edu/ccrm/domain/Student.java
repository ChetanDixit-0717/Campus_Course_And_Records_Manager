package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String regNo;
    private StudentStatus status;
    private List<Enrollment> enrolledCourses;

    public Student(String fullName, String email, LocalDate dateOfBirth, String regNo) {
        super(fullName, email, dateOfBirth);
        this.regNo = regNo;
        this.status = StudentStatus.ACTIVE;
        this.enrolledCourses = new ArrayList<>();
    }

    public String getRole() {
        return "Student";
    }

    public String getRegNo() {
        return regNo;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public List<Enrollment> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrolledCourses.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrolledCourses.remove(enrollment);
    }

    public String toString() {
        return "Student [RegNo: " + regNo + ", Status: " + status + ", " + super.toString() + "]";
    }
    
    public enum StudentStatus {
        ACTIVE,
        INACTIVE,
        GRADUATED,
        DEACTIVATED
    }
}