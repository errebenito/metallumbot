package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

class SystemCurlExecutorTest {

    @Test
    void shouldReturnOutputWhenExitZero() throws Exception {

        Process fakeProcess = mock(Process.class);

        when(fakeProcess.getInputStream())
            .thenReturn(new ByteArrayInputStream("hello".getBytes()));

        when(fakeProcess.waitFor()).thenReturn(0);

        ProcessStarter fakeStarter = url -> fakeProcess;

        SystemCurlExecutor executor =
            new SystemCurlExecutor(fakeStarter);

        String result = executor.execute("ignored");

        assertEquals("hello", result);
    }

    @Test
    void shouldThrowWhenCurlFails() throws Exception {

        Process fakeProcess = mock(Process.class);

        when(fakeProcess.getInputStream())
            .thenReturn(new ByteArrayInputStream("error".getBytes()));

        when(fakeProcess.waitFor()).thenReturn(7);

        ProcessStarter fakeStarter = url -> fakeProcess;

        SystemCurlExecutor executor =
            new SystemCurlExecutor(fakeStarter);

        IllegalStateException ex = assertThrows(
            IllegalStateException.class,
            () -> executor.execute("ignored")
        );

        assertEquals("curl failed with exit code 7", ex.getMessage());
    }
}


