package yiranlihao.learn.util;

import java.util.Calendar;

public class DateUtil {
	public static void main(String[] args) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		System.out.println(calendar.getTime());
	}
}
