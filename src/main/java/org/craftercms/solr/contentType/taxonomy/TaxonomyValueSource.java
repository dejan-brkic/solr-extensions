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
package org.craftercms.solr.contentType.taxonomy;

import org.apache.lucene.queries.function.ValueSource;
import org.craftercms.solr.contentType.RepeatingGroupContentTypeValueSource;

/**
 * Defines a function for mapping a Crafter Taxonomy key to its respective value.
 * 
 * When the key is not found, null is returned.
 * 
 * Throws runtime exceptions for invalid function arguments or refernces to invalid taxonomies.
 * 
 * Invocation requires taxonomy name, and key to map.
 * For sample usage, see {@link TaxonomyValueSourceParser}
 * 
 * @author Zachary Poe
 *
 */
public class TaxonomyValueSource extends RepeatingGroupContentTypeValueSource {
	private static final String CONTENT_TYPE = "/component/taxonomy";

	private static final String FIELD_INTERNAL_NAME = "internal-name",
			FIELD_ITEM_KEYS = "items.item.key",
			FIELD_ITEM_VALUES = "items.item.value";

	/**
	 * Initialize this value source
	 * @param internalName Taxonomy internal name to reference for mapping
	 * @param keySource Query field source
	 */
	public TaxonomyValueSource(String internalName, ValueSource key) {
		super(CONTENT_TYPE, FIELD_INTERNAL_NAME, internalName, FIELD_ITEM_KEYS, FIELD_ITEM_VALUES, key);
	}

	@Override
	public String description() {
		return new StringBuilder()
	    	.append("taxonomyValue(\"")
	    	.append(contentTypeUniqueId)
	    	.append("\",")
	    	.append(key)
	    	.append(")")
	    	.toString();
	}
}
