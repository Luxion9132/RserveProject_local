/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luxion.ottzServlet;

/**
 *
 * @author Leon
 */
public class TradeRecord{
    String buyDate;
    String buyPrice;
    String sellDate;
    String sellPrice;
    public void setBuyDate(String buydate){
        this.buyDate = buydate;
    }
    public void setBuyPrice(String buyprice){
        this.buyPrice = buyprice;
    }
    public void setSellDate(String selldate){
        this.sellDate = selldate;
    }
    public void setSellPrice(String sellprice){
        this.sellPrice = sellprice;                
    }
    public String getBuyDate(){
        return this.buyDate;
    }
    public String getBuyPrice(){
        return this.buyPrice;
    }
    public String getSellDate(){
        return this.sellDate;
    }
    public String getSellPrice(){
        return this.sellPrice;
    }
    
}