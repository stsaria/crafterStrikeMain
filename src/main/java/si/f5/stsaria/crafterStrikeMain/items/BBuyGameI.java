package si.f5.stsaria.crafterStrikeMain.items;

public abstract class BBuyGameI extends BItem {
    abstract int PRICE();

    String ABOUT(){
        return "値段:"+PRICE();
    }
}
