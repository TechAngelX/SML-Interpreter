package sml.services;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class FileService {

    /**
     * Reads the contents of a file at the specified path.
     *
     * @param path Path to the file to read
     * @return The contents of the file as a string
     * @throws IOException If an I/O error occurs
     */
    public Scanner createFileScanner(String path) throws IOException {
        return new Scanner(new File(path), StandardCharsets.UTF_8);
    }
}