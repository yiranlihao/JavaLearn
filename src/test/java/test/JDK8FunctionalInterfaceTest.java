package test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class JDK8FunctionalInterfaceTest {

	public static void main(String[] args) {
		
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        
	      // Predicate<Integer> predicate = n -> true
	      // n ��һ���������ݵ� Predicate �ӿڵ� test ����
	      // n ��������� test �������� true
	        
	      System.out.println("�����������:");
	        
	      // ���ݲ��� n
	      eval(list, n->true);
	        
	      // Predicate<Integer> predicate1 = n -> n%2 == 0
	      // n ��һ���������ݵ� Predicate �ӿڵ� test ����
	      // ��� n%2 Ϊ 0 test �������� true
	      System.out.println("");
	      System.out.println("�������ż��:");
	      eval(list, n-> n%2 == 0 );
	        
	      // Predicate<Integer> predicate2 = n -> n > 3
	      // n ��һ���������ݵ� Predicate �ӿڵ� test ����
	      // ��� n ���� 3 test �������� true
	      System.out.println("");
	      System.out.println("������� 3 ����������:");
	      eval(list, n-> n > 3 );
	}
	    
   public static void eval(List<Integer> list, Predicate<Integer> predicate) {
      for(Integer n: list) {
        
         if(predicate.test(n)) {
            System.out.print(n + " ");
         }
      }
   }
}
