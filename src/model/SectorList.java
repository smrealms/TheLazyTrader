package model;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;
import java.util.Vector;

/**
 * An array-backed implementation of the List interface. This implements all
 * optional list operations, and permits only Sector elements. Random access is
 * roughly constant time, and iteration is roughly linear time, so it is nice
 * and fast, with less overhead than a LinkedList.
 * <p>
 * 
 * Each list has a capacity, and as the array reaches that capacity it is
 * automatically transferred to a larger array. You also have access to
 * ensureCapacity and trimToSize to control the backing array's size, avoiding
 * reallocation or wasted memory.
 * <p>
 * 
 * SectorList is not synchronized, so if you need multi-threaded access,
 * consider using:<br>
 * <code>List l = Collections.synchronizedList(new SectorList(...));</code>
 * <p>
 * 
 * The iterators are <i>fail-fast</i>, meaning that any structural
 * modification, except for <code>remove()</code> called on the iterator
 * itself, cause the iterator to throw a {@link ConcurrentModificationException}
 * rather than exhibit non-deterministic behavior.
 * 
 * @author Jon A. Zeppieri
 * @author Bryce McKinlay
 * @author Eric Blake (ebb9@email.byu.edu)
 * @author Pagan Gazzard
 * @see Collection
 * @see List
 * @see LinkedList
 * @see Vector
 * @see Collections#synchronizedList(List)
 * @see AbstractList
 * @status updated to 1.4
 */
public class SectorList extends AbstractList<Sector> implements List<Sector>, RandomAccess, Cloneable
{

	/**
	 * The default capacity for new SectorLists.
	 */
	private static final int DEFAULT_CAPACITY = 30;

	/**
	 * The number of elements in this list.
	 * 
	 * @serial the list size
	 */
	private int size;

	/**
	 * The maximum number of elements allowed in this list.
	 * 
	 * @serial the list size
	 */
	private final int MAX_SIZE;

	/**
	 * Where the Sectors are stored.
	 */
	private Sector[] data;

	/**
	 * Construct a new ArrayList with the supplied initial capacity.
	 * 
	 * @param capacity
	 *            initial capacity of this ArrayList
	 * @throws IllegalArgumentException
	 *             if capacity is negative
	 */
	public SectorList(int capacity)
	{
		// Must explicitly check, to get correct exception.
		if (capacity < 0)
			throw new IllegalArgumentException();
		data = new Sector[capacity];
		MAX_SIZE = Integer.MAX_VALUE;
	}

	/**
	 * Construct a new ArrayList with the supplied initial capacity.
	 * 
	 * @param capacity
	 *            initial capacity of this SectorList
	 * @param max
	 *            maximum size of this SectorList
	 * @throws IllegalArgumentException
	 *             if capacity is negative, or maxSize is less than capacity
	 */
	public SectorList(int capacity, int max)
	{
		// Must explicitly check, to get correct exception.
		if (capacity < 0)
			throw new IllegalArgumentException();
		if (max < capacity)
			throw new IllegalArgumentException();
		data = new Sector[capacity];
		MAX_SIZE = max;
	}

	/**
	 * Construct a new ArrayList with the default capacity (16).
	 */
	public SectorList()
	{
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Construct a new ArrayList from a Sector Array (16).
	 * 
	 * @param c
	 *            Sector array to start with.
	 * @throws IllegalArgumentException
	 *             if maxSize is less than c.length
	 */
	public SectorList(Sector[] c)
	{
		MAX_SIZE = Integer.MAX_VALUE;
		if (MAX_SIZE < c.length)
			throw new IllegalArgumentException();
		data = c;
		size = c.length;
	}

	/**
	 * Construct a new ArrayList from a Sector Array (16).
	 * 
	 * @param c
	 *            Sector array to start with.
	 * @param max
	 *            maximum size of this SectorList
	 * @throws IllegalArgumentException
	 *             if maxSize is less than c.length
	 */
	public SectorList(Sector[] c, int max)
	{
		if (max < c.length)
			throw new IllegalArgumentException();
		data = c;
		size = c.length;
		MAX_SIZE = max;
	}

	/**
	 * Construct a new ArrayList, and initialize it with the elements in the
	 * supplied Collection. The initial capacity is 110% of the Collection's
	 * size.
	 * 
	 * @param c
	 *            the collection whose elements will initialize this list
	 * @throws NullPointerException
	 *             if c is null
	 */
	public SectorList(SectorList c)
	{
		this((int) (c.size() * 1.1f));
		addAll(c);
	}

	/**
	 * Trims the capacity of this List to be equal to its size; a memory saver.
	 */
	public void trimToSize()
	{
		// Not a structural change from the perspective of iterators on this
		// list,
		// so don't update modCount.
		if (size != data.length)
		{
			Sector[] newData = new Sector[size];
			System.arraycopy(data, 0, newData, 0, size);
			data = newData;
		}
	}

	/**
	 * Guarantees that this list will have at least enough capacity to hold
	 * minCapacity elements. This implementation will grow the list to
	 * max(current * 2, minCapacity) if (minCapacity &gt; current). The JCL says
	 * explictly that "this method increases its capacity to minCap", while the
	 * JDK 1.3 online docs specify that the list will grow to at least the size
	 * specified.
	 * 
	 * @param minCapacity
	 *            the minimum guaranteed capacity
	 */
	public void ensureCapacity(int minCapacity)
	{
		int current = data.length;

		if (minCapacity > MAX_SIZE)
			throw new ListTooBigException();

		if (minCapacity > current)
		{
			Sector[] newData = new Sector[Math.min(MAX_SIZE, Math.max(current * 2, minCapacity))];
			System.arraycopy(data, 0, newData, 0, size);
			data = newData;
		}
	}

	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the list size
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Returns the maximum number of elements allowed in this list.
	 * 
	 * @return the list size
	 */
	public int maxSize()
	{
		return MAX_SIZE;
	}

	/**
	 * Checks if the list is empty.
	 * 
	 * @return true if there are no elements
	 */
	public boolean isEmpty()
	{
		return size == 0;
	}

	/**
	 * Returns true iff element is in this ArrayList.
	 * 
	 * @param e
	 *            the element whose inclusion in the List is being tested
	 * @return true if the list contains e
	 */
	public boolean contains(Sector e)
	{
		return indexOf(e) != -1;
	}

	/**
	 * Returns the lowest index at which element appears in this List, or -1 if
	 * it does not appear.
	 * 
	 * @param e
	 *            the element whose inclusion in the List is being tested
	 * @return the index where e was found
	 */
	public int indexOf(Sector e)
	{
		for (int i = 0; i < size; i++)
			if (e.equals(data[i]))
				return i;
		return -1;
	}

	/**
	 * Returns the highest index at which element appears in this List, or -1 if
	 * it does not appear.
	 * 
	 * @param e
	 *            the element whose inclusion in the List is being tested
	 * @return the index where e was found
	 */
	public int lastIndexOf(Sector e)
	{
		for (int i = size - 1; i >= 0; i--)
			if (e.equals(data[i]))
				return i;
		return -1;
	}

	/**
	 * Creates a shallow copy of this ArrayList (elements are not cloned).
	 * 
	 * @return the cloned object
	 */
	public SectorList clone()
	{
		SectorList clone = null;
		try
		{
			clone = (SectorList) super.clone();
			clone.data = data.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// Impossible to get here.
		}
		return clone;
	}

	/**
	 * Returns an Sector array containing all of the elements in this ArrayList.
	 * The array is independent of this list.
	 * 
	 * @return an array representation of this list
	 */
	public Sector[] toArray()
	{
		Sector[] array = new Sector[size];
		System.arraycopy(data, 0, array, 0, size);
		return array;
	}

	/**
	 * Retrieves the element at the user-supplied index.
	 * 
	 * @param index
	 *            the index of the element we are fetching
	 * @throws IndexOutOfBoundsException
	 *             if index &lt; 0 || index &gt;= size()
	 */
	public Sector get(int index)
	{
		checkBoundExclusive(index);
		return data[index];
	}

	/**
	 * Gets the last elements.
	 * 
	 * @param elements
	 *            The number of elements from the end to get
	 * @return the Sectors found
	 */
	public SectorList getLast(int elements)
	{
		Sector[] r = new Sector[elements];
		int newSize = size - elements;
		System.arraycopy(data, newSize, r, 0, elements);
		// Aid for garbage collection by releasing this pointer.
		return new SectorList(r);
	}

	/**
	 * Retrieves the first element.
	 * 
	 * @return The first Sector in the list.
	 */
	public Sector getFirst()
	{
		return get(0);
	}

	/**
	 * Retrieves the last element.
	 * 
	 * @return The last Sector in the list.
	 */
	public Sector get()
	{
		return (size == 0 ? null : get(size - 1));
	}

	/**
	 * Sets the element at the specified index. The new element, e, can be an
	 * Sector object only.
	 * 
	 * @param index
	 *            the index at which the element is being set
	 * @param e
	 *            the element to be set
	 * @return the element previously at the specified index
	 * @throws IndexOutOfBoundsException
	 *             if index &lt; 0 || index &gt;= 0
	 */
	public Sector set(int index, Sector e)
	{
		checkBoundExclusive(index);
		Sector result = data[index];
		data[index] = e;
		return result;
	}

	/**
	 * Appends the supplied element to the end of this list. The element, e, can
	 * be a Sector object.
	 * 
	 * @param e
	 *            the element to be appended to this list
	 * @return true, the add will always succeed
	 */
	public boolean add(Sector c)
	{
		if (c == null)
			throw new NullPointerException();
		modCount++;
		if (size == data.length)
			ensureCapacity(size + 1);
		data[size++] = c;
		return true;
	}

	/**
	 * Adds the supplied element at the specified index, shifting all elements
	 * currently at that index or higher one to the right. The element, e, can
	 * be a Sector object only.
	 * 
	 * @param index
	 *            the index at which the element is being added
	 * @param e
	 *            the item being added
	 * @throws IndexOutOfBoundsException
	 *             if index &lt; 0 || index &gt; size()
	 */
	public void add(int index, Sector c)
	{
		if (c == null)
			throw new NullPointerException();
		checkBoundInclusive(index);
		modCount++;
		if (size == data.length)
			ensureCapacity(size + 1);
		if (index != size)
			System.arraycopy(data, index, data, index + 1, size - index);
		data[index] = c;
		size++;
	}

	/**
	 * Removes the element at the user-supplied index.
	 * 
	 * @param index
	 *            the index of the element to be removed
	 * @return the removed Sector
	 * @throws IndexOutOfBoundsException
	 *             if index &lt; 0 || index &gt;= size()
	 */
	public Sector remove(int index)
	{
		checkBoundExclusive(index);
		Sector r = data[index];
		modCount++;
		if (index != --size)
			System.arraycopy(data, index + 1, data, index, size - index);
		// Aid for garbage collection by releasing this pointer.
		data[size] = null;
		return r;
	}

	/**
	 * Removes the last element.
	 * 
	 * @return the removed Sectors
	 */
	public Sector removeLast()
	{
		Sector r = data[--size];
		modCount++;
		// Aid for garbage collection by releasing this pointer.
		data[size] = null;
		return r;
	}

	/**
	 * Removes the last elements.
	 * 
	 * @param elements
	 *            The number of elements from the end to remove
	 * @return the removed Sectors
	 */
	public SectorList removeLast(int elements)
	{
		Sector[] r = new Sector[elements];
		int newSize = size - elements;
		System.arraycopy(data, newSize, r, 0, elements);
		modCount++;
		// Aid for garbage collection by releasing this pointer.
		removeRange(newSize, size);
		return new SectorList(r);
	}

	/**
	 * Removes all elements from this List
	 */
	public void clear()
	{
		if (size > 0)
		{
			modCount++;
			// Allow for garbage collection.
			Arrays.fill(data, 0, size, null);
			size = 0;
		}
	}

	public void reverse()
	{
		Collections.reverse(this);
	}

	/**
	 * Add each element in the supplied SectorList to this List. It is undefined
	 * what happens if you modify the list while this is taking place; for
	 * example, if the collection contains this list.
	 * 
	 * @param c
	 *            a Collection containing elements to be added to this List
	 * @return true if the list was modified, in other words c is not empty
	 * @throws NullPointerException
	 *             if c is null
	 */
	public boolean addAll(SectorList c)
	{
		return addAll(size, c);
	}

	/**
	 * Add all elements in the supplied SectorList, inserting them beginning at
	 * the specified index.
	 * 
	 * @param index
	 *            the index at which the elements will be inserted
	 * @param c
	 *            the Collection containing the elements to be inserted
	 * @throws IndexOutOfBoundsException
	 *             if index &lt; 0 || index &gt; 0
	 * @throws NullPointerException
	 *             if c is null
	 */
	public boolean addAll(int index, SectorList c)
	{
		checkBoundInclusive(index);
		Iterator<Sector> itr = c.iterator();
		int csize = c.size();

		modCount++;
		if (csize + size > data.length)
			ensureCapacity(size + csize);
		int end = index + csize;
		if (size > 0 && index != size)
			System.arraycopy(data, index, data, end, size - index);
		size += csize;
		for (; index < end; index++)
			data[index] = itr.next();
		return csize > 0;
	}

	/**
	 * Removes all elements in the half-open interval [fromIndex, toIndex). Does
	 * nothing when toIndex is equal to fromIndex.
	 * 
	 * @param fromIndex
	 *            the first index which will be removed
	 * @param toIndex
	 *            one greater than the last index which will be removed
	 * @throws IndexOutOfBoundsException
	 *             if fromIndex &gt; toIndex
	 */
	protected void removeRange(int fromIndex, int toIndex)
	{
		int change = toIndex - fromIndex;
		if (change > 0)
		{
			modCount++;
			System.arraycopy(data, toIndex, data, fromIndex, size - toIndex);
			size -= change;
		}
		else if (change < 0)
			throw new IndexOutOfBoundsException();
	}

	/**
	 * Checks that the index is in the range of possible elements (inclusive).
	 * 
	 * @param index
	 *            the index to check
	 * @throws IndexOutOfBoundsException
	 *             if index &gt; size
	 */
	private void checkBoundInclusive(int index)
	{
		// Implementation note: we do not check for negative ranges here, since
		// use of a negative index will cause an ArrayIndexOutOfBoundsException,
		// a subclass of the required exception, with no effort on our part.
		if (index > size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	}

	/**
	 * Checks that the index is in the range of existing elements (exclusive).
	 * 
	 * @param index
	 *            the index to check
	 * @throws IndexOutOfBoundsException
	 *             if index &gt;= size
	 */
	private void checkBoundExclusive(int index)
	{
		// Implementation note: we do not check for negative ranges here, since
		// use of a negative index will cause an ArrayIndexOutOfBoundsException,
		// a subclass of the required exception, with no effort on our part.
		if (index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	}

	/**
	 * Remove from this list all elements contained in the given collection.
	 * This is not public, due to Sun's API, but this performs in linear time
	 * while the default behavior of AbstractList would be quadratic.
	 * 
	 * @param c
	 *            the collection to filter out
	 * @return true if this list changed
	 * @throws NullPointerException
	 *             if c is null
	 */
	boolean removeAllInternal(SectorList c)
	{
		int i;
		int j;
		for (i = 0; i < size; i++)
			if (c.contains(data[i]))
				break;
		if (i == size)
			return false;

		modCount++;
		for (j = i++; i < size; i++)
			if (!c.contains(data[i]))
				data[j++] = data[i];
		size -= i - j;
		return true;
	}

	/**
	 * Retain in this vector only the elements contained in the given
	 * collection. This is not public, due to Sun's API, but this performs in
	 * linear time while the default behavior of AbstractList would be
	 * quadratic.
	 * 
	 * @param c
	 *            the collection to filter by
	 * @return true if this vector changed
	 * @throws NullPointerException
	 *             if c is null
	 * @since 1.2
	 */
	boolean retainAllInternal(SectorList c)
	{
		int i;
		int j;
		for (i = 0; i < size; i++)
			if (!c.contains(data[i]))
				break;
		if (i == size)
			return false;

		modCount++;
		for (j = i++; i < size; i++)
			if (c.contains(data[i]))
				data[j++] = data[i];
		size -= i - j;
		return true;
	}
}