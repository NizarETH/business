package com.euphor.paperpad.utils.actionsSummary;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Euphor on 22/05/2015.
 */
public class ActionItem1 {
    /*private Drawable icon;
    private Bitmap thumb;
    private String title;
    private int actionId = -1;
    private boolean selected;
    private boolean sticky;
    private ElementSummary1 element;
    private View view;
    private boolean isTitle;

    public ActionItem1(View view) {
        this(-1, null, null);
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    *//**
     * Constructor
     *
     * @param actionId  Action id for case statements
     * @param title     Title
     * @param icon      Icon to use
     *//*
    public ActionItem1(int actionId, String title, Drawable icon) {
        this.title = title;
        this.icon = icon;
        this.actionId = actionId;
    }

    *//**
     * Constructor
     *//*
    public ActionItem1() {
        this(-1, null, null);
    }

    *//**
     * Constructor
     *
     * @param actionId  Action id of the item
     * @param title     Text to show for the item
     *//*
    public ActionItem1(int actionId, String title) {
        this(actionId, title, null);
    }

    *//**
     * Constructor
     *
     * @param icon {@link Drawable} action icon
     *//*
    public ActionItem1(Drawable icon) {
        this(-1, null, icon);
    }

    *//**
     * Constructor
     *
     * @param actionId  Action ID of item
     * @param icon      {@link Drawable} action icon
     *//*
    public ActionItem1(int actionId, Drawable icon) {
        this(actionId, null, icon);
    }

    *//**
     * Set action title
     *
     * @param title action title
     *//*
    public void setTitle(String title) {
        this.title = title;
    }

    *//**
     * Get action title
     *
     * @return action title
     *//*
    public String getTitle() {
        return this.title;
    }

    *//**
     * Set action icon
     *
     * @param icon {@link Drawable} action icon
     *//*
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    *//**
     * Get action icon
     * @return  {@link Drawable} action icon
     *//*
    public Drawable getIcon() {
        return this.icon;
    }

    *//**
     * Set action id
     *
     * @param actionId  Action id for this action
     *//*
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    *//**
     * @return  Our action id
     *//*
    public int getActionId() {
        return actionId;
    }

    *//**
     * Set sticky status of button
     *
     * @param sticky  true for sticky, pop up sends event but does not disappear
     *//*
    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    *//**
     * @return  true if button is sticky, menu stays visible after press
     *//*
    public boolean isSticky() {
        return sticky;
    }

    *//**
     * Set selected flag;
     *
     * @param selected Flag to indicate the item is selected
     *//*
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    *//**
     * Check if item is selected
     *
     * @return true or false
     *//*
    public boolean isSelected() {
        return this.selected;
    }

    *//**
     * Set thumb
     *
     * @param thumb Thumb image
     *//*
    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    *//**
     * Get thumb image
     *
     * @return Thumb image
     *//*
    public Bitmap getThumb() {
        return this.thumb;
    }

    *//**
     * @param title
     * @param actionId
     * @param page
     *//*
    public ActionItem1(String title, int actionId, ElementSummary1 page) {
        super();
        this.title = title;
        this.actionId = actionId;
        this.element = page;
        this.icon = null;
    }


    public ActionItem1(String title, int actionId, boolean pIsTitle) {
        super();
        this.title = title;
        this.actionId = actionId;
        this.icon = null;
        this.isTitle = pIsTitle;
    }


    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    *//**
     * @return the element
     *//*
    public ElementSummary1 getElement() {
        return element;
    }

    *//**
     * @param element the element to set
     *//*
    public void setElement(ElementSummary1 element) {
        this.element = element;
    }
*/
}