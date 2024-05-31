package application.ProcessSchedule;

import IntervalSet.IntervalSet;
import java.util.*;

public class ProcessScheduleApp {
    private static Map<Process, Boolean> processes = new HashMap<>();
    private static ProcessIntervalSet<Process> processProcessIntervalSet = new ProcessIntervalSet<>();

    private static long makeRandom(long now, Process process){
        long nextTime = process.getMaxRunningTime();
        long nextTimeMIn = process.getMinRunningTime();
        IntervalSet<Integer> intervalSet = processProcessIntervalSet.intervals(process);
        for (Integer i : intervalSet.labels()){
            nextTime -= intervalSet.end(i) - intervalSet.start(i);
            nextTimeMIn -= intervalSet.end(i) - intervalSet.start(i);
        }
        Random random = new Random();
        long start = now + Math.abs(random.nextLong(100000));
        long end = start +Math.abs(random.nextLong(nextTime)) ;
        if (end - start >= nextTimeMIn){
            processes.put(process,true);
        }
        processProcessIntervalSet.insert(start,end,process);
        return end;
    }
    private static boolean isAllVisited(){
        boolean flag=true;
        for (Process process : processes.keySet()){
            flag = flag&&processes.get(process);
        }
        return flag;
    }

    private static Process findMinTime(){
        long min = 9223372036854775807L;
        Process p = new Process();
        for (Process process : processes.keySet()){
            long time = process.getMaxRunningTime();
            IntervalSet<Integer> intervalSet = processProcessIntervalSet.intervals(process);
            for (Integer i : intervalSet.labels()){
                time -= intervalSet.end(i) - intervalSet.start(i);
            }
            if (time<min&&!processes.get(process)){
                min = time;
                p = process;
            }
        }
        return p;
    }

    public static void main(String[] args) {
        int choice = 1;
        Scanner scanner = new Scanner(System.in);
        while (choice != 0) {
            System.out.println("请选择需要的操作：\n0：退出程序；\n1:输入进程\n2:随机安排进程并执行\n3:最短进程优先安排进程并执行\n4:删除进程\n5:显示进程执行情况\n6:显示输入的进程");
            choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            switch (choice) {
                case 0: {
                    break;
                }
                case 1: {
                    while (true) {
                        System.out.println("请输入进程ID:");
                        String id = scanner.nextLine();
                        System.out.println("请输入进程Name:");
                        String name = scanner.nextLine();
                        System.out.println("请输入进程MinRunningTime:");
                        long minrunningtime = scanner.nextLong();
                        scanner.nextLine();
                        System.out.println("请输入进程MaxRunningTime:");
                        long maxrunningtime = scanner.nextLong();
                        scanner.nextLine();
                        Process process = new Process(id, name, minrunningtime, maxrunningtime);
                        processes.put(process, false);
                        System.out.println("是否继续输入下一进程(y/n):");
                        String isNext = scanner.nextLine();
                        if (isNext.equalsIgnoreCase("n")) {
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    long now = 0L;
                    while (!isAllVisited()) {
                        for (Process process : processes.keySet()) {
                            if (!processes.get(process)) {
                                now = makeRandom(now, process);
                            }
                        }
                    }
                    break;
                }
                case 3: {
                    long now = 0L;
                    while (!isAllVisited()) {
                        now = makeRandom(now, findMinTime()); // 优先对最早结束进程进行编排
                    }
                    break;
                }
                case 4: {
                    boolean found = false;
                    System.out.println("请输入你要删除进程的ID：");
                    String id = scanner.nextLine();
                    for (Process process : processProcessIntervalSet.labels()) {
                        if (id.equals(process.getID())) {
                            System.out.println("该进程已经被按执行.");
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                    for (Process process : processes.keySet()) {
                        if (id.equals(process.getID())) {
                            processes.remove(process);
                            System.out.println("该进程已删除.");
                            break;
                        }
                    }
                    break;
                }
                case 5: {
                    if (!ProcessVisualizer.isLaunched()) {
                        ProcessVisualizer.setProcessIntervalSet(processProcessIntervalSet);
                        new Thread(() -> ProcessVisualizer.launch(ProcessVisualizer.class)).start();
                    } else {
                        ProcessVisualizer.refresh();
                    }
                    break;
                }
                case 6: {
                    System.out.println(processes.keySet());
                    break;
                }
            }
        }
    }
}
