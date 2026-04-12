## ADR-005: Architecture

**Status:** accepted  
**Date:** 12/04/2026  

### Context
We want an architecture that supports ease of development, adaptability and testability. 

### Considered options
Layered vs. hexagonal.

### Decision outcome
Chosen option: hexagonal architecture with packaging by business concern because

- Explicit separation of concerns-
- Higher cohesion, as all code related to a specific feature or functionality lives in the same folder.
- Ease of refactoring.
- Ease of testing.
