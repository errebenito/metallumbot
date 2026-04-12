## ADR-003: Interaction with Telegram API

**Status:** accepted  
**Date:** 12/04/2026  

### Context
We want to easily interact with the Telegram Bots API.

### Considered options
Existing library vs. custom library vs. direct network calls.

### Decision outcome
Chosen option: to keep things simple it was decided that the existing [TelegramBots](https://github.com/rubenlagus/TelegramBots) library for Java would be used, as it offers good support of the Telegram Bots API features and is actively maintained.
