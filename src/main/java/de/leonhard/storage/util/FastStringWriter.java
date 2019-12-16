package de.leonhard.storage.util;

import java.io.Writer;

/**
 * A Writer writing in a StringBuilder. This is NOT Thread safe.
 *
 * @author TheElectronWill
 * Edited by JavaFactoryDev.
 */
public class FastStringWriter extends Writer {

	/**
	 * The underlying StringBuilder. Everything is appended to it.
	 */
	private final StringBuilder stringBuilder;

	/**
	 * Creates a new FastStringWriter with a default StringBuilder
	 */
	public FastStringWriter() {
		stringBuilder = new StringBuilder();
	}

	/**
	 * Creates a new FastStringWriter with a given StringBuilder. It will append everything to this StringBuilder.
	 *
	 * @param sb the StringBuilder
	 */
	public FastStringWriter(StringBuilder sb) {
		this.stringBuilder = sb;
	}

	/**
	 * Returns the underlying StringBuilder.
	 *
	 * @return the underlying StringBuilder.
	 */
	public StringBuilder getBuilder() {
		return stringBuilder;
	}

	/**
	 * Returns the content of the underlying StringBuilder, as a String. Equivalent to {@link #getBuilder()#toString()}.
	 *
	 * @return the content of the underlying StringBuilder.
	 */
	@Override
	public String toString() {
		return stringBuilder.toString();
	}

	@Override
	public FastStringWriter append(char c) {
		stringBuilder.append(c);
		return this;
	}

	@Override
	public FastStringWriter append(CharSequence csq, int start, int end) {
		stringBuilder.append(csq, start, end);
		return this;
	}

	@Override
	public FastStringWriter append(CharSequence csq) {
		stringBuilder.append(csq);
		return this;
	}

	@Override
	public void write(String str, int off, int len) {
		stringBuilder.append(str, off, off + len);
	}

	@Override
	public void write(String str) {
		stringBuilder.append(str);
	}

	@Override
	public void write(char[] cbuf, int off, int len) {
		stringBuilder.append(cbuf, off, len);
	}

	@Override
	public void write(int c) {
		stringBuilder.append(c);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
	}

}