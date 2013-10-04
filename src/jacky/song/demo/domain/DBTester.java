/*
 * Created on Oct 4, 2013
 *
 * TODO
 */
package jacky.song.demo.domain;

import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;


public class DBTester {
  
  public void saveCate() {
    Category restaurants = new Category();
    restaurants.name = "Restaurants";
    restaurants.save();
    
    ActiveAndroid.beginTransaction();
    try {
      for (int i = 0; i < 100; i++) {
        Item item = new Item("Example " + i, restaurants);
        item.save();
      }
      ActiveAndroid.setTransactionSuccessful();
    }
    finally {
      ActiveAndroid.endTransaction();
    }
  }
  
  public void deleteItems() {
    Item item = Item.load(Item.class, 1);
    item.delete();
    
    Item.delete(Item.class, 2);
    
    new Delete().from(Item.class).where("Id = ?", 3).execute();
  }
  
  public static Item getRandomItem() {
    return new Select().from(Item.class).orderBy("RANDOM()").executeSingle();
  }
  
  public static Item getRandomItem(Category category) {
    return new Select().from(Item.class).where("Category = ?", category.getId()).orderBy("RANDOM()").executeSingle();
  }
  
  public static List<Item> getAllItems(Category category) {
    return new Select().from(Item.class).where("Category = ?", category.getId()).orderBy("Name ASC").execute();
  }
}
