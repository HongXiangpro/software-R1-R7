package IntervalSet;

import java.util.*;

/**
 * IntervalSet<L>的实现
 * @param <L>泛型
 */
public class CommonIntervalSet<L> implements IntervalSet<L>{
    private final Map<L, ArrayList<Long>> commonintervalset = new HashMap<>();
    long start=0L,end = 0L;
    // Abstraction function:
    // 构建一个时间表，一个标签只能有一个时间段commomintervalset保存时间表，可实现插入、获取全部标签，去除标签、获取某个标签的开始和截至时间等方法
    // Representation invariant:
    // 时间表中标签非空，时间end大于start
    // Safety from rep exposure:
    // 将commonintervalset设置为private和final,通过labels、start、end方法来观察，在labels、start、end方法中均使用了防御式拷贝

    //建立空的时间表
    public CommonIntervalSet(){

    }
    //建立非空时间表
    public CommonIntervalSet(IntervalSet<L> s){
        for (L l:s.labels()){
            ArrayList<Long> a = new ArrayList<>();
            a.add(s.start(l));
            a.add(s.end(l));
            this.commonintervalset.put(l,a);
        }
        checkRep();
    }
    private void checkRep(){
        for (Map.Entry<L,ArrayList<Long>> entry :this.commonintervalset.entrySet()){
            if(entry.getKey() == null){
                assert false;
            }
            if (entry.getValue().get(1)<entry.getValue().getFirst()){
                assert false;
            }
        }
    }
    /**
     * 在当前对象中插入新的时间段和标签
     * @param start：开始时间
     * @param end：结束时间
     * @param label：标签(不可重复,immutable)
     */
    @Override
    public void insert(long start, long end, L label) throws IllegalArgumentException{
        ArrayList<Long> T = new ArrayList<>();
        T.add(start);
        T.add(end);
        for (L l : this.commonintervalset.keySet()){
            if(l.equals(label)){
                throw new IllegalArgumentException("该label已经存在!");
            }
        }
        this.commonintervalset.put(label,T);
        checkRep();
    }
    /**
     *获取这个对象中所有的标签
     * @return 这个对象中标签的集合
     */
    @Override
    public Set<L> labels() {
        Set<L> la = new HashSet<>();
        la.addAll(this.commonintervalset.keySet());
        checkRep();
        return la;
    }
    /**
     * 去除对象中的一个标签和时间段
     * @param label：标签
     * @return 去除成功返回true，没有找到返回false
     */
    @Override
    public boolean remove(L label) {
        for(L l : this.commonintervalset.keySet()){
            if(l.equals(label)){
                this.commonintervalset.remove(l);
                checkRep();
                return true;
            }
        }
        checkRep();
        return false;
    }
    /**
     * 获取对应标签的开始时间
     * @param label:标签
     * @return 对象中对应标签的开始时间
     */
    @Override
    public long start(L label) {
        long start;
        ArrayList<Long> a = new ArrayList<>();
        a = this.commonintervalset.get(label);
        if (a!=null){
            start=a.getFirst();
        }
        else {
            return -1;
        }
        checkRep();
        return start;
    }
    /**
     * 获取对应标签的结束时间
     * @param label：标签
     * @return 对象中对应标签的结束时间
     */
    @Override
    public long end(L label) {
        long end;
        ArrayList<Long> a = new ArrayList<>();
        a = this.commonintervalset.get(label);
        if (a!=null){
            end=a.get(1);
        }
        else {
            return -1;
        }
        checkRep();
        return end;
    }
    /**
     * 输入整个活动开始结束时间
     * @param start 整个时间表开始时间
     * @param end 整个时间表结束时间
     * @return 输入成功为true失败false
     */
    @Override
    public boolean InputStartEndTime(long start, long end) {
        if(start<end){
            this.start = start;
            this.end = end;
            return true;
        }
        return false;
    }
    /**
     *  获取活动开始时间
     * @return 如果start存在则返回，否则返回-1
     */
    @Override
    public long GetStart() {
        if(this.start !=0){
            return this.start;
        }
        else {
            return -1;
        }
    }
    /**
     * 获取活动结束时间
     * @return 如果end存在则返回，否则返回-1
     */
    @Override
    public long GetEnd() {
        if(this.end !=0){
            return this.end;
        }
        else {
            return -1;
        }
    }
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("label"+"  时间段\n");
        for (Map.Entry<L,ArrayList<Long>> entry :this.commonintervalset.entrySet()){
            s.append(entry.getKey()).append("     ").append(entry.getValue()).append("\n");
            }
        return s.toString();
        }
}
