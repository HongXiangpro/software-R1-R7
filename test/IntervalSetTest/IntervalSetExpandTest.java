package IntervalSetTest;

import IntervalSet.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IntervalSetExpandTest {

    public IntervalSet<String> emptyInstance(){
        return new CommonIntervalSet<String>();
    }
    IntervalSet<String> set;

    @Before
    public void setToempty(){
        set = emptyInstance();
    }
    @Test
    public void constructorTest(){
        set.insert(12345,23456,"刘");
        set.insert(23456,34567,"王");
        IntervalSet<String>  N_set = new NonOverlapIntervalSet<String>(set);
        String s = "label  时间段\n刘     [12345, 23456]\n王     [23456, 34567]\n";
        assertEquals(s,N_set.toString());
        NoBlankIntervalSet<String> noBlankIntervalSet = new NoBlankIntervalSet<String>(set);
        assertEquals(s,noBlankIntervalSet.toString());
    }
    @Test
    public void NonOverlapTest(){
        set.insert(12345,23456,"刘");
        set.insert(23456,34567,"王");
        IntervalSet<String>  N_set = new NonOverlapIntervalSet<String>(set);
        try{
            N_set.insert(22222,33333,"张");//抛出异常
        }catch (IllegalArgumentException illegalArgumentException){

        }
    }
    @Test
    public void NonOverlapTest_1(){
        set.insert(12345,23456,"刘");
        set.insert(23456,34567,"王");
        set.insert(22222,33333,"张");
        try {
            IntervalSet<String>  N_set = new NonOverlapIntervalSet<String>(set);//抛出异常
        }catch (Exception e){
            assertEquals("有时间段重叠", e.getMessage());
        }

    }
    @Test
    public void NonBlankTest(){
        set.insert(12345,23456,"刘");
        set.insert(23456,34567,"王");
        NoBlankIntervalSet<String> noBlankIntervalSet = new NoBlankIntervalSet<String>(set);
        noBlankIntervalSet.InputStartEndTime(12345,66666);
        try {
            noBlankIntervalSet.IsInputOver(true);//抛出异常
        }catch (Exception e){
            assertEquals("含有空闲时间",e.getMessage());
        }

    }

}
