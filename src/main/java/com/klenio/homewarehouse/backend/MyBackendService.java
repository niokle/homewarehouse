package com.klenio.homewarehouse.backend;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyBackendService {
    private List<MyProduct> myProducts;

    /*
    {
    // Init dummy data

        Long id = 42L;

        myProducts = new ArrayList<>();
        myProducts.add(new MyProduct(id++, "20201011", "Mielonka", "Lodówka", "new"));
        myProducts.add(new MyProduct(id++, "20201011","Alvinia", "Delong", "new"));
        myProducts.add(new MyProduct(id++, "20201011","Leodora", "Burry",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Karen", "Oaten",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Mariele", "Huke",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Grata", "Widdowes",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Donna", "Roadknight",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Tommi", "Nowland",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Tonya", "Teresia",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Steffen", "Yon",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Consalve", "Willes",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Jeanelle", "Lambertz",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Odelia", "Loker",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Briano", "Shawell",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Tarrance", "Mainston",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Torrence", "Gehring",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Augie", "Pionter",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Marillin", "Aveson",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Jacquelyn", "Moreby",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Glenn", "Bangley",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Isidoro", "Glave",  "new"));
        myProducts.add(new MyProduct(id++, "20201011","Cchaddie", "Spatarul",  "new"));
    }

     */

    private static String fileName = "products.txt";

    public List<MyProduct> getMyProducts(MyExternalData myExternalData) {
        myProducts = myExternalData.loadMyProducts(fileName).stream().sorted(Comparator.comparing(MyProduct::getDate)).collect(Collectors.toList());
        return myProducts.stream().filter(myProduct -> !myProduct.getStatus().equals("zjedzone")).collect(Collectors.toList());
    }

    public void addMyProduct(MyProduct myProduct) {
        myProducts.add(myProduct);
    }

    public void setMyProducts(MyExternalData myExternalData) {
        myExternalData.saveMyProducts(myProducts, fileName);
    }
}
