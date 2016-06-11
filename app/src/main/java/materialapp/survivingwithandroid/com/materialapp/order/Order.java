package materialapp.survivingwithandroid.com.materialapp.order;

import java.util.ArrayList;

/**
 * Created by dganeshappa on 11/10/2015.
 */

public class Order implements Comparable {
    Customer customer;
    String current_status;
    String id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    String _id;
    ArrayList<HotelMenuItem> hotelMenuItems;
    ArrayList<Tracker> trackerDetail;
    int bill_value;

    public Order()
    {
        this.bill_value =0;
        this.deliveryCharge = 0;
        this.totalCost = 0;
    }
    public int getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(int deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public int getBill_value() {
        return bill_value;
    }

    public void setBill_value(int bill_value) {
        this.bill_value = bill_value;
    }

    public int getTotalCost() {
        return this.totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    int deliveryCharge;
    int totalCost;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public ArrayList<HotelMenuItem> getHotelMenuItems() {
        return hotelMenuItems;
    }

    public void setHotelMenuItems(ArrayList<HotelMenuItem> hotelMenuItems) {
        this.hotelMenuItems = hotelMenuItems;
    }



    public ArrayList<Tracker> getTrackerDetail() {
        return trackerDetail;
    }

    public void setTrackerDetail(ArrayList<Tracker> trackerDetail) {
        this.trackerDetail = trackerDetail;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCurrent_status()
    {
        return current_status;
    }
    public void setCurrent_status(String current_status)
    {
        this.current_status = current_status;
    }

    @Override
    public int compareTo(Object another) {
        Order anotherOrder = (Order)another;
        return anotherOrder.getId().compareTo(this.id);
    }
}
