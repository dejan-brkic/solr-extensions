package org.crafter.solr;

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
