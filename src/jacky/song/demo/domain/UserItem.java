package jacky.song.demo.domain;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;

/**
 * UserItems entity. Represents record stored in SQLite database
 * 
 * @author Vladimir Kroz
 * 
 */
public class UserItem extends ActiveRecordBase {
  
  private long userId;
  
  private String description;
  
  private String price;
  
  private Timestamp purchaseDate;
  
  private String status;
  
  private Timestamp created;
  
  private Timestamp modified;
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("UserItems [_id=").append(_id).append(", userId=").append(userId).append(", description=")
        .append(description).append(", price=").append(price).append(", purchaseDate=").append(purchaseDate)
        .append(", status=").append(status).append(", created=").append(created).append(", modified=").append(modified)
        .append("]");
    return builder.toString();
  }
  
  public void setUserId(long userId) {
    this.userId = userId;
  }
  
  public long getUserId() {
    return this.userId;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getPrice() {
    return this.price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public Timestamp getPurchaseDate() {
    return this.purchaseDate;
  }
  
  public void setPurchaseDate(Timestamp purchaseDate) {
    this.purchaseDate = purchaseDate;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public Timestamp getCreated() {
    return this.created;
  }
  
  public void setCreated(Timestamp created) {
    this.created = created;
  }
  
  public Timestamp getModified() {
    return this.modified;
  }
  
  public void setModified(Timestamp modified) {
    this.modified = modified;
  }

}
