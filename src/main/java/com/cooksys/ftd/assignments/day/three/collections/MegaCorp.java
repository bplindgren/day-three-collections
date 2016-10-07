package com.cooksys.ftd.assignments.day.three.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cooksys.ftd.assignments.day.three.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.day.three.collections.model.Capitalist;
import com.cooksys.ftd.assignments.day.three.collections.model.FatCat;
import com.cooksys.ftd.assignments.day.three.collections.model.WageSlave;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

// By extending a Hierarchy, we are implementing a kind of collection
public class MegaCorp implements Hierarchy<Capitalist, FatCat> {

	private Map<FatCat, Set<Capitalist>> company = new HashMap<FatCat, Set<Capitalist>>();

	/**
	 * Adds a given element to the hierarchy.
	 * <p>
	 * If the given element is already present in the hierarchy, do not add it
	 * and return false
	 * <p>
	 * If the given element has a parent and the parent is not part of the
	 * hierarchy, add the parent and then add the given element
	 * <p>
	 * If the given element has no parent but is a Parent itself, add it to the
	 * hierarchy
	 * <p>
	 * If the given element has no parent and is not a Parent itself, do not add
	 * it and return false
	 *
	 * @param capitalist
	 *            the element to add to the hierarchy
	 * @return true if the element was added successfully, false otherwise
	 */
	@Override
	public boolean add(Capitalist capitalist) {
		// if the capitalist == null or the HashMap already has the capitalist,
		// return false
		if (capitalist == null || has(capitalist)) {
			return false;
		}

		// if the capitalist has a parent
		if (capitalist.hasParent()) {
			// Get the parent
			FatCat parent = capitalist.getParent();

			// If the company already has the parent, add the capitalist to the parent's children
			if (has(parent)) {
				getChildren(parent).add(capitalist);
			} else { // else, add the parent to the company
				add(parent);
			}

			// if the capitalist is an instance of FatCat, add it to the company
			// with an empty set
			if (capitalist instanceof FatCat) {
				company.put((FatCat) capitalist, new HashSet<Capitalist>());
			}

			// if the capitalist is not an instance of FatCat, add the
			// capitalist to its parent's set
			company.get(parent).add(capitalist);
			return true;

		} else { // if the capitalist doesn't have a parent
			// if the capitalist is a FatCat
			if (capitalist instanceof FatCat) {
				// Add it to the company
				company.put((FatCat) capitalist, new HashSet<Capitalist>());
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * @param capitalist
	 *            the element to search for
	 * @return true if the element has been added to the hierarchy, false
	 *         otherwise
	 */
	@Override
	public boolean has(Capitalist capitalist) {
		// if the capitalist is a key in the company
		if (company.containsKey(capitalist)) {
			return true;
		} else { // for each key in the company...
			for (FatCat cat : company.keySet()) {
				// if the capitalist is in the set, return true
				if (company.get(cat).contains(capitalist)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return all elements in the hierarchy, or an empty set if no elements
	 *         have been added to the hierarchy
	 */
	@Override
	public Set<Capitalist> getElements() {
		// create an empty set of Capitalist elements
		Set<Capitalist> elements = new HashSet<Capitalist>();
		// for in the the company keys
		for (Capitalist cap : company.keySet()) {
			// add the key to the elements
			elements.add(cap);
			// add all capitalists from the set to elements
			Set<Capitalist> caps = company.get(cap);
			elements.addAll(caps);
		}
		return elements;
	}

	/**
	 * @return all parent elements in the hierarchy, or an empty set if no
	 *         parents have been added to the hierarchy
	 */
	@Override
	public Set<FatCat> getParents() {
		Set<FatCat> parents = company.keySet();
		if (parents.isEmpty()) {
			Set<FatCat> empty = new HashSet<FatCat>();
			return empty;
		} else {
			return parents;
		}
	}

	/**
	 * @param fatCat
	 *            the parent whose children need to be returned
	 * @return all elements in the hierarchy that have the given parent as a
	 *         direct parent, or an empty set if the parent is not present in
	 *         the hierarchy or if there are no children for the given parent
	 */
	@Override
	public Set<Capitalist> getChildren(FatCat fatCat) {
		// if the company doesn't have the FatCat or the set is empty, return empty set
		if (!company.containsKey(fatCat) || company.get(fatCat).isEmpty()) {
			return new HashSet<Capitalist>();
		} else { // return the fatCat's set
			return (company.get(fatCat));
		}
	}

	/**
	 * @return a map in which the keys represent the parent elements in the
	 *         hierarchy, and the each value is a set of the direct children of
	 *         the associate parent, or an empty map if the hierarchy is empty.
	 */
	@Override
	public Map<FatCat, Set<Capitalist>> getHierarchy() {
		// Create a new hierarchy
		Map<FatCat, Set<Capitalist>> hierarchy = new HashMap<FatCat, Set<Capitalist>>();
		// Get the parents of the hierarchy
		Set<FatCat> keys = getParents();
		for (FatCat key : keys) {
			// get the key's children and add the pair to the hierarchy
			Set<Capitalist> children = getChildren(key);
			hierarchy.put(key, children);
		}
		return hierarchy;
	}

	/**
	 * @param capitalist
	 * @return the parent chain of the given element, starting with its direct
	 *         parent, then its parent's parent, etc, or an empty list if the
	 *         given element has no parent or if its parent is not in the
	 *         hierarchy
	 */
	@Override
	public List<FatCat> getParentChain(Capitalist capitalist) {
		// Create new ArrayList
		List<FatCat> parentChain = new ArrayList<FatCat>();
		if (capitalist == null) {
			return parentChain;
		}
		// If THIS capitalist has a parent...
		while (capitalist.hasParent()) {
			// Get the parent
			FatCat parent = capitalist.getParent();
			// If the parent is already in the company 
			if (has(parent)) {
				// Add the parent to the chain
				parentChain.add(parent);
			}
			// Set capitalist = parent to find this parent's parent
			capitalist = parent;
		}
		return parentChain;
	}

}
