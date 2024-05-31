package IntervalSetTest;

import IntervalSet.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public  class IntervalSetTest {
    public IntervalSet<String> emptyInstance(){
        return new CommonIntervalSet<String>();
    }
    IntervalSet<String> set;

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
        set.insert(23456,34567,"王");
        set.insert(34567,45678,"张");
        String s = "label  时间段\n张     [34567, 45678]\n刘     [12345, 23456]\n王     [23456, 34567]\n";
        assertEquals(s,set.toString());
    }
    @Test(expected= IllegalArgumentException.class)
    public void insertsamelabelTest(){
        set.insert(12345,23456,"刘");
        set.insert(23456,34567,"王");
        set.insert(34567,45678,"刘");//抛出异常
    }
    @Test
    public void labelsTest(){
        set.insert(12345,23456,"刘");
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
        set.insert(23456,34567,"王");
        assertTrue(set.remove("王"));
        Set<String> res = new HashSet<>();
        res.add("刘");
        assertEquals(res,set.labels());
    }
    @Test
    public void startTest(){
        set.insert(12345,23456,"刘");
        set.insert(23456,34567,"王");
        assertEquals(12345,set.start("刘"));
        assertEquals(23456,set.start("王"));
        assertEquals(-1,set.start("li"));
    }
    @Test
    public void endTest(){
        set.insert(12345,23456,"刘");
        set.insert(23456,34567,"王");
        assertEquals(23456,set.end("刘"));
        assertEquals(34567,set.end("王"));
    }
    @Test
    public void toStringTest(){
        set.insert(12345,23456,"刘");
        set.insert(23456,34567,"王");
        String s = "label  时间段\n刘     [12345, 23456]\n王     [23456, 34567]\n";
        System.out.println(set.toString());
        //好好测试，
        assertEquals(s,set.toString());
    }
}
