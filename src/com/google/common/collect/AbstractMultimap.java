package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

abstract class AbstractMultimap<K, V>
  implements Multimap<K, V>, Serializable
{
  private static final long serialVersionUID = 2447537837011683357L;
  private transient Map<K, Collection<V>> asMap;
  private transient Set<K> keySet;
  private transient Map<K, Collection<V>> map;
  private transient int totalSize;

  protected AbstractMultimap(Map<K, Collection<V>> paramMap)
  {
    Preconditions.checkArgument(paramMap.isEmpty());
    this.map = paramMap;
  }

  private Map<K, Collection<V>> createAsMap()
  {
    SortedMap localSortedMap;
    if ((this.map instanceof SortedMap))
      localSortedMap = (SortedMap)this.map;
    Map localMap;
    for (Object localObject = new SortedAsMap(localSortedMap); ; localObject = new AsMap(localMap))
    {
      return localObject;
      localMap = this.map;
    }
  }

  private Set<K> createKeySet()
  {
    SortedMap localSortedMap;
    if ((this.map instanceof SortedMap))
      localSortedMap = (SortedMap)this.map;
    Map localMap;
    for (Object localObject = new SortedKeySet(localSortedMap); ; localObject = new KeySet(localMap))
    {
      return localObject;
      localMap = this.map;
    }
  }

  private Collection<V> getOrCreateCollection(K paramK)
  {
    Collection localCollection = (Collection)this.map.get(paramK);
    if (localCollection == null)
    {
      localCollection = createCollection(paramK);
      Object localObject = this.map.put(paramK, localCollection);
    }
    return localCollection;
  }

  private Iterator<V> iteratorOrListIterator(Collection<V> paramCollection)
  {
    if ((paramCollection instanceof List));
    for (Object localObject = ((List)paramCollection).listIterator(); ; localObject = paramCollection.iterator())
      return localObject;
  }

  private int removeValuesForKey(Object paramObject)
  {
    int i = 0;
    try
    {
      Collection localCollection = (Collection)this.map.remove(paramObject);
      i = 0;
      if (localCollection != null)
      {
        i = localCollection.size();
        localCollection.clear();
        int j = this.totalSize - i;
        this.totalSize = j;
      }
      label49: return i;
    }
    catch (NullPointerException localNullPointerException)
    {
      break label49;
    }
    catch (ClassCastException localClassCastException)
    {
      break label49;
    }
  }

  private Collection<V> unmodifiableCollectionSubclass(Collection<V> paramCollection)
  {
    Object localObject;
    if ((paramCollection instanceof SortedSet))
      localObject = Collections.unmodifiableSortedSet((SortedSet)paramCollection);
    while (true)
    {
      return localObject;
      if ((paramCollection instanceof Set))
        localObject = Collections.unmodifiableSet((Set)paramCollection);
      else if ((paramCollection instanceof List))
        localObject = Collections.unmodifiableList((List)paramCollection);
      else
        localObject = Collections.unmodifiableCollection(paramCollection);
    }
  }

  private Collection<V> wrapCollection(K paramK, Collection<V> paramCollection)
  {
    Object localObject;
    if ((paramCollection instanceof SortedSet))
    {
      SortedSet localSortedSet = (SortedSet)paramCollection;
      localObject = new WrappedSortedSet(paramK, localSortedSet, null);
    }
    while (true)
    {
      return localObject;
      if ((paramCollection instanceof Set))
      {
        Set localSet = (Set)paramCollection;
        localObject = new WrappedSet(paramK, localSet);
      }
      else if ((paramCollection instanceof List))
      {
        List localList = (List)paramCollection;
        localObject = wrapList(paramK, localList, null);
      }
      else
      {
        localObject = new WrappedCollection(paramK, paramCollection, null);
      }
    }
  }

  private List<V> wrapList(K paramK, List<V> paramList, AbstractMultimap<K, V>.WrappedCollection paramAbstractMultimap)
  {
    if ((paramList instanceof RandomAccess));
    for (Object localObject = new RandomAccessWrappedList(paramK, paramList, paramAbstractMultimap); ; localObject = new WrappedList(paramK, paramList, paramAbstractMultimap))
      return localObject;
  }

  public Map<K, Collection<V>> asMap()
  {
    Map localMap = this.asMap;
    if (localMap == null)
    {
      localMap = createAsMap();
      this.asMap = localMap;
    }
    return localMap;
  }

  public void clear()
  {
    Iterator localIterator = this.map.values().iterator();
    while (localIterator.hasNext())
      ((Collection)localIterator.next()).clear();
    this.map.clear();
    this.totalSize = 0;
  }

  public boolean containsKey(Object paramObject)
  {
    return this.map.containsKey(paramObject);
  }

  abstract Collection<V> createCollection();

  Collection<V> createCollection(K paramK)
  {
    return createCollection();
  }

  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (paramObject == this)
      bool = true;
    while (true)
    {
      return bool;
      if ((paramObject instanceof Multimap))
      {
        Multimap localMultimap = (Multimap)paramObject;
        Map localMap1 = this.map;
        Map localMap2 = localMultimap.asMap();
        bool = localMap1.equals(localMap2);
      }
      else
      {
        bool = false;
      }
    }
  }

  public Collection<V> get(K paramK)
  {
    Collection localCollection = (Collection)this.map.get(paramK);
    if (localCollection == null)
      localCollection = createCollection(paramK);
    return wrapCollection(paramK, localCollection);
  }

  public int hashCode()
  {
    return this.map.hashCode();
  }

  public Set<K> keySet()
  {
    Set localSet = this.keySet;
    if (localSet == null)
    {
      localSet = createKeySet();
      this.keySet = localSet;
    }
    return localSet;
  }

  public boolean put(K paramK, V paramV)
  {
    if (getOrCreateCollection(paramK).add(paramV))
    {
      int i = this.totalSize + 1;
      this.totalSize = i;
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean remove(Object paramObject1, Object paramObject2)
  {
    Collection localCollection = (Collection)this.map.get(paramObject1);
    boolean bool;
    if (localCollection == null)
      bool = false;
    while (true)
    {
      return bool;
      bool = localCollection.remove(paramObject2);
      if (bool)
      {
        int i = this.totalSize + -1;
        this.totalSize = i;
        if (localCollection.isEmpty())
          Object localObject = this.map.remove(paramObject1);
      }
    }
  }

  public Collection<V> removeAll(Object paramObject)
  {
    Collection localCollection1 = (Collection)this.map.remove(paramObject);
    Collection localCollection2 = createCollection();
    if (localCollection1 != null)
    {
      boolean bool = localCollection2.addAll(localCollection1);
      int i = this.totalSize;
      int j = localCollection1.size();
      int k = i - j;
      this.totalSize = k;
      localCollection1.clear();
    }
    return unmodifiableCollectionSubclass(localCollection2);
  }

  final void setMap(Map<K, Collection<V>> paramMap)
  {
    this.map = paramMap;
    this.totalSize = 0;
    Iterator localIterator = paramMap.values().iterator();
    if (!localIterator.hasNext())
      return;
    Collection localCollection = (Collection)localIterator.next();
    if (!localCollection.isEmpty());
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      int i = this.totalSize;
      int j = localCollection.size();
      int k = i + j;
      this.totalSize = k;
      break;
    }
  }

  public String toString()
  {
    return this.map.toString();
  }

  private class SortedAsMap extends AbstractMultimap<K, V>.AsMap
    implements SortedMap<K, Collection<V>>
  {
    SortedSet<K> sortedKeySet;

    SortedAsMap()
    {
      super(localMap);
    }

    public Comparator<? super K> comparator()
    {
      return sortedMap().comparator();
    }

    public K firstKey()
    {
      return sortedMap().firstKey();
    }

    public SortedMap<K, Collection<V>> headMap(K paramK)
    {
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      SortedMap localSortedMap = sortedMap().headMap(paramK);
      return new SortedAsMap(localAbstractMultimap, localSortedMap);
    }

    public SortedSet<K> keySet()
    {
      Object localObject = this.sortedKeySet;
      if (localObject == null)
      {
        AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
        SortedMap localSortedMap = sortedMap();
        localObject = new AbstractMultimap.SortedKeySet(localAbstractMultimap, localSortedMap);
        this.sortedKeySet = ((SortedSet)localObject);
      }
      return localObject;
    }

    public K lastKey()
    {
      return sortedMap().lastKey();
    }

    SortedMap<K, Collection<V>> sortedMap()
    {
      return (SortedMap)this.submap;
    }

    public SortedMap<K, Collection<V>> subMap(K paramK1, K paramK2)
    {
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      SortedMap localSortedMap = sortedMap().subMap(paramK1, paramK2);
      return new SortedAsMap(localAbstractMultimap, localSortedMap);
    }

    public SortedMap<K, Collection<V>> tailMap(K paramK)
    {
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      SortedMap localSortedMap = sortedMap().tailMap(paramK);
      return new SortedAsMap(localAbstractMultimap, localSortedMap);
    }
  }

  private class AsMap extends AbstractMap<K, Collection<V>>
  {
    transient Set<Map.Entry<K, Collection<V>>> entrySet;
    final transient Map<K, Collection<V>> submap;

    AsMap()
    {
      Object localObject;
      this.submap = localObject;
    }

    public void clear()
    {
      Map localMap1 = this.submap;
      Map localMap2 = AbstractMultimap.this.map;
      if (localMap1 == localMap2)
      {
        AbstractMultimap.this.clear();
        return;
      }
      Iterators.clear(new AsMapIterator());
    }

    public boolean containsKey(Object paramObject)
    {
      return Maps.safeContainsKey(this.submap, paramObject);
    }

    public Set<Map.Entry<K, Collection<V>>> entrySet()
    {
      Object localObject = this.entrySet;
      if (localObject == null)
      {
        localObject = new AsMapEntries();
        this.entrySet = ((Set)localObject);
      }
      return localObject;
    }

    public boolean equals(Object paramObject)
    {
      if ((this == paramObject) || (this.submap.equals(paramObject)));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public Collection<V> get(Object paramObject)
    {
      Collection localCollection = (Collection)Maps.safeGet(this.submap, paramObject);
      if (localCollection == null);
      Object localObject2;
      for (Object localObject1 = null; ; localObject1 = AbstractMultimap.this.wrapCollection(localObject2, localCollection))
      {
        return localObject1;
        localObject2 = paramObject;
      }
    }

    public int hashCode()
    {
      return this.submap.hashCode();
    }

    public Set<K> keySet()
    {
      return AbstractMultimap.this.keySet();
    }

    public Collection<V> remove(Object paramObject)
    {
      Collection localCollection = (Collection)this.submap.remove(paramObject);
      Object localObject;
      if (localCollection == null)
        localObject = null;
      while (true)
      {
        return localObject;
        localObject = AbstractMultimap.this.createCollection();
        boolean bool = ((Collection)localObject).addAll(localCollection);
        AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
        int i = localCollection.size();
        int j = AbstractMultimap.access$220(localAbstractMultimap, i);
        localCollection.clear();
      }
    }

    public int size()
    {
      return this.submap.size();
    }

    public String toString()
    {
      return this.submap.toString();
    }

    class AsMapIterator
      implements Iterator<Map.Entry<K, Collection<V>>>
    {
      Collection<V> collection;
      final Iterator<Map.Entry<K, Collection<V>>> delegateIterator;

      AsMapIterator()
      {
        Iterator localIterator = AbstractMultimap.AsMap.this.submap.entrySet().iterator();
        this.delegateIterator = localIterator;
      }

      public boolean hasNext()
      {
        return this.delegateIterator.hasNext();
      }

      public Map.Entry<K, Collection<V>> next()
      {
        Map.Entry localEntry = (Map.Entry)this.delegateIterator.next();
        Object localObject = localEntry.getKey();
        Collection localCollection1 = (Collection)localEntry.getValue();
        this.collection = localCollection1;
        AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
        Collection localCollection2 = this.collection;
        Collection localCollection3 = localAbstractMultimap.wrapCollection(localObject, localCollection2);
        return Maps.immutableEntry(localObject, localCollection3);
      }

      public void remove()
      {
        this.delegateIterator.remove();
        AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
        int i = this.collection.size();
        int j = AbstractMultimap.access$220(localAbstractMultimap, i);
        this.collection.clear();
      }
    }

    class AsMapEntries extends Maps.EntrySet<K, Collection<V>>
    {
      AsMapEntries()
      {
      }

      public boolean contains(Object paramObject)
      {
        return Collections2.safeContains(AbstractMultimap.AsMap.this.submap.entrySet(), paramObject);
      }

      public Iterator<Map.Entry<K, Collection<V>>> iterator()
      {
        AbstractMultimap.AsMap localAsMap = AbstractMultimap.AsMap.this;
        return new AbstractMultimap.AsMap.AsMapIterator(localAsMap);
      }

      Map<K, Collection<V>> map()
      {
        return AbstractMultimap.AsMap.this;
      }

      public boolean remove(Object paramObject)
      {
        if (!contains(paramObject));
        for (boolean bool = false; ; bool = true)
        {
          return bool;
          Map.Entry localEntry = (Map.Entry)paramObject;
          AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
          Object localObject = localEntry.getKey();
          int i = localAbstractMultimap.removeValuesForKey(localObject);
        }
      }
    }
  }

  private class SortedKeySet extends AbstractMultimap<K, V>.KeySet
    implements SortedSet<K>
  {
    SortedKeySet()
    {
      super(localMap);
    }

    public Comparator<? super K> comparator()
    {
      return sortedMap().comparator();
    }

    public K first()
    {
      return sortedMap().firstKey();
    }

    public SortedSet<K> headSet(K paramK)
    {
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      SortedMap localSortedMap = sortedMap().headMap(paramK);
      return new SortedKeySet(localAbstractMultimap, localSortedMap);
    }

    public K last()
    {
      return sortedMap().lastKey();
    }

    SortedMap<K, Collection<V>> sortedMap()
    {
      return (SortedMap)this.subMap;
    }

    public SortedSet<K> subSet(K paramK1, K paramK2)
    {
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      SortedMap localSortedMap = sortedMap().subMap(paramK1, paramK2);
      return new SortedKeySet(localAbstractMultimap, localSortedMap);
    }

    public SortedSet<K> tailSet(K paramK)
    {
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      SortedMap localSortedMap = sortedMap().tailMap(paramK);
      return new SortedKeySet(localAbstractMultimap, localSortedMap);
    }
  }

  private class KeySet extends Maps.KeySet<K, Collection<V>>
  {
    final Map<K, Collection<V>> subMap;

    KeySet()
    {
      Object localObject;
      this.subMap = localObject;
    }

    public void clear()
    {
      Iterators.clear(iterator());
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      return this.subMap.keySet().containsAll(paramCollection);
    }

    public boolean equals(Object paramObject)
    {
      if ((this == paramObject) || (this.subMap.keySet().equals(paramObject)));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public int hashCode()
    {
      return this.subMap.keySet().hashCode();
    }

    public Iterator<K> iterator()
    {
      return new Iterator()
      {
        Map.Entry<K, Collection<V>> entry;
        final Iterator<Map.Entry<K, Collection<V>>> entryIterator;

        public boolean hasNext()
        {
          return this.entryIterator.hasNext();
        }

        public K next()
        {
          Map.Entry localEntry = (Map.Entry)this.entryIterator.next();
          this.entry = localEntry;
          return this.entry.getKey();
        }

        public void remove()
        {
          if (this.entry != null);
          for (boolean bool = true; ; bool = false)
          {
            Preconditions.checkState(bool);
            Collection localCollection = (Collection)this.entry.getValue();
            this.entryIterator.remove();
            AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
            int i = localCollection.size();
            int j = AbstractMultimap.access$220(localAbstractMultimap, i);
            localCollection.clear();
            return;
          }
        }
      };
    }

    Map<K, Collection<V>> map()
    {
      return this.subMap;
    }

    public boolean remove(Object paramObject)
    {
      int i = 0;
      Collection localCollection = (Collection)this.subMap.remove(paramObject);
      if (localCollection != null)
      {
        i = localCollection.size();
        localCollection.clear();
        int j = AbstractMultimap.access$220(AbstractMultimap.this, i);
      }
      if (i > 0);
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }

  private class RandomAccessWrappedList extends AbstractMultimap.WrappedList
    implements RandomAccess
  {
    RandomAccessWrappedList(List<V> paramAbstractMultimap, AbstractMultimap<K, V>.WrappedCollection arg3)
    {
      super(paramAbstractMultimap, localList, localWrappedCollection);
    }
  }

  private class WrappedList extends AbstractMultimap<K, V>.WrappedCollection
    implements List<V>
  {
    WrappedList(List<V> paramAbstractMultimap, AbstractMultimap<K, V>.WrappedCollection arg3)
    {
      super(paramAbstractMultimap, localCollection, localWrappedCollection);
    }

    public void add(int paramInt, V paramV)
    {
      refreshIfEmpty();
      boolean bool = getDelegate().isEmpty();
      getListDelegate().add(paramInt, paramV);
      int i = AbstractMultimap.access$208(AbstractMultimap.this);
      if (!bool)
        return;
      addToMap();
    }

    public boolean addAll(int paramInt, Collection<? extends V> paramCollection)
    {
      boolean bool;
      if (paramCollection.isEmpty())
        bool = false;
      while (true)
      {
        return bool;
        int i = size();
        bool = getListDelegate().addAll(paramInt, paramCollection);
        if (bool)
        {
          int j = getDelegate().size();
          AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
          int k = j - i;
          int m = AbstractMultimap.access$212(localAbstractMultimap, k);
          if (i == 0)
            addToMap();
        }
      }
    }

    public V get(int paramInt)
    {
      refreshIfEmpty();
      return getListDelegate().get(paramInt);
    }

    List<V> getListDelegate()
    {
      return (List)getDelegate();
    }

    public int indexOf(Object paramObject)
    {
      refreshIfEmpty();
      return getListDelegate().indexOf(paramObject);
    }

    public int lastIndexOf(Object paramObject)
    {
      refreshIfEmpty();
      return getListDelegate().lastIndexOf(paramObject);
    }

    public ListIterator<V> listIterator()
    {
      refreshIfEmpty();
      return new WrappedListIterator();
    }

    public ListIterator<V> listIterator(int paramInt)
    {
      refreshIfEmpty();
      return new WrappedListIterator(paramInt);
    }

    public V remove(int paramInt)
    {
      refreshIfEmpty();
      Object localObject = getListDelegate().remove(paramInt);
      int i = AbstractMultimap.access$210(AbstractMultimap.this);
      removeIfEmpty();
      return localObject;
    }

    public V set(int paramInt, V paramV)
    {
      refreshIfEmpty();
      return getListDelegate().set(paramInt, paramV);
    }

    public List<V> subList(int paramInt1, int paramInt2)
    {
      refreshIfEmpty();
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      Object localObject = getKey();
      List localList = getListDelegate().subList(paramInt1, paramInt2);
      if (getAncestor() == null);
      while (true)
      {
        return localAbstractMultimap.wrapList(localObject, localList, this);
        this = getAncestor();
      }
    }

    private class WrappedListIterator extends AbstractMultimap<K, V>.WrappedCollection.WrappedIterator
      implements ListIterator<V>
    {
      WrappedListIterator()
      {
        super();
      }

      public WrappedListIterator(int arg2)
      {
        super(localListIterator);
      }

      private ListIterator<V> getDelegateListIterator()
      {
        return (ListIterator)getDelegateIterator();
      }

      public void add(V paramV)
      {
        boolean bool = AbstractMultimap.WrappedList.this.isEmpty();
        getDelegateListIterator().add(paramV);
        int i = AbstractMultimap.access$208(AbstractMultimap.this);
        if (!bool)
          return;
        AbstractMultimap.WrappedList.this.addToMap();
      }

      public boolean hasPrevious()
      {
        return getDelegateListIterator().hasPrevious();
      }

      public int nextIndex()
      {
        return getDelegateListIterator().nextIndex();
      }

      public V previous()
      {
        return getDelegateListIterator().previous();
      }

      public int previousIndex()
      {
        return getDelegateListIterator().previousIndex();
      }

      public void set(V paramV)
      {
        getDelegateListIterator().set(paramV);
      }
    }
  }

  private class WrappedSortedSet extends AbstractMultimap<K, V>.WrappedCollection
    implements SortedSet<V>
  {
    WrappedSortedSet(SortedSet<V> paramAbstractMultimap, AbstractMultimap<K, V>.WrappedCollection arg3)
    {
      super(paramAbstractMultimap, localCollection, localWrappedCollection);
    }

    public Comparator<? super V> comparator()
    {
      return getSortedSetDelegate().comparator();
    }

    public V first()
    {
      refreshIfEmpty();
      return getSortedSetDelegate().first();
    }

    SortedSet<V> getSortedSetDelegate()
    {
      return (SortedSet)getDelegate();
    }

    public SortedSet<V> headSet(V paramV)
    {
      refreshIfEmpty();
      WrappedSortedSet localWrappedSortedSet = new com/google/common/collect/AbstractMultimap$WrappedSortedSet;
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      Object localObject = getKey();
      SortedSet localSortedSet = getSortedSetDelegate().headSet(paramV);
      if (getAncestor() == null);
      while (true)
      {
        localWrappedSortedSet.<init>(localAbstractMultimap, localObject, localSortedSet, this);
        return localWrappedSortedSet;
        this = getAncestor();
      }
    }

    public V last()
    {
      refreshIfEmpty();
      return getSortedSetDelegate().last();
    }

    public SortedSet<V> subSet(V paramV1, V paramV2)
    {
      refreshIfEmpty();
      WrappedSortedSet localWrappedSortedSet = new com/google/common/collect/AbstractMultimap$WrappedSortedSet;
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      Object localObject = getKey();
      SortedSet localSortedSet = getSortedSetDelegate().subSet(paramV1, paramV2);
      if (getAncestor() == null);
      while (true)
      {
        localWrappedSortedSet.<init>(localAbstractMultimap, localObject, localSortedSet, this);
        return localWrappedSortedSet;
        this = getAncestor();
      }
    }

    public SortedSet<V> tailSet(V paramV)
    {
      refreshIfEmpty();
      WrappedSortedSet localWrappedSortedSet = new com/google/common/collect/AbstractMultimap$WrappedSortedSet;
      AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
      Object localObject = getKey();
      SortedSet localSortedSet = getSortedSetDelegate().tailSet(paramV);
      if (getAncestor() == null);
      while (true)
      {
        localWrappedSortedSet.<init>(localAbstractMultimap, localObject, localSortedSet, this);
        return localWrappedSortedSet;
        this = getAncestor();
      }
    }
  }

  private class WrappedSet extends AbstractMultimap<K, V>.WrappedCollection
    implements Set<V>
  {
    WrappedSet(Set<V> arg2)
    {
      super(localObject, localCollection, null);
    }
  }

  private class WrappedCollection extends AbstractCollection<V>
  {
    final AbstractMultimap<K, V>.WrappedCollection ancestor;
    final Collection<V> ancestorDelegate;
    Collection<V> delegate;
    final K key;

    WrappedCollection(Collection<V> paramAbstractMultimap, AbstractMultimap<K, V>.WrappedCollection arg3)
    {
      this.key = paramAbstractMultimap;
      Object localObject1;
      this.delegate = localObject1;
      Object localObject2;
      this.ancestor = localObject2;
      if (localObject2 == null);
      for (Collection localCollection = null; ; localCollection = localObject2.getDelegate())
      {
        this.ancestorDelegate = localCollection;
        return;
      }
    }

    public boolean add(V paramV)
    {
      refreshIfEmpty();
      boolean bool1 = this.delegate.isEmpty();
      boolean bool2 = this.delegate.add(paramV);
      if (bool2)
      {
        int i = AbstractMultimap.access$208(AbstractMultimap.this);
        if (bool1)
          addToMap();
      }
      return bool2;
    }

    public boolean addAll(Collection<? extends V> paramCollection)
    {
      boolean bool;
      if (paramCollection.isEmpty())
        bool = false;
      while (true)
      {
        return bool;
        int i = size();
        bool = this.delegate.addAll(paramCollection);
        if (bool)
        {
          int j = this.delegate.size();
          AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
          int k = j - i;
          int m = AbstractMultimap.access$212(localAbstractMultimap, k);
          if (i == 0)
            addToMap();
        }
      }
    }

    void addToMap()
    {
      if (this.ancestor != null)
      {
        this.ancestor.addToMap();
        return;
      }
      Map localMap = AbstractMultimap.this.map;
      Object localObject1 = this.key;
      Collection localCollection = this.delegate;
      Object localObject2 = localMap.put(localObject1, localCollection);
    }

    public void clear()
    {
      int i = size();
      if (i == 0)
        return;
      this.delegate.clear();
      int j = AbstractMultimap.access$220(AbstractMultimap.this, i);
      removeIfEmpty();
    }

    public boolean contains(Object paramObject)
    {
      refreshIfEmpty();
      return this.delegate.contains(paramObject);
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      refreshIfEmpty();
      return this.delegate.containsAll(paramCollection);
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this);
      for (boolean bool = true; ; bool = this.delegate.equals(paramObject))
      {
        return bool;
        refreshIfEmpty();
      }
    }

    AbstractMultimap<K, V>.WrappedCollection getAncestor()
    {
      return this.ancestor;
    }

    Collection<V> getDelegate()
    {
      return this.delegate;
    }

    K getKey()
    {
      return this.key;
    }

    public int hashCode()
    {
      refreshIfEmpty();
      return this.delegate.hashCode();
    }

    public Iterator<V> iterator()
    {
      refreshIfEmpty();
      return new WrappedIterator();
    }

    void refreshIfEmpty()
    {
      if (this.ancestor != null)
      {
        this.ancestor.refreshIfEmpty();
        Collection localCollection1 = this.ancestor.getDelegate();
        Collection localCollection2 = this.ancestorDelegate;
        if (localCollection1 == localCollection2)
          return;
        throw new ConcurrentModificationException();
      }
      if (!this.delegate.isEmpty())
        return;
      Map localMap = AbstractMultimap.this.map;
      Object localObject = this.key;
      Collection localCollection3 = (Collection)localMap.get(localObject);
      if (localCollection3 == null)
        return;
      this.delegate = localCollection3;
    }

    public boolean remove(Object paramObject)
    {
      refreshIfEmpty();
      boolean bool = this.delegate.remove(paramObject);
      if (bool)
      {
        int i = AbstractMultimap.access$210(AbstractMultimap.this);
        removeIfEmpty();
      }
      return bool;
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      boolean bool;
      if (paramCollection.isEmpty())
        bool = false;
      while (true)
      {
        return bool;
        int i = size();
        bool = this.delegate.removeAll(paramCollection);
        if (bool)
        {
          int j = this.delegate.size();
          AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
          int k = j - i;
          int m = AbstractMultimap.access$212(localAbstractMultimap, k);
          removeIfEmpty();
        }
      }
    }

    void removeIfEmpty()
    {
      if (this.ancestor != null)
      {
        this.ancestor.removeIfEmpty();
        return;
      }
      if (!this.delegate.isEmpty())
        return;
      Map localMap = AbstractMultimap.this.map;
      Object localObject1 = this.key;
      Object localObject2 = localMap.remove(localObject1);
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      Object localObject = Preconditions.checkNotNull(paramCollection);
      int i = size();
      boolean bool = this.delegate.retainAll(paramCollection);
      if (bool)
      {
        int j = this.delegate.size();
        AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
        int k = j - i;
        int m = AbstractMultimap.access$212(localAbstractMultimap, k);
        removeIfEmpty();
      }
      return bool;
    }

    public int size()
    {
      refreshIfEmpty();
      return this.delegate.size();
    }

    public String toString()
    {
      refreshIfEmpty();
      return this.delegate.toString();
    }

    class WrappedIterator
      implements Iterator<V>
    {
      final Iterator<V> delegateIterator;
      final Collection<V> originalDelegate;

      WrappedIterator()
      {
        Collection localCollection1 = AbstractMultimap.WrappedCollection.this.delegate;
        this.originalDelegate = localCollection1;
        AbstractMultimap localAbstractMultimap = AbstractMultimap.this;
        Collection localCollection2 = AbstractMultimap.WrappedCollection.this.delegate;
        Iterator localIterator = localAbstractMultimap.iteratorOrListIterator(localCollection2);
        this.delegateIterator = localIterator;
      }

      WrappedIterator()
      {
        Collection localCollection = AbstractMultimap.WrappedCollection.this.delegate;
        this.originalDelegate = localCollection;
        Object localObject;
        this.delegateIterator = localObject;
      }

      Iterator<V> getDelegateIterator()
      {
        validateIterator();
        return this.delegateIterator;
      }

      public boolean hasNext()
      {
        validateIterator();
        return this.delegateIterator.hasNext();
      }

      public V next()
      {
        validateIterator();
        return this.delegateIterator.next();
      }

      public void remove()
      {
        this.delegateIterator.remove();
        int i = AbstractMultimap.access$210(AbstractMultimap.this);
        AbstractMultimap.WrappedCollection.this.removeIfEmpty();
      }

      void validateIterator()
      {
        AbstractMultimap.WrappedCollection.this.refreshIfEmpty();
        Collection localCollection1 = AbstractMultimap.WrappedCollection.this.delegate;
        Collection localCollection2 = this.originalDelegate;
        if (localCollection1 == localCollection2)
          return;
        throw new ConcurrentModificationException();
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractMultimap
 * JD-Core Version:    0.6.2
 */