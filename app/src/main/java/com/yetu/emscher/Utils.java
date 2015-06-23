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
		int counter = 0;
		int length = parts.length;
		for (String s : parts) {
			String part = s;
			if (counter != 0) {
				if (s.startsWith("/")) {
					part = s.substring(1);
				}
			}
			builder.append(part);
			if (counter != length - 1) {
				if (!part.endsWith("/")) {
					builder.append('/');
				}
			}
			counter++;
		}
		return builder.toString();
	}

}
