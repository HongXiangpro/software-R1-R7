package IntervalSet;

import java.util.Set;

public class IntervalSetDecorator<L> implements IntervalSet<L>{
    private final IntervalSet<L> set ;

    // Abstraction function:
    // 装饰器将其他表进行包装
    // Representation invariant:
    //
    // Safety from rep exposure:
    // 将所有操作委托给传入的表执行

    /**
     * 构造器
     * @param set 按照set进行扩张
     */
    public IntervalSetDecorator(IntervalSet<L> set) {
        if(set ==null){
            throw new NullPointerException();//runTimeException
        }
        this.set = set;
    }
    /**
     * 在当前对象中插入新的时间段和标签
     * @param start：开始时间
     * @param end：结束时间
     * @param label：标签(不可重复,immutable)
     */
    @Override
    public void insert(long start, long end, L label) {
        set.insert(start,end,label);
    }
    /**
     *获取这个对象中所有的标签
     * @return 这个对象中标签的集合
     */
    @Override
    public Set<L> labels() {
        return set.labels();
    }
    /**
     * 去除对象中的一个标签和时间段
     * @param label：标签
     * @return 去除成功返回true，没有找到返回false
     */
    @Override
    public boolean remove(L label) {
        return set.remove(label);
    }
    /**
     * 获取对应标签的开始时间
     * @param label:标签
     * @return 对象中对应标签的开始时间
     */
    @Override
    public long start(L label) {
        return set.start(label);
    }
    /**
     * 获取对应标签的结束时间
     * @param label：标签
     * @return 对象中对应标签的结束时间
     */
    @Override
    public long end(L label) {
        return set.end(label);
    }
    /**
     * 输入整个活动开始结束时间
     * @param start 整个时间表开始时间
     * @param end 整个时间表结束时间
     * @return 输入成功为true失败false
     */
    @Override
    public boolean InputStartEndTime(long start, long end) {
        return set.InputStartEndTime(start,end);
    }
    /**
     *  获取活动开始时间
     * @return 如果start存在则返回，否则返回-1
     */
    @Override
    public long GetStart() {
        return set.GetStart();
    }
    /**
     * 获取活动结束时间
     * @return 如果end存在则返回，否则返回-1
     */
    @Override
    public long GetEnd() {
        return set.GetEnd();
    }
    @Override
    public String toString(){
        return set.toString();
    }
}
