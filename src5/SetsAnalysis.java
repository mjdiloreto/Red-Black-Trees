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
    for(int i = 0; i < 6; i++) {
      s0 = s0.adjoin(i);
    }

    Sets empty = Sets.empty();

    Sets s1 = empty.adjoin(1);
    Sets s2 = empty.adjoin(2);
    Sets s3 = empty.adjoin(1).adjoin(2);
    Sets s4 = empty.adjoin(2).adjoin(1);

    Sets s0To100 = Sets.empty();
    Sets s100To0 = Sets.empty();
    for(int i = 0; i < 100; i++) {
      s0To100 = s0To100.adjoin(i);
    }
    for(int i = 99; i >= 0; i--) {
      s100To0 = s100To0.adjoin(i);
    }

    System.out.println(empty.hashCode());
    System.out.println(s1.hashCode());
    System.out.println(s2.hashCode());
    System.out.println(s3.hashCode());
    System.out.println(s4.hashCode());
    System.out.println(s0To100.hashCode());
    System.out.println(s100To0.hashCode());



//    for(int i = 0; i < 100; i ++) {
//      System.out.print(i);
//      double A = Sets.hashConstant * i;
//      //System.out.println(Math.pow(2, 30));
//      System.out
//          .println(" " + Sets.hashSlots * (A - Math.floor(A)));//(int) Math.floor(Sets.hashSlots *
//      // (A - Math.floor
//      // (A)))); // prime number
//      // > 1
//      // billion
//    }
//
//    Queue<Sets> toCheck = new LinkedList<>();
//    toCheck.add(s0);
//
//    while(toCheck.peek() != null) {
//      Sets consider = toCheck.remove();
//      System.out.println(consider);
//      toCheck.add(consider.left);
//      toCheck.add(consider.right);
//    }


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
