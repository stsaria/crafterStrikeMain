package si.f5.stsaria.crafterStrikeMain.items;

import si.f5.stsaria.crafterStrikeMain.Game;

public abstract class BBuyGameI extends BItem {
    abstract int PRICE();

    String ABOUT(){
        return Game.configGetString("wordPrice")+":"+PRICE();
    }
}
