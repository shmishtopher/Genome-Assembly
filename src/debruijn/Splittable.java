package debruijn;


/**
 * The splittable interface represents a sequence that can have both a
 * prefix item and a suffix item.  This is required for insertion into
 * the De Bruijn graph
 * 
 * @param <T>
 */
public interface Splittable<T> {
  public T getPrefix();
  public T getSuffix();
  public int size();
}