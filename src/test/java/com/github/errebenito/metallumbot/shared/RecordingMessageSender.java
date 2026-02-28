package com.github.errebenito.metallumbot.shared;

import com.github.errebenito.metallumbot.messaging.MessageSending;

public class RecordingMessageSender implements MessageSending {

    public String receiver;
    public String message;
    public boolean called = false;

    @Override
    public void sendMessage(String receiver, String message) {
        this.called = true;
        this.receiver = receiver;
        this.message = message;
    }
}
