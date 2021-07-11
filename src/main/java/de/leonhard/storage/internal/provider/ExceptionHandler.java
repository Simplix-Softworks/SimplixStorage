package de.leonhard.storage.internal.provider;

import de.leonhard.storage.internal.exceptions.LightningValidationException;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public abstract class ExceptionHandler {

    public @NotNull RuntimeException create(@NonNull final Throwable throwable,
                                            @NonNull final String... messages) {
        return new LightningValidationException(throwable, messages);
    }
}
