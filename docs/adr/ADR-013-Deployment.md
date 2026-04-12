## ADR-013: Deployment

**Status:** accepted  
**Date:** 12/04/2026  

### Context
We want to host and run the bot to make it possible for the general public to use it.

### Considered options
Self-hosting vs. cloud provider.

### Decision outcome
Cloud provider. The bot is hosted and runs on a cloud platform that is directly connected to the Github repository so that every commit automatically triggers a deploy, therefore achieving continuous deployment. The chosen platform offers free deployment options, but as a tradeoff the only option they offer that would be feasible for the bot requires that it waits for connections on a port. Therefore the bot starts an HTTP server that waits on port 10000 and replies with an HTTP status code of 200 and a body of "OK" to any incoming connections.
