package com.example.project_one;

class Constantinfo {
    private static String allProducts= "";
    static String totalprice = "";
    static int selectCapacity = 0;

    String getAllProducts(){
        return allProducts;
    }

    void setAllProducts(String product){
        allProducts=product;
    }

    String getTotalprice(){
        return totalprice;
    }

    void setTotalprice(String totalprice){
        this.totalprice =totalprice;
    }

    int getSelectCapacity(){
        return selectCapacity;
    }
    void setSelectCapacity(int selectCapacity){
        this.selectCapacity = selectCapacity;
    }
}
