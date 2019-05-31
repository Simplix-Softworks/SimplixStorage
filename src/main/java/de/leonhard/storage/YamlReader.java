package de.leonhard.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.util.*;

import java.io.File;

@AllArgsConstructor
@Getter
public class YamlReader {
    private File file;


    public List<String> read() throws FileNotFoundException {
        final Scanner scanner = new Scanner(file);
        final List<String> result = new ArrayList<>();
        while (scanner.hasNext()) {
            result.add(scanner.next());
        }
        return result;
    }


}
