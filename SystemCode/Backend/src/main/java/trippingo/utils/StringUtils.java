package trippingo.utils;

public class StringUtils {

	public static boolean isNotNull(String value) {
		return value !=null && value.trim() != "";
	}
	
	public static boolean isNull(String value) {
		return value ==null || value.trim() == "";
	}
}
