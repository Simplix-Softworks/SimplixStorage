package de.leonhard.storage.internal.editor.toml;

import de.leonhard.storage.internal.exceptions.TomlException;
import de.leonhard.storage.util.FastStringWriter;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Map;

/**
 * Utility class for reading and writing TOML v0.4.0. This class internally uses {@link TomlReader}
 * and {@link TomlWriter}.
 *
 * @author TheElectronWill Rewritten by JavaFactoryDev for LighntingStorage.
 */
@UtilityClass
@SuppressWarnings("unused")
public class TomlManager {

    /**
     * A DateTimeFormatter that uses the TOML format.
     */
    public final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart()
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .optionalStart()
            .appendOffsetId()
            .optionalEnd()
            .toFormatter();

    /**
     * Writes the specified data to a String, in the TOML format.
     *
     * @param data the data to write
     * @return a String that contains the data in the TOML format.
     * @throws IOException if an error occurs
     */
    public @NotNull String writeToString(final @NotNull Map<String, Object> data) throws IOException {
        val writer = new FastStringWriter();
        write(data, writer);
        return writer.toString();
    }

    /**
     * Writes data to a File, in the TOML format and with the UTF-8 encoding. The default indentation
     * parameters are used, ie each indent is one tab character.
     *
     * @param data the data to write
     * @param file where to write the data
     * @throws IOException if an error occurs
     */
    public void write(final @NotNull Map<String, Object> data,
                      final @NotNull File file)
            throws IOException {
        val out = new FileOutputStream(file);
        write(data, out);
    }

    /**
     * Writes data to an OutputStream, in the TOML format and with the UTF-8 encoding. The default
     * indentation parameters are used, ie each indent is one tab character.
     *
     * @param data the data to write
     * @throws IOException   if a read error occurs
     * @throws TomlException if a parse error occurs
     */
    public void write(final @NotNull Map<String, Object> data,
                      final @NotNull OutputStream out)
            throws IOException {
        val writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        write(data, writer);
    }

    /**
     * Writes data to a Writer, in the TOML format and with the default parameters, ie each indent is
     * 1 tab character. This is the same as {@code write(data, writer, 1, false)}.
     *
     * @param data   the data to write
     * @param writer where to write the data
     * @throws IOException   if a read error occurs
     * @throws TomlException if a parse error occurs
     */
    public void write(final @NotNull Map<String, Object> data,
                      final Writer writer)
            throws IOException {
        val tw = new TomlWriter(writer);
        tw.write(data);
        tw.close();
    }

    /**
     * Writes the specified data to a Writer, in the TOML format and with the specified parameters.
     *
     * @param data             the data to write
     * @param writer           where to write the data
     * @param indentSize       the indentation size, ie the number of times the indentation character
     *                         is repeated in one indent.
     * @param indentWithSpaces true to indent with spaces, false to indent with tabs
     * @throws IOException   if a read error occurs
     * @throws TomlException if a parse error occurs
     */
    public void write(final @NotNull Map<String, Object> data,
                      final Writer writer,
                      final int indentSize,
                      final boolean indentWithSpaces)
            throws IOException {
        val tw = new TomlWriter(writer, indentSize, indentWithSpaces);

        tw.write(data);
        tw.close();
    }

    /**
     * Reads a String that contains TOML data. Lenient bare keys are allowed (see {@link
     * TomlManager}).
     *
     * @param toml a String containing TOML data
     * @return a {@code Map<String, Object>} containing the parsed data
     * @throws TomlException if a parse error occurs
     */
    public @NotNull Map<String, Object> read(final String toml) throws TomlException {
        return read(toml, false);
    }

    /**
     * Reads a String that contains TOML data.
     *
     * @param toml                a String containing TOML data
     * @param strictAsciiBareKeys <code>true</code> to enforce strict bare keys (see {@link
     *                            TomlManager}).
     * @return a {@code Map<String, Object>} containing the parsed data
     * @throws TomlException if a parse error occurs
     */
    public @NotNull Map<String, Object> read(final String toml,
                                             final boolean strictAsciiBareKeys) {
        val tr = new TomlReader(toml, strictAsciiBareKeys);
        return tr.read();
    }

    /**
     * Reads TOML data from an UTF-8 encoded File. Lenient bare keys are allowed (see {@link
     * TomlManager}).
     *
     * @param file the File to read data from
     * @return a {@code Map<String, Object>} containing the parsed data
     * @throws IOException   if a read error occurs
     * @throws TomlException if a parse error occurs
     */
    public @NotNull Map<String, Object> read(final @NotNull File file) throws IOException, TomlException {
        return read(file, false);
    }

    /**
     * Reads TOML data from an UTF-8 encoded File.
     *
     * @param file                the File to read data from
     * @param strictAsciiBareKeys <code>true</code> to enforce strict bare keys (see {@link
     *                            TomlManager}).
     * @return a {@code Map<String, Object>} containing the parsed data
     * @throws IOException   if a read error occurs
     * @throws TomlException if a parse error occurs
     */
    public @NotNull Map<String, Object> read(final @NotNull File file,
                                             final boolean strictAsciiBareKeys)
            throws IOException, TomlException {
        return read(new FileInputStream(file), strictAsciiBareKeys);
    }

    /**
     * Reads TOML data from an UTF-8 encoded InputStream. Lenient bare keys are allowed (see {@link
     * TomlManager}).
     *
     * @param in the InputStream to read data from
     * @return a {@code Map<String, Object>} containing the parsed data
     * @throws IOException   if a read error occurs
     * @throws TomlException if a parse error occurs
     */
    public @NotNull Map<String, Object> read(final @NotNull InputStream in)
            throws IOException, TomlException {
        return read(in, false);
    }

    /**
     * Reads TOML data from an UTF-8 encoded InputStream.
     *
     * @param in                  the InputStream to read data from
     * @param strictAsciiBareKeys <code>true</code> to enforce strict bare keys (see {@link
     *                            TomlManager}).
     * @return a {@code Map<String, Object>} containing the parsed data
     * @throws IOException   if a read error occurs
     * @throws TomlException if a parse error occurs
     */
    public @NotNull Map<String, Object> read(final @NotNull InputStream in,
                                             final boolean strictAsciiBareKeys)
            throws IOException, TomlException {
        return read(new InputStreamReader(in, StandardCharsets.UTF_8), in.available(), strictAsciiBareKeys);
    }

    /**
     * Reads TOML data from a Reader. The data is read until the end of the stream is reached.
     *
     * @param bufferSize          the initial size of the internal buffer that will contain the entire
     *                            data.
     * @param strictAsciiBareKeys <code>true</code> to enforce strict bare keys (see {@link
     *                            TomlManager}).
     * @return a {@code Map<String, Object>} containing the parsed data
     * @throws IOException   if a read error occurs
     * @throws TomlException if a parse error occurs
     */
    public @NotNull Map<String, Object> read(final @NotNull Reader reader,
                                             final int bufferSize,
                                             final boolean strictAsciiBareKeys)
            throws IOException, TomlException {
        val sb = new StringBuilder(bufferSize);
        val buf = new char[8192];
        int read;

        while ((read = reader.read(buf)) != -1) {
            sb.append(buf, 0, read);
        }

        val tr = new TomlReader(sb.toString(), strictAsciiBareKeys);
        return tr.read();
    }
}
