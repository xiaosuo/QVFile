package com.github.xiaosuo.qvfile;

/**
 * Format the number for readability.
 */
public class PrettyPrint {
	public static final long K = 1024;
	public static final String[] UNITS = {"KiB", "MiB", "GiB", "TiB"};

	/**
	 * Append unit when formating, and at most 2 decimal-point characters are preserved.
	 *
	 * @param length The length of data.
	 * @return the result string.
	 */
	public static String length(long length) {
		float num = length;
		String unitResult = "B";
		for (String unit : UNITS) {
			if (num < K)
				break;
			num /= K;
			unitResult = unit;
		}
		return String.format("%.2f%s", num, unitResult);
	}
}
