package test;

import java.util.ArrayList;
import java.util.List;

/**
 * 把Stream 元素组合起来
 * 
 * @author Lihao
 *
 */
public class JDK8StreamReduceTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		List list = new ArrayList();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		System.out.println(list.stream().reduce((param1, param2) -> (int) param1 + (int) param2).get());
	}

}
