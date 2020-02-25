package sr.server;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import spaceraze.util.general.Logger;
import sr.webb.mail.MailHandler;

/**
 * @author WMPABOD
 *
 * This threaded class controls when a server should update when a 
 * certain time has passed.
 * Different values of time means different update schemes:
 * 1 -> 1/week (monday 03:00)
 * 2 -> 2/week (monday and thursdag 03:00)
 * 3 -> 3/week (monday, wednesday and friday 03:00)
 * 5 -> 5/week (monday, tuesday. wednesday, thursday and friday 03:00)
 * 7 -> 7/week (all days in week 03:00)
 */
public class UpdateRunner extends Thread{
    private long time;
    private SR_Server s;
//    private long nrUpdatesToday = 1;
//    private String nextupdate;

    public UpdateRunner(long time, SR_Server s){
        this.time = time;
        this.s = s;
    }

    public void run(){
		Logger.info("Updater running...");
        boolean keepRunning = true;
        while(keepRunning){
            try{
                sleep(getMilliSeconds());
            }catch(InterruptedException ie){
            }
            catch(IllegalArgumentException iae){
        		Logger.severe("IllegalArgumentException catched: " + getMilliSeconds());
            }
    		Logger.finer("Updating at: " + (new Date()).toString());
            if (!s.hasAutoUpdated()){
            	if (!s.getLastUpdateComplete()){ // error in game, do not update
            		Logger.finer("UpdateRunner: not updating due to error in game: " + s.getGameName() + ". Date/Time: " + (new Date()).toString());
            	}else{
            		try {
            			if (!s.getGalaxy().gameEnded){
            				s.updateGalaxy(false);
            				MailHandler.sendNewTurnMessage(s);
            			}else{
            				// game is over
                			Logger.fine("Game " + s.getGameName() + " has ended and is not updated, and keepRunning is set to false.");
                			if (s.tooOld()){
                    			keepRunning = false;
                    			// remove game
                    			s.removeGame();
                			}
            			}
            		} catch (Exception e) {
            			Logger.fine("Exception while updating/mailing: " + e.toString());
            			e.printStackTrace();
            		}
            	}
            }else{
            	s.setHasAutoUpdated(false);
            	GalaxySaver gs = new GalaxySaver();
                gs.saveGalaxy(s.getGameName(),"saves",s.getGalaxy());
                gs.saveGalaxy(s.getGameName() + "_" + s.getTurn(),"saves/previous",s.getGalaxy());
            }
        }
    }

    @SuppressWarnings("deprecation")
	private long getMilliSeconds(){
    	Logger.finer("New getMilliSeconds called, time: " + time);
    	long timeToNextUpdate = -1;
    	// calculate time to next update
        Calendar nowcal = Calendar.getInstance();
        Calendar nextcal = (Calendar)nowcal.clone();
        nextcal.set(Calendar.HOUR_OF_DAY,3);
        nextcal.set(Calendar.MINUTE,0);
        nextcal.set(Calendar.SECOND,0);
        nextcal.add(Calendar.DATE,1);
        int dayOfWeek = nextcal.get(Calendar.DAY_OF_WEEK);
    	Logger.finer("day of week: " + dayOfWeek);
    	Logger.finest("Calendar.getTime: " + nextcal.getTime().toGMTString());
    	while (!isUpdateDay(dayOfWeek)){
            nextcal.add(Calendar.DATE,1);
            dayOfWeek = nextcal.get(Calendar.DAY_OF_WEEK);
        	Logger.finest("day of week (in loop): " + dayOfWeek);
        	Logger.finest("Calendar.getTime (in loop): " + nextcal.getTime().toGMTString());
    	}
    	// set next update string
//        nextupdate = nextcal.getTime().toString() + "\n";
//        LoggingHandler.finer("Next update: " + nextupdate);
        // calculate time to next update
        timeToNextUpdate = nextcal.getTime().getTime() - nowcal.getTime().getTime();
        if (timeToNextUpdate < 0){
        	timeToNextUpdate = 1000000; // to prevent very fast updates... if errors...
        }
    	return timeToNextUpdate;
    }

    public String getNextUpdate(){
    	String nextUpdate = null;
    	Calendar nextcal = getNextUpdateCal();
    	SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, HH:mm",Locale.US);
    	// set next update string
        nextUpdate = sdf.format(nextcal.getTime());
        return nextUpdate;
    }

    public Calendar getNextUpdateCalendar(){
        return getNextUpdateCal();
    }

    public String getNextUpdateShort(){
    	String nextUpdate = null;
    	Calendar nextcal = getNextUpdateCal();
    	SimpleDateFormat sdf = new SimpleDateFormat("EEE HH:mm",Locale.US);
    	// set next update string
        nextUpdate = sdf.format(nextcal.getTime());
        return nextUpdate;
    }

    @SuppressWarnings("deprecation")
	private Calendar getNextUpdateCal(){
    	Logger.finest("New dynamic getNextUpdate() called");
    	Logger.finest("getNextUpdate(): " + time + " " + s.hasAutoUpdated());
//    	long timeToNextUpdate = -1;
    	// calculate time to next update
        Calendar nowcal = Calendar.getInstance();
        Calendar nextcal = (Calendar)nowcal.clone();
        nextcal.set(Calendar.HOUR_OF_DAY,3);
        nextcal.set(Calendar.MINUTE,0);
        nextcal.set(Calendar.SECOND,0);
        nextcal.add(Calendar.DATE,1);
        int dayOfWeek = nextcal.get(Calendar.DAY_OF_WEEK);
    	Logger.finest("Day of week: " + dayOfWeek);
    	Logger.finest("Calendar.getTime: " + nextcal.getTime().toGMTString());
    	boolean hasAutoUpdated = s.hasAutoUpdated();
    	boolean updateFound = false;
		Logger.finest("before loop: " + updateFound);
    	while (!updateFound){
    		Logger.finest("in loop: " + hasAutoUpdated);
    		if (isUpdateDay(dayOfWeek)){
    			if (hasAutoUpdated){
    				hasAutoUpdated = false;
    				nextcal.add(Calendar.DATE,1);
                    dayOfWeek = nextcal.get(Calendar.DAY_OF_WEEK);
                	Logger.finest("hasAutoUpdated set to " + hasAutoUpdated);
                	Logger.finest("day of week (in loop): " + dayOfWeek);
                	Logger.finest("calendar.getTime (in loop): " + nextcal.getTime().toGMTString());
    			}else{
    				updateFound = true;
    			}
    		}else{
				nextcal.add(Calendar.DATE,1);
                dayOfWeek = nextcal.get(Calendar.DAY_OF_WEEK);
            	Logger.finest("Day of week (in loop): " + dayOfWeek);
            	Logger.finest("Calendar.getTime (in loop): " + nextcal.getTime().toGMTString());
    		}
    	}
        return nextcal;
    }

    private boolean isUpdateDay(int dayOfWeek){
    	boolean updateDay = false; 
    	if (dayOfWeek == Calendar.MONDAY){
    		updateDay = true;
    	}else
        if (dayOfWeek == Calendar.TUESDAY){
        	if ((time == 5) | (time == 7)){
        		updateDay = true;
        	}
        }else
        if (dayOfWeek == Calendar.WEDNESDAY){
        	if ((time == 3) | (time == 5) | (time == 7)){
        		updateDay = true;
        	}
        }else
        if (dayOfWeek == Calendar.THURSDAY){
        	if ((time == 2) | (time == 5) | (time == 7)){
        		updateDay = true;
        	}
        }else
        if (dayOfWeek == Calendar.FRIDAY){
        	if ((time == 3) | (time == 5) | (time == 7)){
        		updateDay = true;
        	}
        }else
        if (dayOfWeek == Calendar.SATURDAY){
        	if (time == 7){
        		updateDay = true;
        	}
        }else
        if (dayOfWeek == Calendar.SUNDAY){
        	if (time == 7){
        		updateDay = true;
        	}
        }
    	return updateDay;
    }
    
    public static String getUpdateDescription(int aTime){
    	return getDescription(aTime);
    }

    public String getUpdateDescription(){
    	return getDescription(time);
    }

    private static String getDescription(long aTime){
    	String desc = "Game has no update timer...";
    	String hourStr = "03:00";
    	switch ((int)aTime) {
		case 1:
			desc = "Server will update " + hourStr + " every Monday.";
			break;
		case 2:
			desc = "Server will update " + hourStr + " every Monday and Thursday.";
			break;
		case 3:
			desc = "Server will update " + hourStr + " every Monday, Wednesday and Friday.";
			break;
		case 5:
			desc = "Server will update " + hourStr + " every Monday through Friday.";
			break;
		case 7:
			desc = "Server will update " + hourStr + " every day in the week.";
			break;
		default:
			desc = "Never";
			break;
		}
    	return desc;
    }

    public static String getShortDescription(long aTime){
    	String desc = null;
    	if ((aTime >= 1) & (aTime <= 7)){
    		desc = aTime + "/week";
    	}else
    	if (aTime == 0){
			desc = "Never";
		}
    	return desc;
    }

/*
    private long getMilliSeconds(){
        // 8641 ms = 8.6 sec.
        // 8640 updates/24h -> 10 sec.
        // 86400000 millis / day
        Date updateDate;
        if (time > 8640){
            updateDate = new Date((new Date().getTime()) + time);
            nextupdate = updateDate.toString() + "\n";
            System.out.println("Next update: " + nextupdate);
            return time;
        }else{
            if (time == nrUpdatesToday){
                nrUpdatesToday = 1;
                Calendar nowcal = Calendar.getInstance();
                Calendar nextcal = (Calendar)nowcal.clone();
                nextcal.roll(Calendar.DATE,true);
                nextcal.set(Calendar.HOUR_OF_DAY,0);
                nextcal.set(Calendar.MINUTE,0);
                nextcal.set(Calendar.SECOND,0);
                nextupdate = nextcal.getTime().toString() + "\n";
                System.out.println("Next update: " + nextupdate);
                return nextcal.getTime().getTime() - nowcal.getTime().getTime();
            }else{
                nrUpdatesToday++;
                long cycles = 86400000/time;
                updateDate = new Date((new Date().getTime()) + cycles);
                nextupdate = updateDate.toString() + "\n";
                System.out.println("Next update: " + nextupdate);
                return cycles;
            }
        }
    }
*/
}