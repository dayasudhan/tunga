package khaanavali.vendor;



/**
 * Created by gagan on 10/31/2015.
 */
public class HotelMenu {

    private String availability;
    private String price;
    private String name;

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    private String timings;
    // private String image;
    private String id;
    public HotelMenu() {
        // TODO Auto-generated constructor stub
    }
    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }
}
