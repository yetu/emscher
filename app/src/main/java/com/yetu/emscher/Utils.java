package com.yetu.emscher;

public class Utils {

	/**
	 * Concatenate the given parts to an url. This URL will always and in a /.
	 * 
	 * @param parts
	 * @return
	 */
	public static String concatUrl(String... parts) {
		StringBuilder builder = new StringBuilder();
		for (String s : parts) {
			String part = s;
			if (s.startsWith("/")) {
				part = s.substring(1);
			}
			builder.append(part);
			if (!part.endsWith("/")) {
				builder.append('/');
			}

		}
		return builder.toString();
	}

}
