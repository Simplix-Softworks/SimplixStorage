package de.leonhard.storage;

public class Main {
	public static void main(String[] args) {
		final Config cfg = new Config("cfg", null);
		System.out.println(cfg.getFileData().toMap().getClass());
		cfg.set("LeonhardIstDer", "Boss");
	}
}
