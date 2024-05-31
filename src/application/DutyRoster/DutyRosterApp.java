package application.DutyRoster;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DutyRosterApp {
    private static final DutyIntervalSet<Employee> set = new DutyIntervalSet<Employee>();
    private static final List<Employee> allEmployees = new ArrayList<>();
    private static final Pattern EMPLOYEE_PATTERN = Pattern.compile("([A-Za-z]+)\\{(\\w+(?:\\s+\\w+)*),([0-9\\-]+)}");
    private static final Pattern PERIOD_PATTERN = Pattern.compile("Period\\{(\\d{4}-\\d{2}-\\d{2}),(\\d{4}-\\d{2}-\\d{2})}");
    private static final Pattern ROSTER_ENTRY_PATTERN = Pattern.compile("(\\w+)\\{(\\d{4}-\\d{2}-\\d{2}),(\\d{4}-\\d{2}-\\d{2})}");
    private static final long oneDayInMillis = 24 * 60 * 60 * 1000; // 一天的毫秒数

    private static void parseFile(String filePath) {
        List<Employee> employees = new ArrayList<>();
        String periodStart = null;
        String periodEnd = null;
        Map<String, String[]> roster = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean inRoster = false;
            boolean inEmployee = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("//")) {
                    continue; // 忽略空行和注释行
                }

                if (line.startsWith("Employee")) {
                    // 开始解析Employee部分
                    inEmployee = true;
                }
                else if(inEmployee&&!line.startsWith("}")){
                    parseEmployees(line, employees);
                }
                else if(inEmployee&&line.startsWith("}")){
                    inEmployee = false;
                }
                else if (line.startsWith("Period")) {
                    // 解析Period
                    Matcher matcher = PERIOD_PATTERN.matcher(line);
                    if (matcher.find()) {
                        periodStart = matcher.group(1);
                        periodEnd = matcher.group(2);
                    }
                }
                else if (line.startsWith("Roster")) {
                    // 开始解析Roster部分
                    inRoster = true;
                }
                else if (inRoster) {
                    // 解析Roster中的条目
                    parseRosterEntry(line, roster);
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long starTime = sdf.parse(periodStart).getTime();
            long endTime = endOfDay(sdf.parse(periodEnd));
            set.InputStartEndTime(starTime,endTime);
            //插入到表中
            for (Map.Entry<String, String[]> entry : roster.entrySet()) {
                for(Employee employee : employees){
                    if(entry.getKey().equals(employee.getName())){
                        allEmployees.add(employee);//插入员工
                        long start = sdf.parse(entry.getValue()[0]).getTime();
                        long end = endOfDay(sdf.parse(entry.getValue()[1]));
                        set.insert(start,end,employee);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("输入的日期格式不正确，请确保格式为yyyy-MM-dd");
        }
    }

    private static void parseEmployees(String line, List<Employee> employees) {
        line = line.trim();
        Matcher matcher = EMPLOYEE_PATTERN.matcher(line);
        if (matcher.find()) {
            String name = matcher.group(1);
            String jobtitle = matcher.group(2);
            String phonenumer = matcher.group(3).replace("-","");
            employees.add(new Employee(name,jobtitle,phonenumer));
        }
        }


    private static void parseRosterEntry(String line, Map<String, String[]> roster) {
        Matcher matcher = ROSTER_ENTRY_PATTERN.matcher(line);
        if (matcher.find()) {
            String name = matcher.group(1);
            String startDate = matcher.group(2);
            String endDate = matcher.group(3);
            roster.put(name, new String[]{startDate, endDate});
        }
    }

    private static long endOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Set the time to the end of the day
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        // Get the time in milliseconds
        return calendar.getTimeInMillis();
    }
    private static void RandomlyArrange(){
        if(!set.labels().isEmpty()){
            throw new IllegalArgumentException("有员工已经安排了工作时间");
        }
        int emptyNum =  allEmployees.size();
        List<long[]> TimeSet = splitIntervalRandomly(set.GetStart(),set.GetEnd(),emptyNum);
        for (int i =0 ;i<allEmployees.size();i++){
            set.insert(TimeSet.get(i)[0],TimeSet.get(i)[1],allEmployees.get(i));
        }
    }

    private static List<long[]> splitIntervalRandomly(long start, long end, int n) {
        if (n <= 0 || start >= end) {
            throw new IllegalArgumentException("无效参数：n 必须大于 0，start 必须小于 end。");
        }

        List<long[]> intervals = new ArrayList<>();
        Random random = new Random();

        // 计算总长度
        long totalLength = end - start + 1;

        // 每个区间至少占1个单位长度，所以先给每个区间分配1
        long remainingLength = totalLength - n;

        // 如果没有剩余长度，则每个区间都是等长的
        if (remainingLength <= 0) {
            for (int i = 0; i < n; i++) {
                long endOfInterval = start + (i * (totalLength / n));
                intervals.add(new long[]{start + (i * (totalLength / n)), endOfInterval - 1});
            }
            // 处理不能整除的情况
            if (totalLength % n != 0) {
                intervals.get(n - 1)[1] = end;
            }
            return intervals;
        }

        // 随机分配剩余长度到n-1个区间
        long currentStart = start;
        for (int i = 0; i < n - 1; i++) {
            // 分配长度（包括1和剩余的部分）
            long allocatedLength = 1 + random.nextLong( Math.min(remainingLength, end - currentStart));
            // 更新剩余长度
            remainingLength -= allocatedLength - 1;
            // 更新当前区间的结束位置
            long currentEnd = currentStart + allocatedLength - 1;
            intervals.add(new long[]{currentStart, currentEnd});
            // 更新下一个区间的起始位置
            currentStart = currentEnd + 1;
        }

        // 添加最后一个区间（它总是从上一个区间的结束位置开始，到end结束）
        intervals.add(new long[]{currentStart, end});

        return intervals;
    }

    public static void main(String[] args) throws ParseException {
        int choice = 1;

        Scanner scanner = new Scanner(System.in);
        while (choice!=0){
            System.out.println("请选择需要的操作：\n0：退出程序；\n1：从文件读取员工安排信息；\n2：插入员工信息；\n3：删除员工信息；\n4：删除排班表中的员工信息；\n5:输入开始结束时间;\n6:打印员工排班表;\n7:打印目前输入的员工;\n8:自动编排值班表\n9:对员工安排是否结束(进行对空白的判断)");
            choice = scanner.nextInt();
            scanner.nextLine();//消耗换行符
            try {
                switch (choice){
                    case 0:{
                        break;
                    }
                    case 1:{
                        parseFile("src/application/DutyRoster/Employee安排信息");
                        break;
                    }
                    case 2:{
                        System.out.println("请输入员工姓名:");
                        String inputName = scanner.nextLine();
                        System.out.println("请输入员工职务:");
                        String inputJob = scanner.nextLine();
                        System.out.println("请输入员工电话:");
                        String inputPhoneNumber = scanner.nextLine();
                        Employee employee = new Employee(inputName,inputJob,inputPhoneNumber);

                        System.out.println("是否现在对其进行安排(y/n)：");
                        String IsArrange = scanner.nextLine();
                        if(IsArrange.equals("y")||IsArrange.equals("Y")){
                            System.out.println("请输入开始日期（格式为yyyy-MM-dd）:");
                            String inputStartDate = scanner.nextLine();
                            SimpleDateFormat sdfStart = new SimpleDateFormat("yyyy-MM-dd");
                            Date dateStart = null;
                            System.out.println("请输入结束日期（格式为yyyy-MM-dd）:");
                            String inputEndDate = scanner.nextLine();
                            SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
                            Date dateEnd = null;

                            try {
                                dateStart = sdfStart.parse(inputStartDate);
                                dateEnd = sdfEnd.parse(inputEndDate);
                            } catch (ParseException e) {
                                System.out.println("输入的日期格式不正确，请确保格式为yyyy-MM-dd");
                                break;
                            }
                            long timestampStart = 0,timestampEnd = 0;
                            if(dateStart != null){
                                timestampStart = dateStart.getTime();
                            }
                            if(dateEnd != null){
                                timestampEnd=  endOfDay(dateEnd);
                            }
                            set.insert(timestampStart,timestampEnd,employee);
                        }

                        allEmployees.add(employee);

                        break;
                    }
                    case 3:{
                        boolean found = false,isArrange = false;
                        System.out.println("请输入要删除员工的姓名:");
                        String inputName = scanner.nextLine();
                        for (Employee e : set.labels()){
                            if (e.getName().equals(inputName)) {
                                isArrange = true;
                                break;
                            }
                        }
                        if(isArrange){
                            System.out.println("员工正在排表中");
                            break;
                        }
                        for (int i =0 ;i<allEmployees.size();i++){
                            if(allEmployees.get(i).getName().equals(inputName)){
                                allEmployees.remove(i);
                                found = true;
                            }
                        }
                        if(!found){
                            System.out.println("没有找到该员工");
                        }
                        break;
                    }
                    case 4:{
                        boolean found = false;
                        System.out.println("请输入要删除员工的姓名:");
                        String inputName = scanner.nextLine();
                        for (Employee e : set.labels()){
                            if(e.getName().equals(inputName)){
                                set.remove(e);
                                found = true;
                            }
                        }
                        if(!found){
                            System.out.println("没有在值班表中找到员工");
                        }
                        break;
                    }
                    case 6:{
                        System.out.println(set);
                        break;
                    }
                    case 5:{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        System.out.println("请输入开始日期（格式为yyyy-MM-dd）:");
                        long start = 0L,end = 0L;
                        try {
                            start = sdf.parse(scanner.nextLine()).getTime();
                        } catch (ParseException e) {
                            System.out.println("输入的日期格式不正确，请确保格式为yyyy-MM-dd");
                        }
                        System.out.println("请输入结束日期（格式为yyyy-MM-dd）:");
                        try {
                            end = endOfDay(sdf.parse(scanner.nextLine()));
                        } catch (ParseException e) {
                            System.out.println("输入的日期格式不正确，请确保格式为yyyy-MM-dd");
                        }
                        set.InputStartEndTime(start,end);
                        break;
                    }
                    case 7:{
                        System.out.println("姓名    职务    电话号码");
                        for (Employee employee :allEmployees){
                            System.out.println(employee.toString());
                        }
                        break;
                    }
                    case 8:{
                        RandomlyArrange();
                        break;
                    }
                    case 9:{
                        System.out.println("是否对员工安排完成(y/n：");
                        String IsOver = scanner.nextLine();
                        set.IsInputOver(IsOver.equals("y") || IsOver.equals(("Y")));
                        break;
                    }
                }
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }
}
