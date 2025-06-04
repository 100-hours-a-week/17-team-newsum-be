package com.akatsuki.newsum.common.pagination.model.page;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PageItemList<T> implements List<T> {
	private List<T> items;

	public PageItemList(List<T> items) {
		this.items = items;
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return items.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	@Override
	public Object[] toArray() {
		return items.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return items.toArray(a);
	}

	@Override
	public boolean add(T t) {
		return items.add(t);
	}

	@Override
	public boolean remove(Object o) {
		return items.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return items.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return items.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return items.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return items.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return items.retainAll(c);
	}

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public T get(int index) {
		return items.get(index);
	}

	@Override
	public T set(int index, T element) {
		return items.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		items.add(index, element);
	}

	@Override
	public T remove(int index) {
		return items.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return items.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return items.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return items.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return items.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return items.subList(fromIndex, toIndex);
	}

	public static <T> PageItemList<T> empty() {
		return new PageItemList<>(List.of());
	}

	public static <T> PageItemList<T> of(List<T> result) {
		return new PageItemList(result);
	}
}
