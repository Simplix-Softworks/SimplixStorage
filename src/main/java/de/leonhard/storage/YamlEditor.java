package de.leonhard.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class YamlEditor {
    private final File file;

    public YamlEditor(final File file) {
        this.file = file;
    }


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

    public void write(final List<String> lines) throws IOException {
        final FileWriter writer = new FileWriter(file);
        System.out.println(lines);
        for (final String str : lines){
            writer.write(str + "\n");
            System.out.println("LINE  " +  str);
        }
        writer.close();


    }

}


