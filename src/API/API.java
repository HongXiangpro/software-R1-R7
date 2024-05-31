package API;

import IntervalSet.*;
import MultiIntervalSet.*;

import java.util.HashSet;
import java.util.Set;

public class API <L>{
    private final long oneDayInMillis = 24 * 60 * 60 * 1000; // 一天的毫秒数
    /**
     *  计算两个 MultiIntervalSet 对象的相似度；
     * @param multiIntervalSet1 第一个时间表集合
     * @param multiIntervalSet2 第二个时间表集合
     * @return 相似度大小
     */
    public double Similarity(MultiIntervalSet<L> multiIntervalSet1, MultiIntervalSet<L> multiIntervalSet2) {
        long startTime = Math.min(multiIntervalSet1.GetStart(), multiIntervalSet2.GetStart());
        long stopTime = Math.max(multiIntervalSet1.GetEnd(), multiIntervalSet2.GetEnd());
        double totalSimilarity = 0;
        long totalTime = stopTime - startTime;
        Set<L> set1 = multiIntervalSet1.labels();
        Set<L> set2 = multiIntervalSet2.labels();
        Set<L> similarSet1 = new HashSet<>();   //multiIntervalSet1中对应的标签
        Set<L> similarSet2 = new HashSet<>();   //multiIntervalSet2中对应的标签
        double intervalSimilarity;  //每个时间段对应的相似度
        for (long time = startTime; time < stopTime; time += oneDayInMillis) {
            similarSet1.clear();
            similarSet2.clear();
            intervalSimilarity = 0;
            //寻找该时间段内s1中对应的标签
            for (L label1 : set1) {
                IntervalSet<Integer> intervalSet1 = multiIntervalSet1.intervals(label1);
                for (Integer childLabel : intervalSet1.labels()) {
                    if (intervalSet1.start(childLabel)<=time && intervalSet1.end(childLabel)>=time+1) {
                        similarSet1.add(label1);
                    }
                }
            }
            //寻找该时间段内s2中对应的标签
            for (L label2 : set2) {
                IntervalSet<Integer> intervalSet2 = multiIntervalSet2.intervals(label2);
                for (Integer childLabel : intervalSet2.labels()) {
                    if (intervalSet2.start(childLabel)<=time && intervalSet2.end(childLabel)>=time+1) {
                        similarSet2.add(label2);
                    }
                }
            }
            for (L set : similarSet1) {
                if (similarSet2.contains(set)) {
                    intervalSimilarity = oneDayInMillis;
                }
            }
            totalSimilarity += intervalSimilarity;
        }
        return totalSimilarity * 1.0 / (stopTime - startTime);
    }
    /**
     * 发现一个IntervalSet中的时间冲突比
     * @param set IntervalSet对象
     * @return 时间冲突比
     */
    public double calcConflictRatio(IntervalSet<L> set) {
        long totalLength = set.GetEnd() - set.GetStart();
        long conflictedLength = 0;
        double answer = 0;
        //判断是否有冲突，大于1表示冲突
        int conflict;
        for (long time = set.GetStart(); time < set.GetEnd(); time+= oneDayInMillis) {
            conflict = 0;
            for (L label : set.labels()) {
                if (set.start(label)<=time && set.end(label)>=time+1) {
                    conflict++;
                }
            }
            if (conflict > 1) {
                conflictedLength+= oneDayInMillis;
            }
        }
        answer = (double) 1.0*conflictedLength/totalLength;
        return answer;
    }

    /**
     * 发现一个MultiIntervalSet中的时间冲突比
     * @param set MultiIntervalSet对象
     * @return 时间冲突比
     */
    public double calcConflictRatio(MultiIntervalSet<L> set) {
        long totalLength = set.GetEnd() - set.GetStart();
        long conflictedLength = 0;
        double answer = 0;
        int conflict;   //判断是否有冲突，大于1则表示冲突了
        for (long time = set.GetStart(); time < set.GetEnd(); time+= oneDayInMillis) {
            conflict = 0;
            for (L label : set.labels()) {
                IntervalSet<Integer> intervalSet = set.intervals(label);
                for (Integer childLabel : intervalSet.labels()) {
                    if (intervalSet.start(childLabel)<=time && intervalSet.end(childLabel)>=time+1) {
                        conflict++;
                    }
                }
            }
            if (conflict > 1) {
                conflictedLength+= oneDayInMillis;
            }
        }
        answer = (double) 1.0*conflictedLength/totalLength;
        return answer;
    }
    /**
     * 计算一个IntervalSet的空闲时间比；
     * @param set IntervalSet对象
     * @return 空闲时间比
     */
    public double calcFreeTimeRatio(IntervalSet<L> set) {
        // 总时间
        long totalTime = set.GetEnd() - set.GetStart();
        long totalFreeTime = totalTime;
        double answer = 0;
        //判断是否有空余时间，
        int freetime;
        for (long time = set.GetStart(); time < set.GetEnd(); time+= oneDayInMillis) {
            freetime = 0;
            for (L label : set.labels()) {
                if (set.start(label)<=time && set.end(label)>=time+1) {
                    freetime++;
                }
            }
            if (freetime > 0) {
                totalFreeTime-= oneDayInMillis;
            }
        }
        answer = 1.0*totalFreeTime/totalTime;
        return answer;
    }
    /**
     * 计算一个MultiIntervalSet的空闲时间比
     * @param set MultiIntervalSet对象
     * @return 空闲时间比
     */
    public double calcFreeTimeRatio(MultiIntervalSet<L> set) {
        // 总时间
        long totalTime = set.GetEnd() - set.GetStart();
        long totalFreeTime = totalTime;
        double answer = 0;
        //判断是否有空余时间
        int freetime;
        for (long time = set.GetStart(); time < set.GetEnd(); time+= oneDayInMillis) {
            freetime = 0;
            for (L label : set.labels()) {
                IntervalSet<Integer> intervalSet = set.intervals(label);
                for (Integer childLabel : intervalSet.labels()) {
                    if (intervalSet.start(childLabel)<=time && intervalSet.end(childLabel)>=time+1) {
                        freetime++;
                    }
                }
            }
            if (freetime > 0) {
                totalFreeTime-= oneDayInMillis;
            }
        }
        answer = 1.0*totalFreeTime/totalTime;
        return answer;
    }
}