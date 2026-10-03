
public class CartItem {
    Medicine medicine;
    int quantity;

    public CartItem(Medicine medicine, int quantity) {
        this.medicine = medicine;
        this.quantity = quantity;
    }

    public double getTotal() {
        return quantity * medicine.price;
    }
}
