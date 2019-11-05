package de.leonhard.storage.internal.utils.basic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Parsing utilities for Primitive Types
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Primitive {

	public static <T> T getFromDef(final @NotNull Object obj, final @NotNull Class def) {
		Object tempObj = obj;
		if (obj instanceof String && def == Integer.class) {
			tempObj = Integer.parseInt((String) obj);
		} else if (obj instanceof String && def == Long.class) {
			tempObj = Long.parseLong((String) obj);
		} else if (obj instanceof String && def == Double.class) {
			tempObj = Double.parseDouble((String) obj);
		} else if (obj instanceof String && def == Float.class) {
			tempObj = Double.parseDouble((String) obj);
		} else if (obj instanceof String && def == Short.class) {
			tempObj = Short.parseShort((String) obj);
		} else if (obj instanceof String && def == Boolean.class) {
			tempObj = ((String) obj).equalsIgnoreCase("true");
		}
		//noinspection unchecked
		return (T) tempObj;
	}


	public static class BOOLEAN {

		public static boolean getBoolean(final @NotNull Object obj) {
			if (obj instanceof Boolean) {
				return (boolean) obj;
			} else if (obj instanceof String) {
				return ((String) obj).equalsIgnoreCase("true");
			} else {
				return obj.toString().equalsIgnoreCase("true");
			}
		}
	}


	public static class LONG {

		public static long getLong(final @NotNull Object obj) {
			if (obj instanceof Long) {
				return (long) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).longValue();
			} else if (obj instanceof String) {
				return Long.parseLong((String) obj);
			} else {
				return Long.parseLong(obj.toString());
			}
		}
	}


	public static class DOUBLE {

		public static double getDouble(final @NotNull Object obj) {
			if (obj instanceof Double) {
				return (double) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).longValue();
			} else if (obj instanceof String) {
				return Double.parseDouble((String) obj);
			} else {
				return Double.parseDouble(obj.toString());
			}
		}
	}


	public static class FLOAT {

		public static float getFloat(final @NotNull Object obj) {
			if (obj instanceof Float) {
				return (float) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).floatValue();
			} else if (obj instanceof String) {
				return Float.parseFloat((String) obj);
			} else {
				return Float.parseFloat(obj.toString());
			}
		}
	}


	public static class INTEGER {

		public static int getInt(final @NotNull Object obj) {
			if (obj instanceof Integer) {
				return (int) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).intValue();
			} else if (obj instanceof String) {
				return Integer.parseInt((String) obj);
			} else {
				return Integer.parseInt(obj.toString());
			}
		}
	}


	public static class SHORT {

		public static short getShort(final @NotNull Object obj) {
			if (obj instanceof Number) {
				return ((Number) obj).shortValue();
			} else if (obj instanceof String) {
				return Short.parseShort((String) obj);
			} else {
				return Short.parseShort(obj.toString());
			}
		}
	}


	public static class BYTE {

		public static byte getByte(final @NotNull Object obj) {
			if (obj instanceof Byte) {
				return (byte) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).byteValue();
			} else if (obj instanceof String) {
				return Byte.parseByte((String) obj);
			} else {
				return Byte.parseByte(obj.toString());
			}
		}
	}
}