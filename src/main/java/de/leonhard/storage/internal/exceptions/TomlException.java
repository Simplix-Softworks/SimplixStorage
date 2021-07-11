package de.leonhard.storage.internal.exceptions;

import de.leonhard.storage.internal.exception.LightningException;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a problem occurs during parsing or writing NBT data.
 */
public class TomlException extends LightningException {

    private static final long serialVersionUID = 1L;

    public TomlException(final @NotNull Throwable cause, final String... messages) {
        super(cause, messages);
    }

    public TomlException(final String... messages) {
        super(messages);
    }
}
