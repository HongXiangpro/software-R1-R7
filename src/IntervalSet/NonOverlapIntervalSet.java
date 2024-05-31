package IntervalSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NonOverlapIntervalSet<L> extends IntervalSetDecorator<L>{
    // Abstraction function:
    // 构建一个时间表set，一个标签只能有一个时间段保存时间表，没有相重叠的时间，可实现插入、获取全部标签，去除标签、获取某个标签的开始和截至时间等方法
    // Representation invariant:
    // 时间表中标签非空，时间end大于start,所有时间没有重叠
    // Safety from rep exposure:
    // 其余操作委托给CommomIntervalSet利用checkRep()来确保没有重叠。

    //检测是否有重叠
    private void checkRep() throws IllegalArgumentException{
        Set<L> labels = super.labels();
        List<L> labelsList = new ArrayList<>(labels);

        for (int i = 0; i < labelsList.size(); i++) {
            L label1 = labelsList.get(i);
            long start1 = super.start(label1);
            long end1 = super.end(label1);

            for (int j = i + 1; j < labelsList.size(); j++) { // 从当前元素的下一个元素开始遍历
                L label2 = labelsList.get(j);
                long start2 = super.start(label2);
                long end2 = super.end(label2);

                // 检查label1和label2对应的时间段是否重叠
                if (start1 < end2 && start2 < end1) {
                    throw new IllegalArgumentException("有时间段重叠");
                }
            }
        }
    }
    //依靠传入表创建无重叠表
    public NonOverlapIntervalSet(IntervalSet<L> s){
        super(s);
        checkRep();
    }
    @Override
    public void insert(long start, long end, L label) throws IllegalArgumentException{
        super.insert(start,end,label);
        checkRep();
    }
    @Override
    public Set<L> labels(){
        Set<L> set = super.labels();
        checkRep();
        return set;
    }
    @Override
    public boolean remove(L label){
        boolean f;
        f = super.remove(label);
        checkRep();
        return f;
    }
    @Override
    public long start(L label){
        long start = super.start(label);
        checkRep();
        return start;
    }
    @Override
    public long end(L label){
        long end = super.end(label);
        checkRep();
        return end;
    }
    @Override
    public String toString(){
        String string = super.toString();
        checkRep();
        return string;
    }
}
