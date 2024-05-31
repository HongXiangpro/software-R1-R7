package MultilntervalSetTest;
import MultiIntervalSet.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiIntervalSetExpandTest {
    MultiIntervalSet<String> multiIntervalSet;
    @Before
    public void setMultiIntervalSet(){
        multiIntervalSet = new CommomMultilntervalSet<String>();
    }
    @Test
    public void NoOverlapMultiIntervalSetTest(){
        multiIntervalSet =new NoOverlapMultiIntervalSet<String>(multiIntervalSet);
        multiIntervalSet.insert(12346,23456,"刘");
        multiIntervalSet.insert(0,12345,"刘");
        multiIntervalSet.insert(23457,34567,"王");
        try{
            multiIntervalSet.insert(11111,22222,"李");
        }catch (Exception e){
            assertEquals("有时间段重叠",e.getMessage());
        }
    }
    @Test
    public void PeriodicMultiIntervalSet(){
        PeriodicMultiIntervalSet<String> periodicMultiIntervalSet = new PeriodicMultiIntervalSet<String>(multiIntervalSet);
        periodicMultiIntervalSet.insert(0,12345,"刘",1,12345*8);
        assertTrue(periodicMultiIntervalSet.checkPeriodicity(0, 12345, "刘"));
        periodicMultiIntervalSet.insert(123456,234567,"刘");
        periodicMultiIntervalSet.SetPeriodic(123456,234567,"刘",1,12345678);
        assertTrue(periodicMultiIntervalSet.checkPeriodicity(123456,234567,"刘"));
    }
}
