package com.euphor.paperpad.Beans;



import com.euphor.paperpad.utils.JsonSI;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Parameters extends RealmObject {
    @PrimaryKey
    private int id;

    private int account_id;
    private String languages ;
    private RealmList<MyString> list=new RealmList<MyString>();
    private String url;
    private String title;
    private String client;
    private int creation_date;
    private int modification_date;
    private String foreground_color;
    private String background_color;
    private String title_font;
    private String body_font;
    private String title_font_android;
    private String body_font_android;
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
    private String cart_button_color;
    private String currency;

    private boolean show_cart_smartphone;
    private boolean show_intro_video;
    private boolean enable_store_order;
    private boolean show_rating_alert;
    private String shop_url;
    private String navigation_type;
    private String home_type;
    private String navigation_center_icon;
    private String app_icon;
    private String navigation_bar_image;
    private String app_store;
    private String google_play;
    private boolean show_cart;
    private String web_home_test_url;

    public String getWeb_home_test_url() {
        return web_home_test_url;
    }

    public void setWeb_home_test_url(String web_home_test_url) {
        this.web_home_test_url = web_home_test_url;
    }

    public Parameters() {       }

    public Parameters(int id, int account_id, String languages, RealmList<MyString> list, String url, String title, String client, int creation_date, int modification_date, String foreground_color, String background_color, String title_font, String body_font, String title_font_android, String body_font_android, String title_color, String body_color, String tabs_background_color, String side_tabs_background_color, String side_tabs_foreground_color, String side_tabs_separators_color, String tabs_selected_background_color, String tabs_foreground_color, String tabs_selected_foreground_color, String navigation_background_color, String navigation_foreground_color, String navigation_shadow_color, String cart_button_color, String currency, boolean show_cart, boolean show_cart_smartphone, boolean show_intro_video, boolean enable_store_order, boolean show_rating_alert, String shop_url, String navigation_type, String home_type, String navigation_center_icon, String app_icon, String navigation_bar_image, String app_store, String google_play) {
        this.id = id;
        this.account_id = account_id;
        this.languages = languages;
        this.list = list;
        this.url = url;
        this.title = title;
        this.client = client;
        this.creation_date = creation_date;
        this.modification_date = modification_date;
        this.foreground_color = foreground_color;
        this.background_color = background_color;
        this.title_font = title_font;
        this.body_font = body_font;
        this.title_font_android = title_font_android;
        this.body_font_android = body_font_android;
        this.title_color = title_color;
        this.body_color = body_color;
        this.tabs_background_color = tabs_background_color;
        this.side_tabs_background_color = side_tabs_background_color;
        this.side_tabs_foreground_color = side_tabs_foreground_color;
        this.side_tabs_separators_color = side_tabs_separators_color;
        this.tabs_selected_background_color = tabs_selected_background_color;
        this.tabs_foreground_color = tabs_foreground_color;
        this.tabs_selected_foreground_color = tabs_selected_foreground_color;
        this.navigation_background_color = navigation_background_color;
        this.navigation_foreground_color = navigation_foreground_color;
        this.navigation_shadow_color = navigation_shadow_color;
        this.cart_button_color = cart_button_color;
        this.currency = currency;
        this.show_cart = show_cart;
        this.show_cart_smartphone = show_cart_smartphone;
        this.show_intro_video = show_intro_video;
        this.enable_store_order = enable_store_order;
        this.show_rating_alert = show_rating_alert;
        this.shop_url = shop_url;
        this.navigation_type = navigation_type;
        this.home_type = home_type;
        this.navigation_center_icon = navigation_center_icon;
        this.app_icon = app_icon;
        this.navigation_bar_image = navigation_bar_image;
        this.app_store = app_store;
        this.google_play = google_play;
    }


    public void setLanguages(String languages) {

      list= JsonSI.JsonToString(languages);
       this.languages=list.first().getMyString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getLanguages() {
        return getList().first().getMyString();
    }

    public RealmList<MyString> getList() {
        return list;
    }

    public void setList(RealmList<MyString> list) {
        this.list = list;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(int creation_date) {
        this.creation_date = creation_date;
    }

    public int getModification_date() {
        return modification_date;
    }

    public void setModification_date(int modification_date) {
        this.modification_date = modification_date;
    }

    public String getForeground_color() {
        return foreground_color;
    }

    public void setForeground_color(String foreground_color) {
        this.foreground_color = foreground_color;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getTitle_font() {
        return title_font;
    }

    public void setTitle_font(String title_font) {
        this.title_font = title_font;
    }

    public String getBody_font() {
        return body_font;
    }

    public void setBody_font(String body_font) {
        this.body_font = body_font;
    }

    public String getTitle_font_android() {
        return title_font_android;
    }

    public void setTitle_font_android(String title_font_android) {
        this.title_font_android = title_font_android;
    }

    public String getBody_font_android() {
        return body_font_android;
    }

    public void setBody_font_android(String body_font_android) {
        this.body_font_android = body_font_android;
    }

    public String getTitle_color() {
        return title_color;
    }

    public void setTitle_color(String title_color) {
        this.title_color = title_color;
    }

    public String getBody_color() {
        return body_color;
    }

    public void setBody_color(String body_color) {
        this.body_color = body_color;
    }

    public String getTabs_background_color() {
        return tabs_background_color;
    }

    public void setTabs_background_color(String tabs_background_color) {
        this.tabs_background_color = tabs_background_color;
    }

    public String getSide_tabs_background_color() {
        return side_tabs_background_color;
    }

    public void setSide_tabs_background_color(String side_tabs_background_color) {
        this.side_tabs_background_color = side_tabs_background_color;
    }

    public String getSide_tabs_foreground_color() {
        return side_tabs_foreground_color;
    }

    public void setSide_tabs_foreground_color(String side_tabs_foreground_color) {
        this.side_tabs_foreground_color = side_tabs_foreground_color;
    }

    public String getSide_tabs_separators_color() {
        return side_tabs_separators_color;
    }

    public void setSide_tabs_separators_color(String side_tabs_separators_color) {
        this.side_tabs_separators_color = side_tabs_separators_color;
    }

    public String getTabs_selected_background_color() {
        return tabs_selected_background_color;
    }

    public void setTabs_selected_background_color(String tabs_selected_background_color) {
        this.tabs_selected_background_color = tabs_selected_background_color;
    }

    public String getTabs_foreground_color() {
        return tabs_foreground_color;
    }

    public void setTabs_foreground_color(String tabs_foreground_color) {
        this.tabs_foreground_color = tabs_foreground_color;
    }

    public String getTabs_selected_foreground_color() {
        return tabs_selected_foreground_color;
    }

    public void setTabs_selected_foreground_color(String tabs_selected_foreground_color) {
        this.tabs_selected_foreground_color = tabs_selected_foreground_color;
    }

    public String getNavigation_background_color() {
        return navigation_background_color;
    }

    public void setNavigation_background_color(String navigation_background_color) {
        this.navigation_background_color = navigation_background_color;
    }

    public String getNavigation_foreground_color() {
        return navigation_foreground_color;
    }

    public void setNavigation_foreground_color(String navigation_foreground_color) {
        this.navigation_foreground_color = navigation_foreground_color;
    }

    public String getNavigation_shadow_color() {
        return navigation_shadow_color;
    }

    public void setNavigation_shadow_color(String navigation_shadow_color) {
        this.navigation_shadow_color = navigation_shadow_color;
    }

    public String getCart_button_color() {
        return cart_button_color;
    }

    public void setCart_button_color(String cart_button_color) {
        this.cart_button_color = cart_button_color;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isShow_cart_smartphone() {
        return show_cart_smartphone;
    }

    public void setShow_cart_smartphone(boolean show_cart_smartphone) {
        this.show_cart_smartphone = show_cart_smartphone;
    }

    public boolean isShow_intro_video() {
        return show_intro_video;
    }

    public void setShow_intro_video(boolean show_intro_video) {
        this.show_intro_video = show_intro_video;
    }

    public boolean isEnable_store_order() {
        return enable_store_order;
    }

    public void setEnable_store_order(boolean enable_store_order) {
        this.enable_store_order = enable_store_order;
    }

    public boolean isShow_rating_alert() {
        return show_rating_alert;
    }

    public void setShow_rating_alert(boolean show_rating_alert) {
        this.show_rating_alert = show_rating_alert;
    }

    public String getShop_url() {
        return shop_url;
    }

    public void setShop_url(String shop_url) {
        this.shop_url = shop_url;
    }

    public String getNavigation_type() {
        return navigation_type;
    }

    public void setNavigation_type(String navigation_type) {
        this.navigation_type = navigation_type;
    }

    public String getHome_type() {
        return home_type;
    }

    public void setHome_type(String home_type) {
        this.home_type = home_type;
    }

    public String getNavigation_center_icon() {
        return navigation_center_icon;
    }

    public void setNavigation_center_icon(String navigation_center_icon) {
        this.navigation_center_icon = navigation_center_icon;
    }

    public String getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(String app_icon) {
        this.app_icon = app_icon;
    }

    public String getNavigation_bar_image() {
        return navigation_bar_image;
    }

    public void setNavigation_bar_image(String navigation_bar_image) {
        this.navigation_bar_image = navigation_bar_image;
    }

    public String getApp_store() {
        return app_store;
    }

    public void setApp_store(String app_store) {
        this.app_store = app_store;
    }

    public String getGoogle_play() {
        return google_play;
    }

    public void setGoogle_play(String google_play) {
        this.google_play = google_play;
    }

    public boolean isShow_cart() {
        return show_cart;
    }

    public void setShow_cart(boolean show_cart) {
        this.show_cart = show_cart;
    }
}