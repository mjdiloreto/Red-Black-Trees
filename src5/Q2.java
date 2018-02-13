class Q2 {

  // Given a array of n integers
  // and a non-negative integer k <= n,
  // returns an array of length k containing the k smallest
  // values in the given array, in non-decreasing order.
  // ANALYSIS: Runs in OMEGA(n * OMEGA(insert) + k) time in all cases. (OMEGA(kn))

  public static int[] smallest (int[] seq, int k) {
    int[] ret = new int[k];
    for (int i = 0; i < k; i++) {
      ret[i] = Integer.MAX_VALUE;
    }

    for (int n: seq) {
        insert(n, ret);
    }

    return ret;
  }

  // Given an array of size k and an integer, return the sequence
  // with the k smallest elements of the set of size k+1.
  // PRECONDITION: seq is in non-decreasing order.
  // ANALYSIS: If k = seq.length, runs in OMEGA(k) time in all cases.

  public static void insert(int n, int[] seq) {
    boolean placed = false;  // has n been placed in seq?
    int prev = Integer.MAX_VALUE;  // if in seq, bug

    for(int i = 0; i < seq.length; i++) {
      if (placed) {  // shift all elts down.
        int temp = seq[i];
        seq[i] = prev;
        prev = temp;
        continue;
      }
      if (n < seq[i]) {  // compare n to current elt.
        prev = seq[i];
        seq[i] = n;
        placed = true;
      }
    }
  }

  public static void main(String[] args) {
    int[] seq = {1,2,3,4,5,6,7,8};
    int[] seq1 = {8,7,6,5,4,3,2,1};
    int[] seq2 = {3,5,2,6,24,1,3,7,4,21,9,7,4,7,53};

    for(int n: smallest(seq2, 15)) {
      System.out.println(n);
    }
  }
}