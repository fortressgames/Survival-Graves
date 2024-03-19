package net.fortressgames.survivalgraves.utils;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;
import java.util.UUID;

public class UUIDHandler {

	public static void getUUIDFromName(ResultCallBack<String> callBack, String username) {
		try {
			String url = "https://api.mojang.com/users/profiles/minecraft/" + username;
			String UUIDJson = IOUtils.toString(new URL(url));

			if(UUIDJson.isEmpty()) {
				callBack.callBack(false, null);
				return;
			}

			JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
			String uuid = UUIDObject.get("id").toString();

			String formattedUUID = formatUUID(uuid);
			callBack.callBack(true, formattedUUID);

		} catch (Exception e) {
			callBack.callBack(false, null);
		}
	}

	public static String getNameFromUUID(String uuid) {
		try {
			String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.replaceAll("-", "");
			String UUIDJson = IOUtils.toString(new URL(url));

			if(UUIDJson.isEmpty()) {
				return uuid;
			}

			JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
			return UUIDObject.get("name").toString();

		} catch (Exception ignored) {}

		return uuid;
	}

	private static String formatUUID(String uuid) {
		StringBuilder formattedUUID = new StringBuilder(uuid);

		formattedUUID.insert(8, '-');
		formattedUUID.insert(13, '-');
		formattedUUID.insert(18, '-');
		formattedUUID.insert(23, '-');

		return formattedUUID.toString();
	}

	@FunctionalInterface
	public interface ResultCallBack<T> {
		void callBack(boolean successful, T uuid);
	}

	public static class Builder {

		@Getter private final UUID uuid;
		@Getter private final String name;

		public Builder(UUID uuid) {
			this.uuid = uuid;
			this.name = UUIDHandler.getNameFromUUID(this.uuid.toString());
		}
	}
}