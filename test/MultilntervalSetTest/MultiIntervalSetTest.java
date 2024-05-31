package MultilntervalSetTest;

import IntervalSet.*;
import MultiIntervalSet.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class MultiIntervalSetTest{
    public  CommomMultilntervalSet<String> emptyInstance(){
        return new CommomMultilntervalSet<String>();
    }
    CommomMultilntervalSet<String> set;

    @Before
    public void setToempty(){
        set = emptyInstance();
    }

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    @Test
    public void insertTest(){
        set.insert(12345,23456,"刘");
        set.insert(0,12345,"刘");
        set.insert(23456,34567,"王");
        set.insert(34567,45678,"张");
        IntervalSet<Integer> set1 = new CommonIntervalSet<Integer>();
        set1.insert(12345,23456,0);
        set1.insert(0,12345,1);
        assertEquals(set1.labels(),set.intervals("刘").labels());
        assertEquals(set1.start(0),set.intervals("刘").start(0));
        assertEquals(set1.start(1),set.intervals("刘").start(1));
        assertEquals(set1.end(1),set.intervals("刘").end(1));
        assertEquals(set1.end(0),set.intervals("刘").end(0));
    }
    @Test
    public void labelsTest(){
        set.insert(12345,23456,"刘");
        set.insert(0,12345,"刘");
        set.insert(23456,34567,"王");
        Set<String> res = new HashSet<>();
        res.add("刘");
        res.add("王");
        assertEquals(res,set.labels());
        set.insert(0,123456,"强");
        res.add("强");
        assertEquals(res,set.labels());
    }
    @Test
    public void removeTest(){
        set.insert(12345,23456,"刘");
        set.insert(0,12345,"刘");
        set.insert(23456,34567,"王");
        assertTrue(set.remove("刘"));
        Set<String> res = new HashSet<>();
        res.add("王");
        assertEquals(res,set.labels());
    }
    @Test
    public void intervalsTest(){
        set.insert(12345,23456,"刘");
        set.insert(0,12345,"刘");
        set.insert(23456,34567,"王");
        set.insert(34567,45678,"张");
        IntervalSet<Integer> res_liu = new CommonIntervalSet<Integer>();
        res_liu.insert(12345,23456,0);
        res_liu.insert(0,12345,1);
        assertEquals(res_liu.labels(),set.intervals("刘").labels());
        assertEquals(res_liu.start(0),set.intervals("刘").start(0));
        assertEquals(res_liu.start(1),set.intervals("刘").start(1));
        assertEquals(res_liu.end(0),set.intervals("刘").end(0));
        assertEquals(res_liu.start(0),set.intervals("刘").start(0));
    }
    @Test
    public void toStringTest(){
        set.insert(12345,23456,"刘");
        set.insert(0,12345,"刘");
        set.insert(23456,34567,"王");
        set.insert(34567,45678,"张");
        String s = "label"+"  时间段\n" +
                "张  [34567, 45678] \n" +
                "刘  [12345, 23456] [0, 12345] \n" +
                "王  [23456, 34567] \n";;
        System.out.println(set.toString());
        assertEquals(s,set.toString());
    }


}
