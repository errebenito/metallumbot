## ADR-008: Artifact Versioning

**Status:** accepted  
**Date:** 12/04/2026  

### Context
We want to convey information about the kinds of changes that a particular version of an artifact contains.

### Considered options
Changelog vs. semantic versioning.

### Decision outcome
Chosen option: semantic versioning. While a changelog file may be provided, it is simpler to convey via semantic versioning if a major change, new functionality, or other low risk changes are included in a given release.
