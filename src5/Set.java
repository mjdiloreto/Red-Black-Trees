// Set is an immutable abstract data type (ADT)
// whose values represent sets of int values.
//
// Sets.empty() returns an empty Set.
//
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
// None of those operations have any externally visible side effects.

public interface Set {

    // Returns a set representing the union of this Set with { k }.

    Set adjoin (int k);

    // Returns the number of distinct integers in this Set.

    int size ();

    // Returns true iff the given integer is an element of this Set.

    boolean contains (int k2);

    // Returns the least element of this Set.
    // Precondition: this Set is non-empty.

    int min ();

    // Returns the greatest element of this Set.
    // Precondition: this Set is non-empty.

    int max ();
}
