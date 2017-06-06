package yiranlihao.learn.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DateUtil {

	private static Logger LOG = LoggerFactory.getLogger(DateUtil.class);
	
	public static void main(String[] args) throws Exception {
		
		Date orderDate = new Date();
		int orderDay = orderDate.getDay();//当前周几
		int orderHour = orderDate.getHours();
		int orderMin = orderDate.getMinutes();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		
		System.out.println(format.format(orderDate));
		
		orderDate.setHours(orderHour+24);
		
		
		System.out.println(format.format(orderDate));
		
		//System.err.println(format.format(getEndTime(1,new Date(),6*24,0)));
		
//		Calendar calendar =  Calendar.getInstance();
//
//		System.out.println(format.format(calendar.getTime()));
//		
//		calendar.setTime(orderDate);
//		
//		System.err.println(format.format(calendar.getTime()));
//		
//		calendar.add(Calendar.HOUR_OF_DAY, 24);//+小时
//		
//		System.err.println(format.format(calendar.getTime()));
//		
//		calendar.add(Calendar.DAY_OF_MONTH, 7);//+天数
//		
//		System.err.println(format.format(calendar.getTime()));
		
		LOG.info("id：{} name:{} p:{}",1,2,3);
		
		System.out.println(format.format(getEndTime2(1,96,0)));
		
	}
	
	public static Date getEndTime(long erpId, Date orderDate, int endTimeNum, Integer conOtherIsWeek)throws Exception{
		try{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间.预留时长：{} 小时",new Object[]{erpId,endTimeNum});
			if(null==orderDate){
				orderDate = new Date();
				LOG.info("ERPID：{} 下单操作，函数获取的参数orderDate为null，默认取当前时间",new Object[]{erpId,format.format(orderDate)});
			}
			//接收传过来的系统预留小时或对接的产品预留时间
			int orderDay = orderDate.getDay();//当前周几
			int orderHour = orderDate.getHours();
			int orderMin = orderDate.getMinutes();
			LOG.info("当前订单下单时间：{} ，需顺延时长：{} 小时",new Object[]{format.format(orderDate),endTimeNum});
			//con_Other_isWeek 1 表示不顺延
			//con_Other_isWeek: 0 表示顺延
			if(conOtherIsWeek == null){
				conOtherIsWeek = 0;//默认给0 ，走【顺延】
				LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间,BaseConfig配置参数异常,默认走【顺延】线路..",erpId);
			}
			
			//不顺延
			if(conOtherIsWeek == 1){
				orderDate.setHours(orderHour+endTimeNum);//当前时间加上预留时长
				LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间,走【不顺延】线路，预留截止时间：{} ",new Object[]{erpId,format.format(orderDate)});
				return orderDate;
			}else{//顺延
				LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间,走【顺延】线路....",erpId);
				
				//预留时长超过一周
				if(endTimeNum >= 7*24 ){
					orderDate.setDate(orderDate.getDate()+7);//直接加7天，按7天之后的时间处理
					LOG.info("ERPID：{} 下单操作，预留时长超过7*24h....取操作时间：{} 进行递归运算...",new Object[]{erpId,format.format(orderDate)});
					/**
					 * 高能！！！ 
					 * 往后推7天，顺延周末两天不算，实际占用的预留时间是5天
					 */
					getEndTime(erpId, orderDate, endTimeNum-5*24, conOtherIsWeek);
				}else{
					if(orderDay == 5) {//周五 -- 顺延两天
						if (orderHour+endTimeNum >23) {
							orderDate.setDate(orderDate.getDate()+3);//周五下单顺延两天
							orderDate.setHours(orderHour+endTimeNum);
							orderDate.setMinutes(orderMin);
							orderDate.setSeconds(30);
						}else{
							orderDate.setHours(orderHour+endTimeNum);
						}
					}else if(orderDay==6 || orderDay == 0) {//周六或周日
						if (orderDay==6) {
							orderDate.setDate(orderDate.getDate()+2);
						}else {
							orderDate.setDate(orderDate.getDate()+1);
						}
						orderDate.setHours(endTimeNum-1);
					    orderDate.setMinutes(59);
					    orderDate.setSeconds(59);
					}else {//周一至周四
						orderDate.setHours(orderHour+endTimeNum);
					}
					return orderDate;
				}
			}
		}catch(Exception e){
			LOG.error("ERPID：{} 下单操作，获取预留单预留截止时间 系统异常："+e.getMessage(),e);
		}
		return  orderDate;
	}
	/**
	 * 计算预留截止时间
	 * @param erpId
	 * @param endTimeNum 预留时间
	 * @param ConOtherIsWeek 顺延与否：1-不顺延 0-顺延
	 * @return 预留截止时间
	 * @throws Exception
	 */
	public static Date getEndTime2(long erpId, int endTimeNum, Integer ConOtherIsWeek)throws Exception{
		Calendar calendar = Calendar.getInstance();
		try{
			LOG.info("ERPID：{}  下单操作，获取预留单预留截止时间.预留时长：{} 小时",new Object[]{erpId,endTimeNum});
			//接收传过来的系统预留小时或对接的产品预留时间
			int orderDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//当前周几
			int orderHour = calendar.get(Calendar.HOUR_OF_DAY);//当前时间
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			LOG.info("当前订单下单时间：{} ",format.format(calendar.getTime()));
			//con_Other_isWeek 1 表示不顺延
			//con_Other_isWeek: 0 表示顺延
			
			//不顺延
			if(ConOtherIsWeek == 1){
				calendar.add(Calendar.HOUR_OF_DAY, endTimeNum);//当前时间加上预留时长
				LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间,走【不顺延】线路，预留截止时间：{} ",new Object[]{erpId,calendar.getTime()});
				return calendar.getTime();
			}else{//顺延
				LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间,走【顺延】线路....",erpId);
				
				/**
				 * 根据下单时间判断
				 * 如果是周六周日，则下单时间按照下周一的零点零分计算
				 */
				if(orderDayOfWeek==Calendar.SATURDAY || orderDayOfWeek == Calendar.SUNDAY) {//周六或周日
					if (orderDayOfWeek==Calendar.SATURDAY ) {
						//周六下单，计算下单开始时间
						calendar.add(Calendar.DAY_OF_MONTH, 2);//加2天，天数设置为下周一对应的日期天数
						calendar.add(Calendar.HOUR_OF_DAY, -orderHour);//小时数置零
						calendar.set(Calendar.MINUTE,0);//分钟数置零
						calendar.set(Calendar.SECOND,0);//秒数置零
						calendar.set(Calendar.MILLISECOND, 0);//微秒数置零
						LOG.info("ERPID：{} 下单操作，周六下单，下单时间从下周一凌晨0点0分开始计算....下单时间：{} ",new Object[]{erpId,format.format(calendar.getTime())});
					}else {
						//周日下单，计算下单开始时间
						calendar.add(Calendar.DAY_OF_MONTH, 1);//加2天，天数设置为下周一对应的日期天数
						calendar.add(Calendar.HOUR_OF_DAY, -orderHour);//小时数置零
						calendar.set(Calendar.MINUTE,0);//分钟数置零
						calendar.set(Calendar.SECOND,0);//秒数置零
						calendar.set(Calendar.MILLISECOND, 0);//微秒数置零
						LOG.info("ERPID：{} 下单操作，周日下单，下单时间从下周一凌晨0点0分开始计算....下单时间：{} ",new Object[]{erpId,format.format(calendar.getTime())});
					}
					calendar.add(Calendar.HOUR_OF_DAY, endTimeNum);//计算预留截止时间
					/**
					 * 计算预留截止时间，根据预留截止时间判断，如果时间落在周六，往后延48小时，如果落在周天，往后延24小时
					 */
					if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
						LOG.info("ERPID：{} 下单操作，预留截止时间为：{}  落在周六，需往后延长48小时,,,",new Object[]{erpId,format.format(calendar.getTime())});
						calendar.add(Calendar.HOUR_OF_DAY, 48);
						LOG.info("ERPID：{} 下单操作，预留截止时间落在周六，往后延长48小时,,,最终预留截止时间为：{}",new Object[]{erpId,format.format(calendar.getTime())});
					}else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
						LOG.info("ERPID：{} 下单操作，预留截止时间为：{}  落在周日，需往后延长24小时,,,",new Object[]{erpId,format.format(calendar.getTime())});
						calendar.add(Calendar.HOUR_OF_DAY, 24);
						LOG.info("ERPID：{} 下单操作，预留截止时间落在周日，往后延长24小时,,,最终预留截止时间为：{}",new Object[]{erpId,format.format(calendar.getTime())});
					}else{
						LOG.info("ERPID：{} 下单操作，预留截止时间为：{} 非周六周日，直接处理..",new Object[]{erpId,format.format(calendar.getTime())});
					}
					return calendar.getTime();
				}else {
					/**
					 * 周一至周五下单
					 */
					//获取预留截止时间
					Calendar calendarT = Calendar.getInstance();
					calendarT.setTime(calendar.getTime());
					calendarT.add(Calendar.HOUR_OF_DAY, endTimeNum);//当前时间加上预留时长
					LOG.info("ERPID：{} 下单操作，周一至周四下单.....下单时间：{} 预留截止时间：{}",new Object[]{erpId,format.format(calendar.getTime()),format.format(calendarT.getTime())});
					/**
					 * 根据预留截止时间判断:
					 * 如果时间落在周六:往后延48小时，再退前{hoursCha}小时 hoursCha=预留截止时间所在的周六凌晨0点-下单时间
					 * 如果落在周天，往后延24小时，再退前{hoursCha}小时 hoursCha=预留截止时间所在的周六凌晨0点-下单时间
					 * @author Lihao 2017-3-4 9:41:49
					 */
					if(calendarT.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){//预留截止时间落在周六
						//计算周六凌晨0点与下单隔多长时间
						int hour = calendarT.get(Calendar.HOUR_OF_DAY);//当前小时数
						calendarT.add(Calendar.HOUR_OF_DAY, -hour);//获取周六凌晨的小时数
						int hoursCha = (int) ((calendarT.getTimeInMillis()- calendar.getTimeInMillis())/(1000 * 3600));
						calendarT.add(Calendar.HOUR_OF_DAY, endTimeNum-hoursCha+48);
						LOG.info("ERPID：{} 下单操作，预留截止时间落在周六，推延48-{}小时,,,最终预留截止时间为：{}",new Object[]{erpId,hoursCha,format.format(calendarT.getTime())});
					}else if(calendarT.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){//预留截止时间落在周日
						//计算周六凌晨0点与下单隔多长时间
						int hour = calendarT.get(Calendar.HOUR_OF_DAY);//当前小时数
						calendarT.add(Calendar.DAY_OF_MONTH, -1);//获取周六的天数
						calendarT.add(Calendar.HOUR_OF_DAY, -hour);//获取周六凌晨的小时数
						int hoursCha = (int) ((calendarT.getTimeInMillis()- calendar.getTimeInMillis())/(1000 * 3600));
						calendarT.add(Calendar.HOUR_OF_DAY, endTimeNum-hoursCha+24);
						LOG.info("ERPID：{} 下单操作，预留截止时间落在周日，推延24-{}小时,,,最终预留截止时间为：{}",new Object[]{erpId,hoursCha,format.format(calendarT.getTime())});
					}
					return calendarT.getTime();
				}
			}
		}catch(Exception e){
			LOG.info("ERPID："+erpId+" 下单操作，计算预留截止时间异常："+e.getMessage(),e);
		}
		return  calendar.getTime();
	}
	
	public static Date getEndTime1(long erpId, Date orderDate, int endTimeNum, Integer conOtherIsWeek)throws Exception{
		
		try{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间.预留时长：{} 小时",new Object[]{erpId,endTimeNum});
			if(null==orderDate){
				orderDate = new Date();
				LOG.info("ERPID：{} 下单操作，函数获取的参数orderDate为null，默认取当前时间",new Object[]{erpId,format.format(orderDate)});
			}
			Calendar calendar =  Calendar.getInstance();
			calendar.setTime(orderDate);
			
			if(conOtherIsWeek == null){
				conOtherIsWeek = 0;//默认给0 ，走【顺延】
				LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间,BaseConfig配置参数异常,默认走【顺延】线路..",erpId);
			}
			
			//不顺延
			if(conOtherIsWeek == 1){
				calendar.add(Calendar.HOUR_OF_DAY, endTimeNum);//当前时间加上预留时长
				LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间,走【不顺延】线路，预留截止时间：{} ",new Object[]{erpId,format.format(calendar.getTime())});
				return calendar.getTime();
			}else{//顺延
				LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间,走【顺延】线路....",erpId);
				
				//预留时长超过一周
				if(endTimeNum >= 5*24 ){
					calendar.add(Calendar.DAY_OF_MONTH,7);//直接加7天，按7天之后的时间处理
					LOG.info("ERPID：{} 下单操作，预留时长超过5*24h....取操作时间：{} 进行递归运算...",new Object[]{erpId,format.format(calendar.getTime())});
					/**
					 * 高能！！！ 
					 * 往后推7天，顺延周末两天不算，实际占用的预留时间是5天:剩余预留时长 = 预留时长 - 5*24
					 */
					getEndTime(erpId, calendar.getTime(), endTimeNum-5*24, conOtherIsWeek);
				}else{
//					if(Calendar.FRIDAY == calendar.get(Calendar.DAY_OF_WEEK)) {//周五 -- 顺延两天
//						
//						if(calendar.get(Calendar.HOUR_OF_DAY)+endTimeNum > 23){
//							
//							
//						}
//						
//						if (orderHour+endTimeNum >23) {
//							orderDate.setDate(orderDate.getDate()+3);//周五下单顺延两天
//							orderDate.setHours(orderHour+endTimeNum);
//							orderDate.setMinutes(orderMin);
//							orderDate.setSeconds(30);
//						}else{
//							orderDate.setHours(orderHour+endTimeNum);
//						}
//					}else if(orderDay==6 || orderDay == 0) {//周六或周日
//						if (orderDay==6) {
//							orderDate.setDate(orderDate.getDate()+2);
//						}else {
//							orderDate.setDate(orderDate.getDate()+1);
//						}
//						orderDate.setHours(endTimeNum-1);
//					    orderDate.setMinutes(59);
//					    orderDate.setSeconds(59);
//					}else {//周一至周四
//						orderDate.setHours(orderHour+endTimeNum);
//					}
					return calendar.getTime();
				}
			}
			
			
			
			
			
			
			
			
		}catch(Exception e){
			
			
			
			
		}
		
		
		
		return orderDate;
	}
}
