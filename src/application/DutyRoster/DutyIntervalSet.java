package application.DutyRoster;

import IntervalSet.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class DutyIntervalSet<L>{
 private NoBlankIntervalSet<L> set = new NoBlankIntervalSet<L>(new NonOverlapIntervalSet<L>(new CommonIntervalSet<L>()));
    // Abstraction function:
    // 构建一个不重叠、无空白的单时间区间表
    // Representation invariant:
    // 各个时间段不重叠、无空白
    // Safety from rep exposure:
    //
    public DutyIntervalSet(){

    }

    /**
     *
     * @param intervalSet 一个CommonIntervalSet，其他IntervalSet也可以但是会多次运行重复代码
     */
    public DutyIntervalSet(IntervalSet<L> intervalSet){
        set = new NoBlankIntervalSet<L>(new NonOverlapIntervalSet<L>(new CommonIntervalSet<L>(intervalSet)));
    }

    /**
     * 在当前对象中插入新的时间段和标签
     * @param start：开始时间
     * @param end：结束时间
     * @param label：标签(不可重复,immutable)
     */
    public void insert(long start,long end,L label){
        set.insert(start,end,label);
    }
    /**
     *获取这个对象中所有的标签
     * @return 这个对象中标签的集合
     */
    public Set<L> labels(){
        return set.labels();
    }
    /**
     * 去除对象中的一个标签和时间段
     * @param label：标签
     * @return 去除成功返回true，没有找到返回false
     */
    public boolean remove(L label){
        return set.remove(label);
    }
    /**
     * 获取对应标签的开始时间
     * @param label:标签
     * @return 对象中对应标签的开始时间
     */
    public long start(L label){
        return  set.start(label);
    }
    /**
     * 获取对应标签的结束时间
     * @param label：标签
     * @return 对象中对应标签的结束时间
     */
    public long end(L label){
        return set.end(label);
    }
    /**
     *
     * @return 如果start存在则返回，否则返回-1
     */
    public long GetStart(){
        return set.GetStart();
    }
    /**
     *
     * @return 如果end存在则返回，否则返回-1
     */
    public long GetEnd(){
        return set.GetEnd();
    }
    /**
     *
     * @param start 整个时间表开始时间
     * @param end 整个时间表结束时间
     * @return 输入成功为true失败false
     */
    public boolean InputStartEndTime(long start,long end){
        return set.InputStartEndTime(start,end);
    }
    /**
     * 是否输入完毕
     * @param f 输入完毕为true，否则false
     */
    public void IsInputOver(boolean f){
        set.IsInputOver(f);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("日期\t\t").append("值班人名字\t").append("职位\t\t").append("手机号码\n");
        long max = 0, min = Long.MAX_VALUE;
        // 找出时间区间
        for (L l : set.labels()) {
            long e = set.end(l);
            long s = set.start(l);
            if (max < e) {
                max = e;
            }
            if (min > s) {
                min = s;
            }
        }
        // 将最小值和最大值设置为整天的时间戳
        min = truncateToDay(min);
        max = truncateToDay(max) + 86399999L; // 一天结束的时间戳（午夜前一秒）
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(min);
        while (currentDate.getTime() <= max) {
            stringBuilder.append(dateFormat.format(currentDate)).append("\t\t");
            stringBuilder.append(findLabel(currentDate).toString()).append("\n");
            currentDate = addDays(currentDate, 1);
        }
        return stringBuilder.toString();
    }

    // 截断时间戳到一天的开始
    private long truncateToDay(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    // 给日期增加指定的天数
    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    private L findLabel(Date date){
        long time = date.getTime();
        for (L l: set.labels()){
            if(set.start(l)<=time&&set.end(l)>=time){
                return l;
            }
        }
        throw new IllegalArgumentException("输入时间不在给定时间之内或未按天进行输入");
    }
}
