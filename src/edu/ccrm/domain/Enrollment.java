package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Enrollment {
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private Double marks;
    private Grade grade;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDate.now();
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public Double getMarks() {
        return marks;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setMarks(Double marks) {
        assert marks >= 0 && marks <= 100 : "Marks must be between 0 and 100.";
        this.marks = marks;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String toString() {
        return "Enrollment [Student: " + student.getFullName() +
               ", Course: " + course.getTitle() +
               ", Date: " + enrollmentDate +
               ", Marks: " + (marks != null ? marks : "N/A") +
               ", Grade: " + (grade != null ? grade : "N/A") + "]";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(student, that.student) &&
               Objects.equals(course, that.course);
    }

    public int hashCode() {
        return Objects.hash(student, course);
    }
}
