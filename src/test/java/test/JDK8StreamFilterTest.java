package test;

import java.util.ArrayList;
import java.util.List;

public class JDK8StreamFilterTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		
		/**
		 * 有一链表，{1,2,3,4,5}，把偶数过滤掉
		 */
		List list = new ArrayList();  
        for(int i = 1 ; i <= 5; ++i){  
            list.add(i);  
        }  
        list.stream().filter(param -> (int)param % 2 == 1).forEach(System.out::println);  
		
	}

}
