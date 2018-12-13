package org.crafter.solr;

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
		this.searcher = new IndexSearcher(getParnetContext(reader));
	}

	private IndexReaderContext getParnetContext(IndexReaderContext reader) {
		return reader.parent == null ? reader : getParnetContext(reader.parent);
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
