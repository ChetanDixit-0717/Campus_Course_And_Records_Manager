package edu.ccrm.io;

import edu.ccrm.domain.Student;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportExportService {

    public List<Student> importStudents(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }
        System.out.println("Importing students from: " + filePath);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(1)
                    .map(this::parseStudentLine)
                    .filter(s -> s != null)
                    .collect(Collectors.toList());
        }
    }

    private Student parseStudentLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            try {
                String regNo = parts[0].trim();
                String fullName = parts[1].trim();
                String email = parts[2].trim();
                LocalDate dateOfBirth = LocalDate.parse(parts[3].trim());
                return new Student(fullName, email, dateOfBirth, regNo);
            } catch (Exception e) {
                System.err.println("Error parsing student line: " + line + " - " + e.getMessage());
                return null;
            }
        }
        System.err.println("Skipping malformed line: " + line);
        return null;
    }

    public void exportStudents(List<Student> students, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        System.out.println("Exporting " + students.size() + " students to: " + filePath);

        String header = "regNo,fullName,email,dateOfBirth";
        List<String> lines = students.stream()
                .map(s -> String.join(",",
                        s.getRegNo(),
                        s.getFullName(),
                        s.getEmail(),
                        s.getDateOfBirth().toString()))
                .collect(Collectors.toList());

        lines.add(0, header);

        Files.write(path, lines);
        System.out.println("Export completed successfully.");
    }

}