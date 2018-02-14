public class Sets implements Set {

  Sets left;
  Sets right;
  Integer val;
  int size;
  Color color;

  // keep track of the product of all elts in the set to use as a hash.
  // INVARIANT: hash is the product of all elements in the tree.
  int hash;

  static final Sets empty = new Sets(null, null, null, 0, null, 1);

  // constant described in  Donald E. Knuth. Sorting and Searching,
  // volume 3 of The Art of Computer Programming. Addison-Wesley, 1973. Second edition, 1998.
  static final double hashConstant = (Math.sqrt(5) - 1) / 2;
  static final int hashSlots = (int)Math.pow(2, 30);  // want more than 1 billion slots to hash
  // into.

  public static Sets empty() {
    return empty;
  }

  @Override
  public Sets adjoin(int k) {
    return makeBlack(ins(k));
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean contains(int k2) {
    if (size == 0)
      return false;

    return val == k2 || left.contains(k2) || right.contains(k2);
  }

  @Override
  public int min() {
    if (size == 0)
      throw new UnsupportedOperationException("Cannot find min of empty set.");

    if (left.size() == 0)
      return val;
    else
      return left.min();
  }

  @Override
  public int max() {
    if (size == 0)
      throw new UnsupportedOperationException("Cannot find max of empty set.");

    if (right.size() == 0)
      return val;
    else
      return right.max();
  }

  public Sets(Sets left, Sets right, Integer val, int size, Color color, int hash) {
    this.left = left;
    this.right = right;
    this.val = val;
    this.size = size;
    this.color = color;
    this.hash = hash;
  }

  public Sets(Sets left, Sets right, Integer val, int size, Color color) {
    this.left = left;
    this.right = right;
    this.val = val;
    this.size = size;
    this.color = color;
    this.hash = Integer.MAX_VALUE;
  }

  // Given a set, return a new set with a black root node.
  private static Sets makeBlack(Sets s) {
    return new Sets(s.left, s.right, s.val, s.size, Color.BLACK, s.hash);
  }

  // Given a sequence of integers, return the value of their multiplication bounded by the number
  // of hashslots specified.
  private static int bounded(int... k) {
    long ans = 1;
    for (int i = 0; i < k.length; i++) {
      int i1 = k[i];
      ans = ans * i1;
    }
    return (int)(ans % hashSlots);  // if hashSlots < 2^31 - 1, no overflow.
  }

  // insert the element into this set.
  private Sets ins(int k) {
    if (size == 0)
      return new Sets(empty(), empty(), k, 1, Color.RED, bounded(k));

    if (k == val)
      return this;
    if (k < val) {
      Sets l = left.ins(k);
      return balance(new Sets(l, right, val, size - left.size() + l.size(), color,
          bounded(l.hash, right.hash, val)));
    } else {
      Sets r = right.ins(k);
      return balance(new Sets(left, r, val, size - right.size() + r.size(), color,
          bounded(left.hash, r.hash, val)));
    }
  }

  // Given the components of a balanced Red-Black tree described in Okasaki fig. 1,
  // Return a balanced Red-black tree that upholds the invariants.

  private static Sets balanced(Sets x, Sets y, Sets z, Sets a, Sets b, Sets c, Sets d) {
    Sets newX = new Sets(a, b, x.val, a.size + b.size + 1, Color.BLACK,
        bounded(a.hash, b.hash, x.val));
    Sets newZ =  new Sets(c, d, z.val, c.size + d.size + 1, Color.BLACK,
        bounded(c.hash, d.hash, z.val));
    return new Sets(newX, newZ, y.val, newX.size + newZ.size + 1, Color.RED,
        bounded(newX.hash, newZ.hash, y.val));
  }

  // Given an arbitrary red-black tree s, return a balanced version of that tree that upholds the
  // invariants.

  public static Sets balance(Sets s) {

    // These sets correspond directly to the diagram in Okasaki Fig 1.
    Sets x;
    Sets y;
    Sets z;
    Sets a;
    Sets b;
    Sets c;
    Sets d;

    // Check if the tree is of any of the forms described in Okasaki Fig. 1
    if (s.left.size() >= 2) {  // check for first 2 cases

      if(s.left.color == Color.RED && s.left.left.color == Color.RED) {
        z = s;
        y = s.left;
        x = s.left.left;
        a = x.left;
        b = x.right;
        c = y.right;
        d = z.right;
        return balanced(x, y, z, a, b, c, d);
      } else if (s.left.color == Color.RED && s.left.right.color == Color.RED) {
        z = s;
        x = z.left;
        y = x.right;
        a = x.left;
        b = y.left;
        c = y.right;
        d = z.right;
        return balanced(x, y, z, a, b, c, d);
      }
    }

    if (s.right.size() >= 2) {  // check for 2nd two cases
      if(s.right.color == Color.RED && s.right.right.color == Color.RED) {
        x = s;
        y = x.right;
        z = y.right;
        a = x.left;
        b = y.left;
        c = z.left;
        d = z.right;
        return balanced(x, y, z, a, b, c, d);
      } else if (s.right.color == Color.RED && s.right.left.color == Color.RED) {
        x = s;
        z = x.right;
        y = z.left;
        a = x.left;
        b = y.left;
        c = y.right;
        d = z.right;
        return balanced(x, y, z, a, b, c, d);
      }
    }

    // all other cases are already balanced.
    return s;
  }

  // Given another object, is it equivalent to this set?
  // Sets are equivalent if their sizes are equal,
  // and for every elt i in set 1, i is an elt of set 2
  // ANALYSIS: always runs in Theta(nlg(n)) time.

  @Override
  public boolean equals(Object o) {
    if (o instanceof Set) {
      Set so = (Set)o;
      if (so.size() == this.size()) {

        // We have to check if every elt in this is in o.
        // Do this recursively on this set.
        return this.checkEquals(so);
      }
    }
    return false;
  }

  // Given a Set, is every element of this set in that set?

  private boolean checkEquals(Set so) {
    // Contains runs in Theta(lg(n)) time, and this function recurs n times, so this function
    // runs in Theta(nlg(n)) time.
    if (this.size() != 0) {
      return so.contains(this.val) && this.left.checkEquals(so) && this.right.checkEquals(so);
    }

    return true;  // empty set is contained by every set.
  }

  // Add the value at this node to the array in its place in non-decreasing order.
  private void insertToArray(int[] acc) {
    if (this.size() == 0) {
      return; // base case adds nothing.
    }

    // Everything smaller should come first.
    this.left.insertToArray(acc);

    // Insertion subroutine for single element.
    boolean placed = false;
    int prev = Integer.MAX_VALUE; // If in array, bug.
    for(int i = 0; i < acc.length; i++) {  // Theta(n) always.
      if (placed) {
        int temp = acc[i];
        acc[i] = prev;
        prev = temp;
        continue;
      }
      if (this.val < acc[i]) {
        prev = acc[i];
        acc[i] = this.val;
        placed = true;
      }
    }

    // Everything larger should come after.
    this.right.insertToArray(acc);
  }

  // Return a string representing the Set in non-decreasing order.
  @Override
  public String toString() {
    // Strategy: Traverse the entire tree, inserting elements into an
    // array (Theta(n^2) in worst case). Then Traverse that and add it to a
    // String that will be returned (Theta(n)).
    StringBuilder s = new StringBuilder();
    int[] vals = new int[this.size()]; // O(n) time

    // Initialize all to maximum so we can order the elements.
    for(int i = 0; i < vals.length; i++) {  // O(n) time
      vals[i] = Integer.MAX_VALUE;
    }

    this.insertToArray(vals);  // Theta(n^2) time

    s.append("{");
    for(int i = 0; i < this.size(); i++) {  // Theta(n) time
      s.append(vals[i]);
      if (i < this.size() - 1) {
        s.append(",");
      }
    }
    s.append("}");

    return s.toString();
  }

  // Hashing satisfies the requirements that
  //   1. Runs in Theta(1) time.
  //   2. If s1.equals(s2), then s1.hashcode == s2.hashcode
  //   3. If !s1.equals(s2), the probability that s1 and s2 hash to the same value is less than 1
  //      in a billion
  @Override
  public int hashCode() {
    // The method implements the multiplication method of hashing presented on pp. 264 ch 11
    // of Cormen, Leiserson, Rivest, Stein 3rd. edition.
    double A = hashConstant * this.hash * this.size();
    return (int)(hashSlots * (A - Math.floor(A))); // prime number > 1 billion
  }

  public static enum Color {
    BLACK, RED;
  }
}
