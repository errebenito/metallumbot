## ADR-004: Type of Bot

**Status:** accepted  
**Date:** 12/04/2026  

### Context
Telegram bots can use different mechanisms to check if they have received any messages. Polling bots query the Telegram API for updates, whereas webhook bots set a special URL where the updates are received.

### Considered options
Polling vs. webhook-based bot.

### Decision outcome
Chosen option: polling bot due to its simplicity. This decision has implications on deployment. See [#ADR-013][adr] for details.

[adr]: ADR-013-Deployment.md
