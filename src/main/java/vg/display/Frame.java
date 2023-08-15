package vg.display;

import javax.swing.*;

public class Frame extends JFrame {

    public Frame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new Panel());
        this.pack();
        this.setVisible(true);
    }

}
