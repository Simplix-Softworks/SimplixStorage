package de.leonhard.storage.util;

import de.leonhard.storage.internal.DataStorage;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import lombok.experimental.UtilityClass;

@SuppressWarnings("unchecked")
@UtilityClass
public class ClassWrapper {

  /**
   * Method to cast an object to a given datatype Used for example in {@link DataStorage} to cast
   * the results of get() to for example a String
   *
   * @param obj Object to cast
   * @param def type of result
   * @return Casted object
   */
  public <T> T getFromDef(final Object obj, final T def) {
    if (def instanceof Integer) {
      return (T) INTEGER.getInt(obj);
    } else if (def instanceof Float) {
      return (T) FLOAT.getFloat(obj);
    } else if (def instanceof Double) {
      return (T) DOUBLE.getDouble(obj);
    } else if (def instanceof Long) {
      return (T) LONG.getLong(obj);
    } else if (def instanceof Boolean) {
      return (T) (Boolean) obj.toString().equalsIgnoreCase("true");
    } else if (def instanceof String[]) {
      return (T) STRING.getStringArray(obj);
    } else if (def instanceof Long[] || def instanceof long[]) {
      return (T) LONG.getLongArray(obj);
    } else if (def instanceof Double[] || def instanceof double[]) {
      return (T) DOUBLE.getDoubleArray(obj);
    } else if (def instanceof Float[] || def instanceof float[]) {
      return (T) FLOAT.getFloatArray(obj);
    } else if (def instanceof Short[] || def instanceof short[]) {
      return (T) SHORT.getShortArray(obj);
    } else if (def instanceof Byte[] || def instanceof byte[]) {
      return (T) BYTE.getByteArray(obj);
    }
    return (T) obj;
  }

  /**
   * Method to cast an object to a given datatype Used for example in {@link DataStorage} to cast
   * the results of get() to for example a String
   *
   * @param obj   Object to cast
   * @param clazz class of result
   * @return Casted object
   */
  public <T> T getFromDef(final Object obj, final Class<T> clazz) {
    if (clazz == int.class || clazz == Integer.class) {
      return (T) INTEGER.getInt(obj);
    } else if (clazz == float.class || clazz == Float.class) {
      return (T) FLOAT.getFloat(obj);
    } else if (clazz == double.class || clazz == Double.class) {
      return (T) DOUBLE.getDouble(obj);
    } else if (clazz == long.class || clazz == Long.class) {
      return (T) LONG.getLong(obj);
    } else if (clazz == boolean.class || clazz == Boolean.class) {
      return (T) (Boolean) obj.toString().equalsIgnoreCase("true");
    } else if (clazz == String[].class) {
      return (T) STRING.getStringArray(obj);
    } else if (clazz == Double[].class || clazz == double[].class) {
      return (T) DOUBLE.getDoubleArray(obj);
    } else if (clazz == Float[].class || clazz == float[].class) {
      return (T) FLOAT.getFloatArray(obj);
    } else if (clazz == Integer[].class || clazz == int[].class) {
      return (T) INTEGER.getIntArray(obj);
    } else if (clazz == Short[].class || clazz == short[].class) {
      return (T) SHORT.getShortArray(obj);
    } else if (clazz == Byte[].class || clazz == byte[].class) {
      return (T) BYTE.getByteArray(obj);
    }
    return (T) obj;
  }

  @UtilityClass
  public class LONG {

    public Long[] getLongArray(final Object obj) {
      if (obj instanceof List) {
        final List<Long> list = (List<Long>) obj;
        return list.toArray(new Long[0]);
      }

      return new Long[0];
    }

    public Long getLong(final Object obj) {
      if (obj instanceof Number) {
        return ((Number) obj).longValue();
      } else if (obj instanceof String) {
        return Long.parseLong((String) obj);
      } else {
        return Long.parseLong(obj.toString());
      }
    }
  }

  @UtilityClass
  public class DOUBLE {

    public Double[] getDoubleArray(final Object obj) {
      if (obj instanceof List) {
        final List<Double> list = (List<Double>) obj;
        return list.toArray(new Double[0]);
      }

      return new Double[0];
    }

    public Double getDouble(final Object obj) {
      if (obj instanceof Number) {
        return ((Number) obj).doubleValue();
      } else if (obj instanceof String) {
        return Double.parseDouble((String) obj);
      } else {
        return Double.parseDouble(obj.toString());
      }
    }
  }

  @UtilityClass
  public class FLOAT {

    public Float[] getFloatArray(final Object obj) {
      if (obj instanceof List) {
        final List<Float> list = (List<Float>) obj;
        return list.toArray(new Float[0]);
      }

      return new Float[0];
    }

    public Float getFloat(final Object obj) {
      if (obj instanceof Number) {
        return ((Number) obj).floatValue();
      } else if (obj instanceof String) {
        return Float.parseFloat((String) obj);
      } else {
        return Float.parseFloat(obj.toString());
      }
    }
  }

  @UtilityClass
  public class INTEGER {

    public Integer[] getIntArray(final Object obj) {
      if (obj instanceof List) {
        final List<Integer> list = (List<Integer>) obj;
        return list.toArray(new Integer[0]);
      }

      return new Integer[0];
    }

    public Integer getInt(final Object obj) {
      if (obj instanceof Number) {
        return ((Number) obj).intValue();
      } else if (obj instanceof String) {
        return Integer.parseInt((String) obj);
      } else {
        return Integer.parseInt(obj.toString());
      }
    }
  }

  @UtilityClass
  @SuppressWarnings("unused")
  public class SHORT {

    public Short[] getShortArray(final Object obj) {
      if (obj instanceof List) {
        final List<Short> list = (List<Short>) obj;
        return list.toArray(new Short[0]);
      }

      return new Short[0];
    }

    public Short getShort(final Object obj) {
      if (obj instanceof Number) {
        return ((Number) obj).shortValue();
      } else if (obj instanceof String) {
        return Short.parseShort((String) obj);
      } else {
        return Short.parseShort(obj.toString());
      }
    }
  }

  @UtilityClass
  public class BYTE {

    public Byte[] getByteArray(final Object obj) {
      if (obj instanceof List) {
        final List<Byte> list = (List<Byte>) obj;
        return list.toArray(new Byte[0]);
      }

      return new Byte[0];
    }

    public Byte getByte(final Object obj) {
      if (obj instanceof Number) {
        return ((Number) obj).byteValue();
      } else if (obj instanceof STRING) {
        return Byte.parseByte(obj.toString());
      } else {
        return Byte.parseByte(obj.toString());
      }
    }
  }

  @UtilityClass
  public class STRING {

    public String[] getStringArray(final Object obj) {
      if (obj instanceof List) {
        final List<String> list = (List<String>) obj;
        return list.toArray(new String[0]);
      }

      return new String[0];
    }

    public String getString(final Object obj) {
      if (obj instanceof Collection && ((Collection<?>) obj).size() == 1) {
        return ((List<?>) obj).get(0).toString();
      }
      return new String(obj.toString().getBytes(), StandardCharsets.UTF_8);
    }
  }
}
