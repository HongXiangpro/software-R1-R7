package MultiIntervalSet;

import IntervalSet.*;
import java.util.Set;

public class MultiIntervalSetDecorator <L> implements MultiIntervalSet<L> {
    private final MultiIntervalSet<L> set ;

    // Abstraction function:
    // 装饰器将其他表进行包装
    // Representation invariant:
    //
    // Safety from rep exposure:
    // 将所有操作委托给传入的表执行

    public MultiIntervalSetDecorator(MultiIntervalSet<L> set) {
        if(set ==null){
            throw new NullPointerException();//runTimeException
        }
        this.set = set;
        checkRep();
    }

    /**
     * 插入标签及其3时间段
     * @param start：开始时间
     * @param end：结束时间
     * @param label：标签(不可重复,immutable)
     */
    @Override
    public void insert(long start, long end, L label) {
        set.insert(start,end,label);
        checkRep();
    }

    /**
     * 获取表中所有标签
     * @return 表中所有标签Set
     */
    @Override
    public Set<L> labels() {
        checkRep();
        return set.labels();
    }

    /**
     * 将该标签所有时间段存为一个以Integer为标签的时间表
     * @param label 标签
     * @return 标签对应所有时间段表
     */
    @Override
    public IntervalSet<Integer> intervals(L label) {
        checkRep();
        return set.intervals(label);
    }

    /**
     * 删除标签及其时间段
     * @param label：标签
     * @return 删除成功返回true，否则返回false
     */
    @Override
    public boolean remove(L label) {
        checkRep();
        return set.remove(label);
    }

    /**
     * 输入整个活动的开始和结束时间
     * @param start 整个时间表开始时间
     * @param end 整个时间表结束时间
     * @return 正确输入返回true，否则返回false
     */
    @Override
    public boolean InputStartEndTime(long start, long end) {
        checkRep();
        return set.InputStartEndTime(start,end);
    }

    /**
     * 获取整个活动开始时间
     * @return 活动开始时间
     */
    @Override
    public long GetStart() {
        checkRep();
        return set.GetStart();
    }

    /**
     * 获取整个活动结束时间
     * @return 活动结束时间
     */
    @Override
    public long GetEnd() {
        checkRep();
        return set.GetEnd();
    }
    @Override
    public String toString(){
        checkRep();
        return set.toString();
    }
    private void checkRep(){

    }
}
