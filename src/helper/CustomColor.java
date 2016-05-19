package helper;

import java.awt.Color;

/**
 * 
 * @author Rick Arts
 * @version 1.2
 * @since 04 june 2015
 * 
 * changes:
 * 
 * v 1.2
 * - Added color green
 * - Added new synonym for darkrRed
 * 
 * v 1.1
 * - Added color darkred
 *
 */
@SuppressWarnings("serial")
public class CustomColor extends Color {

	public static final CustomColor red = new CustomColor(133, 9, 7);
	public static final CustomColor RED = red;
	public static final CustomColor orange = new CustomColor(184, 71, 23);
	public static final CustomColor Orange = orange;
	public static final CustomColor darkRed = new CustomColor(60, 1, 2);
	public static final CustomColor DARK_RED = darkRed;
	public static final CustomColor darkred = darkRed;
	public static final CustomColor green = new CustomColor(140,185,0);
	public static final CustomColor GREEN = green;

	public CustomColor(int r, int g, int b) {
		super(r, g, b);
	}
}
