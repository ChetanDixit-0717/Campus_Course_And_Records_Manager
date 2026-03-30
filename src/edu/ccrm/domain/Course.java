package edu.ccrm.domain;

import java.util.Objects;

public class Course {
    private CourseCode code;
    private String title;
    private int credits;
    private Instructor instructor;
    private Semester semester;
    private String department;

    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
    }

    public CourseCode getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public Semester getSemester() {
        return semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String toString() {
        return "Course [Code: " + code + ", Title: " + title + ", Credits: " + credits +
               ", Instructor: " + (instructor != null ? instructor.getFullName() : "N/A") +
               ", Semester: " + semester + ", Department: " + department + "]";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    public int hashCode() {
        return Objects.hash(code);
    }

    public static class Builder {
        private CourseCode code;
        private String title;
        private int credits;
        private Instructor instructor;
        private Semester semester;
        private String department;

        public Builder(String codeValue) {
            this.code = new CourseCode(codeValue);
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }

        public Builder instructor(Instructor instructor) {
            this.instructor = instructor;
            return this;
        }

        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Course build() {
            if (code == null || title == null || credits <= 0 || semester == null || department == null) {
                throw new IllegalStateException("Course code, title, credits, semester, and department are required.");
            }
            return new Course(this);
        }
    }
}
