package IntervalSet;

import java.util.*;

public class NoBlankIntervalSet1<L> extends IntervalSetDecorator<L> {

    // Abstraction function:
    // 构建一个时间表set，一个标签只能有一个时间段保存时间表，在完成插入删除操作后没有空闲的时间，可实现插入、获取全部标签，去除标签、获取某个标签的开始和截至时间等方法
    // Representation invariant:
    // 时间表中标签非空，时间end大于start,在完成插入删除操作后没有空闲的时间
    // Safety from rep exposure:
    // 其余操作委托给CommomIntervalSet利用checkRep()在完成插入删除操作后没有空闲的时间。

    //检测是否有空闲，在insert和remove完成后
    public void checkEmpty() throws IllegalArgumentException{
        long max=0,min = Long.MAX_VALUE;
        //找出时间区间
        if(super.GetStart() == super.GetEnd()){
            for (L l :super.labels()){
                long e = super.end(l);
                long s = super.start(l);
                if(max < e){
                    max = e;
                }
                if(min > s){
                    min = s;
                }
            }
        }
        else {
            max = super.GetEnd();
            min = super.GetStart();
        }
        if(isHasEmptyTime(min,max)){
            throw new IllegalArgumentException("含有空闲时间");
        }
    }
    //依靠输入表创建无空闲表
    public NoBlankIntervalSet1(IntervalSet<L> s) {
        super(s);
    }
    @Override
    public void insert(long start, long end, L label) throws IllegalArgumentException {
        super.insert(start, end, label);
    }

    @Override
    public Set<L> labels() {
        Set<L> set = super.labels();
        checkEmpty();
        return set;
    }

    @Override
    public boolean remove(L label) {
        boolean f;
        f = super.remove(label);
        return f;
    }
    @Override
    public long start(L label) {
        long start = super.start(label);
        checkEmpty();
        return start;
    }
    @Override
    public long end(L label) {
        long end = super.end(label);
        checkEmpty();
        return end;
    }
    @Override
    public String toString() {
        String string = super.toString();
        checkEmpty();
        return string;
    }
    //检测start与end之间是否有空闲
    private boolean isHasEmptyTime(long start, long end) {
        long end_m = 0;
        Map<L,Boolean> visited = new HashMap<>();//标记是否拜访过
        for(L l :super.labels()){
            visited.put(l,false);
        }

        for(L l :super.labels()){
            if(super.start(l) == start){
                end_m = super.end(l);
                break;
            }
        }

        boolean flag = false;

        while (!flag){
            boolean flag_1 = false;//标记是否更替过区间
            //逐步扩展区间
            for (L l : super.labels()){
                if(!visited.get(l)&&super.start(l)>= start&&super.start(l)<=end_m+1){
                    if(super.end(l)>end_m){
                        end_m = super.end(l);
                    }
                    visited.replace(l,false,true);
                    flag_1 = true;//标记更替过区间
                }
                //小区间的end已经在区间中则不用再进行访问
                if(!visited.get(l)&&super.end(l)<=end_m){
                    visited.replace(l,false,true);
                }
            }
            //没有更新过区间
            if(!flag_1){
                break;
            }
            //查看是否都访问过
            for(L l :super.labels()){
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