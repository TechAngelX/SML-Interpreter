package sml.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Service class providing file reading utilities for the Simple Machine Language program.
 * This service offers methods for creating file scanners with UTF-8 encoding.
 * <p>
 * The class is annotated with Spring's {@code @Service} stereotype, indicating it is a
 * Spring-managed service component that can be automatically discovered and injected.
 * </p>
 *
 * @author Ricki Angel
 */
@Service
public class FileService {

    /**
     * Creates a {@code Scanner} for reading the contents of a file with UTF-8 encoding.
     * <p>
     * This method opens a file at the specified path and returns a {@code Scanner}
     * that can be used to read the file's contents. The scanner uses UTF-8 character encoding
     * to ensure proper handling of international characters.
     * </p>
     *
     * @param path The file path from which to create the scanner. Must be a valid,
     *             accessible file path on the local filesystem.
     * @return A {@code Scanner} configured to read the specified file with UTF-8 encoding.
     * @throws IOException If an I/O error occurs while attempting to open the file,
     *                     such as the file not existing, being inaccessible, or other
     *                     filesystem-related issues.
     * @see java.util.Scanner
     * @see java.nio.charset.StandardCharsets
     * @see java.io.File
     */
    public Scanner createFileScanner(String path) throws IOException {
        return new Scanner(new File(path), StandardCharsets.UTF_8);
    }
}
