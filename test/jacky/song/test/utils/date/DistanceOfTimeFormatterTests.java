package jacky.song.test.utils.date;

import java.util.Date;

import jacky.song.test.BaseTest;
import jacky.song.android.util.Range;
import jacky.song.android.util.date.DistanceOfTimeFormatter;
import jacky.song.android.util.date.DistanceOfTimeFormatter.Unit;

public class DistanceOfTimeFormatterTests extends BaseTest{
  private DistanceOfTimeFormatter fmt;

  protected void before() throws Exception {
    fmt = new DistanceOfTimeFormatter();
    fmt.addRule(new Range(1, 10), Unit.MINUTE, "大约", "分钟之前", true);
    fmt.addRule(new Range(30, 40), Unit.MINUTE, "大约", "半小时之前", false);
    fmt.addRule(new Range(1, 3), Unit.HOUR, "", "小时之前", true);
    fmt.addRule(new Range(1, 4), Unit.WEEK, "", "周之前", true);
  }

  public void testFormat() throws Exception {
    String result = fmt.distanceOfTimeInWords(new Date(System.currentTimeMillis()-2*60*1000));
    System.out.println(result);
    
    result = fmt.distanceOfTimeInWords(new Date(System.currentTimeMillis()-35*60*1000));
    System.out.println(result);

    result = fmt.distanceOfTimeInWords(new Date(System.currentTimeMillis()-120*60*1000));
    System.out.println(result);

    result = fmt.distanceOfTimeInWords(new Date(System.currentTimeMillis()-1000*60*60*24* 15));
    System.out.println(result);

  }

}
