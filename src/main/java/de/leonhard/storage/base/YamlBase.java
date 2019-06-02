package de.leonhard.storage.base;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface YamlBase extends StorageBase {

    void write(Map data) throws IOException;

    List<String> getHeader();


    /*
    Comments to arrays of blocks
    ->Safe relative position
     */

    default List<String> updateWithComments(final List<String> updated, final List<String> footer,
                                            final List<String> header, final List<String> comments, Map<String, List<String>> parsed) {
        final List<String> result = header;
        result.addAll(updated);
        result.addAll(footer);


        //TODO Add normal comments


        for (final String key : parsed.keySet()) {
            int i = 0;
            for (final String line : parsed.get(key)) {
                if (line.isEmpty())
                    continue;
                if (updated.contains(key))
                    result.add(result.indexOf(key) + i, line);
            }
        }


        return result;

    }

}
