package de.leonhard.storage.internal.base;

import java.util.List;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public interface ConfigBase {

	List<String> getHeader();

	void setHeader(final @Nullable List<String> header);

	List<String> getFooter();

	void setFooter(final @Nullable List<String> footer);

	List<String> getComments();
}