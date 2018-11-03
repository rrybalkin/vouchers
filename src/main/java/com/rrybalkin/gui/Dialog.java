package com.rrybalkin.gui;

import com.rrybalkin.utils.Messages;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Window;
import jfx.messagebox.MessageBox;

public class Dialog {
    public static final int YES = 1;
    public static final int NO = 0;

    private static boolean show = true;

    public static void showError(String message) {
        if (show) MessageBox.show(null, message, Messages.get("dialog.error.title"), MessageBox.ICON_ERROR | MessageBox.OK);
    }

    public static void showWarning(String message) {
        if (show) MessageBox.show(null, message, Messages.get("dialog.warn.title"), MessageBox.ICON_WARNING | MessageBox.OK);
    }

    public static void showInfo(String message) {
        if (show) MessageBox.show(null, message, Messages.get("dialog.info.title"), MessageBox.ICON_INFORMATION | MessageBox.OK);
    }

    public static int showQuestion(String message, Event event) {
        Window window = null;
        if (event != null) {
            window = ((Node) event.getTarget()).getScene().getWindow();
        }
        int answer = NO;
        if (show) answer = MessageBox.show(window,
                message, Messages.get("dialog.warn.title"), MessageBox.ICON_QUESTION | MessageBox.YES | MessageBox.NO);

        if (show) return (answer == MessageBox.YES) ? YES : NO;
        else return YES;
    }

    public static void showErrorOnException(Exception ex) {
        showError(Messages.get("dialog.error-happened", ex.getMessage()));
    }
}
