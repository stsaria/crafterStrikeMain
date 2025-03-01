package si.f5.stsaria.crafterStrikeMain.items;

public abstract class BuyGameItem extends GameItem {
    abstract int PRICE();

    String ABOUT(){
        return "値段:"+PRICE();
    }
}
