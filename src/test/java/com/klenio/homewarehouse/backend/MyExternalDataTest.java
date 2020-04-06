package com.klenio.homewarehouse.backend;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyExternalDataTest {
    private MyExternalData myExternalData = new MyExternalData();

    @Test
    public void saveAndLoadMyProducts() {
        //given
        String fileName = "saveTEST.txt";
        MyProduct myProduct1 = new MyProduct(1L, "20200101", "szynka", "półka", "aaa");
        MyProduct myProduct2 = new MyProduct(2L, "20200202", "ser", "lodówka", "bbb");
        MyProduct myProduct3 = new MyProduct(3L, "20200303", "czekolada", "lodówka", "ccc");
        List<MyProduct> myProducts = new ArrayList<>();
        myProducts.add(myProduct1);
        myProducts.add(myProduct2);
        myProducts.add(myProduct3);

        //when
        myExternalData.saveMyProducts(myProducts, fileName);
        List<MyProduct> result = myExternalData.loadMyProducts(fileName);
        String myProduct1Date = result.get(0).getDate();
        String myProduct2Name = result.get(1).getName();
        String myProduct3Place = result.get(2).getPlace();

        //then
        Assert.assertEquals("20200101", myProduct1Date);
        Assert.assertEquals("ser", myProduct2Name);
        Assert.assertEquals("lodówka", myProduct3Place);

        //cleanup
        File file = new File(fileName);
        file.delete();
    }
}