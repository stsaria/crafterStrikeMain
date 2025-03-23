package si.f5.stsaria.crafterStrikeMain;

public class Calculator {
    public static boolean isBetween(int a, int n, int b){
        return (a <= n && n <= b) || (b <= n && n <= a);
    }
    public static boolean isIncludeRange(int aX, int aY, int aZ, int bX, int bY, int bZ, int x, int y, int z) {
        return isBetween(aX, x, bX) && isBetween(aY, y, bY) && isBetween(aZ, z, bZ);
    }
}
