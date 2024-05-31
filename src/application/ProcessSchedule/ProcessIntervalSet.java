package application.ProcessSchedule;

import IntervalSet.IntervalSet;
import MultiIntervalSet.*;
import java.util.Set;
public class ProcessIntervalSet <L> {
    MultiIntervalSet<L> multiIntervalSet = new NoOverlapMultiIntervalSet<L>(new CommomMultilntervalSet<L>());

    /**
     * 在当前对象中插入新的时间段和标签
     *
     * @param start：开始时间
     * @param end：结束时间
     * @param label：标签(不可重复,immutable)
     */
    public void insert(long start, long end, L label) {
        multiIntervalSet.insert(start, end, label);
    }

    /**
     * 获取这个对象中所有的标签
     *
     * @return 这个对象中标签的集合
     */
    public Set<L> labels() {
        return multiIntervalSet.labels();
    }

    /**
     * 获取对应标签的所有时间区间
     *
     * @param label 标签
     * @return 以标签时间区间的标号为标签的IntervalSet
     */
    public IntervalSet<Integer> intervals(L label) {
        return multiIntervalSet.intervals(label);
    }

    /**
     * 去除对象中的一个标签和时间段
     *
     * @param label：标签
     * @return 去除成功返回true，没有找到返回false
     */
    public boolean remove(L label) {
        return multiIntervalSet.remove(label);
    }

    /**
     * @param start 整个时间表开始时间
     * @param end   整个时间表结束时间
     * @return 输入成功为true失败false
     */
    public boolean InputStartEndTime(long start, long end) {
        return multiIntervalSet.InputStartEndTime(start, end);
    }

    /**
     * @return 如果start存在则返回，否则返回-1
     */
    public long GetStart() {
        return multiIntervalSet.GetStart();
    }

    /**
     * @return 如果end存在则返回，否则返回-1
     */
    public long GetEnd() {
        return multiIntervalSet.GetEnd();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("进程"+"                            时间段\n");
        for (L l : this.labels()) {
            IntervalSet<Integer> set = this.intervals(l);
            StringBuilder time = new StringBuilder();
            for (int i = 0;i<set.labels().size();i++){
                time.append("[").append(set.start(i)).append(", ").append(set.end(i)).append("] ");
            }
            s.append(l).append("  "+time+"\n");
        }
        return s.toString();
    }

}
