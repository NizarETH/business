package com.euphor.paperpad.Beans;


import android.util.Log;

import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.utils.Images;
import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.JsonSI;
import com.euphor.paperpad.utils.RelatedItem1;
import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Child_pages extends RealmObject implements CommunElements1, RelatedItem1 {
    @PrimaryKey
    private int id_cp;
    // l'ajout d' id_cp ecrasent toutes les valeurs jusk à la dernière.
    private String ItemTitle1;
    private String ItemIntro1;
    private Illustration ItemIllustration1;
    private String ItemType1;
    private RealmList<RelatedCatIds> RelatedCategories1;
    private int ItemExtra1;
    /****************************/
    private RelatedContactForm relatedContactForm;
    private RelatedLocation relatedLocation;

    /*************************************/


    private String localDate;
    private int id;
    private String day;
    private int category_id;
    private String title;
    private RealmList<MyString> list=new RealmList<MyString>();
    private String body;
    private String intro;
    private String images;
    private String video_path;
    private boolean visible;
    private boolean truncate_boby;
    private boolean show_related_page_prices;
    private boolean smartphone_hide_text;
    private boolean hide_product_detail;
    private RealmList<Price> prices ;
    private Related related;
    private String design;
    private String design_smartphone;
    private Linked linked;
    private String bar_button_title;
    private String bar_button_link;
    private String bar_button_icon;
    private RealmList<MyString> attribute_values = new RealmList<MyString>();
    private Illustration illustration;
    private ExtraField extra_fields;
    private Category category;
    private RealmList<Illustration> imagePages;
    private String auto_open_url;
    private String CommunTitle1;
    private String CommunImagePath1;
    private String CommunDesc1;
    private Illustration illustration1;
    private boolean selected = false;
    private int store_product_id;


    public int getStore_product_id() {
        return store_product_id;
    }

    public void setStore_product_id(int store_product_id) {
        this.store_product_id = store_product_id;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setCommunTitle1(String communTitle1) {
        CommunTitle1 = communTitle1;
    }

    public void setCommunImagePath1(String communImagePath1) {
        CommunImagePath1 = communImagePath1;
    }

    public void setCommunDesc1(String communDesc1) {
        CommunDesc1 = communDesc1;
    }

    public void setIllustration1(Illustration illustration1) {
        this.illustration1 = illustration1;
    }

    public int getId_cp() {
        return id_cp;
    }

    public void setId_cp(int id_cp) {
        this.id_cp = id_cp;
    }

    public String getAuto_open_url() {
        return auto_open_url;
    }

    public void setAuto_open_url(String auto_open_url) {
        this.auto_open_url = auto_open_url;
    }


    public ExtraField getExtra_fields() {
        return extra_fields;
    }

    public void setExtra_fields(ExtraField extra_fields) {
        this.extra_fields = extra_fields;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id_cp= Increment.Primary_cp(id); //Increment.Primary_cp(id);
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImages() {
        return images;
    }

    public RelatedContactForm getRelatedContactForm() {
        return relatedContactForm;
    }

    public RelatedLocation getRelatedLocation() {
        return relatedLocation;
    }

    public void setImages(String images) {

      RealmList<Illustration> illustrations = new RealmList<>();
       list= JsonSI.JsonToStringImage(images);
        //Log.e("images:"+images,"");
         this.images = images;

        if (list.size() != 0) {
            for (MyString l : list) {
              if(l.getMyString() !=null) {
                  try {
                      //Log.e("l.getMyString() :"+l.getMyString(),"");

                      if (!l.getMyString().isEmpty()) {

                          illustration= Utils1.Download_images(l.getMyString(), illustration);

//                          imagePages.add(illustration);
                          imagePages = Images.pages(illustration ,illustrations);

                      }
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
            }
        }

    }

    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {

        this.illustration = illustration;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isTruncate_boby() {
        return truncate_boby;
    }

    public void setTruncate_boby(boolean truncate_boby) {
        this.truncate_boby = truncate_boby;
    }

    public boolean isShow_related_page_prices() {
        return show_related_page_prices;
    }

    public void setShow_related_page_prices(boolean show_related_page_prices) {
        this.show_related_page_prices = show_related_page_prices;
    }

    public boolean isSmartphone_hide_text() {
        return smartphone_hide_text;
    }

    public void setSmartphone_hide_text(boolean smartphone_hide_text) {
        this.smartphone_hide_text = smartphone_hide_text;
    }

    public boolean isHide_product_detail() {
        return hide_product_detail;
    }

    public void setHide_product_detail(boolean hide_product_detail) {
        this.hide_product_detail = hide_product_detail;
    }

    public RealmList<Price> getPrices() {
        return prices;
    }

    public void setPrices(RealmList<Price> prices) {
        this.prices = prices;
    }

    public Related getRelated() {
        return related;
    }

    public void setRelated(Related related) {
        this.related = related;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public String getDesign_smartphone() {
        return design_smartphone;
    }

    public void setDesign_smartphone(String design_smartphone) {
        this.design_smartphone = design_smartphone;
    }

    public Linked getLinked() {
        return linked;
    }

    public void setLinked(Linked linked) {
        this.linked = linked;
    }

    public String getBar_button_title() {
        return bar_button_title;
    }

    public void setBar_button_title(String bar_button_title) {
        this.bar_button_title = bar_button_title;
    }

    public String getBar_button_link() {
        return bar_button_link;
    }

    public void setBar_button_link(String bar_button_link) {
        this.bar_button_link = bar_button_link;
    }

    public String getBar_button_icon() {
        return bar_button_icon;
    }

    public void setBar_button_icon(String bar_button_icon) {
        this.bar_button_icon = bar_button_icon;
    }

    public RealmList<MyString> getAttribute_values() {
        return attribute_values;
    }

    public void setAttribute_values(RealmList<MyString> attribute_values) {
        this.attribute_values = attribute_values;
    }

    public RealmList<MyString> getList() {
        return list;
    }

    public void setList(RealmList<MyString> list) {
        this.list = list;
    }

    public Child_pages() {
    }

    public RealmList<Illustration> getImagePages()
    {
        return imagePages;
    }

    public void setImagePages(RealmList<Illustration> imagePages) {

        this.imagePages = imagePages;
    }

    public Child_pages(int id, String day, int category_id, String title, RealmList<MyString> list, String body, String intro, String images, String video_path, boolean visible, boolean truncate_boby, boolean show_related_page_prices, boolean smartphone_hide_text, boolean hide_product_detail, RealmList<Price> prices, Related related, String design, String design_smartphone, Linked linked, String bar_button_title, String bar_button_link, String bar_button_icon, RealmList<MyString> attribute_values, Illustration illustration, ExtraField extra_fields, Category category) {

        this.id = id;
        this.day = day;
        this.category_id = category_id;
        this.title = title;
        this.list = list;
        this.body = body;
        this.intro = intro;
        this.images = images;
        this.video_path = video_path;
        this.visible = visible;
        this.truncate_boby = truncate_boby;
        this.show_related_page_prices = show_related_page_prices;
        this.smartphone_hide_text = smartphone_hide_text;
        this.hide_product_detail = hide_product_detail;
        this.prices = prices;
        this.related = related;
        this.design = design;
        this.design_smartphone = design_smartphone;
        this.linked = linked;
        this.bar_button_title = bar_button_title;
        this.bar_button_link = bar_button_link;
        this.bar_button_icon = bar_button_icon;
        this.attribute_values = attribute_values;
        this.illustration = illustration;
        this.extra_fields = extra_fields;
        this.category = category;
    }

    public void setItemTitle1(String itemTitle1) {
        ItemTitle1 = itemTitle1;
    }

    public void setItemIntro1(String itemIntro1) {
        ItemIntro1 = itemIntro1;
    }

    public void setItemIllustration1(Illustration itemIllustration1) {
        ItemIllustration1 = itemIllustration1;
    }

    public void setItemType1(String itemType1) {
        ItemType1 = itemType1;
    }

    public void setRelatedCategories1(RealmList<RelatedCatIds> relatedCategories1) {
        RelatedCategories1 = relatedCategories1;
    }

    public void setItemExtra1(int itemExtra1) {
        ItemExtra1 = itemExtra1;
    }

    public void setRelatedContactForm(RelatedContactForm relatedContactForm) {
        this.relatedContactForm = relatedContactForm;
    }

    public void setRelatedLocation(RelatedLocation relatedLocation) {
        this.relatedLocation = relatedLocation;
    }

    @Override
    public String getCommunTitle1() {
        return getTitle();
    }

    @Override
    public String getCommunImagePath1() {
        String path = null;
        if(getIllustration() !=null && !getIllustration().getPath().isEmpty()) {
            path = getIllustration().getPath();
        }
        return path;
    }

    @Override
    public String getCommunDesc1() {
        return getIntro();
    }

    @Override
    public Illustration getIllustration1() {
        return getIllustration();
    }

    @Override
    public String getItemTitle1() {
        return getTitle();
    }

    @Override
    public String getItemIntro1() {
        return getIntro();
    }

    @Override
    public Illustration  getItemIllustration1() {

          return getIllustration();
    }

    @Override
    public String getItemType1() {
        return null;
    }

    @Override
    public RealmList<RelatedCatIds> getRelatedCategories1() {
        return null;
    }

    @Override
    public int getItemExtra1() {
        return 0;
    }




}