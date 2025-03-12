package si.f5.stsaria.crafterStrikeMain;

public class Calculator {
    public static boolean isXYZIncludeRange(int cX, int cY, int cZ, int x, int y, int z, Integer rD){
        return Math.abs(Math.sqrt((cX-x)^2+(cY-y)^2+(cZ-z)^2)) <= rD;
    }
}
