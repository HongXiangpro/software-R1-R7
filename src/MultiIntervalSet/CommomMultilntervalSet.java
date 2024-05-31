package MultiIntervalSet;

import java.util.*;

import IntervalSet.*;

public class CommomMultilntervalSet<L> implements MultiIntervalSet<L>{
    private final ArrayList<IntervalSet<L>> multiintervalset =new ArrayList<>();
    long startTime  = 0L,endTime = 0L;
    // Abstraction function:
    // 构建一个时间表，一个标签只能有一个时间段commomintervalset保存时间表允许出现一个label多个时间段的情况，可实现插入标签时间段、获取全部标签，去除标签,获取标签的所有时间段等方法
    // Representation invariant:
    // 时间表中所有标签非空，所有时间段end均大于start
    // Safety from rep exposure:
    //  multiintervalset设为private和final,仅通过方法进行操作
    public CommomMultilntervalSet(){

    }

    public CommomMultilntervalSet(IntervalSet<L> initial) {
        this.multiintervalset.add(initial);
    }

    @Override
    public void insert(long start, long end, L label) throws IllegalArgumentException{
        if(end<start){
            throw new IllegalArgumentException("时间段输入错误");
        }
        for(IntervalSet<L> set : this.multiintervalset){
            boolean flag = false;
            //寻找每个表中对应标签
            for(L l : set.labels()){
                //找到则跳往下一个时间表
                if(l.equals(label)){
                    flag = true;
                    break;
                }
            }
            //没找到则在时间表中插入新标签
            if (!flag){
                set.insert(start,end,label);
                checkRep();
                return;
            }
        }
        //如果全都有对应标签
        IntervalSet<L> set = new CommonIntervalSet<L>();
        set.insert(start,end,label);
        this.multiintervalset.add(set);
    }

    @Override
    public Set<L> labels() {
        if (multiintervalset.isEmpty()){
            checkRep();
            return new HashSet<>();
        }
        checkRep();
        return this.multiintervalset.getFirst().labels();//第一个表中一定包含了所有标签
    }

    @Override
    public IntervalSet<Integer> intervals(L label) {
        IntervalSet<Integer> set = new CommonIntervalSet<Integer>();
        int time=0;
        for(IntervalSet<L> s : this.multiintervalset){
            for (L l : s.labels()){
                if(l.equals(label)){
                    set.insert(s.start(label),s.end(label), time);
                    time++;
                }
            }
        }
        checkRep();
        return set;
    }


    @Override
    public boolean remove(L label) {
        boolean flag = false;
        for (IntervalSet<L> set :this.multiintervalset){
             flag = flag || set.remove(label);
        }
        checkRep();
        return flag;
    }

    @Override
    public boolean InputStartEndTime(long start, long end) {
        if (start<end){
            this.endTime = end;
            this.startTime = start;
            checkRep();
            return true;
        }
        checkRep();
        return false;
    }

    @Override
    public long GetStart() {
        checkRep();
        return this.startTime;
    }

    @Override
    public long GetEnd() {
        return this.endTime;
    }
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("label"+"  时间段\n");
        for (L l : this.labels()) {
            IntervalSet<Integer> set = this.intervals(l);
            StringBuilder time = new StringBuilder();
            for (int i = 0;i<set.labels().size();i++){
                time.append("[").append(set.start(i)).append(", ").append(set.end(i)).append("] ");
            }
            s.append(l).append("  "+time+"\n");
        }
        checkRep();
        return s.toString();
    }
    private void checkRep(){
        for (IntervalSet<L> intervalSet : this.multiintervalset){
            for ( L l :intervalSet.labels()){
                if(l == null){
                    assert false;
                }
                if (intervalSet.start(l)>intervalSet.end(l)){
                    assert false;
                }
            }
        }
    }
}
