public class Sets implements Set {

  Sets left;
  Sets right;
  Integer val;
  int size;
  Color color;

  static final Sets empty = new Sets(null, null, null, 0, null);

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

  public Sets(Sets left, Sets right, Integer val, int size, Color color) {
    this.left = left;
    this.right = right;
    this.val = val;
    this.size = size;
    this.color = color;
  }

  // Given a set, return a new set with a black root node.
  private static Sets makeBlack(Sets s) {
    return new Sets(s.left, s.right, s.val, s.size, Color.BLACK);
  }

  // insert the element into this set.
  private Sets ins(int k) {
    if (size == 0)
      return new Sets(empty(), empty(), k, 1, Color.RED);

    if (k == val)
      return this;
    if (k < val) {
      Sets l = left.ins(k);
      return balance(new Sets(l, right, val, size - left.size() + l.size(), color));
    } else {
      Sets r = right.ins(k);
      return balance(new Sets(left, r, val, size - right.size() + r.size(), color));
    }
  }

  public static Sets balanceCase1(Sets s) {
    Sets z = s;
    Sets y = s.left;
    Sets x = s.left.left;
    Sets a = x.left;
    Sets b = x.right;
    Sets c = y.right;
    Sets d = z.right;
    Sets newX = makeBlack(x);
    Sets newZ =  new Sets(c, d, z.val, c.size + d.size + 1, Color.BLACK);
    return new Sets(newX, newZ, y.val, newX.size + newZ.size + 1, Color.RED);
  }

  public static Sets balanceCase2(Sets s) {
    Sets z = s;
    Sets x = z.left;
    Sets y = x.right;
    Sets a = x.left;
    Sets b = y.left;
    Sets c = y.right;
    Sets d = z.right;
    Sets newX = new Sets(a, b, x.val, a.size + b.size + 1, Color.BLACK);
    Sets newZ =  new Sets(c, d, z.val, c.size + d.size + 1, Color.BLACK);
    return new Sets(newX, newZ, y.val, newX.size + newZ.size + 1, Color.RED);
  }

  public static Sets balanceCase3(Sets s) {
    Sets x = s;
    Sets y = x.right;
    Sets z = y.right;
    Sets a = x.left;
    Sets b = y.left;
    Sets c = z.left;
    Sets d = z.right;
    Sets newX = new Sets(a, b, x.val, a.size + b.size + 1, Color.BLACK);
    Sets newZ =  new Sets(c, d, z.val, c.size + d.size + 1, Color.BLACK);
    return new Sets(newX, newZ, y.val, newX.size + newZ.size + 1, Color.RED);
  }

  public static Sets balanceCase4(Sets s) {
    Sets x = s;
    Sets z = x.right;
    Sets y = z.left;
    Sets a = x.left;
    Sets b = y.left;
    Sets c = y.right;
    Sets d = z.right;
    Sets newX = new Sets(a, b, x.val, a.size + b.size + 1, Color.BLACK);
    Sets newZ =  new Sets(c, d, z.val, c.size + d.size + 1, Color.BLACK);
    return new Sets(newX, newZ, y.val, newX.size + newZ.size + 1, Color.RED);
  }

  private static Sets balance(Sets s) {
    if (s.left.size() >= 2) {  // check for first 2 cases
      if(s.left.color == Color.RED && s.left.left.color == Color.RED) {
        return balanceCase1(s);
      } else if (s.left.color == Color.RED && s.left.right.color == Color.RED) {
        return balanceCase2(s);
      }
    } if (s.right.size() >= 2) {  // check for 2nd two cases
      if(s.right.color == Color.RED && s.right.right.color == Color.RED) {
        return balanceCase3(s);
      } else if (s.right.color == Color.RED && s.right.left.color == Color.RED) {
        return balanceCase4(s);
      }
    }
    return s;  // base cases are OK
  }

  @Override
  public String toString() {
    return "Val: " + val + " size: " + size + " color: " + color;
  }

  public static enum Color {
    BLACK, RED;
  }
}
