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
package org.craftercms.solr.contentType;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.lucene.document.Document;

/**
 * Provides translation of Solr type and mapping capabilities for Crafter Content Type containing a repeating group
 * 
 * This class expects the Crafter Content Type to be persisted in Solr using parallel string arrays for keys and values
 * 
 * @author Zachary Poe
 *
 */
public class RepeatingGroupContentType {
	public static final String FIELD_CONTENT_TYPE = "content-type";

	private final Dictionary<String, String> keyValueMap;

	/**
	 * Constructs a Content Type from a Solr document, validating required document format
	 * @param contentTypeDocument Solr document to build from
	 * @param keyField Key field on Solr document
	 * @param valueField Value field on Solr document
	 */
	public RepeatingGroupContentType(Document contentTypeDocument, String keyField, String valueField) {
		final String[] keys = contentTypeDocument.getValues(keyField),
			values = contentTypeDocument.getValues(valueField);		
		keyValueMap = new Hashtable<>();
		for(int i = 0; i< keys.length; i++) {
			if(i >= values.length) {
				throw new IllegalStateException(String.format("Repeating group Content Type has %s key %s, but no corresponding %s value",
						keyField, keys[i], valueField));
			}
			keyValueMap.put(keys[i], values[i]);
		}
	}

	/**
	 * Retrieves the corresponding repeating group value for the provided key, if found.
	 * Assumes the document is structured in solr using parallel arrays for keys/values
	 * @param key Key to map
	 * @return Content type's value or null if key is not found
	 */
	public String getValue(String key) {
		return keyValueMap.get(key);
	}
}
