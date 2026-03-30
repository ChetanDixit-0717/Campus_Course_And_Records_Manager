package edu.ccrm.service;

import edu.ccrm.domain.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentService {
    private final List<Student> students;

    public StudentService() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        if (students.stream().anyMatch(s -> s.getRegNo().equals(student.getRegNo()))) {
            System.out.println("Error: Student with registration number " + student.getRegNo() + " already exists.");
            return;
        }
        this.students.add(student);
        System.out.println("Student added: " + student.getFullName());
    }

    public Optional<Student> getStudentByRegNo(String regNo) {
        return students.stream()
                .filter(s -> s.getRegNo().equalsIgnoreCase(regNo))
                .findFirst();
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public void updateStudent(Student updatedStudent) {
        Optional<Student> studentOpt = getStudentByRegNo(updatedStudent.getRegNo());
        if (studentOpt.isPresent()) {
            Student existingStudent = studentOpt.get();
            existingStudent.setFullName(updatedStudent.getFullName());
            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
            System.out.println("Student updated: " + existingStudent.getFullName());
        } else {
            System.out.println("Error: Student with registration number " + updatedStudent.getRegNo() + " not found for update.");
        }
    }

    public void deactivateStudent(String regNo) {
        getStudentByRegNo(regNo).ifPresent(student -> {
            student.setStatus(Student.StudentStatus.DEACTIVATED);
            System.out.println("Student " + student.getFullName() + " has been deactivated.");
        });
    }
}