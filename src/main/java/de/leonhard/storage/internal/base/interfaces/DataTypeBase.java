package de.leonhard.storage.internal.base.interfaces;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public interface DataTypeBase {

	/**
	 * Get a Map of the proper Type defined by your DataType and CommentSetting.
	 *
	 * @param commentSetting the CommentSetting to be used.
	 * @param map            the Map to be imported from(an empty Map will be returned if @param map is null)
	 * @return a Map containing the Data of @param map.
	 */
	Map<String, Object> getNewDataMap(final @NotNull CommentSettingBase commentSetting, final @Nullable Map<String, Object> map);

	/**
	 * Get a List of the proper Type defined by your DataType and CommentSetting.
	 *
	 * @param commentSetting the CommentSetting to be used.
	 * @param list           the Map to be imported from(an empty List will be returned if @param list is null)
	 * @return a List containing the Data of @param list.
	 */
	List<String> getNewDataList(final @NotNull CommentSettingBase commentSetting, final @Nullable List<String> list);
}