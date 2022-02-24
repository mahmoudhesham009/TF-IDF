import java.math.BigDecimal;

public class main {
    public static void main(String[] args) {
        BigDecimal x=new BigDecimal(5.5645);
        //System.out.println(x%x.intValue()==0);

        System.out.println(String.format("%.2f",x));
    }
}
