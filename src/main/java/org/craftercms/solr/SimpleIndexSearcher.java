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
package org.craftercms.solr;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

/**
 * Helper class for simplifying working with native Lucene index searches
 * 
 * @author Zachary Poe
 *
 */
public class SimpleIndexSearcher {
	private final IndexSearcher searcher;
	
	public SimpleIndexSearcher(IndexReaderContext reader) {
		this.searcher = new IndexSearcher(getParentContext(reader));
	}

	private IndexReaderContext getParentContext(IndexReaderContext reader) {
		return reader.parent == null ? reader : getParentContext(reader.parent);
	}

	/**
	 * @param query Query to execute
	 * @return First document from results, or null if search returned no results
	 * @throws IOException
	 */
	public Document getFirstResult(Query query) throws IOException {
		ScoreDoc[] results = searcher.search(query, 1).scoreDocs;
		return results.length > 0 ? searcher.getIndexReader().document(results[0].doc) : null;
	}	
}
