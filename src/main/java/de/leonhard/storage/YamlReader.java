package de.leonhard.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class YamlReader {
    private File file;


    public List<String> read() throws IOException {
        final byte[] fileBytes = Files.readAllBytes(file.toPath());
        final String asString = new String(fileBytes);
        return new ArrayList<>(Arrays.asList(asString.split("\n")));
    }

    public List<String> getHeader() throws IOException {
        final byte[] fileBytes = Files.readAllBytes(file.toPath());
        final String asString = new String(fileBytes);
        final List<String> result = new ArrayList<>();

        for (final String line : asString.split("\n")) {
            if (!line.startsWith("#"))
                return result;
            result.add(line);
        }
        return result;
    }

}


