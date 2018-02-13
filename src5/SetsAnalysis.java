import java.util.ArrayList;
import java.util.function.Supplier;

public class SetsAnalysis {
  public static <T> long time(Supplier<T> thunk) {
    try {
      long t0 = System.currentTimeMillis();
      T result = thunk.get();
      long t1 = System.currentTimeMillis();
      long t = t1 - t0;
      return t;
    } catch (Error e) {
      System.out.println("\nERROR: " + e);
      return -1;
    }
  }

  public static void main(String[] args) {
    ArrayList<Long> times = new ArrayList<>();
    int size0 = 100;
    int size1 = 1000;
    int size2 = 10000;
    int size3 = 100000;

    int iter = 1000;

    int[] sizes = {size0, size1, size2, size3};

    for (int k = 0; k < sizes.length; k++) {
      int size = sizes[k];
      Set s0 = Sets.empty();
      for(int i = 0; i < size; i++) {
        s0 = s0.adjoin(i);
      }
      for(int i = 0; i < iter; i++) {
        Set finalS = s0;
        long t = time(() -> finalS.contains(size));
        times.add(i, t);
      }
      System.out.println("Avg of " + iter + " runs of size " + size + " was "
          + times.stream()
          .mapToLong(val -> val)
          .average().getAsDouble());
    }
  }
}
