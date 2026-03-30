package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import edu.ccrm.domain.Grade;

public class EnrollmentService {
    private List<Enrollment> enrollments;

    public EnrollmentService() {
        this.enrollments = new ArrayList<>();
    }

    public void enrollStudent(Student student, Course course) throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        if (enrollments.stream().anyMatch(e -> e.getStudent().equals(student) && e.getCourse().equals(course))) {
            throw new DuplicateEnrollmentException("Student " + student.getFullName() + " is already enrolled in " + course.getTitle());
        }

        int currentCredits = student.getEnrolledCourses().stream()
                                    .filter(e -> e.getCourse().getSemester().equals(course.getSemester()))
                                    .mapToInt(e -> e.getCourse().getCredits())
                                    .sum();
        int maxCredits = 18;
        if (currentCredits + course.getCredits() > maxCredits) {
            throw new MaxCreditLimitExceededException("Enrolling in " + course.getTitle() + " would exceed the max credit limit of " + maxCredits + " for " + course.getSemester());
        }

        Enrollment newEnrollment = new Enrollment(student, course);
        enrollments.add(newEnrollment);
        student.addEnrollment(newEnrollment);
        System.out.println("Student " + student.getFullName() + " enrolled in " + course.getTitle());
    }

    public void unenrollStudent(Student student, Course course) {
        Optional<Enrollment> enrollmentToRemove = enrollments.stream()
                                                            .filter(e -> e.getStudent().equals(student) && e.getCourse().equals(course))
                                                            .findFirst();
        if (enrollmentToRemove.isPresent()) {
            enrollments.remove(enrollmentToRemove.get());
            student.removeEnrollment(enrollmentToRemove.get());
            System.out.println("Student " + student.getFullName() + " unenrolled from " + course.getTitle());
        } else {
            System.out.println("Error: Enrollment not found for " + student.getFullName() + " in " + course.getTitle());
        }
    }

    public void recordMarks(Student student, Course course, double marks) {
        Optional<Enrollment> enrollment = enrollments.stream()
                                                    .filter(e -> e.getStudent().equals(student) && e.getCourse().equals(course))
                                                    .findFirst();
        if (enrollment.isPresent()) {
            enrollment.get().setMarks(marks);
            enrollment.get().setGrade(Grade.fromMarks(marks)); // Compute grade
            System.out.println("Marks " + marks + " recorded for " + student.getFullName() + " in " + course.getTitle() + ". Grade: " + enrollment.get().getGrade());
        } else {
            System.out.println("Error: Enrollment not found to record marks.");
        }
    }

    public List<Enrollment> getEnrollmentsForStudent(Student student) {
        return enrollments.stream().filter(e -> e.getStudent().equals(student)).collect(Collectors.toList());
    }

    // TODO: Add methods for computing GPA (might delegate to TranscriptService)
}
