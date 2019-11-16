package de.leonhard.storage.internal.settings;

import de.leonhard.storage.utils.LightningProvider;

import java.util.Map;

public enum DataType {
    SORTED {
        @Override
        public Map getMapImplementation() {
            return getMapImplementation(null);
        }

        @Override
        public Map getMapImplementation(ConfigSettings configSettings) {
            return LightningProvider.getDefaultMapImplementation();
        }
    },

    AUTOMATIC {
        @Override
        public Map getMapImplementation() {
            return getMapImplementation(null);
        }

        @Override
        public Map getMapImplementation(ConfigSettings configSettings) {
            return LightningProvider.getDefaultMapImplementation();
        }
    },
    INTELLIGENT {
        @Override
        public Map getMapImplementation() {
            return getMapImplementation(null);
        }

        @Override
        public Map getMapImplementation(ConfigSettings configSettings) {
            if (configSettings == null) {
                return LightningProvider.getDefaultMapImplementation();
            }
            if (ConfigSettings.PRESERVE_COMMENTS == configSettings) {
                return LightningProvider.getSortedMapImplementation();
            }
            return LightningProvider.getDefaultMapImplementation();
        }
    };


    public Map getMapImplementation() {
        throw new AbstractMethodError("Not implemented");
    }

    public Map getMapImplementation(ConfigSettings configSettings) {
        throw new AbstractMethodError("Not implemented");
    }
}
