package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 返回一个新的集合
 * @author Lihao
 *
 */
public class JDK8StreamCollectTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main1(String[] args) {
		
		/**
		 * 先把 list 集合的 奇数去掉，然后把剩下的偶数返回到 _list 集合中
		 */
		List list = new ArrayList();  
        list.add(1);  
        list.add(2);  
        list.add(3);  
        list.add(4);  
        list.add(5);  
          
        List _list = (List) list.stream().filter((param) -> (int)param % 2 == 0).collect(Collectors.toList());  
        _list.forEach(System.out::println);  

	}


	/**
	 * 计算成本价，用于 [我是卖家] -- [订单管理]下显示
	 * @author Lihao 2016-11-30 10:20:44
	 * 
	 */
	/*List<LinePlanOrd> linePlanOrds = (List<LinePlanOrd>) paginate.getResult();
	
	if(null!=linePlanOrds && !linePlanOrds.isEmpty()){
		
		Map<Long,BaseTransportCity> baseTransportCityMap = new HashMap<>();
		
		List<Long> transportCityIds = (List<Long>) linePlanOrds.stream().map(l->l.getBaseTransportCityId()).filter(l -> l>0 ).collect(Collectors.toList());
		
		if(null != transportCityIds && !transportCityIds.isEmpty()){

			List<BaseTransportCity> baseTransportCitys = baseTransportCityService.search(search.getErpId(), transportCityIds);
			
			baseTransportCityMap = baseTransportCitys.stream().collect(Collectors.toMap(BaseTransportCity::getId,l->l));
		}
		
		for (LinePlanOrd l : linePlanOrds) {
			
			//l.getAdultPriceT();//同行成人价
			//l.getChildPriceT();//同行小童价
			
			BigDecimal costAmount = new BigDecimal(0);
			
			costAmount = l.getAdultPriceT().multiply(new BigDecimal(l.getAduNum())).add(l.getChildPriceT().multiply(new BigDecimal(l.getChdNum()))) ;
			
			if (l.getBaseTransportCityId() > 0) {

				costAmount.add(baseTransportCityMap.get(l.getBaseTransportCityId()).getSalePrice());
			}
			l.setCostAmount(costAmount);//成本价
		}
	}*/
	
	
	public static void main(String[] args) {
		
		List<User> users = new ArrayList<>();
		
		User c1 = new User();
		c1.setId(1);
		c1.setTid(1);
		c1.setName("lihao");
		users.add(c1);
		
		User c2 = new User();
		c2.setId(2);
		c2.setTid(2);
		c2.setName("王五");
		users.add(c2);
		
		User c3 = new User();
		c3.setId(3);
		c3.setTid(2);
		c3.setName("赵四");
		users.add(c3);
		
		System.out.println(users.stream().map(l->l.getName()).collect(Collectors.toList()).toString());
		
		Map<Integer, List<User>> linePlanUserCpyMap = users.stream().collect(Collectors.groupingBy(User::getTid)); 
		
		//System.err.println(linePlanUserCpyMap.size());
		
		for (int i = 1; i <= linePlanUserCpyMap.size(); i++) {
			
			List<String>    StringList =  linePlanUserCpyMap.get(i).stream().map(l->l.getName()).collect(Collectors.toList());
			
			for (int j = 0; j < StringList.size(); j++) {
				
				//System.out.println(StringList.get(j));
			}
			
			//System.out.println();
		}
	}
	
	public static void main3(String[] args) {
		
		List<Map<String,Object>> mapList = new ArrayList<>();
		
		Map<String,Object> map1 = new HashMap<>();
		map1.put("lineId", "1000");
		map1.put("planCount", "2");
		mapList.add(map1);
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("lineId", "1001");
		map2.put("planCount", "3");
		mapList.add(map2);
		
		Map<String,Object> map3 = new HashMap<>();
		map3.put("lineId", "1002");
		map3.put("planCount", "4");
		mapList.add(map3);
		
		Map<Long,Map<String, Object>> linePlanMap =  mapList.stream().collect(Collectors.toMap(k->Long.valueOf((String) k.get("lineId")),l->l));
		
		linePlanMap.forEach((id, val) -> System.out.println(val.get("lineId") +"  :  "+val.get("planCount")));
		
	}

}
