package e.ptextarea;


import java.awt.*;

/**
 * A PTextSegment is a PLineSegment which knows how to deal with styled characters.
 * 
 * @author Phil Norman
 */

public class PTextSegment implements PLineSegment {
    private int styleIndex;
    private String text;
    private PTextSegment superSegment;
    
    public PTextSegment(int styleIndex, String text) {
        this.styleIndex = styleIndex;
        this.text = text;
        superSegment = this;
    }
    
    public PTextSegment(int styleIndex, String text, PTextSegment superSegment) {
        this.styleIndex = styleIndex;
        this.text = text;
        this.superSegment = superSegment;
    }
    
    public int getStyleIndex() {
        return styleIndex;
    }
    
    public String getText() {
        return text;
    }
    
    /**
     * Return the PTextSegment that this segment is a subsegment of, or <code>this</code>
     * if this segment isn't a subsegment of another.
     */
    public PTextSegment getSuperSegment() {
        return superSegment;
    }
    
    public PTextSegment subSegment(int start) {
        return new PTextSegment(styleIndex, text.substring(start), getSuperSegment());
    }
    
    public PTextSegment subSegment(int start, int end) {
        return new PTextSegment(styleIndex, text.substring(start, end), getSuperSegment());
    }
    
    public int getLength() {
        return text.length();
    }
    
    public int getDisplayWidth(FontMetrics metrics, int startX) {
        return metrics.stringWidth(text);
    }
    
    public int getDisplayWidth(FontMetrics metrics, int startX, int charOffset) {
        return subSegment(0, charOffset).getDisplayWidth(metrics, startX);
    }
    
    public int getCharOffset(FontMetrics metrics, int startX, int x) {
        char[] ch = text.toCharArray();
        int min = 0;
        int max = ch.length;
        while (max - min > 1) {
            int mid = (min + max) / 2;
            int width = metrics.charsWidth(ch, 0, mid);
            if (width > x - startX) {
                max = mid;
            } else {
                min = mid;
            }
        }
        int charPixelOffset = x - startX - metrics.charsWidth(ch, 0, min);
        if (charPixelOffset > metrics.charWidth(ch[min]) / 2) {
            min++;
        }
        return min;
    }
    
    public void paint(Graphics2D graphics, int x, int yBaseline) {
        graphics.drawString(getText(), x, yBaseline);
    }
    
    public String toString() {
        return "PTextSegment[" + styleIndex + ", " + text + "]";
    }
}
