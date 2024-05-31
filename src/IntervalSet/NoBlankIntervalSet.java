package IntervalSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NoBlankIntervalSet<L> {
    // Abstraction function:
    // 构建一个时间表set，一个标签只能有一个时间段保存时间表，在完成插入删除操作后没有空闲的时间，可实现插入、获取全部标签，去除标签、获取某个标签的开始和截至时间等方法
    // Representation invariant:
    // 时间表中标签非空，时间end大于start,在完成插入删除操作后检测没有空闲的时间
    // Safety from rep exposure:
    // 其余操作委托给所给的IntervalSet利用checkEmpty()在完成插入删除操作后没有空闲的时间。

    private final IntervalSet<L> intervalSet;
    private boolean flag = false;

    /**
     * 是否输入完毕
     * @param f 输入完毕为true，否则false
     */
    public void IsInputOver(boolean f){
        flag = f;
        if(flag){
            checkEmpty();
        }
    }
    //检测是否有空闲
    private void checkEmpty() throws IllegalArgumentException{
        long max=0,min = Long.MAX_VALUE;
        //找出时间区间
        if(intervalSet.GetStart() == intervalSet.GetEnd()){
            for (L l :intervalSet.labels()){
                long e = intervalSet.end(l);
                long s = intervalSet.start(l);
                if(max < e){
                    max = e;
                }
                if(min > s){
                    min = s;
                }
            }
        }
        else {
            max = intervalSet.GetEnd();
            min = intervalSet.GetStart();
        }
        if(isHasEmptyTime(min,max)){
            throw new IllegalArgumentException("含有空闲时间");
        }
    }

    /**
     * 用输入的时间表来构造intervalSet
     * @param s 输入的时间表
     */
    public NoBlankIntervalSet(IntervalSet<L> s) {
        intervalSet = s;
    }
    /**
     * 在当前对象中插入新的时间段和标签
     * @param start：开始时间
     * @param end：结束时间
     * @param label：标签(不可重复,immutable)
     */
    public void insert(long start, long end, L label) throws IllegalArgumentException {
        intervalSet.insert(start, end, label);
        if(flag){
            checkEmpty();
        }
    }
    /**
     *获取这个对象中所有的标签
     * @return 这个对象中标签的集合
     */
    //获取所有label
    public Set<L> labels() {
        Set<L> set = intervalSet.labels();
        if(flag){
            checkEmpty();
        }
        return set;
    }
    /**
     * 去除对象中的一个标签和时间段
     * @param label：标签
     * @return 去除成功返回true，没有找到返回false
     */
    //去除
    public boolean remove(L label) {
        boolean f;
        f = intervalSet.remove(label);
        if(flag){
            checkEmpty();
        }
        return f;
    }
    /**
     * 获取对应标签的开始时间
     * @param label:标签
     * @return 对象中对应标签的开始时间
     */
    //获取一个label的起点
    public long start(L label) {
        long start = intervalSet.start(label);
        if(flag){
            checkEmpty();
        }
        return start;
    }
    /**
     * 获取对应标签的结束时间
     * @param label：标签
     * @return 对象中对应标签的结束时间
     */
    //获取一个label的终点
    public long end(L label) {
        long end = intervalSet.end(label);
        if(flag){
            checkEmpty();
        }
        return end;
    }
    /**
     * 输入整个活动开始结束时间
     * @param start 整个时间表开始时间
     * @param end 整个时间表结束时间
     * @return 输入成功为true失败false
     */
    public boolean InputStartEndTime(long start,long end){
        if(flag){
            checkEmpty();
        }
        return intervalSet.InputStartEndTime(start,end);
    }
    /**
     *  获取活动开始时间
     * @return 如果start存在则返回，否则返回-1
     */
    public long GetStart(){
        if(flag){
            checkEmpty();
        }
        return intervalSet.GetStart();
    }
    /**
     *  获取活动结束时间
     * @return 如果end存在则返回，否则返回-1
     */
    public long GetEnd(){
        if(flag){
            checkEmpty();
        }
        return intervalSet.GetEnd();
    }
    @Override
    public String toString() {
        String string = intervalSet.toString();
        if(flag){
            checkEmpty();
        }
        return string;
    }
    //检测start与end之间是否有空闲
    private boolean isHasEmptyTime(long start, long end) {
        long end_m = 0;
        Map<L,Boolean> visited = new HashMap<>();//标记是否拜访过
        for(L l :intervalSet.labels()){
            visited.put(l,false);
        }

        for(L l :intervalSet.labels()){
            if(intervalSet.start(l) == start){
                end_m = intervalSet.end(l);
                break;
            }
        }

        boolean flag = false;

        while (!flag){
            boolean flag_1 = false;//标记是否更替过区间
            //逐步扩展区间
            for (L l : intervalSet.labels()){
                if(!visited.get(l)&&intervalSet.start(l)>= start&&intervalSet.start(l)<=end_m+1){
                    if(intervalSet.end(l)>end_m){
                        end_m = intervalSet.end(l);
                    }
                    visited.replace(l,false,true);
                    flag_1 = true;//标记更替过区间
                }
                //小区间的end已经在区间中则不用再进行访问
                if(!visited.get(l)&&intervalSet.end(l)<=end_m){
                    visited.replace(l,false,true);
                }
            }
            //没有更新过区间
            if(!flag_1){
                break;
            }
            //查看是否都访问过
            for(L l :intervalSet.labels()){
                if(!visited.get(l)){
                    flag = false;
                    break;
                }
                else {
                    flag = true;
                }
            }
        }
        if(end_m > end){
            System.out.println("实际安排结束时间大于活动时间");
        }
        //没有空闲
        return end_m < end;
    }
}
