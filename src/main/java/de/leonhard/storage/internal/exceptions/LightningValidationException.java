package de.leonhard.storage.internal.exceptions;

import de.leonhard.storage.internal.exception.LightningException;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown to indicate that something went wrong, or just to end our code
 */
@SuppressWarnings("unused")
public class LightningValidationException extends LightningException {

    private static final long serialVersionUID = -7961367314553460325L;

    public LightningValidationException(final @NotNull Throwable throwable, final String... messages) {
        super(throwable, messages);
    }

    public LightningValidationException(final String... messages) {
        super(messages);
    }
}
