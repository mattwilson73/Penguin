public class Candy {
    private int ID;
    private String name;
    private int stock;
    private int capacity;
    private int orderAmount;

    public Candy(int ID, String name, int stock, int capacity, int orderAmount){
        this.ID = ID;
        this.name = name;
        this.stock = stock;
        this.capacity = capacity;
        this.orderAmount = 0;
    }

    public int getID(){
        return this.ID;
    }
    public String getName(){
        return this.name;
    }
    public int getStock(){
        return this.stock;
    }
    public int getCapacity(){
        return this.capacity;
    }
    public int getOrderAmout(){
        return this.orderAmount;
    }

    public void setOrderAmout(int orderAmount){
        this.orderAmount = orderAmount;
    }

    public Boolean isLowStock(){
 
        if ((double)this.stock / (double)this.capacity < 0.25){
            return true;
        }
        else{
            return false;
        }
    }
    
}