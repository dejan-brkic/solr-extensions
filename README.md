# About
This repository contains code to improve the search capabilities from Crafter CMS applications when using the Solr search index.

# (Value Sources) Functions
These value sources are available to configure and use as solr functions.

## TaxonomyValueSource
Maps a taxonomy key to that taxonomy's value as part of the solr query.  
Arguments:
1. internal name of Taxonomy
1. key field (or literal) to map to taxonomy value

## RepeatingGroupContentTypeValueSource
Maps a repeating group from any content type from that repeating group's keys to values as part of the solr query.  
Arguments:
1. Crafter content type
1. field name of unique ID for this content type
1. value of unique ID for this content type
1. key field name for repeating group
1. value field name for repeating group
1. key field (or literal) to map to taxonomy value

# Example Usage
Follow these steps for example setup and usage of these functions.

## Setup
1. Define a Crafter Taxonomy called "Countries"
    1. Add one or more key/value pairs to this taxonomy
1. Define a Crafter Content type called "Asset"
    1. Add a site component data source to the taxonomy created above
    1. Add a metadata dropdown field, called "country_s", which points to this datasource
    1. Create one or more instances of "Asset"
1. Build and deploy this jar to {crafter-home}/bin/solr/server/solr/configsets/crafter_configs/lib
1. Add configuration to these new value sources
    1. Add the dependent solr core jars, if not already present: `<lib dir="${solr.install.dir:../../../..}/dist/" regex="solr-core-\d.*\.jar" />`
    1. Add the function reference and name (as desired) for the taxonomy value source: `<valueSourceParser name="taxonomyValue" class="org.crafter.solr.contentType.taxonomy.TaxonomyValueSourceParser" />`
    1. Add the function reference and name (as desired) for the generic content type value source: `<valueSourceParser name="contentTypeValue" class="org.crafter.solr.contentType.RepeatingGroupContentTypeValueSourceParser" />`

## Querying
-  Execute a query for `content-type: "/component/asset"` and observe country_s shows up as a key string
- Modify the fl for the query above to: `country_s,taxonomyValue("Countries",country_s)` and observe the result documents containing both the key field from the document and the mapped taxonomy value
- Remove the fl query and add `taxonomyValue("Countries",country_s)` to the query sort and observe that the resulting documents are sorted by the taxonomy Country _value_ instead of the key
- The same can be repeated with _any_ Crafter content type containing a repeating group through use of the RepeatingGroupContentTypeValueSourceParser. Use `contentTypeValue("/component/taxonomy","internal-name","Countries","items.item.key","items.item.value",country_s)` to get the same effect against a taxonomy. Alternatively, create a new Crafter content type as a data source for your Asset and use this function appropriately with that new content type.