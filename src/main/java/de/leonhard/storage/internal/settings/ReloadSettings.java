package de.leonhard.storage.internal.settings;

/**
 * Reload settings.
 */
@SuppressWarnings("unused")
public enum ReloadSettings {
    /**
     * This will reload the File everytime you access it.
     */
    AUTOMATICALLY,
    /**
     * This will only reload when the FileContent has changed since the last access.
     */
    INTELLIGENT,
    /**
     * This will only reload when you call the forceReload() method.
     */
    MANUALLY
}
