/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.solr.contentType;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.docvalues.StrDocValues;
import org.apache.lucene.search.SortField;
import org.craftercms.solr.SimpleIndexSearcher;
import org.craftercms.solr.SimpleQueryBuilder;
import org.craftercms.solr.StringValueSourceSortField;

/**
 * Defines a function for mapping any Crafter Content Type containing a Repeating Group from that group's key to its respective value.
 * 
 * When the key is not found, null is returned.
 * 
 * Throws runtime exceptions for invalid function arguments or refernces to an invalid content type.
 * 
 * Invocation requires content type, content type internal name, content type id field, content type id, content type value field, and key to map.
 * For sample usage, see {@link RepeatingGroupContentTypeValueSourceParser}
 * 
 * @author Zachary Poe
 *
 */
public class RepeatingGroupContentTypeValueSource extends ValueSource {
	protected final ValueSource key;
	protected final String contentType, contentTypeUniqueIdField, contentTypeUniqueId, contentTypeKeyField, contentTypeValueField;

	/**
	 * Initialize this value source
	 * @param contentType Content type to reference for mapping
	 * @param contentTypeUniqueIdField Content Type's unique ID field
	 * @param contentTypeUniqueId Content Type's unique ID value
	 * @param contentTypeKeyField Content Type's key field to reference for mapping
	 * @param contentTypeValueField Content Type's value field to reference for mapping
	 * @param keySource Query field source
	 */
	public RepeatingGroupContentTypeValueSource(String contentType, String contentTypeUniqueIdField, String contentTypeUniqueId,
			String contentTypeKeyField, String contentTypeValueField, ValueSource key) {
		if(StringUtils.isBlank(contentType)
				|| StringUtils.isBlank(contentTypeUniqueIdField)
				|| StringUtils.isBlank(contentTypeUniqueId)
				|| StringUtils.isBlank(contentTypeKeyField)
				|| StringUtils.isBlank(contentTypeValueField)
				|| key == null) {
			throw new IllegalArgumentException(String.format("%s requires a non empty value for contentType, contentTypeUniqueKeyField, contentTypeUniqueKey, contentTypeKeyField, contentTypeValueField, and document key",
					getClass().getSimpleName()));
		}
		this.contentType = contentType;
		this.contentTypeUniqueIdField = contentTypeUniqueIdField;
		this.contentTypeUniqueId = contentTypeUniqueId;
		this.contentTypeKeyField = contentTypeKeyField;
		this.contentTypeValueField = contentTypeValueField;
		this.key = key;
	}

	@Override
	public FunctionValues getValues(@SuppressWarnings("rawtypes") Map context, LeafReaderContext readerContext) throws IOException {
	    final FunctionValues keyValues = key.getValues(context, readerContext);
		final RepeatingGroupContentType repeatingGroupContentType = getRepeatingGroupContentType(readerContext);

		return new StrDocValues(this) {
			@Override
			public String strVal(int doc) throws IOException {
				return repeatingGroupContentType.getValue(keyValues.strVal(doc));
			}
		};
	}
	
	/**
	 * Retrieves the repeating group content type so that it can be mapped
	 * @param readerContext Index reader context provided to this function
	 * @return retrieved content type
	 * @throws IOException
	 */
	protected RepeatingGroupContentType getRepeatingGroupContentType(IndexReaderContext readerContext) throws IOException {
		Document repeatingGroupDocument = new SimpleIndexSearcher(readerContext)
				.getFirstResult(new SimpleQueryBuilder()
					.and(RepeatingGroupContentType.FIELD_CONTENT_TYPE, contentType)
					.and(contentTypeUniqueIdField, contentTypeUniqueId)
					.build());
		if(repeatingGroupDocument == null) {
			throw new IllegalArgumentException(String.format("No \"%s\" content type found with %s \"%s\"",
					contentType, contentTypeUniqueIdField, contentTypeUniqueId));
		}
		return new RepeatingGroupContentType(repeatingGroupDocument, contentTypeKeyField, contentTypeValueField);
	}

	/**
	 * Provides sort capabilities for this {@link StrDocValues} based function
	 */
	@Override
	public SortField getSortField(boolean reverse) {
		return new StringValueSourceSortField(this, reverse);
	}

	@Override
	public String description() {
		return new StringBuilder()
	    	.append("contentTypeValue(\"")
	    	.append(contentType)
	    	.append("\",")
	    	.append(contentTypeUniqueIdField)
	    	.append("\",")
	    	.append(contentTypeUniqueId)
	    	.append("\",")
	    	.append(contentTypeKeyField)
	    	.append("\",")
	    	.append(contentTypeValueField)
	    	.append("\",")
	    	.append(key)
	    	.append(")")
	    	.toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(contentType, contentTypeUniqueIdField, contentTypeUniqueId, contentTypeKeyField, contentTypeValueField, key);
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && getClass().isAssignableFrom(other.getClass())) {
			return hashCode() == other.hashCode();
		}
		
		return false;
	}
}
