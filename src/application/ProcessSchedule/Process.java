package application.ProcessSchedule;

public class Process {
    private final String ID;
    private final String Name;
    private final long MinRunningTime;
    private final long MaxRunningTime;
    public Process(){
        this("","",0L,0L);
    }
    public Process(String id ,String name ,long minRunningTime,long maxRunningTime){
        ID = id;
        Name = name;
        MinRunningTime = minRunningTime;
        MaxRunningTime = maxRunningTime;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public long getMinRunningTime() {
        return MinRunningTime;
    }

    public long getMaxRunningTime() {
        return MaxRunningTime;
    }
    @Override
    public String toString(){
        return "ID:" + ID + "  " + "Name:" + Name + "RunTime:" + "[" + MinRunningTime + "~" + MaxRunningTime + "]";
    }
}
