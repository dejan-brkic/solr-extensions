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
