package com.klenio.homewarehouse.backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyExternalData {
    public List<MyProduct> loadMyProducts() {
        List<MyProduct> myProducts = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("products.txt"))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray myProductsList = (JSONArray) obj;
            myProductsList.forEach( myProduct -> myProducts.add(parseMyProductObject((JSONObject) myProduct )));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myProducts;
    }

    private MyProduct parseMyProductObject(JSONObject myProduct)
    {
        JSONObject myProductObject = (JSONObject) myProduct.get("myProduct");

        Long id = (Long) myProductObject.get("id");
        String date = (String) myProductObject.get("date");
        String name = (String) myProductObject.get("name");
        String place = (String) myProductObject.get("place");
        String status = (String) myProductObject.get("status");
        return new MyProduct(id, date, name, place, status);
    }

    public void saveMyProducts(List<MyProduct> myProducts)
    {
        JSONObject myProductJSON = new JSONObject();
        JSONObject myProductDetailsJSON = new JSONObject();
        JSONArray myProductsJSONArray = new JSONArray();
        for (MyProduct myProduct : myProducts) {
            myProductDetailsJSON.clear();
            myProductDetailsJSON.put("id", myProduct.getId());
            myProductDetailsJSON.put("date", myProduct.getDate());
            myProductDetailsJSON.put("name", myProduct.getName());
            myProductDetailsJSON.put("place", myProduct.getPlace());
            myProductDetailsJSON.put("status", myProduct.getStatus());

            myProductJSON.clear();
            myProductJSON.put("myProduct", myProductDetailsJSON);

            myProductsJSONArray.add(myProductJSON);
        }

        try (FileWriter file = new FileWriter("products.txt")) {
            file.write(myProductsJSONArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
