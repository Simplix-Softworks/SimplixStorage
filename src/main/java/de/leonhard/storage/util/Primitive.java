package de.leonhard.storage.util;

public class Primitive {


    public static class LONG {
        public static long getLong(Object obj) {
            if (obj instanceof Number)
                return ((Number) obj).longValue();
            else if (obj instanceof String)
                return Long.parseLong((String) obj);
            else
                return Long.parseLong(obj.toString());
        }
    }

    public static class DOUBLE {
        public static double getDouble(Object obj) {
            if (obj instanceof Number)
                return ((Number) obj).longValue();
            else if (obj instanceof String)
                return Double.parseDouble((String) obj);
            else
                return Double.parseDouble(obj.toString());

        }
    }

    public static class FLOAT {
        public static float getFloat(Object obj) {
            if (obj instanceof Number)
                return ((Number) obj).floatValue();
            else if (obj instanceof String)
                return Float.parseFloat((String) obj);
            else
                return Float.parseFloat(obj.toString());
        }
    }

    public static class INTEGER {
        public static int getInt(Object obj) {
            if (obj instanceof Number)
                return ((Number) obj).intValue();
            else if (obj instanceof String)
                return Integer.parseInt((String) obj);
            else
                return Integer.parseInt(obj.toString());
        }
    }

    public static class SHORT {
        public static short getShort(Object obj) {
            if (obj instanceof Number)
                return ((Number) obj).shortValue();
            else if (obj instanceof String)
                return Short.parseShort((String) obj);
            else
                return Short.parseShort(obj.toString());
        }
    }
    public static class BYTE {
        public static byte getByte(Object obj) {
            if (obj instanceof Number)
                return ((Number) obj).byteValue();
            else if (obj instanceof String)
                return Byte.parseByte(obj.toString());
            else
                return Byte.parseByte(obj.toString());
        }
    }
}
