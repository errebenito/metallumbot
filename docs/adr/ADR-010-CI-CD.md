## ADR-010: CI/CD

**Status:** accepted  
**Date:** 12/04/2026  

### Context
We want each commit to be automatically built, including running the tests, and running various checks to ensure code quality. 

### Considered options
Github Actions vs. Jenkins vs. Travis vs. CircleCI.

### Decision outcome
Chosen option: Github Actions because of the support given that the chosen developer platform is Github, as well as for the many existing integrations with other tools (CodeQL, SonarQube, etc.). The following checks are executed (either via Github Actions or via authorized Github Apps) in addition to building the bot:

- SonarCloud and CodeQL for static analysis.
- Dependency Review, Snyk, OSSF Scorecard and Harden-runner for security.
