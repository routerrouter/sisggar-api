import java.util.Arrays;
import java.util.List;

public class TestStreams {
    public static void main(String[] args) {
        List<Integer> integerList =  Arrays.asList(11,2,3,34,5,6,7,8,9,0);
        integerList.stream().sorted().forEach(System.out::println);
    }
}
