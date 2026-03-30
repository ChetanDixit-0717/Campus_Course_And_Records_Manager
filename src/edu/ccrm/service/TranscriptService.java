package edu.ccrm.service;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TranscriptService {

    public double calculateGPA(Student student) {
        List<Enrollment> studentEnrollments = student.getEnrolledCourses();
        if (studentEnrollments.isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0;
        int totalCredits = 0;

        for (Enrollment enrollment : studentEnrollments) {
            if (enrollment.getGrade() != null && enrollment.getGrade() != Grade.F) {
                totalGradePoints += enrollment.getGrade().getGradePoint() * enrollment.getCourse().getCredits();
                totalCredits += enrollment.getCourse().getCredits();
            }
        }

        if (totalCredits == 0) {
            return 0.0;
        }
        return totalGradePoints / totalCredits;
    }

    public String generateTranscript(Student student) {
        StringBuilder transcript = new StringBuilder();
        transcript.append("--- Transcript for ").append(student.getFullName()).append(" (RegNo: ").append(student.getRegNo()).append(") ---\n");
        transcript.append("Email: ").append(student.getEmail()).append("\n");
        transcript.append("Status: ").append(student.getStatus()).append("\n");
        transcript.append("Date of Birth: ").append(student.getDateOfBirth()).append("\n\n");
        transcript.append("Enrolled Courses:\n");

        if (student.getEnrolledCourses().isEmpty()) {
            transcript.append("  No courses enrolled.\n");
        } else {
            student.getEnrolledCourses().forEach(enrollment -> {
                transcript.append("  - ").append(enrollment.getCourse().getTitle())
                        .append(" (Credits: ").append(enrollment.getCourse().getCredits()).append(")")
                        .append(", Marks: ").append(enrollment.getMarks() != null ? enrollment.getMarks() : "N/A")
                        .append(", Grade: ").append(enrollment.getGrade() != null ? enrollment.getGrade().name() : "N/A")
                        .append("\n");
            });
        }

        transcript.append("\nOverall GPA: ").append(String.format("%.2f", calculateGPA(student))).append("\n");
        transcript.append("---------------------------------------------------\n");
        return transcript.toString();
    }

    public void printGpaDistributionReport(List<Student> students) {
        System.out.println("\n--- GPA Distribution Report ---");
        Map<String, Long> gpaDistribution = students.stream()
                .collect(Collectors.groupingBy(
                        student -> {
                            double gpa = calculateGPA(student);
                            if (gpa >= 9.0) return "9.0 - 10.0 (Excellent)";
                            if (gpa >= 8.0) return "8.0 - 8.9 (Very Good)";
                            if (gpa >= 7.0) return "7.0 - 7.9 (Good)";
                            if (gpa >= 6.0) return "6.0 - 6.9 (Average)";
                            return "Below 6.0 (Needs Improvement)";
                        },
                        Collectors.counting()
                ));

        gpaDistribution.forEach((range, count) ->
                System.out.println("GPA Range " + range + ": " + count + " student(s)"));
        System.out.println("---------------------------------");
    }
}