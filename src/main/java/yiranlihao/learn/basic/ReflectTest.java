package yiranlihao.learn.basic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectTest {
	public static void main(String[] args) throws Exception {
		/**
		 * 获得字节码对应的实例对象的 3 种方式 以 String 为例
		 */
		String testStr = "goodleiwei";
		Class getClassWay1 = testStr.getClass();
		Class getClassWay2 = String.class;
		Class getClassWay3 = Class.forName("java.lang.String");
		System.out.println(getClassWay1 == getClassWay2);
		System.out.println(getClassWay2 == getClassWay3);

		/**
		 * 构造方法的反射应用：构造方法是产生对象的根本途径 获得一个类的所有构造方法 Constructor[] getConstructor =
		 * Class.forName("java.lang.String").getConstructors();
		 */
		String str1 = (String) Class.forName("java.lang.String").newInstance(); // 默认构造方法
																				// 得到字节码产生实例对象

		String str2 = (String) Class.forName("java.lang.String") // 得到字节码即事例对象
				.getConstructor(StringBuffer.class) // 获得该对象中的特定构造器
				.newInstance(new StringBuffer("abc")); // 根据构造器产生对象实例
		System.out.println(str2.charAt(2));

		/**
		 * 成员变量的反射 Field
		 */
		User user2 = new User(3, 5);
		Field fieldX = user2.getClass().getField("x");
		// fieldX 的值是多少 ? 是 3, 错 !fieldX 不是对象身上的变量，而是类上的变量。要用 fieldX 到某个对象上去取值如：
		System.out.println(fieldX.get(user2));

		// 对于不可见的 private 修饰的字段只能通过如下方式获得并输出，否则报 java.lang.NoSuchFieldException
		// java.lang.IllegalAccessException
		Field fieldY = user2.getClass().getDeclaredField("y");
		fieldY.setAccessible(true);
		System.out.println(fieldY.get(user2));
		/**
		 * 成员变量的反射应用 Field
		 * 
		 * 将 user 类中的 String 属性字段之中的 s 全部改成 H
		 */
		User user = (User) Class.forName("yiranlihao.learn.basic.User").newInstance();
		changStringValue(user);
		System.out.println(user);

		/**
		 * 成员方法的反射应用 Method 调用 User 的 main 方法
		 */
		// new User().main(new String[]{"111","222","333"}); 传统的干法

		/*
		 * 不知道要启动 被访问类的名字 --- 可在 run as-->run configuration-->arguments 中配置 yiranlihao.learn.basic.User 不然将报 java.lang.ArrayIndexOutOfBoundsException
		 */
		String startingClassName = args[0];
		// 反射获取到 main 方法
		Method method = Class.forName(startingClassName).getMethod("main", String[].class);
		/*
		 * 调用静态方法第一个参数不需要传值 第二个参数必须 写成如下形式 或 new Object[]{new
		 * String[]{"111","222","333"}} 否则将报 java.lang.IllegalArgumentException:
		 * wrong number of arguments
		 */
		method.invoke(null, (Object) new String[] { "111", "222", "333" });
	}

	private static void changStringValue(Object obj) throws IllegalArgumentException, IllegalAccessException {

		// 得到 obj 类中所有的属性字段
		Field[] fields = obj.getClass().getFields();
		for (Field field : fields) {
			if (field.getType() == String.class) {
				// 得到 obj 类身上的 filed 字段的值
				String oldStrValue = (String) field.get(obj);
				// 替换 obj 类身上的 filed 字段的值
				String newStrValue = oldStrValue.replace('s', 'H');
				field.set(obj, newStrValue);
			}
		}
	}
}

class User {
	public String name1 = "zhangsan";
	public String name2 = "lisi";
	public String name3 = "wangwu";

	public int x;
	private int y;

	public User() {
	}

	public User(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return name1 + ":" + name2 + ":" + name3;
	}

	public static void main(String[] args) {
		for (String string : args) {
			System.out.println(string);
		}
	}

}