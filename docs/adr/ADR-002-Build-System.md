## ADR-002: Build System

**Status:** accepted  
**Date:** 12/04/2026  

### Context
We want to easily build the bot, manage the required dependencies, and execute tasks such as compilation, testing and packaging.  

### Considered options
The two major players for Java are Maven and Gradle.

### Decision outcome
Chosen option: Maven, because

- Wide ecosystem and community support.
- Stability and maturity.
- Existing familiarity with XML-based configuration over Gradle's DSL.
