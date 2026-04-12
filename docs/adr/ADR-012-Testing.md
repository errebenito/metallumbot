## ADR-012: Testing

**Status:** accepted  
**Date:** 12/04/2026  

### Context
We want to ensure that the bot works correctly.

### Considered options
Manual vs. automated testing.

### Decision outcome
Automated testing using JUnit. Additionally, to ensure that the tests are meaningfully testing the right things and will detect regressions, mutation testing with PITest is performed. `Main.java` is excluded from mutation testing due to its simplicity. Mocking libraries such as Mockito aer intentionally not used, preferring instead to inject test implementations of any required dependencies when testing a specific behaviour.
