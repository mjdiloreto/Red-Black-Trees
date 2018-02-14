import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Supplier;
import org.junit.Test;


// Algebraic specification:
//
//     Sets.empty().size()  ==  0
//     s.adjoin(k).size()  ==  s.size()              if s.contains(k)
//     s.adjoin(k).size()  ==  1 + s.size()          if ! s.contains(k)
//
//     Sets.empty().contains(k2)  ==  false
//     s.adjoin(k).contains(k2)  ==  true            if k == k2
//     s.adjoin(k).contains(k2)  ==  s.contains(k2)  if k != k2
//
//     Sets.empty().adjoin(k).min()  ==  k
//     s.adjoin(k).adjoin(k2).min()  ==  k2          if k2 <= s.adjoin(k).min()
//     s.adjoin(k).adjoin(k2).min()
//         ==  s.adjoin(k).min()                     if k2 > s.adjoin(k).min()
//
//     Sets.empty().adjoin(k).max()  ==  k
//     s.adjoin(k).adjoin(k2).max()  ==  k2          if k2 >= s.adjoin(k).max()
//     s.adjoin(k).adjoin(k2).max()
//         ==  s.adjoin(k).max()                     if k2 < s.adjoin(k).max()
//

public class SetTests {

  @Test
  public void testEmpty() {
    assertEquals(true, Sets.empty().equals(Sets.empty()));
    assertEquals(0, Sets.empty().size());
    assertEquals(false, Sets.empty().equals( Sets.empty().adjoin(1)));
  }

  @Test
  public void testContains() {
    assertEquals(false, Sets.empty().contains(0));
    assertEquals(false, Sets.empty().contains(1));
    assertEquals(false, Sets.empty().contains(Integer.MAX_VALUE));
    assertEquals(false, Sets.empty().contains(Integer.MIN_VALUE));

    assertEquals(false, Sets.empty().adjoin(1).contains(0));
    assertEquals(true, Sets.empty().adjoin(0).contains(0));

    assertEquals(false, Sets.empty().adjoin(1).adjoin(2).contains(0));
    assertEquals(true, Sets.empty().adjoin(1).adjoin(2).contains(1));
    assertEquals(true, Sets.empty().adjoin(1).adjoin(2).contains(2));
  }

  @Test
  public void testSize() {
    assertEquals(0, Sets.empty().size());
    assertEquals(1, Sets.empty().adjoin(1).size());
    assertEquals(2, Sets.empty().adjoin(1).adjoin(2).size());
    assertEquals(2, Sets.empty().adjoin(2).adjoin(1).size());
    assertEquals(3, Sets.empty().adjoin(2).adjoin(1).adjoin(3).size());

    assertEquals(1, Sets.empty().adjoin(1).size());
    assertEquals(1, Sets.empty().adjoin(1).adjoin(1).size());
    assertEquals(3, Sets.empty().adjoin(2).adjoin(1).adjoin(3).adjoin(2).size());
  }

  @Test
  public void testMinMax() {
    assertEquals(1, Sets.empty().adjoin(1).min());
    assertEquals(1, Sets.empty().adjoin(1).max());

    assertEquals(1, Sets.empty().adjoin(1).adjoin(2).min());
    assertEquals(2, Sets.empty().adjoin(1).adjoin(2).max());

    assertEquals(2, Sets.empty().adjoin(2).adjoin(1).max());
    assertEquals(1, Sets.empty().adjoin(2).adjoin(1).min());

    Sets s0 = Sets.empty();
    for (int i = 0; i < 3; i++) {
      s0 = s0.adjoin(i);
    }
    //assertEquals(0, s0.min());
    //assertEquals(99, s0.max());
  }

  @Test
  public void testEquals() {

    Sets s1 =
        new Sets(
            Sets.empty(),
            new Sets(
                new Sets(Sets.empty(), Sets.empty(), 2, 1, Sets.Color.RED),
                Sets.empty(),
                3,
                2, Sets.Color.RED),
            1,
            3,
            Sets.Color.RED
        );

    Sets s2 = Sets.empty();
    Sets s3 = Sets.empty();
    for(int i = 0; i < 100; i++) {
      s2 = s2.adjoin(i);
    }
    for(int i = 99; i >= 0; i--) {
      s3 = s3.adjoin(i);
    }
    assertEquals(true, Sets.empty().equals(Sets.empty()));
    assertEquals(false, Sets.empty().equals(Sets.empty().adjoin(1)));
    assertEquals(true, Sets.empty().adjoin(1).equals(Sets.empty().adjoin(1)));
    assertEquals(false, Sets.empty().adjoin(1).equals(Sets.empty().adjoin(2)));
    assertEquals(true,
        Sets.empty().adjoin(1).adjoin(2).equals(
        Sets.empty().adjoin(2).adjoin(1))
    );
    assertEquals(true, s1.adjoin(2).equals(s1.adjoin(1)));
    assertEquals(true, s2.equals(s3));
    assertEquals(true, s2.adjoin(75).equals(s3.adjoin(75)));
    assertEquals(true, s2.adjoin(75).equals(s3.adjoin(74)));
    assertEquals(false, s2.adjoin(100).equals(s3.adjoin(74)));
    assertEquals(true, s2.adjoin(100).equals(s3.adjoin(100)));
  }

  @Test
  public void testHashCode() {
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

    assertEquals(empty.hashCode(), empty.hashCode());
    //assertNotEquals(empty.hashCode(), s1.hashCode());
    assertNotEquals(empty.hashCode(), s2.hashCode());
    assertNotEquals(s1.hashCode(), s2.hashCode());
    assertNotEquals(s1.hashCode(), s3.hashCode());
    assertNotEquals(s2.hashCode(), s3.hashCode());
    assertEquals(s3.hashCode(), s3.hashCode());
    assertEquals( s4.hashCode(), s3.hashCode());

    assertEquals(s0To100.hashCode(), s100To0.hashCode());


  }

  @Test
  public void testToString() {
    Sets s2 = Sets.empty();
    Sets s3 = Sets.empty();
    for(int i = 0; i < 100; i++) {
      s2 = s2.adjoin(i);
    }
    for(int i = 99; i >= 0; i--) {
      s3 = s3.adjoin(i);
    }

    assertEquals(s2.toString(), s3.toString());
  }

  @Test
  public void testBalance() {
    int xval = 1;
    int yval = 2;
    int zval = 3;

    Sets s1 = new Sets(new Sets(
        new Sets(Sets.empty(), Sets.empty(), xval, 1, Sets.Color.RED),
        Sets.empty(),
        yval,
        2,
        Sets.Color.RED
    ), Sets.empty(), zval, 3, Sets.Color.BLACK);

    Sets b1 = Sets.balance(s1);

    assertEquals((int)b1.val, yval);
    assertEquals(b1.color, Sets.Color.RED);
    assertEquals(b1.size, 3);
    assertEquals((int)b1.left.val, xval);
    assertEquals(b1.left.size, 1);
    assertEquals(b1.left.color, Sets.Color.BLACK);

    assertEquals((int)b1.right.val, zval);
    assertEquals(b1.right.size, 1);
    assertEquals(b1.right.color, Sets.Color.BLACK);

  }

  @Test
  public void testBalance2() {
    int xval = 1;
    int yval = 2;
    int zval = 3;

    Sets s1 = new Sets(
        new Sets(
          Sets.empty(),
          new Sets(Sets.empty(), Sets.empty(), yval, 1, Sets.Color.RED),
          xval,
          2,
          Sets.Color.RED
        ),
        Sets.empty(), zval, 3, Sets.Color.BLACK);

    Sets b1 = Sets.balance(s1);

    assertEquals((int)b1.val, yval);
    assertEquals(b1.color, Sets.Color.RED);
    assertEquals(b1.size, 3);
    assertEquals((int)b1.left.val, xval);
    assertEquals(b1.left.size, 1);
    assertEquals(b1.left.color, Sets.Color.BLACK);

    assertEquals((int)b1.right.val, zval);
    assertEquals(b1.right.size, 1);
    assertEquals(b1.right.color, Sets.Color.BLACK);

  }

  @Test
  public void testBalance3() {
    int xval = 1;
    int yval = 2;
    int zval = 3;

    Sets s1 =
        new Sets(
            Sets.empty(),
            new Sets(Sets.empty(),
                  new Sets(Sets.empty(), Sets.empty(), zval, 1, Sets.Color.RED),
                  yval,
                  2, Sets.Color.RED),
            xval,
            3,
            Sets.Color.RED
        );

    Sets b1 = Sets.balance(s1);

    assertEquals((int)b1.val, yval);
    assertEquals(b1.color, Sets.Color.RED);
    assertEquals(b1.size, 3);
    assertEquals((int)b1.left.val, xval);
    assertEquals(b1.left.size, 1);
    assertEquals(b1.left.color, Sets.Color.BLACK);

    assertEquals((int)b1.right.val, zval);
    assertEquals(b1.right.size, 1);
    assertEquals(b1.right.color, Sets.Color.BLACK);

  }

  @Test
  public void testBalance4() {
    int xval = 1;
    int yval = 2;
    int zval = 3;

    Sets s1 =
        new Sets(
            Sets.empty(),
            new Sets(
                new Sets(Sets.empty(), Sets.empty(), yval, 1, Sets.Color.RED),
                Sets.empty(),
                zval,
                2, Sets.Color.RED),
            xval,
            3,
            Sets.Color.RED
        );

    Sets b1 = Sets.balance(s1);

    assertEquals((int)b1.val, yval);
    assertEquals(b1.color, Sets.Color.RED);
    assertEquals(b1.size, 3);
    assertEquals((int)b1.left.val, xval);
    assertEquals(b1.left.size, 1);
    assertEquals(b1.left.color, Sets.Color.BLACK);

    assertEquals((int)b1.right.val, zval);
    assertEquals(b1.right.size, 1);
    assertEquals(b1.right.color, Sets.Color.BLACK);

  }
}