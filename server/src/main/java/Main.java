import static spark.Spark.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class Main {

    public static void main(String[] args) {

        //This is required to allow GET and POST requests with the header 'content-type'
        options("/*",
                (request, response) -> {
                        response.header("Access-Control-Allow-Headers",
                                "content-type");

                        response.header("Access-Control-Allow-Methods",
                                "GET, POST");


                    return "OK";
                });

        HashMap<Integer, Double> lowestCost = loadDistributors();
        ArrayList<Candy> inventory = loadInventory();

        //This is required to allow the React app to communicate with this API
        before((request, response) -> response.header("Access-Control-Allow-Origin", "http://localhost:3000"));

        //TODO: Return JSON containing the candies for which the stock is less than 25% of it's capacity
        get("/low-stock", (request, response) -> {
            ArrayList<Candy> lowStock = new ArrayList<Candy>();
            for (Candy c : inventory) {
                if (c.isLowStock()) {
                    lowStock.add(c);
                } 
            }
            response.type("application/json");
            return new Gson().toJson(lowStock);
        });

        //TODO: Return JSON containing the total cost of restocking candy
        post("/restock-cost", (request, response) -> {
            Gson gson = new Gson();
            Candy[] lowItems = gson.fromJson(request.body(), Candy[].class);
            double tCost = 0;
            for (Candy c : lowItems){
                tCost += c.getOrderAmout() * lowestCost.get(c.getID());
            }
            tCost = Math.round(tCost*100.00)/100.00;
            return gson.toJson(tCost);
        });

    }

    private static HashMap<Integer, Double> loadDistributors() {
        HashMap<Integer, Double> lowestCost = new HashMap<Integer, Double>();
        try {
            File distributorFile = new File(Main.class.getResource("Distributors.xlsx").toURI());
            FileInputStream fis = new FileInputStream(distributorFile);
            XSSFWorkbook wb = new XSSFWorkbook(fis);

            for (Sheet sheet : wb ) {
                for (Row row : sheet) {
                    try {
                        if(row.getRowNum() == 0){
                            continue;
                        }
                        Integer id = (int) row.getCell(1).getNumericCellValue();
                        Double price = row.getCell(2).getNumericCellValue();

                        //adds new price to hashmap, or updates price if found price is lower
                        if (!lowestCost.containsKey(id)){
                            lowestCost.put(id, price);
                        }
                        if (lowestCost.containsKey(id)){
                            lowestCost.put(id,Math.min(lowestCost.get(id).doubleValue(),price.doubleValue()));
                        }
                        
                    } catch (Exception e) {
                        //reached end of current sheet.
                        //catches null value being converted to Integer or Double that sometimes happens from a blank cell
                        break;
                    }
                }
            }
            wb.close();
        } catch (Exception e) {
            System.out.println("File Error- Distributors");
            System.out.println(e);
        }
        return lowestCost;
    }

    private static ArrayList<Candy> loadInventory() {
        ArrayList<Candy> inventory = new ArrayList<Candy>();
        try {
            File inventoryFile = new File(Main.class.getResource("Inventory.xlsx").toURI());
            FileInputStream fis = new FileInputStream(inventoryFile);
            XSSFWorkbook wb = new XSSFWorkbook(fis);

            Sheet sheet = wb.getSheetAt(0);
            for(Row row : sheet) {
                if(row.getRowNum() == 0){
                    continue;
                }
                try {
                    String name = row.getCell(0).getStringCellValue();
                    int stock = (int) row.getCell(1).getNumericCellValue();
                    int capacity = (int) row.getCell(2).getNumericCellValue();
                    int ID = (int) row.getCell(3).getNumericCellValue();

                    Candy c = new Candy(ID, name, stock, capacity, 0);
                    //orderAmount is set to 0 as default, this is updated on frontend 
                    inventory.add(c);


                    
                } catch (Exception e) {
                    //reached end of current sheet.
                    //catches null value being converted to Integer or Double
                    break;
                }
            }
            wb.close();
        } catch (Exception e) {
            System.out.println("File Error- Inventory");
            System.out.println(e);
        }
        return inventory;
    }   
}




