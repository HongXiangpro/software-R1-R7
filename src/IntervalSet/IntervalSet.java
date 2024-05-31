package IntervalSet;

import java.util.Set;

/**
 *一个时间表记录标签L所在的时间段
 * @param <L> L需为不可变类型
 */
public interface IntervalSet<L> {
    /**
     * 创建一个空的set
     * L必须为不可变的数据类型
     * @return 一个空的时间表
     */
    public static <L> IntervalSet<L> empty(){
        return new CommonIntervalSet<L>();
    }

    /**
     * 在当前对象中插入新的时间段和标签
     * @param start：开始时间
     * @param end：结束时间
     * @param label：标签(不可重复,immutable)
     */
    public void insert(long start,long end,L label);

    /**
     *获取这个对象中所有的标签
     * @return 这个对象中标签的集合
     */
    public Set<L> labels();

    /**
     * 去除对象中的一个标签和时间段
     * @param label：标签
     * @return 去除成功返回true，没有找到返回false
     */
    public boolean remove(L label);

    /**
     * 获取对应标签的开始时间
     * @param label:标签
     * @return 对象中对应标签的开始时间
     */
    public long start(L label);

    /**
     * 获取对应标签的结束时间
     * @param label：标签
     * @return 对象中对应标签的结束时间
     */
    public long end(L label);

    /**
     * 输入整个活动开始结束时间
     * @param start 整个时间表开始时间
     * @param end 整个时间表结束时间
     * @return 输入成功为true失败false
     */
    public boolean InputStartEndTime(long start,long end);

    /**
     *  获取活动开始时间
     * @return 如果start存在则返回，否则返回-1
     */
    public long GetStart();

    /**
     * 获取活动结束时间
     * @return 如果end存在则返回，否则返回-1
     */
    public long GetEnd();
}