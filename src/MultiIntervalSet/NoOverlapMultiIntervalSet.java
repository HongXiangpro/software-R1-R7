package MultiIntervalSet;
import IntervalSet.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoOverlapMultiIntervalSet <L> extends MultiIntervalSetDecorator <L>{
    boolean flag=false;
    private void checkRep() throws IllegalArgumentException{
        Set<L> labels = super.labels();
        List<L> labelsList = new ArrayList<>(labels);

        for (int i = 0; i < labelsList.size(); i++) {
            L label1 = labelsList.get(i);
            IntervalSet<Integer> intervalSet1 = super.intervals(label1);
            for (int j = i + 1; j < labelsList.size(); j++) { // 从当前元素的下一个元素开始遍历
                L label2 = labelsList.get(j);
                IntervalSet<Integer> intervalSet2 = super.intervals(label2);
                // 检查label1和label2对应的时间段是否重叠
                flag = checkOverlaps(intervalSet1,intervalSet2)||flag;
            }
        }
        if (flag){
            throw new  IllegalArgumentException("有时间段重叠");
        }
    }
    private boolean checkOverlaps(IntervalSet<Integer> set1,IntervalSet<Integer> set2) {
        // 遍历当前IntervalSet的所有时间段
        for (Integer label : set1.labels()) {
            long start = set1.start(label);
            long end = set1.end(label);
            // 遍历另一个IntervalSet的所有时间段
            for (Integer otherLabel : set2.labels()) {
                long otherStart = set2.start(otherLabel);
                long otherEnd = set2.end(otherLabel);
                // 检查时间重叠
                if (start <= otherEnd && end >= otherStart) {
                    // 如果有任何一对时间段重叠，则返回true
                    return true;
                }
            }
        }
        // 如果没有任何一对时间段重叠，则返回false
        return false;
    }
    public NoOverlapMultiIntervalSet(MultiIntervalSet<L> set) {
        super(set);
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
        super.insert(start,end,label);
        checkRep();
    }
    /**
     * 获取表中所有标签
     * @return 表中所有标签Set
     */
    @Override
    public Set<L> labels() {
        checkRep();
        return super.labels();
    }
    /**
     * 将该标签所有时间段存为一个以Integer为标签的时间表
     * @param label 标签
     * @return 标签对应所有时间段表
     */
    @Override
    public IntervalSet<Integer> intervals(L label) {
        checkRep();
        return super.intervals(label);
    }
    /**
     * 删除标签及其时间段
     * @param label：标签
     * @return 删除成功返回true，否则返回false
     */
    @Override
    public boolean remove(L label) {
        checkRep();
        return super.remove(label);
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
        return super.InputStartEndTime(start,end);
    }
    /**
     * 获取整个活动开始时间
     * @return 活动开始时间
     */
    @Override
    public long GetStart() {
        checkRep();
        return super.GetStart();
    }
    /**
     * 获取整个活动结束时间
     * @return 活动结束时间
     */
    @Override
    public long GetEnd() {
        checkRep();
        return super.GetEnd();
    }
    @Override
    public String toString(){
        checkRep();
        return super.toString();
    }
}
