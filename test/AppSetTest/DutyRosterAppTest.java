package AppSetTest;
import application.DutyRoster.DutyIntervalSet;
import org.junit.Test;
import org.junit.Before;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
public class DutyRosterAppTest {
    private static final DutyIntervalSet<String> dutyIntervalSet = new DutyIntervalSet<>();

    @Before
    public void set(){
        insertByDate("2024-03-12","2024-03-20","刘");
        insertByDate("2024-03-21","2024-04-01","王");
        insertByDate("2024-04-02","2024-04-10","赵");
    }

    @Test
    public void overlapTest(){
        try {
            insertByDate("2024-03-28","2024-04-07","李");
        }catch (Exception e){
            assertEquals("有时间段重叠",e.getMessage());
        }
    }
    @Test
    public void toStringTest(){
        String string = "日期\t\t值班人名字\t职位\t\t手机号码\n" +
                "2024-03-12\t\t刘\n" +
                "2024-03-13\t\t刘\n" +
                "2024-03-14\t\t刘\n" +
                "2024-03-15\t\t刘\n" +
                "2024-03-16\t\t刘\n" +
                "2024-03-17\t\t刘\n" +
                "2024-03-18\t\t刘\n" +
                "2024-03-19\t\t刘\n" +
                "2024-03-20\t\t刘\n" +
                "2024-03-21\t\t王\n" +
                "2024-03-22\t\t王\n" +
                "2024-03-23\t\t王\n" +
                "2024-03-24\t\t王\n" +
                "2024-03-25\t\t王\n" +
                "2024-03-26\t\t王\n" +
                "2024-03-27\t\t王\n" +
                "2024-03-28\t\t王\n" +
                "2024-03-29\t\t王\n" +
                "2024-03-30\t\t王\n" +
                "2024-03-31\t\t王\n" +
                "2024-04-01\t\t王\n" +
                "2024-04-02\t\t赵\n" +
                "2024-04-03\t\t赵\n" +
                "2024-04-04\t\t赵\n" +
                "2024-04-05\t\t赵\n" +
                "2024-04-06\t\t赵\n" +
                "2024-04-07\t\t赵\n" +
                "2024-04-08\t\t赵\n" +
                "2024-04-09\t\t赵\n" +
                "2024-04-10\t\t赵\n";
        assertEquals(string,dutyIntervalSet.toString());

    }
    private static void insertByDate(String inputStartDate,String inputEndDate,String name){
        SimpleDateFormat sdfStart = new SimpleDateFormat("yyyy-MM-dd");
        Date dateStart = null;
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        Date dateEnd = null;
        try {
            dateStart = sdfStart.parse(inputStartDate);
            dateEnd = sdfEnd.parse(inputEndDate);
        } catch (ParseException e) {
            System.out.println("输入的日期格式不正确，请确保格式为yyyy-MM-dd");
        }
        long timestampStart = 0,timestampEnd = 0;
        if(dateStart != null){
            timestampStart = dateStart.getTime();
        }
        if(dateEnd != null){
            timestampEnd=  endOfDay(dateEnd);
        }
        dutyIntervalSet.insert(timestampStart,timestampEnd,name);
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
}
