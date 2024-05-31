package MultiIntervalSet;

import IntervalSet.IntervalSet;

import java.util.*;

/**
 * 允许时间段集合中的部分或全部标签对应的时间段出现周期性的装饰器；
 * @param <L>
 */
public class PeriodicMultiIntervalSet<L> extends MultiIntervalSetDecorator<L> {
    //List存放多个小List,每个小List包括start、end、是否有周期性(0\1)、deadline、
    private final Map<L, List<List<Long>>> PeriodicMap = new HashMap<>();

    // Abstraction Function:
    // 对于任意一个PeriodicMultiIntervalSet对象，其对应的抽象表示为multiIntervalSet；
    // 具体的抽象函数还是对应multiIntervalSet的；

    // Representation Invariant:
    // multiIntervalSet对象以及isPeriodic和Periodic对象不能为空不能为空；
    // isPeriodic对象的键集合与Periodic对象的键集合必须一一对应；
    // 对应与multiIntervalSet对象的表示不变性；

    // Safety from Rep Exposure:
    // isPeriodic和Periodic对象，外部无法直接访问；
    // 其余对外暴露的方法也继承于父类BasicMultiIntervalSet都进行了防御性拷贝，防止外部修改内部状态；

    /**
     * 初始化构造方法，将基类MultiIntervalSet中已存在的时间段集合赋上周期属性；
     * @param MultiIntervalSet 输入多重时间表
     */
    public PeriodicMultiIntervalSet(MultiIntervalSet<L> MultiIntervalSet) {
        super(MultiIntervalSet);
        Set<L> labels = this.labels();
        for (L label : labels) {
            IntervalSet<Integer> intervalSet = this.intervals(label);
            for (Integer childLabel : intervalSet.labels()) {
                List<Long> list = new ArrayList<>();
                list.add(intervalSet.start(childLabel));
                list.add(intervalSet.end(childLabel));
                list.add((long)0);
                list.add((long)0);
                List<List<Long>> lists = new ArrayList<>();
                lists.add(list);
                PeriodicMap.put(label, lists);
            }
        }
        checkRep();
    }

    /**
     * 重写的insert方法，为了直接赋上周期的属性；
     * @param start 该时间段的开始时间
     * @param end 该时间段的结束时间
     * @param label 标签名
     */
    @Override
    public void insert(long start, long end, L label) {
        boolean isInsert = false;
        super.insert(start, end, label);
        for (L nowlabel : this.labels()) {  //判断一下是否成功插入
            if (nowlabel.equals(label)) {
                IntervalSet<Integer> intervalSet = this.intervals(nowlabel);
                for (Integer childlabel : intervalSet.labels()) {
                    if (intervalSet.start(childlabel)==start && intervalSet.end(childlabel)==end) {
                        isInsert = true;
                    }
                }
            }
        }
        if (isInsert) {
            List<Long> list = new ArrayList<>();
            list.add(start);
            list.add(end);
            list.add((long)0);
            list.add((long)0);
            List<List<Long>> lists;
            if (!PeriodicMap.containsKey(label)) {
                lists = new ArrayList<>();
            } else {
                lists = PeriodicMap.get(label);
            }
            lists.add(list);
            PeriodicMap.put(label, lists);
        }
    }

    /**
     * 重载insert方法，可以在插入时间段的同时赋上周期；
     * @param start 该时间段开始时间
     * @param end 该时间段结束时间
     * @param label 标签
     * @param periodic 该时间段的周期(大于0)
     * @param deadline 该时间段周期截止时间
     */
    public void insert(long start, long end, L label, long periodic, long deadline) {
        boolean isInsert = false;
        super.insert(start, end, label);
        for (L nowlabel : this.labels()) {
            if (nowlabel.equals(label)) {
                IntervalSet<Integer> intervalSet = this.intervals(nowlabel);
                for (Integer childlabel : intervalSet.labels()) {
                    if (intervalSet.start(childlabel)==start && intervalSet.end(childlabel)==end) {
                        isInsert = true;
                    }
                }
            }
        }
        if (isInsert) {
            List<Long> list = new ArrayList<>();
            list.add(start);
            list.add(end);
            list.add(periodic);
            list.add(deadline);
            List<List<Long>> lists;
            if (!PeriodicMap.containsKey(label)) {
                lists = new ArrayList<>();
            } else {
                lists = PeriodicMap.get(label);
            }
            lists.add(list);
            PeriodicMap.put(label, lists);
            if (periodic != 0) {
                IsHasPeriodic(start, end, label, periodic, deadline);
            }
        }
    }

    /**
     * 对已经存在的时间段进行加上周期的操作，若输入不存在的时间段则是会先插入再赋上周期；
     * @param start 该时间段开始时间
     * @param end 该时间段结束时间
     * @param label 标签
     * @param periodic 该时间段的周期(大于0)
     * @param deadline 该时间段周期截止时间
     */
    public void SetPeriodic(long start, long end, L label, long periodic, long deadline) {
        boolean Find = false;
        try {
            for (L l : this.labels()) {
                if (l.equals(label)) {
                    IntervalSet<Integer> intervalSet = this.intervals(l);
                    for (Integer i : intervalSet.labels()) {
                        if (intervalSet.start(i)==start && intervalSet.end(i)==end) {
                            List<List<Long>> lists = PeriodicMap.get(label);

                            for (List<Long> list : lists) {
                                if (list.get(0) == start && list.get(1) == end) {
                                    list.set(2, periodic);
                                    list.set(3, deadline);
                                }
                            }


                            Find = true;
                            if (periodic != 0) {
                                IsHasPeriodic(start, end, label, periodic, deadline);
                            }
                        }
                    }
                }
            }
            if (!Find) {
                this.insert(start, end, label, periodic, deadline);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("这个时间段无周期性");
        }
    }


    /**
     * 判断这段时间是否有周期性
     * @param start 该时间段开始时间
     * @param end 该时间段结束时间
     * @param label 标签
     * @return 有周期性返回true否则返回false、未找到返回false
     */
    public boolean checkPeriodicity(long start, long end, L label) {
        boolean Find = false;
        for (L nowlabel : this.labels()) {
            if (nowlabel.equals(label)) {
                IntervalSet<Integer> intervalSet = this.intervals(nowlabel);
                for (Integer childLabel : intervalSet.labels()) {
                    if (intervalSet.start(childLabel)==start && intervalSet.end(childLabel)==end) {
                        Find = true;
                    }
                }
            }
        }
        if (Find) {
            List<List<Long>> lists = PeriodicMap.get(label);
            for (List<Long> list : lists) {
                if (list.get(0) == start && list.get(1) == end) {
                    return list.get(2) != 0;
                }
            }
        } else {
            throw new IllegalArgumentException("你输入查询的该标签所对应的时间段不存在！");
        }
        return false;
    }

    private  void IsHasPeriodic(long start, long end, L label, long periodicTime, long deadline) {
        long j = end+periodicTime;
        long i = start+periodicTime;

        if (j <= this.GetEnd()&&j <= deadline) {
            this.insert(i, j, label, periodicTime, deadline);
        }
    }


    private void checkRep() {
    }

    public String toString() {
        checkRep();
        return super.toString();
    }
}
