package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseService {
    private final List<Course> courses;

    public CourseService() {
        this.courses = new ArrayList<>();
    }

    public void addCourse(Course course) {
        if (courses.stream().anyMatch(c -> c.getCode().equals(course.getCode()))) {
            System.out.println("Error: Course with code " + course.getCode() + " already exists.");
            return;
        }
        this.courses.add(course);
        System.out.println("Course added: " + course.getTitle());
    }

    public Optional<Course> getCourseByCode(String code) {
        return courses.stream()
                .filter(c -> c.getCode().getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    public List<Course> searchCourses(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return courses.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(lowerKeyword) ||
                        c.getCode().getCode().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public List<Course> filterCourses(Instructor instructor, String department, Semester semester) {
        return courses.stream()
                .filter(c -> (instructor == null || c.getInstructor().equals(instructor)))
                .filter(c -> (department == null || department.isBlank() || c.getDepartment().equalsIgnoreCase(department)))
                .filter(c -> (semester == null || c.getSemester().equals(semester)))
                .collect(Collectors.toList());
    }

    public void updateCourse(Course updatedCourse) {
        getCourseByCode(updatedCourse.getCode().getCode()).ifPresent(course -> {
            course.setTitle(updatedCourse.getTitle());
            course.setCredits(updatedCourse.getCredits());
            course.setDepartment(updatedCourse.getDepartment());
            course.setInstructor(updatedCourse.getInstructor());
            course.setSemester(updatedCourse.getSemester());
            System.out.println("Course updated: " + course.getTitle());
        });
    }

    public void deactivateCourse(String courseCode) {
        getCourseByCode(courseCode).ifPresent(course -> {
            courses.remove(course);
            System.out.println("Course deactivated (removed): " + course.getTitle());
        });
    }
}