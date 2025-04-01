package StaticCounter;

public class CounterID {
    static private long x = 1;

    static public long SetId() {
        return x++;
    }

}
