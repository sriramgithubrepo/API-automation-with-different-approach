package restAssuredRequestPojo;

public class Bookingdates {
    private String checkin;
    private String checkout;

    public Bookingdates(){}
    public Bookingdates(String checkin, String checkout) {
        //super();
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }
}
