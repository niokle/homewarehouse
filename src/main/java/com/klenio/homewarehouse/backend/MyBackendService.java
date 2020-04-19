package com.klenio.homewarehouse.backend;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyBackendService {
    private List<MyProduct> myProducts;

    private static String fileName = "products.txt";

    public List<MyProduct> getMyProducts(MyExternalData myExternalData) {
        myProducts = myExternalData.loadMyProducts(fileName).stream().sorted(Comparator.comparing(MyProduct::getDate)).collect(Collectors.toList());
        return myProducts.stream().filter(myProduct -> !myProduct.getStatus().equals("zjedzone")).collect(Collectors.toList());
    }

    public List<MyProduct> getMyFilteredProducts(MyExternalData myExternalData, String searchText) {
        myProducts = myExternalData.loadMyProducts(fileName).stream().sorted(Comparator.comparing(MyProduct::getDate)).collect(Collectors.toList());
        return myProducts.stream().filter(myProduct -> !myProduct.getStatus().equals("zjedzone"))
                                  .filter(myProduct -> myProduct.getName().toLowerCase().contains(searchText.toLowerCase())
                                          || myProduct.getPlace().toLowerCase().contains(searchText.toLowerCase())
                                          || myProduct.getStatus().toLowerCase().contains(searchText.toLowerCase()))
                                  .collect(Collectors.toList());
    }

    public void addMyProduct(MyProduct myProduct) {
        myProducts.add(myProduct);
    }

    public void setMyProducts(MyExternalData myExternalData) {
        myExternalData.saveMyProducts(myProducts, fileName);
    }
}
