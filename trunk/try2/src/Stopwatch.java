
public class Stopwatch {
	private long elapsedTime = 0;
    private long startTime = 0;

    public void start() {
       startTime = System.currentTimeMillis();
    }
    public void stop() {
    	if(startTime != 0)
    		elapsedTime += (System.currentTimeMillis()- startTime);
    	startTime = 0;
    }
   
    public long getElapsedTime() {
    	return elapsedTime;
    }

    public void reset() {
       startTime = 0;
       elapsedTime = 0;
    }
}