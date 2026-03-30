package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;
import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.TranscriptService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class CCRMApplication {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final EnrollmentService enrollmentService = new EnrollmentService();
    private static final TranscriptService transcriptService = new TranscriptService();
    private static final ImportExportService importExportService = new ImportExportService();
    private static final BackupService backupService = new BackupService();

    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();
        System.out.println("Application started. Data folder: " + appConfig.getDataFolderPath());

        CliMenu.displayWelcomeMessage();

        loadSampleData();

        int choice;
        do {
            CliMenu.displayMainMenu();
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        manageStudents();
                        break;
                    case 2:
                        manageCourses();
                        break;
                    case 3:
                        manageEnrollmentAndGrading();
                        break;
                    case 4:
                        manageFileOperations();
                        break;
                    case 5:
                        generateReports();
                        break;
                    case 0:
                        System.out.println("Exiting Campus Course & Records Manager.\nPresented By Rishabh Raj (24BCE10213)");
                        System.out.println("\n********** Thank You For Using it. **********\n");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                choice = -1;
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
                choice = -1;
            }
            if (choice != 0) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        } while (choice != 0);

        scanner.close();
    }

    private static void manageStudents() {

        int choice;
        do {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add New Student");
            System.out.println("2. List All Students");
            System.out.println("3. Find Student by Registration Number");
            System.out.println("4. Update Student");
            System.out.println("5. Deactivate Student");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    listAllStudents();
                    break;
                case 3:
                    findStudentByRegNo();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deactivateStudent();
                    break;
            }
        } while (choice != 0);
    }
    
    private static void manageCourses() {
        int choice;
        do {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Add New Course");
            System.out.println("2. List All Courses");
            System.out.println("3. Find Course by Code");
            System.out.println("4. Update Course");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addCourse();
                    break;
                case 2:
                    listAllCourses();
                    break;
                case 3:
                    findCourseByCode();
                    break;
                case 4:
                    updateCourse();
                    break;
            }
        } while (choice != 0);
    }


    private static void manageEnrollmentAndGrading() {
        int choice;
        do {
            System.out.println("\n--- Enrollment & Grading ---");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Unenroll Student from Course");
            System.out.println("3. Record Marks for Student");
            System.out.println("4. View a Student's Transcript");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    enrollStudentInCourse();
                    break;
                case 2:
                    unenrollStudentFromCourse();
                    break;
                case 3:
                    recordStudentMarks();
                    break;
                case 4:
                    viewStudentTranscript();
                    break;
            }
        } while (choice != 0);
    }


    private static void manageFileOperations() {
        int choice;
        do {
            System.out.println("\n--- File Operations ---");
            System.out.println("1. Import Students from File");
            System.out.println("2. Export Students to File");
            System.out.println("3. Create Backup");
            System.out.println("4. Show Backup Size");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    importStudentsFromFile();
                    break;
                case 2:
                    exportStudentsToFile();
                    break;
                case 3:
                    createBackup();
                    break;
                case 4:
                    showBackupSize();
                    break;
            }
        } while (choice != 0);
    }
    
    private static void generateReports() {
        System.out.println("\n--- Reports ---");
        transcriptService.printGpaDistributionReport(studentService.getAllStudents());
    }

    private static void addStudent() {
        try {
            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            LocalDate dob = LocalDate.parse(scanner.nextLine());
            System.out.print("Enter Registration Number: ");
            String regNo = scanner.nextLine();
            studentService.addStudent(new Student(name, email, dob, regNo));
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Please use YYYY-MM-DD.");
        } catch (Exception e) {
            System.err.println("Error adding student: " + e.getMessage());
        }
    }

    private static void listAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        students.forEach(s -> System.out.println(s.toString()));
    }
    
    private static void findStudentByRegNo() {
        System.out.print("Enter Registration Number: ");
        String regNo = scanner.nextLine();
        studentService.getStudentByRegNo(regNo)
                .ifPresentOrElse(
                        s -> System.out.println("Found: " + s),
                        () -> System.out.println("Student with RegNo " + regNo + " not found.")
                );
    }
    
    private static void updateStudent() {
        System.out.print("Enter Registration Number of student to update: ");
        String regNo = scanner.nextLine();
        Optional<Student> studentOpt = studentService.getStudentByRegNo(regNo);

        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        Student studentToUpdate = studentOpt.get();
        try {
            System.out.print("Enter new Full Name (or press Enter to keep '" + studentToUpdate.getFullName() + "'): ");
            String name = scanner.nextLine();
            if (!name.isBlank()) studentToUpdate.setFullName(name);

            System.out.print("Enter new Email (or press Enter to keep '" + studentToUpdate.getEmail() + "'): ");
            String email = scanner.nextLine();
            if (!email.isBlank()) studentToUpdate.setEmail(email);

            studentService.updateStudent(studentToUpdate);
        } catch (Exception e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
    }

    private static void deactivateStudent() {
        System.out.print("Enter Registration Number of student to deactivate: ");
        String regNo = scanner.nextLine();
        studentService.deactivateStudent(regNo);
    }
    
    private static void addCourse() {
        try {
            System.out.print("Enter Course Code (e.g., CS101): ");
            String code = scanner.nextLine();
            System.out.print("Enter Course Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Credits: ");
            int credits = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Department: ");
            String dept = scanner.nextLine();
            System.out.print("Enter Semester (SPRING, SUMMER, FALL): ");
            Semester semester = Semester.valueOf(scanner.nextLine().toUpperCase());

            Course newCourse = new Course.Builder(code)
                    .title(title)
                    .credits(credits)
                    .department(dept)
                    .semester(semester)
                    .build();
            courseService.addCourse(newCourse);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error adding course: " + e.getMessage());
        }
    }

    private static void listAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        courses.forEach(c -> System.out.println(c.toString()));
    }
    
    private static void findCourseByCode() {
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine();
        courseService.getCourseByCode(code)
                .ifPresentOrElse(
                        c -> System.out.println("Found: " + c),
                        () -> System.out.println("Course with code " + code + " not found.")
                );
    }

    private static void updateCourse() {
        System.out.print("Enter Course Code to update: ");
        String code = scanner.nextLine();
        Optional<Course> courseOpt = courseService.getCourseByCode(code);

        if (courseOpt.isEmpty()) {
            System.out.println("Course not found.");
            return;
        }
        Course courseToUpdate = courseOpt.get();
        System.out.print("Enter new title (or press Enter to keep '" + courseToUpdate.getTitle() + "'): ");
        String title = scanner.nextLine();
        if (!title.isBlank()) courseToUpdate.setTitle(title);
        
        System.out.print("Enter new credits (or press Enter to keep '" + courseToUpdate.getCredits() + "'): ");
        String creditsStr = scanner.nextLine();
        if(!creditsStr.isBlank()) courseToUpdate.setCredits(Integer.parseInt(creditsStr));

        courseService.updateCourse(courseToUpdate);
    }
    
    private static void enrollStudentInCourse() {
        System.out.print("Enter Student Registration Number: ");
        String regNo = scanner.nextLine();
        Optional<Student> studentOpt = studentService.getStudentByRegNo(regNo);
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        Optional<Course> courseOpt = courseService.getCourseByCode(courseCode);
        if (courseOpt.isEmpty()) {
            System.out.println("Course not found.");
            return;
        }
        try {
            enrollmentService.enrollStudent(studentOpt.get(), courseOpt.get());
        } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException e) {
            System.err.println("Enrollment failed: " + e.getMessage());
        }
    }

    private static void unenrollStudentFromCourse() {
        System.out.print("Enter Student Registration Number: ");
        String regNo = scanner.nextLine();
        Optional<Student> studentOpt = studentService.getStudentByRegNo(regNo);
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        Optional<Course> courseOpt = courseService.getCourseByCode(courseCode);
        if (courseOpt.isEmpty()) {
            System.out.println("Course not found.");
            return;
        }
        enrollmentService.unenrollStudent(studentOpt.get(), courseOpt.get());
    }
    
    private static void recordStudentMarks() {
        System.out.print("Enter Student Registration Number: ");
        String regNo = scanner.nextLine();
        Optional<Student> studentOpt = studentService.getStudentByRegNo(regNo);
        if (studentOpt.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        Optional<Course> courseOpt = courseService.getCourseByCode(courseCode);
        if (courseOpt.isEmpty()) {
            System.out.println("Course not found.");
            return;
        }
        try {
            System.out.print("Enter marks (0-100): ");
            double marks = Double.parseDouble(scanner.nextLine());
            enrollmentService.recordMarks(studentOpt.get(), courseOpt.get(), marks);
        } catch (NumberFormatException e) {
            System.err.println("Invalid marks. Please enter a number.");
        }
    }
    
    private static void viewStudentTranscript() {
        System.out.print("Enter Student Registration Number: ");
        String regNo = scanner.nextLine();
        studentService.getStudentByRegNo(regNo).ifPresent(student -> {
            String transcript = transcriptService.generateTranscript(student);
            System.out.println(transcript);
        });
    }

    private static void importStudentsFromFile() {
        System.out.print("Enter the full path to the student CSV file: ");
        String filePath = scanner.nextLine();
        try {
            List<Student> importedStudents = importExportService.importStudents(filePath);
            AtomicInteger count = new AtomicInteger();
            importedStudents.forEach(s -> {
                studentService.addStudent(s);
                count.getAndIncrement();
            });
            System.out.println(count + " students imported successfully.");
        } catch (IOException e) {
            System.err.println("Error importing students: " + e.getMessage());
        }
    }
    private static void exportStudentsToFile() {
        try {
            Path dataFolderPath = AppConfig.getInstance().getDataFolderPath();
            
            Files.createDirectories(dataFolderPath);

            // Create a unique filename with a timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "student-export-" + timestamp + ".csv";
            Path filePath = dataFolderPath.resolve(fileName);

            // Call the export service with the automatically generated path
            importExportService.exportStudents(studentService.getAllStudents(), filePath.toString());

        } catch (IOException e) {
            System.err.println("Error exporting students: " + e.getMessage());
            e.printStackTrace(); // Also print the full error for better debugging
        }
    }

    private static void createBackup() {
        try {
            Path backupPath = backupService.createTimestampedBackup();
            System.out.println("Backup successfully created at: " + backupPath);
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }

    private static void showBackupSize() {
        System.out.print("Enter the full path of the backup folder to check: ");
        String backupPathStr = scanner.nextLine();
        Path backupPath = Path.of(backupPathStr);
        backupService.showBackupSize(backupPath);
    }
    
    private static void loadSampleData() {
        Student student1 = new Student("Chetan Prakash", "Chetan@gmail.com", LocalDate.of(2005, 10, 17), "24BAI10532");
        Student student2 = new Student("Leo Das", "LEO@gmail.com", LocalDate.of(2006, 6, 5), "24BAI10777");
        studentService.addStudent(student1);
        studentService.addStudent(student2);

        Course course1 = new Course.Builder("CSE2002").title("Data Structures and Algorithms").credits(4).department("SCOPE").semester(Semester.FALL).build();
        Course course2 = new Course.Builder("MAT3002").title("Applied Linear Algebra").credits(3).department("SASL").semester(Semester.FALL).build();
        courseService.addCourse(course1);
        courseService.addCourse(course2);

        System.out.println("Sample data loaded.");
    }
}