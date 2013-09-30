package jacky.song.demo.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;

/**
 * User entity. Example entity. Class name corresponds to a database table; class attributes correspond to table fields.
 * 
 * @author Vladimir Kroz
 * 
 */
public class User extends ActiveRecordBase {
  
  private String firstName;
  
  private String lastName;
  
  private double balanse;
  
  private int rank;
  
  private boolean active;
  
  private Timestamp birthday;
  
  private int newInt;
  
  private transient List<UserItem> items;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    return sb.append(_id).append(" ").append(firstName).append(" ").append(lastName).append(" " + newInt).toString();
  }
  
  public User() {}
  
  public User(String firstName, String lastName, double balanse, int rank, boolean active, Timestamp birthday) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.balanse = balanse;
    this.rank = rank;
    this.active = active;
    this.birthday = birthday;
  }
  
  public User(long id, String firstName, String lastName, double balanse, int rank, boolean active, Timestamp birthday) {
    _id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.balanse = balanse;
    this.rank = rank;
    this.active = active;
    this.birthday = birthday;
  }

  public void addItem(UserItem item) {
    if (items == null) items = new ArrayList<UserItem>();
    item.setUserId(_id);
    items.add(item);
  }

  public void setItems(List<UserItem> items) {
    this.items = items;
  }
  
  public List<UserItem> getItems() {
    return this.items;
  }

  public String getFirstName() {
    return this.firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return this.lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public double getBalanse() {
    return this.balanse;
  }
  
  public void setBalanse(double balanse) {
    this.balanse = balanse;
  }
  
  public int getRank() {
    return this.rank;
  }
  
  public void setRank(int rank) {
    this.rank = rank;
  }
  
  public boolean isActive() {
    return this.active;
  }
  
  public void setActive(boolean active) {
    this.active = active;
  }
  
  public Timestamp getBirthday() {
    return this.birthday;
  }
  
  public void setBirthday(Timestamp birthday) {
    this.birthday = birthday;
  }

  public int getNewInt() {
    return this.newInt;
  }
  
  public void setNewInt(int newInt) {
    this.newInt = newInt;
  }

}
