public class Sets implements Set {

  Set left;
  Set right;
  int val;
  int size;

  public static Set empty() {
    return new EmptySet();
  }

  public Sets(Set left, Set right, int val, int size) {
    this.left = left;
    this.right = right;
    this.val = val;
    this.size = size;
  }

  @Override
  public Set adjoin(int k) {
    if (k == val)
      return this;
    if (k < val) {
      Set l = left.adjoin(k);
      return new Sets(l, right, val, size - left.size() + l.size());
    } else {
      Set r = right.adjoin(k);
      return new Sets(left, r, val, size - right.size() + r.size());
    }
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean contains(int k2) {
    return val == k2 || left.contains(k2) || right.contains(k2);
  }

  @Override
  public int min() {
    if (left.size() == 0)
      return val;
    else
      return left.min();
  }

  @Override
  public int max() {
    if (right.size() == 0)
      return val;
    else
      return right.max();
  }

  private static class EmptySet implements Set {
    @Override
    public Set adjoin(int k) {
      return new Sets(new EmptySet(), new EmptySet(), k, 1);
    }

    @Override
    public int size() {
      return 0;
    }

    @Override
    public boolean contains(int k2) {
      return false;
    }

    @Override
    public int min() {
      throw new UnsupportedOperationException("Cannot find min of empty set.");
    }

    @Override
    public int max() {
      throw new UnsupportedOperationException("Cannot find max of empty set.");
    }

    @Override
    public boolean equals(Object o) {
      return o instanceof EmptySet;
    }

    @Override
    public int hashCode() {
      return 0;
    }
  }
}
