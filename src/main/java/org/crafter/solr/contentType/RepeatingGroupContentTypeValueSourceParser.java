package org.crafter.solr.contentType;

import org.apache.lucene.queries.function.ValueSource;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;

/**
 * This value source parser must be configured in solrconfig.xml to provide the RepeatingGroupContentTypeValueSource function
 * 
 * Example Configuration:
 * <valueSourceParser name="contentTypeValue" class="org.crafter.solr.contentType.RepeatingGroupContentTypeValueSourceParser" />
 * 
 * Example invocation:
 * contentTypeValue("/component/taxonomy", "internal-name", "Countries", "items.item.key", "items.item.value", countryId_s)
 * 
 * @author Zachary Poe
 *
 */
public class RepeatingGroupContentTypeValueSourceParser extends ValueSourceParser {

	@Override
	public ValueSource parse(FunctionQParser functionQueryParser) throws SyntaxError {
		return new RepeatingGroupContentTypeValueSource(functionQueryParser.parseArg(),
				functionQueryParser.parseArg(),
				functionQueryParser.parseArg(),
				functionQueryParser.parseArg(),
				functionQueryParser.parseArg(),
				functionQueryParser.parseValueSource());
	}
}
