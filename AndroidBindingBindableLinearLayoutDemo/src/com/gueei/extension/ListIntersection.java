package com.gueei.extension;

import java.util.ArrayList;
import java.util.List;

public class ListIntersection<T> {

	public List<T> Intersection(List<T> A, List<T> B) {
		List<T> listIntersection = new ArrayList<T>();

		if (A == null || A.size() == 0 || B == null || B.size() == 0 ) {
			return listIntersection;
		}

		if (A.size() >= B.size()) {
			for (T value : A) {
				if (B.contains(value)) {
					if (!listIntersection.contains(value))
						listIntersection.add(value);
				}
			}
		} else {
			for (T value : B) {
				if (A.contains(value)) {
					if (!listIntersection.contains(value))
						listIntersection.add(value);
				}
			}
		}

		return listIntersection;
	}
	
	public List<T> NotIn(List<T> A, List<T> B) {
		List<T> listNotIn = new ArrayList<T>();

		if (A == null || A.size() == 0 || B == null || B.size() == 0 ) {
			return listNotIn;
		}

		if (A.size() >= B.size()) {
			for (T value : A) {
				if (!B.contains(value)) {
					if (!listNotIn.contains(value))
						listNotIn.add(value);
				}
			}
		} else {
			for (T value : B) {
				if (!A.contains(value)) {
					if (!listNotIn.contains(value))
						listNotIn.add(value);
				}
			}
		}

		return listNotIn;
	}
	
	
	public List<T> Union(List<T> A, List<T> B) {
		List<T> listUnion = new ArrayList<T>();
				
		if( A != null ) {
			for (T value : A) {
				listUnion.add(value);
			}
		}
		
		if( B != null ) {
			for (T value : B) {
				if (!A.contains(value))
					listUnion.add(value);
			}
		}		

		return listUnion;
	}
}