package test;

import java.util.ArrayList;
import java.util.List;

/**
 * 也就是说，原来的链表的每个元素可以按照规则变成相应的元素。
 * @author Lihao
 *
 */
public class JDK8StreamMapTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		
		/**
		 * 链表 (1,0)，变成 true，false
		 */
		List list = new ArrayList();  
        list.add(1);  
        list.add(0);  
        list.stream().map( param -> (int)param == 1 ? true:false ).forEach(System.out::println);  
		
	}

}
