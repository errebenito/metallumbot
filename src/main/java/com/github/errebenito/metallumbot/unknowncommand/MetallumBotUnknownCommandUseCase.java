package com.github.errebenito.metallumbot.unknowncommand;

import com.github.errebenito.metallumbot.messaging.MessageSending;

public class MetallumBotUnknownCommandUseCase implements UnknownCommandUseCase {
    static final String USAGE = """
        Usage:
        /band Returns a random band
        /upcoming Returns a random upcoming release""";

    private final MessageSending messageSender;

    public MetallumBotUnknownCommandUseCase(MessageSending messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void sendInstructions(String receiver) {
        messageSender.sendMessage(receiver, USAGE);
    }
}
