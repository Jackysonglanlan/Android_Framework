package jacky.song.android.util;

public class Range {

  public long from;
  public long to;
  
  public Range() {
  }
  
  public Range(long from, long to) {
    this.from = from;
    this.to = to;
  }
  
  /**
   * @param rangeString format: (from,to)
   * @return null if can't convert
   */
  public static Range toRange(String rangeString){
    rangeString = rangeString.replaceAll("\\s", "");
    
    if(!rangeString.matches("\\(\\d+,\\d+\\)"))
      return null;
        
    // remove '(' ')'
    rangeString = rangeString.replaceAll("[\\(\\)]", "");
    
    String[] arr = rangeString.split(",");
    
    Range r = new Range();
    
    r.from = Integer.parseInt(arr[0]);
    r.to = Integer.parseInt(arr[1]);

    return r;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    sb.append(from);
    sb.append(",");
    sb.append(to);
    sb.append(")");
    return sb.toString();
  }
}
