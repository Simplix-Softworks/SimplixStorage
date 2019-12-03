package de.leonhard.storage.util;

import de.leonhard.storage.internal.Storage;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
@UtilityClass
public class ClassWrapper {

	/**
	 * Method to cast an object to a given datatype
	 * Used for example in {@link Storage}
	 * to cast the results of get() to for example a String
	 *
	 * @param obj Object to cast
	 * @param def type of result
	 * @return Casted object
	 */
	public <T> T getFromDef(Object obj, T def) {
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
		}
		return (T) obj;
	}

	/**
	 * Method to cast an object to a given datatype
	 * Used for example in {@link Storage}
	 * to cast the results of get() to for example a String
	 *
	 * @param obj   Object to cast
	 * @param clazz class of result
	 * @return Casted object
	 */
	public <T> T getFromDef(Object obj, Class<T> clazz) {
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
		}
		return (T) obj;
	}

	@UtilityClass
	public class LONG {
		public Long getLong(Object obj) {
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
		public Double getDouble(Object obj) {
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
		public Float getFloat(Object obj) {
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
		public Integer getInt(Object obj) {
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
		public Short getShort(Object obj) {
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
		public Byte getByte(Object obj) {
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
		public String getString(Object obj) {
			if (obj instanceof Collection && ((Collection) obj).size() == 1) {
				return ((List) obj).get(0).toString();
			}
			return obj.toString();
		}
	}
}

