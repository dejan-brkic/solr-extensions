/*
 * Copyright (C) 2007-2018 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.solr;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SimpleFieldComparator;
import org.apache.lucene.search.SortField;

/**
 * Duplicated directly from {@link ValueSource#getSortField(boolean)}, but using String comparison instead of Double
 * 
 * @author Zachary Poe
 * 
 */
@SuppressWarnings("rawtypes")
public class StringValueSourceSortField extends SortField {
	private final ValueSource parent;

	public StringValueSourceSortField(ValueSource parent, boolean reverse) {
		super(parent.description(), SortField.Type.REWRITEABLE, reverse);
		this.parent = parent;
	}

	@Override
	public SortField rewrite(IndexSearcher searcher) throws IOException {
		return new SortField(getField(), new StringValueSourceComparatorSource(ValueSource.newContext(searcher)), getReverse());
	}

	private class StringValueSourceComparatorSource extends FieldComparatorSource {
		private final Map context;

		public StringValueSourceComparatorSource(Map context) {
			this.context = context;
		}

		@Override
		public FieldComparator<String> newComparator(String fieldname, int numHits, int sortPos, boolean reversed) {
			return new StringValueSourceComparator(context, numHits);
		}
	}

	private class StringValueSourceComparator extends SimpleFieldComparator<String> {
		private final String[] values;
		private FunctionValues docVals;
		private String bottom;
		private final Map fcontext;
		private String topValue;

		StringValueSourceComparator(Map fcontext, int numHits) {
			this.fcontext = fcontext;
			values = new String[numHits];
		}

		@Override
		public int compare(int slot1, int slot2) {
			return compare(values[slot1], values[slot2]);
		}

		@Override
		public int compareBottom(int doc) throws IOException {
			return compare(bottom, docVals.strVal(doc));
		}

		@Override
		public void copy(int slot, int doc) throws IOException {
			values[slot] = docVals.strVal(doc);
		}

		@Override
		public void doSetNextReader(LeafReaderContext context) throws IOException {
			docVals = parent.getValues(fcontext, context);
		}

		@Override
		public void setBottom(final int bottom) {
			this.bottom = values[bottom];
		}

		@Override
		public void setTopValue(final String value) {
			this.topValue = value;
		}

		@Override
		public String value(int slot) {
			return values[slot];
		}

		@Override
		public int compareTop(int doc) throws IOException {
			final String docValue = docVals.strVal(doc);
			return compare(topValue, docValue);
		}

		private int compare(String a, String b) {
			return a == b ? 0 : a == null ? -1 : a.compareTo(b);
		}
	}
}
