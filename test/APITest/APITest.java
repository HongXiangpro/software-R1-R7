package APITest;
import API.*;
import application.DutyRoster.Employee;
import org.junit.Test;
import org.junit.Before;
import MultiIntervalSet.*;
import IntervalSet.*;
import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class APITest {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    MultiIntervalSet<Employee> multiIntervalSet1;
    MultiIntervalSet<Employee> multiIntervalSet2;
    IntervalSet<Employee> intervalSet;
    API<Employee> api;
    @Before
    public void set() throws ParseException {
        api = new API<Employee>();
        multiIntervalSet2 = new CommomMultilntervalSet<Employee>();
        multiIntervalSet1 = new CommomMultilntervalSet<Employee>();
        intervalSet = new CommonIntervalSet<Employee>();

        Employee employee1 = new Employee("Li","A","12312312311");
        Employee employee2 = new Employee("Liu","B","12312312322");
        Employee employee3 = new Employee("Wang","C","12312312333");
        multiIntervalSet1.InputStartEndTime(sdf.parse("2024-10-11").getTime(),endOfDay(sdf.parse("2024-10-20")));
        multiIntervalSet1.insert(sdf.parse("2024-10-11").getTime(),endOfDay(sdf.parse("2024-10-16")),employee1);
        multiIntervalSet1.insert(sdf.parse("2024-10-17").getTime(),endOfDay(sdf.parse("2024-10-18")),employee2);
        multiIntervalSet1.insert(sdf.parse("2024-10-19").getTime(),endOfDay(sdf.parse("2024-10-20")),employee3);

        multiIntervalSet2.InputStartEndTime(sdf.parse("2024-10-11").getTime(),endOfDay(sdf.parse("2024-10-20")));
        multiIntervalSet2.insert(sdf.parse("2024-10-11").getTime(),endOfDay(sdf.parse("2024-10-15")),employee1);
        multiIntervalSet2.insert(sdf.parse("2024-10-16").getTime(),endOfDay(sdf.parse("2024-10-18")),employee2);
        multiIntervalSet2.insert(sdf.parse("2024-10-19").getTime(),endOfDay(sdf.parse("2024-10-20")),employee3);

        intervalSet.InputStartEndTime(sdf.parse("2024-10-11").getTime(),endOfDay(sdf.parse("2024-10-20")));
        intervalSet.insert(sdf.parse("2024-10-11").getTime(),endOfDay(sdf.parse("2024-10-16")),employee1);
        intervalSet.insert(sdf.parse("2024-10-17").getTime(),endOfDay(sdf.parse("2024-10-18")),employee2);
        intervalSet.insert(sdf.parse("2024-10-19").getTime(),endOfDay(sdf.parse("2024-10-20")),employee3);
    }
    @Test
    public void SimilarityTest(){
        assertEquals(0.9,api.Similarity(multiIntervalSet1,multiIntervalSet2),0.01);//误差在0.01内
    }
    @Test
    public void calcConflictRatioTest() throws ParseException {
        Employee employee = new Employee("Kai","D","12312312344");
        intervalSet.insert(sdf.parse("2024-10-15").getTime(),endOfDay(sdf.parse("2024-10-18")),employee);
        assertEquals(0.4,api.calcConflictRatio(intervalSet),0.01);//误差在0.01内
    }
    @Test
    public void calcConflictRatioMultiTest() throws ParseException {
        Employee employee = new Employee("Kai","D","12312312344");
        multiIntervalSet1.insert(sdf.parse("2024-10-15").getTime(),endOfDay(sdf.parse("2024-10-18")),employee);
        assertEquals(0.4,api.calcConflictRatio(multiIntervalSet1),0.01);//误差在0.01内
    }
    @Test
    public void calcFreeTimeRatioTest() throws ParseException {
        intervalSet.InputStartEndTime(sdf.parse("2024-10-11").getTime(),endOfDay(sdf.parse("2024-10-30")));
        assertEquals(0.5,api.calcFreeTimeRatio(intervalSet),0.01);
    }
    @Test
    public void calcFreeTimeRatioMultiTest() throws ParseException {
        multiIntervalSet1.InputStartEndTime(sdf.parse("2024-10-11").getTime(),endOfDay(sdf.parse("2024-10-30")));
        assertEquals(0.5,api.calcFreeTimeRatio(multiIntervalSet1),0.01);
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
