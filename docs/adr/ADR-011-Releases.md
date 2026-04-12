## ADR-011: Releases

**Status:** accepted  
**Date:** 12/04/2026  

### Context
We want the general public to be able to easily deploy and execute the bot, should they wish to use their own instance. 

### Considered options
Code only vs. JAR releases vs. Docker

### Decision outcome
Chosen option: While releases of the JAR file are provided, a Dockerfile is also included as part of the codebase.
