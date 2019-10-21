package de.leonhard.storage.lightningstorage.utils.basic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"unused", "unchecked"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Primitive {

	@SuppressWarnings("DuplicatedCode")
	public static <T> T getFromDef(@NotNull final Object obj, @NotNull final T def) {
		Object tempObj = obj;
		if (obj instanceof String && def instanceof Integer) {
			tempObj = Integer.parseInt((String) obj);
		} else if (obj instanceof String && def instanceof Double) {
			tempObj = Double.parseDouble((String) obj);
		} else if (obj instanceof String && def instanceof Float) {
			tempObj = Double.parseDouble((String) obj);
		} else if (obj instanceof String && def instanceof Boolean) {
			tempObj = ((String) obj).equalsIgnoreCase("true");
		}
		return (T) tempObj;
	}

	public static class LONG {

		public static long getLong(@NotNull final Object obj) {
			if (obj instanceof Number) {
				return ((Number) obj).longValue();
			} else if (obj instanceof String) {
				return Long.parseLong((String) obj);
			} else {
				return Long.parseLong(obj.toString());
			}
		}
	}

	public static class DOUBLE {

		public static double getDouble(@NotNull final Object obj) {
			if (obj instanceof Number) {
				return ((Number) obj).longValue();
			} else if (obj instanceof String) {
				return Double.parseDouble((String) obj);
			} else {
				return Double.parseDouble(obj.toString());
			}
		}
	}

	public static class FLOAT {

		public static float getFloat(@NotNull final Object obj) {
			if (obj instanceof Number) {
				return ((Number) obj).floatValue();
			} else if (obj instanceof String) {
				return Float.parseFloat((String) obj);
			} else {
				return Float.parseFloat(obj.toString());
			}
		}
	}

	public static class INTEGER {

		public static int getInt(@NotNull final Object obj) {
			if (obj instanceof Number) {
				return ((Number) obj).intValue();
			} else if (obj instanceof String) {
				return Integer.parseInt((String) obj);
			} else {
				return Integer.parseInt(obj.toString());
			}
		}
	}

	public static class SHORT {

		public static short getShort(@NotNull final Object obj) {
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

		public static byte getByte(@NotNull final Object obj) {
			if (obj instanceof Number) {
				return ((Number) obj).byteValue();
			} else if (obj instanceof String) {
				return Byte.parseByte((String) obj);
			} else {
				return Byte.parseByte(obj.toString());
			}
		}
	}
}