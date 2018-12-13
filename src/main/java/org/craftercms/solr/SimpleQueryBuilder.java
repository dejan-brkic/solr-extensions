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

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

/**
 * Helper builder style class for simplifying working with native Lucene queries
 * 
 * @author Zachary Poe
 *
 */
public class SimpleQueryBuilder {
	private final BooleanQuery.Builder builder;
	
	public SimpleQueryBuilder() {
		this.builder = new BooleanQuery.Builder();
	}
	
	/**
	 * Add a boolean "and" condition
	 * @param field Field to match
	 * @param value Value field must contain
	 * @return this builder
	 */
	public SimpleQueryBuilder and(String field, String value) {
		builder.add(new TermQuery(new Term(field, value)), Occur.MUST);
		return this;
	}
	
	public Query build() {
		return builder.build();
	}
}
