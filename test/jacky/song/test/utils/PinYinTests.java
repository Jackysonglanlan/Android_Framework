package jacky.song.test.utils;

import jacky.song.test.BaseTest;
import jacky.song.android.util.PinYin4j;

public class PinYinTests extends BaseTest{

  public void testPinYin() throws Exception {
    String str = "单田芳";
    System.out.println(PinYin4j.getPinyin(str));
  }
  
}
