package si.f5.stsaria.crafterStrikeMain;

public class Calculator {
    public static boolean isXZIncludeRange(int x1, int z1, int x2, int z2, int rangeDistance){
        return (x1-rangeDistance <= x2 && x2 <= x1+rangeDistance) && (z1-rangeDistance <= z2 && z2 <= z1+rangeDistance);
    }
}
