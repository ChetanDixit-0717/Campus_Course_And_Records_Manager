package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Person {
    protected String fullName;
    protected String email;
    protected LocalDate dateOfBirth;

    public Person(String fullName, String email, LocalDate dateOfBirth) {
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public abstract String getRole();

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String toString() {
        return "Name: " + fullName + ", Email: " + email + ", DoB: " + dateOfBirth;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(fullName, person.fullName) &&
                Objects.equals(email, person.email) &&
                Objects.equals(dateOfBirth, person.dateOfBirth);
    }

    public int hashCode() {
        return Objects.hash(fullName, email, dateOfBirth);
    }
}