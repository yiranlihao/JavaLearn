package test;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Lihao
 *
 */
public class JDK8StreamMaxMinTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		
		List list = new ArrayList();  
        list.add(1);  
        list.add(2);  
        list.add(3);  
        list.add(4);  
        list.add(5);  
          
        System.out.println(list.stream().min(  
                (param1,param2) -> (int)param1 > (int)param2 ? 1:-1 ).get());  
        System.out.println(list.stream().max(  
                (param1,param2) -> (int)param1 > (int)param2 ? 1:-1 ).get()); 

	}

}
