package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JDK8StreamSortedTest {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		
		/**
		 * 有一链表，{2,3,1,5,4}，从小到大排序
		 */
		
		List list = new ArrayList();
		list.add(2);
		list.add(3);
		list.add(1);
		list.add(5);
		list.add(4);
		//list.stream().sorted().forEach(System.out::println);
		//倒序
		list.stream().sorted( (param1,param2) -> ((int)param1 < (int)param2 ? 1 : -1 ) ).forEach(System.out::println);  


		
			
		
	
		List<Integer> list1=Arrays.asList(8,1,6,7,3,0,4,9,5).stream().sorted((x, y) -> x-y).collect(Collectors.toList());
		for(Integer i:list1){
	      //System.out.println(i);
	    }
	    Long a=5L;
	    Long b=3L;
	    //System.out.println(b.compareTo(a));
    
	}
}
