package be.kuleuven.spot;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class calculateDistance {
    public static double calculate(double lat1, double lat2, double lon1, double lon2){
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat/2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon/2), 2);
        double c = (2 * Math.asin(Math.sqrt(a))) * 6371;
        BigDecimal bd = new BigDecimal(c).setScale(2, RoundingMode.HALF_UP);
        return (bd.doubleValue());
    }

}
