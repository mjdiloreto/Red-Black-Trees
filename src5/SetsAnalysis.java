import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
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
    int size0 = 100;
    int size1 = 1000;
    int size2 = 10000;
    int size3 = 100000;
    //int size4 = 1000000;
    //int size5 = 10000000;

    int iter = 100;

    int[] sizes = {size0, size1, size2, size3};//, size4, size5};

    for (int k = 0; k < sizes.length; k++) {
      ArrayList<Long> times = new ArrayList<>();
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
      System.out.println("Avg of " + iter + " runs of size " + size + " was " + times.toString());
//          + times.stream()
//          .mapToLong(val -> val)
//          .average().getAsDouble());
    }

    Sets s0 = Sets.empty();
    for(int i = 0; i < 7; i++) {
      s0 = s0.adjoin(i);
    }

    Queue<Sets> toCheck = new LinkedList<>();
    toCheck.add(s0);

    while(toCheck.peek() != null) {
      Sets consider = toCheck.remove();
      System.out.println(consider);
      toCheck.add(consider.left);
      toCheck.add(consider.right);
    }


//    boolean isL = true;
//    while(s0.left != null && s0.right != null) {
//      System.out.println(s0);
//      if (isL) {
//        s0 = s0.left;
//        isL = false;
//      } else {
//        s0 = s0.right;
//        isL = true;
//      }
//    }
  }
}
