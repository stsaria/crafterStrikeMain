package si.f5.stsaria.crafterStrikeMain;

public class Calculator {
    public static boolean isXZIncludeRange(int cX, int cZ, int x, int z, Integer rD){
        return (cX-rD <= x && x <= cX+rD) && (cZ-rD <= z && z <= cZ+rD);
    }
    public static boolean isXZIncludeRange(int cX, int cZ, int x, int z, Integer rDX, Integer rDZ){
        return (cX-rDX <= x && x <= cX+rDX) && (cZ-rDZ <= z && z <= cZ+rDZ);
    }
}
