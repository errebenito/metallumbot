# MetallumBot

A simple Telegram bot written in Java to retrieve information from [Metal Archives](https://www.metal-archives.com/)

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/errebenito/metallumbot/build.yml?color=%2300aa00&logo=github) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=alert_status)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=ncloc)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=coverage)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?errebenito_metallumbot) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=bugs)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=code_smells)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=sqale_index)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=security_rating)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=errebenito_metallumbot&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=errebenito_metallumbot)

## Requirements

- Java 17+

- Maven

## Features

- Retrieve the link to a random band's page.

- Retrieve the first 10 upcoming albums from the upcoming albums list.

## Configuration
The bot requires the following two environment variables to exist:

- METALLUM_BOT_TOKEN: Your telegram bot token.

- METALLUM_BOT_NAME: Your telegram bot username.

Additionally, some tests make use of the following environment variable:

- CHAT_ID: The unique ID of the chat where the bot's messages should be sent.

## Building the bot

- Clone this repository .

    `git clone https://github.com/errebenito/metallumbot` or Download this repo as zip and unzip.

- Go to the metallumbot folder.

- In your command line of choice, type the following:

    `mvn clean package` (optionally add `-DskipTests` to skip execution of tests).

This will build metallumbot-X.Y.Z.jar in the target directory, where X.Y.Z is the current version number for the bot.

License
-------
Released under the GNU General Public License version 3 or, at your option, any later version.
See [LICENSE.md][license] for details.

[license]: LICENSE.md
