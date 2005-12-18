package e.gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * A simple "about box".
 */
public class AboutBox extends JDialog {
    private Icon icon;
    private String title;
    private String version;
    private ArrayList<String> copyrightLines = new ArrayList<String>();
    
    public AboutBox(String title) {
        this.title = title;
    }
    
    public void setImage(String filename) {
        this.icon = new ImageIcon(filename);
    }
    
    public void setVersion(String version, String build) {
        this.version = version;
        if (build != null) {
            version += " (" + build + ")";
        }
    }
    
    /**
     * Adds a line of copyright text. You can add as many as you like. ASCII
     * renditions of the copyright symbol are automatically converted to the
     * real thing.
     */
    public void addCopyright(String copyright) {
        copyrightLines.add(copyright.replaceAll("\\([Cc]\\)", "\u00a9"));
    }
    
    public void makeUi() {
        // FIXME: add GNOME and Win32 implementations.
        makeMacUi();
    }
    
    private void makeMacUi() {
        // http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGWindows/chapter_17_section_5.html#//apple_ref/doc/uid/20000961-TPXREF17
        
        Font titleFont = new Font("Lucida Grande", Font.BOLD, 14);
        Font versionFont = new Font("Lucida Grande", Font.PLAIN, 10);
        Font copyrightFont = new Font("Lucida Grande", Font.PLAIN, 10);
        
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(8, 12, 20, 12));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // FIXME: we need a .icns reader. Given that, we can get the filename automatically from the environment.
        //panel.add(new JLabel(new ImageIcon("/Applications/Utilities/Terminal.app/Contents/Resources/term_icon.icns")));
        
        Dimension spacerSize = new Dimension(1, 8);
        
        if (icon != null) {
            // FIXME: scale images to 64x64 for Mac OS?
            addLabel(panel, new JLabel(icon));
            panel.add(Box.createRigidArea(spacerSize));
            panel.add(Box.createRigidArea(spacerSize));
        }
        
        addLabel(panel, titleFont, title);
        panel.add(Box.createRigidArea(spacerSize));
        
        if (version != null) {
            addLabel(panel, versionFont, version);
            panel.add(Box.createRigidArea(spacerSize));
        }
        
        for (String copyright : copyrightLines) {
            addLabel(panel, copyrightFont, copyright);
        }
        
        setContentPane(panel);
        
        // Set an appropriate size.
        pack();
        // Disable the "maximize" button.
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        // Stop resizing.
        setResizable(false);
        
        // Center on the display.
        // FIXME: use the visual center.
        setLocationRelativeTo(null);
    }
    
    private static void addLabel(JPanel panel, Icon icon) {
        addLabel(panel, new JLabel(icon));
    }
    
    private static void addLabel(JPanel panel, Font font, String text) {
        // FIXME: Mac OS actually uses selectable text components which is handy for copying & pasting version information.
        // FIXME: support HTML and automatically install code to change the mouse cursor when hovering over links, and use BrowserLauncher when a link is clicked?
        JLabel label = new JLabel(text);
        label.setFont(font);
        addLabel(panel, label);
    }
    
    private static void addLabel(JPanel panel, JLabel label) {
        label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(label);
    }
}
