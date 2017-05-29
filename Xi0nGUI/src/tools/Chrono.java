package tools;

public class Chrono {

    private long startTime=0;
    private long finalTime=0;
    private long startWait=0;
    private long finalWait=0;
    private long duration=0;

    public void start()
        {
        startTime=System.currentTimeMillis();
        finalTime=0;
        startWait=0;
        finalWait=0;
        duration=0;
        }

    public void pause()
        {
        if(startTime==0) {return;}
        startWait=System.currentTimeMillis();
        }

    public void resume()
        {
        if(startTime==0) {return;}
        if(startWait==0) {return;}
        finalWait=System.currentTimeMillis();
        startTime=startTime+finalWait-startWait;
        finalTime=0;
        startWait=0;
        finalWait=0;
        duration=0;
        }
        
    public void stop()
        {
        if(startTime==0) {return;}
        finalTime=System.currentTimeMillis();
        duration=(finalTime-startTime) - (finalWait-startWait);
        startTime=0;
        finalTime=0;
        startWait=0;
        finalWait=0;
        }        

    public long getDurationSec()
        {
        return duration/1000;
        }
        
    public long getDurationMs()
        {
        return duration;
        }        

    public String getDurationTxt()
        {
        return timeToHMS(getDurationSec());
        }

    public static String timeToHMS(long tempsS) {

        // IN : (long) temps en secondes)

        int h = (int) (tempsS / 3600);
        int m = (int) ((tempsS % 3600) / 60);
        int s = (int) (tempsS % 60);

        String r="";

        if(h>0) {r+=h+" h ";}
        if(m>0) {r+=m+" min ";}
        if(s>0) {r+=s+" s";}
        if(h<=0 && m<=0 && s<=0) {r="0 s";}

        return r;
        }

    }