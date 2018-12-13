package org.crafter.solr.contentType.taxonomy;

import org.apache.lucene.queries.function.ValueSource;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;

/**
 * This value source parser must be configured in solrconfig.xml to provide the TaxonomyValueSource function
 * 
 * Example Configuration:
 * <valueSourceParser name="taxonomyValue" class="org.crafter.solr.contentType.taxonomy.TaxonomyValueSourceParser" />
 * 
 * Example invocation:
 * taxonomyValue("Countries", countryId_s)
 * 
 * @author Zachary Poe
 *
 */
public class TaxonomyValueSourceParser extends ValueSourceParser {

	@Override
	public ValueSource parse(FunctionQParser functionQueryParser) throws SyntaxError {
		return new TaxonomyValueSource(functionQueryParser.parseArg(),
				functionQueryParser.parseValueSource());
	}
}
