class FoodItem {
    private String productCode;
    private String name;
    private int stockAmount;
  
    public FoodItem(String productCode, String name) {
      this.productCode = productCode;
      this.name = name;
      this.stockAmount = 0;
    }
  
    public String getProductCode() {
      return productCode;
    }
  
    public String getName() {
      return name;
    }
  
    public int getStockAmount() {
      return stockAmount;
    }
  
    public void stockIn(int quantity) {
      stockAmount += quantity;
    }
  
    public void sell(int quantity) {
      if (stockAmount >= quantity) {
        stockAmount -= quantity;
      }
    }
  
    public double calculatePrice(int quantity) {
      return quantity * getPricePerUnit();
    }
  
    public double getPricePerUnit() {
      return 0;
    }
  }
  
  class GrainItem extends FoodItem {
    private String coarseness;
  
    public GrainItem(String productCode, String name, String coarseness) {
      super(productCode, name);
      this.coarseness = coarseness;
    }
  
    public String getCoarseness() {
      return coarseness;
    }
  
    @Override
    public double getPricePerUnit() {
      return 5.0;
    }
  }
  
  class EggItem extends FoodItem {
    private String type;
    private String size;
  
    public EggItem(String productCode, String name, String type, String size) {
      super(productCode, name);
      this.type = type;
      this.size = size;
    }
  
    public String getType() {
      return type;
    }
  
    public String getSize() {
      return size;
    }
  
    @Override
    public double getPricePerUnit() {
      return 0.5;
    }
  }
  
  public class Exam1 {
    public static void main(String[] args) {
      GrainItem rice = new GrainItem("G01", "Rice", "Thick");
      GrainItem wheat = new GrainItem("G02", "Wheat", "Thin");
      EggItem henEgg = new EggItem("E01", "Hen Egg", "Hen", "Small");
      EggItem duckEgg = new EggItem("E02", "Duck Egg", "Duck", "Large");
      rice.stockIn(500);
      rice.sell(400);
      int total_amount = rice.getStockAmount();
      double total_price = rice.calculatePrice(200);
      System.out.println("Total Stock Amount of rice: " + total_amount + "Total price of 200: " + total_price);
  
      wheat.stockIn(300);
      wheat.sell(40);
      System.out.println("Total Stock Amount of wheat: " + wheat.getStockAmount() + "Per Unit price: " + wheat.getPricePerUnit());
  
      henEgg.stockIn(600);
      henEgg.sell(20);
      System.out.println("Total Stock Amount of henEgg: " + henEgg.getStockAmount() + "Per Unit price: " + henEgg.getPricePerUnit());
  
      duckEgg.stockIn(600);
      duckEgg.sell(20);
      System.out.println("Total Stock Amount of henEgg: " + duckEgg.getStockAmount() + "Per Unit price: " + duckEgg.getPricePerUnit());
    }
  }
