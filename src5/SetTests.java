import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
  public void testMin() {
    assertEquals(1, Sets.empty().adjoin(1).min());
    assertEquals(1, Sets.empty().adjoin(1).max());

    assertEquals(1, Sets.empty().adjoin(1).adjoin(2).min());
    assertEquals(2, Sets.empty().adjoin(1).adjoin(2).max());

    assertEquals(2, Sets.empty().adjoin(2).adjoin(1).max());
    assertEquals(1, Sets.empty().adjoin(2).adjoin(1).min());
  }
}