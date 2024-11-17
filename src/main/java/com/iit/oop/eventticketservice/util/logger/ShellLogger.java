package com.iit.oop.eventticketservice.util.logger;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
public class ShellLogger {

    private final Terminal terminal;

    public ShellLogger(Terminal terminal) {
        this.terminal = terminal;
    }

    public void info(String message) {
        print(message, AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
    }

    public void usageInfo(String message) {
        print(message, AttributedStyle.BOLD.foreground(AttributedStyle.CYAN));
    }

    public void warn(String message) {
        print(message, AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }

    public void error(String message) {
        print(message, AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
    }

    public void debug(String message) {
        print(message, AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
    }

    private void print(String message, AttributedStyle style) {
        AttributedString attributedString = new AttributedString(message, style);
        terminal.writer().println(attributedString.toAnsi());
        terminal.flush();
    }
}

