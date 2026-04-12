## ADR-014: Caching

**Status:** accepted  
**Date:** 12/04/2026  

### Context
The functionality to get a random upcoming album fetches data from [Metal Archives](https://www.metal-archives.com/). We should consider mechanisms to avoid repeatedly fetching the same data, as well as to not put an additional burden on the infrastructure serving the data.

### Considered options
In-memory cache vs. Redis vs. Memcache vs.Ehcache vs. Couchbase.

### Decision outcome
Chosen option: in-memory cache due to its simplicity. The cache stores the entire data for 12 hours, as the size of the data and the rate at which it changes guarantee with enough certainty that a user will rarely, if ever, receive stale data.
