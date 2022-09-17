package com.example.projapp;

public class Model {
    String stockid,stockname,price,quantity,supplier;
    public Model(){}
    public Model(String stockid,String stockname,String price,String quantity,String supplier){
        this.stockname=stockname;
        this.price=price;
        this.stockid=stockid;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    public String getStockid() { return stockid; }

    public void setStockid(String stockid) {
        this.stockid = stockid;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() { return quantity;    }

    public void setQuantity(String quantity) { this.quantity = quantity;  }

    public String getSupplier() { return supplier;    }

    public void setSupplier(String supplier) { this.supplier = supplier;  }
}
