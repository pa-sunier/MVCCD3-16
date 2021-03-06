package main.window.console;

import utilities.window.PanelContent;

import javax.swing.*;
import java.awt.*;

public class WinConsoleContent extends PanelContent {
    private JTextArea textArea;


    public WinConsoleContent(WinConsole console) {
        super(console);
        super.setLayout(new FlowLayout(FlowLayout.LEFT));
        textArea = new JTextArea();
        textArea.append(System.lineSeparator());
        textArea.append(System.lineSeparator());
        textArea.append(System.lineSeparator());

        super.addContent(textArea);

    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
