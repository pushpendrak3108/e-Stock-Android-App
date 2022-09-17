package com.example.projapp;

public class tr_model {
    String stockid,stockname,price,quantity,type, dateandtime;
    public tr_model(){}
    public tr_model(String stockid,String stockname,String price,String quantity,String type, String dateandtime){
        this.stockname=stockname;
        this.price=price;
        this.stockid=stockid;
        this.quantity = quantity;
        this.type = type;
        this.dateandtime = dateandtime;
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

    public String getType() { return type;    }

    public void setType(String type) { this.type = type;  }

    public String getDateandtime() { return dateandtime;    }

    public void setDateandtime(String dateandtime) { this.dateandtime = dateandtime;  }
}
