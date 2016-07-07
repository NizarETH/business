package com.euphor.paperpad.utils;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import com.euphor.paperpad.Beans.Parameters;

/**
 * Created by Euphor on 23/04/2015.
 */
public class Colors {

    private static final GradientDrawable.Orientation DEFAULT_ORIENTATION = GradientDrawable.Orientation.TOP_BOTTOM;
    //the essential colors coming from the database !
    private String foreground_color;
    private String background_color;
    private String title_color;
    private String body_color;
    private String tabs_background_color;
    private String side_tabs_background_color;
    private String side_tabs_foreground_color;
    private String side_tabs_separators_color;
    private String tabs_selected_background_color;
    private String tabs_foreground_color;
    private String tabs_selected_foreground_color;
    private String navigation_background_color;
    private String navigation_foreground_color;
    private String navigation_shadow_color;
    private String pin_color;
    private String cart_button_color;
    private String swipe_color;


    /**to get the color int code from hexadecimal
     * @param color
     * @return
     */
    public int getColor(String color) {
        color = validateColor(color);
        return Color.parseColor("#" + color);
    }


    /**to get the color int code from hexadecimal
     * @param color
     * @param defaut default color to use if given color is not in good format
     * @return
     */
    public int getColorDefault(String color, String defaut, String alpha) {
        color = validateColorDefault(color, defaut);
        return Color.parseColor("#"+alpha+color);
    }
    /**
     * @param color
     * @return
     */
/*	public String validateColor(String color) {
		if (color == null || (color.isEmpty() || color.equals("0")) || color.length() % 2 != 0 ) {
			color = "ffffff";
		}else if (color.length() == 6) {
		}else if (color.length() == 4) {
			color = "00"+color;

		}else if (color.length() == 2) {
			color = "0000"+color;
		}
		return color;
	}*/

    /**
     * @param color
     * @return
     */
    public static String validateColor(String color) {
        if (color == null || (color.isEmpty() || color.equals("0"))) {
            color = "000000";
        }else if (color.length() == 5) {
            color = "0"+color;
        }else if (color.length() == 4) {
            color = "00"+color;

        }else if (color.length() == 3) {
            color = "000"+color;
        }else if (color.length() == 2) {
            color = "0000"+color;
        }
        return color;
    }

    /** Validate the color format if not formated use the default color
     * @param color
     * @return
     */
    public String validateColorDefault(String color , String defaut) {
        if (color == null || (color.isEmpty() || color.equals("0")) || color.length() % 2 != 0 ) {
            color = defaut;
        }else if (color.length() == 6) {
        }else if (color.length() == 4) {
            color = "00"+color;

        }else if (color.length() == 2) {
            color = "0000"+color;
        }
        return color;
    }

    /**to get the color int code from hexadecimal code and the alpha value
     * @param color
     * @return
     */
    public int getColor(String color, String alpha) {
        try {
            if(!validateColor(color).contains(" "))
                return Color.parseColor("#"+alpha+validateColor(color));
            else
                return Color.parseColor("#00000000");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Color.parseColor("#00000000");
    }

    /**
     *
     */
    public Colors() {
        foreground_color = "A89380";
        background_color = "000000";
        title_color = "ffffff";
        body_color = "ffffff";
        tabs_background_color = "000000";
        side_tabs_background_color = "000000";
        side_tabs_foreground_color = "A89380";
        side_tabs_separators_color = "A89380";
        tabs_selected_background_color = "F0E9E4";
        tabs_foreground_color = "A89380";
        tabs_selected_foreground_color = "ffffff";
        navigation_background_color = "000000";
        navigation_foreground_color = "A89380";
        navigation_shadow_color = "000000";
        cart_button_color = "000000";
        swipe_color = "000000";
    }

    /**
     * a constructor that takes the parameters class and extracts all colors.
     */
    public Colors(Parameters parameters) {
        //  public Colors(com.euphor.paperpad.BeansCEP.Parameters parameters) {
        if (parameters != null) {
            foreground_color = (!parameters.getForeground_color().equals("0")) && (isHexColor(parameters.getForeground_color()))?parameters.getForeground_color():"000000";//"A89380";
            background_color = (!parameters.getBackground_color().equals("0")) && (isHexColor(parameters.getBackground_color()))?parameters.getBackground_color():"000000";
            title_color = (!parameters.getTitle_color().equals("0")) && (isHexColor(parameters.getTitle_color()))?parameters.getTitle_color():"000000";
            body_color = (!parameters.getBody_color().equals("0")) && (isHexColor(parameters.getBody_color()))?parameters.getBody_color():"000000";
            tabs_background_color =  (!parameters.getTabs_background_color().equals("0")) && (isHexColor(parameters.getTabs_background_color()))?parameters.getTabs_background_color():"000000";
            side_tabs_background_color =  (!parameters.getSide_tabs_background_color().equals("0")) && (isHexColor(parameters.getSide_tabs_background_color()))?parameters.getSide_tabs_background_color():"000000";
            side_tabs_foreground_color =  (!parameters.getSide_tabs_foreground_color().equals("0")) && (isHexColor(parameters.getSide_tabs_foreground_color()))?parameters.getSide_tabs_foreground_color():"000000";
            side_tabs_separators_color =  (!parameters.getSide_tabs_separators_color().equals("0")) && (isHexColor(parameters.getSide_tabs_separators_color()))?parameters.getSide_tabs_separators_color():"000000";//"A89380";
            tabs_selected_background_color =  (!parameters.getTabs_selected_background_color().equals("0")) && (isHexColor(parameters.getTabs_selected_background_color()))?parameters.getTabs_selected_background_color():"000000";
            tabs_foreground_color =  (!parameters.getTabs_foreground_color().equals("0")) && (isHexColor(parameters.getTabs_foreground_color()))?parameters.getTabs_foreground_color():"000000";
            tabs_selected_foreground_color =  (!parameters.getTabs_selected_foreground_color().equals("0")) && (isHexColor(parameters.getTabs_selected_foreground_color()))?parameters.getTabs_selected_foreground_color():"000000";
            navigation_background_color =  (!parameters.getNavigation_background_color().equals("0")) && (isHexColor(parameters.getNavigation_background_color()))?parameters.getNavigation_background_color():"000000";
            navigation_foreground_color =  (!parameters.getNavigation_foreground_color().equals("0")) && (isHexColor(parameters.getNavigation_foreground_color()))?parameters.getNavigation_foreground_color():"000000";//"A89380";
            navigation_shadow_color =  (!parameters.getNavigation_shadow_color().equals("0")) && (isHexColor(parameters.getNavigation_shadow_color()))?parameters.getNavigation_shadow_color():"000000";
            cart_button_color = (parameters.getCart_button_color()!= null && !parameters.getCart_button_color().equals("0")) && (isHexColor(parameters.getCart_button_color()))?parameters.getCart_button_color():"000000";
        }else {
			/*
			 *  this the default color when the colors aren't specified*/
            foreground_color = "A89380";
            background_color = "000000";
            title_color = "ffffff";
            body_color = "ffffff";
            tabs_background_color = "000000";
            side_tabs_background_color = "000000";
            side_tabs_foreground_color = "A89380";
            side_tabs_separators_color = "A89380";
            tabs_selected_background_color = "F0E9E4";
            tabs_foreground_color = "A89380";
            tabs_selected_foreground_color = "ffffff";
            navigation_background_color = "000000";
            navigation_foreground_color = "A89380";
            navigation_shadow_color = "000000";
            cart_button_color = "000000";
        }
        pin_color = "000000";

    }

    /** Modif Uness changes private mode to public for isHexColor function**/
    public boolean isHexColor(String txt) {
        if (txt.matches("[0-9A-Fa-f]{6}")) {
            return true;
        }else if (txt.matches("[0-9A-Fa-f]{4}")) {
            return true;
        }else if (txt.matches("[0-9A-Fa-f]{2}")) {
            return true;
        }

        return false;
    }
    public Drawable getBackTabsLG() {
        ShapeDrawable greenShape = new ShapeDrawable(new RectShape());
        Shader shader1 = new LinearGradient(0, 0, 50, 0, new int[] {
                getColor(side_tabs_background_color, "BB"), getColor(side_tabs_background_color, "FF")  }, null, Shader.TileMode.CLAMP);
        greenShape.getPaint().setShader(shader1);
        greenShape.getPaint().setStrokeWidth(3);
        greenShape.getPaint().setColor(Color.WHITE);
        greenShape.getPaint().setStyle(Paint.Style.FILL);
        return greenShape;
    }

    /**get the background gradient color
     * @return the background gradient color as {@link GradientDrawable}
     */
    public GradientDrawable getBackPD() {
        GradientDrawable backPd = new GradientDrawable(DEFAULT_ORIENTATION, new int[] {0x00ffffff,0x66000000});
        backPd.setCornerRadius(0f);
        backPd.setColorFilter(getColor(background_color), android.graphics.PorterDuff.Mode.OVERLAY);
        return backPd;
    }

    /** this {@link GradientDrawable} is used in icons and drawings when something is selected
     * it become the background color<b> by default in top to bottom (whiter in top darker in bottom) </b>
     * @return {@link GradientDrawable}
     */
    public GradientDrawable getForePD() {
        GradientDrawable drawable = new GradientDrawable(DEFAULT_ORIENTATION, new int[] {0x00444444,0x66000000});//{0x00ffffff,0x66000000});
        drawable.setCornerRadius(0f);
        drawable.setColorFilter(getColor(foreground_color), android.graphics.PorterDuff.Mode.OVERLAY);
        return drawable;
    }

    /**  Uness Modif **/

    public GradientDrawable makeGradientToColor(String color) {
        GradientDrawable drawable = new GradientDrawable(DEFAULT_ORIENTATION, new int[] {0x44aabbcc,0x44222222});
        drawable.setCornerRadius(0f);
        drawable.setColorFilter(getColor(color), android.graphics.PorterDuff.Mode.OVERLAY);
        return drawable;
    }

    public int getBackMixColor(String color2, float alpha2){
        //int color = 0;

        alpha2 = Math.min( 1.0f, Math.max( 0.0f, alpha2));
        float beta = 1.0f - alpha2;
        float r1, g1, b1, a1, r2, g2, b2, a2;
        int[] selfColor = hex2Rgb(getBackground_color());
        r1 = selfColor[0];  g1 = selfColor[1];  b1 = selfColor[2]; a1 = 1.0f;
        int[] Color2 = hex2Rgb(validateColor(color2));
        r2 = Color2[0];  g2 = Color2[1];  b2 = Color2[2]; a2 = 1.0f;
//	    [self getRed:&r1 green:&g1 blue:&b1 alpha:&a1];
//	    [color2 getRed:&r2 green:&g2 blue:&b2 alpha:&a2];
        int red     = (int)(r1 * beta + r2 * alpha2);
        int green   = (int)(g1 * beta + g2 * alpha2);
        int blue    = (int)(b1 * beta + b2 * alpha2);
        int alpha   = (int)((a1 * beta + a2 * alpha2) * 255);


        return Color.parseColor("#"+decToHex(alpha)+decToHex(red)+decToHex(green)+decToHex(blue));
    }


    public static String decToHex(int dec) {
        int sizeOfIntInHalfBytes = 2;
        int numberOfBitsInAHalfByte = 4;
        int halfByte = 0x0F;
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };

        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
        {
            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= numberOfBitsInAHalfByte;
        }
        return hexBuilder.toString();//.substring(6, 8);
    }

    /** end modif **/

    /** get the navigation bar (for example an actionBar) back color the color is gradient <b> by default in top to bottom (whiter in top darker in bottom) </b>
     * @return  {@link GradientDrawable}
     */
    public GradientDrawable getNavBackPD() {
        GradientDrawable drawable = new GradientDrawable(DEFAULT_ORIENTATION, new int[] {0x00ffffff,0x66000000});
        drawable.setCornerRadius(0f);
        drawable.setColorFilter(getColor(navigation_background_color), android.graphics.PorterDuff.Mode.OVERLAY);
        return drawable;
    }

    /** get the navigation bar (for example an actionBar) foreground color, the color is gradient <b> by default in top to bottom (whiter in top darker in bottom) </b>
     * @return  {@link GradientDrawable}
     */
    public GradientDrawable getNavForePD() {
        GradientDrawable drawable = new GradientDrawable(DEFAULT_ORIENTATION, new int[] {0x00ffffff,0x66000000});
        drawable.setCornerRadius(0f);
        drawable.setColorFilter(getColor(navigation_foreground_color), android.graphics.PorterDuff.Mode.OVERLAY);
        return drawable;
    }

    public GradientDrawable getTabsBackPD(GradientDrawable.Orientation orientation) {
        GradientDrawable drawable = new GradientDrawable(orientation, new int[] {0x00000000,0x33000000});
        drawable.setCornerRadius(0f);
        drawable.setColorFilter(getColor(side_tabs_background_color), android.graphics.PorterDuff.Mode.OVERLAY);
        return drawable;
    }



    /**
     * @return the foreground_color
     */
    public String getForeground_color() {
        return foreground_color;
    }

    /**
     * @param foreground_color the foreground_color to set
     */
    public void setForeground_color(String foreground_color) {
        this.foreground_color = foreground_color;
    }

    /**
     * @return the background_color
     */
    public String getBackground_color() {
        return background_color;
    }

    /**
     * @param background_color the background_color to set
     */
    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    /**
     * @return the title_color
     */
    public String getTitle_color() {
        return title_color;
    }

    /**
     * @param title_color the title_color to set
     */
    public void setTitle_color(String title_color) {
        this.title_color = title_color;
    }

    /**
     * @return the body_color
     */
    public String getBody_color() {
        return body_color;
    }

    /**
     * @param body_color the body_color to set
     */
    public void setBody_color(String body_color) {
        this.body_color = body_color;
    }

    /**
     * @return the tabs_background_color
     */
    public String getTabs_background_color() {
        return tabs_background_color;
    }

    /**
     * @param tabs_background_color the tabs_background_color to set
     */
    public void setTabs_background_color(String tabs_background_color) {
        this.tabs_background_color = tabs_background_color;
    }

    /**
     * @return the side_tabs_background_color
     */
    public String getSide_tabs_background_color() {
        return side_tabs_background_color;
    }

    /**
     * @param side_tabs_background_color the side_tabs_background_color to set
     */
    public void setSide_tabs_background_color(String side_tabs_background_color) {
        this.side_tabs_background_color = side_tabs_background_color;
    }

    /**
     * @return the side_tabs_foreground_color
     */
    public String getSide_tabs_foreground_color() {
        return side_tabs_foreground_color;
    }

    /**
     * @param side_tabs_foreground_color the side_tabs_foreground_color to set
     */
    public void setSide_tabs_foreground_color(String side_tabs_foreground_color) {
        this.side_tabs_foreground_color = side_tabs_foreground_color;
    }

    /**
     * @return the side_tabs_separators_color
     */
    public String getSide_tabs_separators_color() {
        return side_tabs_separators_color;
    }

    /**
     * @param side_tabs_separators_color the side_tabs_separators_color to set
     */
    public void setSide_tabs_separators_color(String side_tabs_separators_color) {
        this.side_tabs_separators_color = side_tabs_separators_color;
    }

    /**
     * @return the tabs_selected_background_color
     */
    public String getTabs_selected_background_color() {
        return tabs_selected_background_color;
    }

    /**
     * @param tabs_selected_background_color the tabs_selected_background_color to set
     */
    public void setTabs_selected_background_color(
            String tabs_selected_background_color) {
        this.tabs_selected_background_color = tabs_selected_background_color;
    }

    /**
     * @return the tabs_foreground_color
     */
    public String getTabs_foreground_color() {
        return tabs_foreground_color;
    }

    /**
     * @param tabs_foreground_color the tabs_foreground_color to set
     */
    public void setTabs_foreground_color(String tabs_foreground_color) {
        this.tabs_foreground_color = tabs_foreground_color;
    }

    /**
     * @return the tabs_selected_foreground_color
     */
    public String getTabs_selected_foreground_color() {
        return tabs_selected_foreground_color;
    }

    /**
     * @param tabs_selected_foreground_color the tabs_selected_foreground_color to set
     */
    public void setTabs_selected_foreground_color(
            String tabs_selected_foreground_color) {
        this.tabs_selected_foreground_color = tabs_selected_foreground_color;
    }

    /**
     * @return the navigation_background_color
     */
    public String getNavigation_background_color() {
        return navigation_background_color;
    }

    /**
     * @param navigation_background_color the navigation_background_color to set
     */
    public void setNavigation_background_color(String navigation_background_color) {
        this.navigation_background_color = navigation_background_color;
    }

    /**
     * @return the navigation_foreground_color
     */
    public String getNavigation_foreground_color() {
        return navigation_foreground_color;
    }

    /**
     * @param navigation_foreground_color the navigation_foreground_color to set
     */
    public void setNavigation_foreground_color(String navigation_foreground_color) {
        this.navigation_foreground_color = navigation_foreground_color;
    }

    /**
     * @return the navigation_shadow_color
     */
    public String getNavigation_shadow_color() {
        return navigation_shadow_color;
    }

    /**
     * @param navigation_shadow_color the navigation_shadow_color to set
     */
    public void setNavigation_shadow_color(String navigation_shadow_color) {
        this.navigation_shadow_color = navigation_shadow_color;
    }

    /**
     * @return the pin_color
     */
    public String getPin_color() {
        return pin_color;
    }

    /**
     * @param pin_color the pin_color to set
     */
    public void setPin_color(String pin_color) {
        this.pin_color = pin_color;
    }

    /**
     *
     * @param colorStr e.g. "RRGGBB"
     * @return
     */
    public static int[] hex2Rgb(String colorStr) {
        return new int[]{
                Integer.valueOf( colorStr.substring( 0, 2 ), 16 ),
                Integer.valueOf( colorStr.substring( 2, 4 ), 16 ),
                Integer.valueOf( colorStr.substring( 4, 6 ), 16 ) };
    }
    public String getCart_button_color() {
        return cart_button_color;
    }
    public void setCart_button_color(String cart_button_color) {
        this.cart_button_color = cart_button_color;
    }
}
