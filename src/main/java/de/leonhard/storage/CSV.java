package de.leonhard.storage;

import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

class CSV extends FlatFile {

	private CSV(String name, String path) {
		super(name, path, FileType.CSV);
	}

	@Override
	public Object get(final String key) {
		return null;
	}

	/**
	 * Get a String from a file
	 *
	 * @param key Path to String in file
	 * @return Returns the value
	 */
	@Override
	public String getString(final String key) {
		return null;
	}

	/**
	 * Gets a long from a file by key
	 *
	 * @param key Path to long in file
	 * @return String from file
	 */
	@Override
	public long getLong(final String key) {
		return 0;
	}

	/**
	 * Gets an int from a file
	 *
	 * @param key Path to int in file
	 * @return Int from file
	 */
	@Override
	public int getInt(final String key) {
		return 0;
	}

	/**
	 * Get a byte from a file
	 *
	 * @param key Path to byte in file
	 * @return Byte from file
	 */
	@Override
	public byte getByte(final String key) {
		return 0;
	}

	/**
	 * Get a boolean from a file
	 *
	 * @param key Path to boolean in file
	 * @return Boolean from file
	 */
	@Override
	public boolean getBoolean(final String key) {
		return false;
	}

	/**
	 * Get a float from a file
	 *
	 * @param key Path to float in file
	 * @return Float from file
	 */
	@Override
	public float getFloat(final String key) {
		return 0;
	}

	/**
	 * Get a double from a file
	 *
	 * @param key Path to double in file
	 * @return Double from file
	 */
	@Override
	public double getDouble(final String key) {
		return 0;
	}

	/**
	 * Get a List from a file
	 *
	 * @param key Path to StringList in file
	 * @return List
	 */
	@Override
	public List<?> getList(final String key) {
		return null;
	}

	/**
	 * Get String List
	 *
	 * @param key Path to String List in file
	 * @return List
	 */
	@Override
	public List<String> getStringList(final String key) {
		return null;
	}

	/**
	 * Get a IntegerList from a file
	 *
	 * @param key Path to Integer-List in file
	 * @return Integer-List
	 */
	@Override
	public List<Integer> getIntegerList(final String key) {
		return null;
	}

	/**
	 * Get a Byte-List from a file
	 *
	 * @param key Path to Byte-List from file
	 * @return Byte-List
	 */
	@Override
	public List<Byte> getByteList(final String key) {
		return null;
	}

	/**
	 * Get a Long-List from a file
	 *
	 * @param key Path to Long-List in file
	 * @return Long-List
	 */
	@Override
	public List<Long> getLongList(final String key) {
		return null;
	}

	/**
	 * Gets a Map
	 *
	 * @param key Path to Map-List in file
	 * @return Map
	 */
	@Override
	public Map getMap(final String key) {
		return null;
	}

	/**
	 * Set a object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */
	@Override
	public void set(final String key, final Object value) {
		//TODO
	}

	/**
	 * Checks wheter a key exists in the file
	 *
	 * @param key Key to check
	 * @return Returned value
	 */
	@Override
	public boolean contains(final String key) {
		return false;
	}

	/**
	 * Sets a value to the file if the file doesn't already contain the value
	 * (Not mix up with Bukkit addDefault)
	 *
	 * @param key   Key to set the value
	 * @param value Value to set
	 */
	@Override
	public void setDefault(final String key, final Object value) {
		//TODO
	}

	@Override
	public <T> T getOrSetDefault(final String key, final T def) {
		return null;
	}

	//added method for later implementation
	@Override
	protected void update() {
		//TODO
	}

	//added method for later implementation
	@Override
	public void remove(final String key) {
		//TODO
	}

	@Override
	public void reload() {

	}

	//added method for later implementation
	@Override
	public Set<String> singleLayerKeySet() {
		return null;
	}

	//added method for later implementation
	@Override
	public Set<String> singleLayerKeySet(final String key) {
		return null;
	}

	//added method for later implementation
	@Override
	public Set<String> keySet() {
		return null;
	}

	//added method for later implementation
	@Override
	public Set<String> keySet(final String key) {
		return null;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			CSV csv = (CSV) obj;
			return super.equals(csv.getFlatFileInstance());
		}
	}
}