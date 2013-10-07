package jacky.song.android.util.date;

import jacky.song.android.util.Range;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DistanceOfTimeFormatter {
  private static final long SECONDS_PER_MINUTE = 60;
  private static final long SECONDS_PER_HOUR   = 3600;
  private static final long SECONDS_PER_DAY    = 86400;
  private static final long SECONDS_PER_MONTH  = 2592000;
  private static final long SECONDS_PER_YEAR   = 31536000;

  public enum Unit{
    MINUTE{
      @Override
      protected long minutes() {
        return SECONDS_PER_MINUTE / 60;
      }      
    },
    
    HOUR{
      @Override
      protected long minutes() {
        return SECONDS_PER_HOUR / 60;
      }      
    },
    
    DAY{
      @Override
      protected long minutes() {
        return SECONDS_PER_DAY / 60;
      }      

    },
    
    WEEK{
      @Override
      protected long minutes() {
        return (SECONDS_PER_DAY / 60) * 7;
      }      

    },
    
    MONTH{
      @Override
      protected long minutes() {
        return SECONDS_PER_MONTH / 60;
      }      

    },
    
    YEAR{
      @Override
      protected long minutes() {
        return SECONDS_PER_YEAR / 60;
      }      

    }
    ;
    
    protected abstract long minutes();
  }
  
  
  // [Range String -> Unit] 
  private Map<String, Unit> rangeUnitMap;
  // [Range String -> Format]
  private Map<String, String> rangeFormatStyleMap;
  
  public DistanceOfTimeFormatter() {
    rangeUnitMap = new HashMap<String, DistanceOfTimeFormatter.Unit>(5);
    rangeFormatStyleMap = new HashMap<String, String>(5);
  }
  
  public void addRule(Range range, Unit unit, String prefix, String suffix, boolean displayNumber) {
    // transform to minute-based range
    Range minutesRange = new Range();
    minutesRange.from = range.from * unit.minutes();
    minutesRange.to = range.to * unit.minutes();
    
    // add rule
    String rangeString = minutesRange.toString();
    rangeUnitMap.put(rangeString, unit);
    
    StringBuilder sb = new StringBuilder();
    sb.append(prefix);
    sb.append(displayNumber ? "%d" : "");
    sb.append(suffix);
    rangeFormatStyleMap.put(rangeString, sb.toString());
  }
  
  /**
   * @return return null if NO RULE matches the time distance
   */
  public String distanceOfTimeInWords(Date time) {
    long now = DateUtils.secondsFrom1970(new Date());
    long since = now - DateUtils.secondsFrom1970(time);
    
    int minutes   = (int)Math.round(since / SECONDS_PER_MINUTE);
    int hours     = (int)Math.round(since / SECONDS_PER_HOUR);
    int days      = (int)Math.round(since / SECONDS_PER_DAY);
    int months    = (int)Math.round(since / SECONDS_PER_MONTH);
    int years     = (int)Math.floor(since / SECONDS_PER_YEAR);

    int number = -1;
    String fmtStyle = null;
    for (String strRange : rangeUnitMap.keySet()) {
      Range timeRange = Range.toRange(strRange);
      if ((timeRange.from <= minutes) && (minutes <= timeRange.to)) {
        Unit unit = rangeUnitMap.get(strRange);
        fmtStyle = rangeFormatStyleMap.get(strRange);
        
        switch (unit) {
          case MINUTE:{
            number = minutes;
          }
            break;
          case HOUR:{
            number = hours;
          }
            break;
          case DAY:{
            number = days;
          }
            break;
          case WEEK:{
            number = days/7;
          }
            break;
          case MONTH:{
            number = months;
          }
            break;
          case YEAR:{
            number = years;
          }
            break;
            
          default:
            break;
        }
      }
    }
    
    if (number != -1) {
      return String.format(fmtStyle, number);
    }

    return null;
  }
  
}
