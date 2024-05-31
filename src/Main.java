import IntervalSet.*;

public class Main {

    public static void main(String[] args) {
        //无空闲，无重叠
        IntervalSet<String> set = new NoBlankIntervalSet1<String>(new NonOverlapIntervalSet<String>(new CommonIntervalSet<String>()));
        set.insert(12345,23456,"刘");
        set.insert(23456,34567,"王");
        set.insert(33333,56666,"张");
        System.out.println(set.toString());
    }
}
