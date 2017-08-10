package lihao.util;

public class Test {

    /**
     * 计算预留截止时间
     * @param erpId
     * @param endTimeNum 预留时间
     * @param ConOtherIsWeek 顺延与否：1-不顺延 0-顺延
     * @return 预留截止时间
     * @throws Exception
     * @author Lihao 2017-3-4 10:25:04
     */
    public Date getEndTime(long erpId, int endTimeNum, Integer ConOtherIsWeek)throws Exception{
        Calendar calendar = Calendar.getInstance();
        try{
            LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间.预留时长：{} 小时",new Object[]{erpId,endTimeNum});
            //接收传过来的系统预留小时或对接的产品预留时间
            int orderDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//当前周几
            int orderHour = calendar.get(Calendar.HOUR_OF_DAY);//当前小时
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LOG.info("ERPID：{} 下单操作，当前订单下单时间：{} ",new Object[]{erpId,format.format(calendar.getTime())});
            //不顺延
            if(ConOtherIsWeek == 1){
                calendar.add(Calendar.HOUR_OF_DAY, endTimeNum);//当前时间加上预留时长
                LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间，参数ConOtherIsWeek={}，走【不顺延】线路，预留截止时间：{} ",new Object[]{erpId,ConOtherIsWeek,format.format(calendar.getTime())});
                return calendar.getTime();
            }else if(ConOtherIsWeek == 0){//顺延
                LOG.info("ERPID：{} 下单操作，获取预留单预留截止时间，参数ConOtherIsWeek={}，走【顺延】线路....",new Object[]{erpId,ConOtherIsWeek});
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
                    }else{
                        LOG.info("ERPID：{} 下单操作，预留截止时间为：{} 非周六周日，直接处理..",new Object[]{erpId,format.format(calendarT.getTime())});
                    }
                    return calendarT.getTime();
                }
            }else{
                calendar.add(Calendar.HOUR_OF_DAY, endTimeNum);//当前时间加上预留时长
                LOG.error("ERPID：{} 下单操作，获取预留单预留截止时间，参数ConOtherIsWeek={} 异常，走【不顺延】线路...预留截止时间：{}",new Object[]{erpId,ConOtherIsWeek,format.format(calendar.getTime())});
                return calendar.getTime();
            }
        }catch(Exception e){
            LOG.error("ERPID："+erpId+" 下单操作，计算预留截止时间异常："+e.getMessage(),e);
        }
        return  calendar.getTime();
    }
}
