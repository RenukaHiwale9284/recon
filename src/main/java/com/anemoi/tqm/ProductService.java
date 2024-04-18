package com.anemoi.tqm;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


@Component("product_service")
@ManagedBean
@ApplicationScoped
public class ProductService {

    private List<TaxPosition> products;

    @PostConstruct
    public void init() {
        products = new ArrayList<>();
        products.add(new TaxPosition(1000, "f230fh0g3", "Bamboo Watch", "Not Required", "bamboo-watch.jpg", 65,
                "Accessories", 24, InventoryStatus.INSTOCK, 5));
        products.add(new TaxPosition(1001, "nvklal433", "Black Watch", "Not Required", "black-watch.jpg", 72,
                "Accessories", 61, InventoryStatus.INSTOCK, 4));
        products.add(new TaxPosition(1002, "zz21cz3c1", "Blue Band", "Not Required", "blue-band.jpg", 79,
                "Fitness", 2, InventoryStatus.LOWSTOCK, 3));
        products.add(new TaxPosition(1003, "244wgerg2", "Blue T-Shirt", "Not Required", "blue-t-shirt.jpg", 29,
                "Clothing", 25, InventoryStatus.INSTOCK, 5));
        products.add(new TaxPosition(1004, "h456wer53", "Bracelet", "Not Required", "bracelet.jpg", 15,
                "Accessories", 73, InventoryStatus.INSTOCK, 4));
        products.add(new TaxPosition(1005, "av2231fwg", "Brown Purse", "Not Required", "brown-purse.jpg", 120,
                "Accessories", 0, InventoryStatus.OUTOFSTOCK, 4));
        products.add(new TaxPosition(1006, "bib36pfvm", "Chakra Bracelet", "Not Required", "chakra-bracelet.jpg", 32,
                "Accessories", 5, InventoryStatus.LOWSTOCK, 3));
        products.add(new TaxPosition(1007, "mbvjkgip5", "Galaxy Earrings", "Not Required", "galaxy-earrings.jpg", 34,
                "Accessories", 23, InventoryStatus.INSTOCK, 5));
        products.add(new TaxPosition(1008, "vbb124btr", "Game Controller", "Not Required", "game-controller.jpg", 99,
                "Electronics", 2, InventoryStatus.LOWSTOCK, 4));
        products.add(new TaxPosition(1009, "cm230f032", "Gaming Set", "Not Required", "gaming-set.jpg", 299,
                "Electronics", 63, InventoryStatus.INSTOCK, 3));
        products.add(new TaxPosition(1010, "plb34234v", "Gold Phone Case", "Not Required", "gold-phone-case.jpg", 24,
                "Accessories", 0, InventoryStatus.OUTOFSTOCK, 4));
        products.add(new TaxPosition(1011, "4920nnc2d", "Green Earbuds", "Not Required", "green-earbuds.jpg", 89,
                "Electronics", 23, InventoryStatus.INSTOCK, 4));
        products.add(new TaxPosition(1012, "250vm23cc", "Green T-Shirt", "Not Required", "green-t-shirt.jpg", 49,
                "Clothing", 74, InventoryStatus.INSTOCK, 5));
        products.add(new TaxPosition(1013, "fldsmn31b", "Grey T-Shirt", "Not Required", "grey-t-shirt.jpg", 48,
                "Clothing", 0, InventoryStatus.OUTOFSTOCK, 3));
        products.add(new TaxPosition(1014, "waas1x2as", "Headphones", "Not Required", "headphones.jpg", 175,
                "Electronics", 8, InventoryStatus.LOWSTOCK, 5));
        products.add(new TaxPosition(1015, "vb34btbg5", "Light Green T-Shirt", "Not Required", "light-green-t-shirt.jpg", 49,
                "Clothing", 34, InventoryStatus.INSTOCK, 4));
        products.add(new TaxPosition(1016, "k8l6j58jl", "Lime Band", "Not Required", "lime-band.jpg", 79,
                "Fitness", 12, InventoryStatus.INSTOCK, 3));
        products.add(new TaxPosition(1017, "v435nn85n", "Mini Speakers", "Not Required", "mini-speakers.jpg", 85,
                "Clothing", 42, InventoryStatus.INSTOCK, 4));
        products.add(new TaxPosition(1018, "09zx9c0zc", "Painted Phone Case", "Not Required", "painted-phone-case.jpg", 56,
                "Accessories", 41, InventoryStatus.INSTOCK, 5));
        products.add(new TaxPosition(1019, "mnb5mb2m5", "Pink Band", "Not Required", "pink-band.jpg", 79,
                "Fitness", 63, InventoryStatus.INSTOCK, 4));
        products.add(new TaxPosition(1020, "r23fwf2w3", "Pink Purse", "Not Required", "pink-purse.jpg", 110,
                "Accessories", 0, InventoryStatus.OUTOFSTOCK, 4));
        products.add(new TaxPosition(1021, "pxpzczo23", "Purple Band", "Not Required", "purple-band.jpg", 79,
                "Fitness", 6, InventoryStatus.LOWSTOCK, 3));
        products.add(new TaxPosition(1022, "2c42cb5cb", "Purple Gemstone Necklace", "Not Required", "purple-gemstone-necklace.jpg", 45,
                "Accessories", 62, InventoryStatus.INSTOCK, 4));
        products.add(new TaxPosition(1023, "5k43kkk23", "Purple T-Shirt", "Not Required", "purple-t-shirt.jpg", 49,
                "Clothing", 2, InventoryStatus.LOWSTOCK, 5));
        products.add(new TaxPosition(1024, "lm2tny2k4", "Shoes", "Not Required", "shoes.jpg", 64,
                "Clothing", 0, InventoryStatus.INSTOCK, 4));
        products.add(new TaxPosition(1025, "nbm5mv45n", "Sneakers", "Not Required", "sneakers.jpg", 78,
                "Clothing", 52, InventoryStatus.INSTOCK, 4));
        products.add(new TaxPosition(1026, "zx23zc42c", "Teal T-Shirt", "Not Required", "teal-t-shirt.jpg", 49,
                "Clothing", 3, InventoryStatus.LOWSTOCK, 3));
        products.add(new TaxPosition(1027, "acvx872gc", "Yellow Earbuds", "Not Required", "yellow-earbuds.jpg", 89,
                "Electronics", 35, InventoryStatus.INSTOCK, 3));
        products.add(new TaxPosition(1028, "tx125ck42", "Yoga Mat", "Not Required", "yoga-mat.jpg", 20,
                "Fitness", 15, InventoryStatus.INSTOCK, 5));
        products.add(new TaxPosition(1029, "gwuby345v", "Yoga Set", "Not Required", "yoga-set.jpg", 20,
                "Fitness", 25, InventoryStatus.INSTOCK, 8));

    }

    public List<TaxPosition> getProducts() {
        return new ArrayList<>(products);
    }

    public List<TaxPosition> getProducts(int size) {

        if (size > products.size()) {
            

            List<TaxPosition> randomList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
            	int randomIndex = UUID.randomUUID().clockSequence() ;
                randomList.add(products.get(randomIndex));
            }

            return randomList;
        }

        else {
            return new ArrayList<>(products.subList(0, size));
        }

    }

    public List<TaxPosition> getClonedProducts(int size) {
        List<TaxPosition> results = new ArrayList<>();
        List<TaxPosition> originals = getProducts(size);
        for (TaxPosition original : originals) {
            results.add(original.clone());
        }
        return results;
    }
}