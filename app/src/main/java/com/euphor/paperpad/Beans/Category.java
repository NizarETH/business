package com.euphor.paperpad.Beans;




import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.utils.Increment;
import com.euphor.paperpad.utils.JsonSI;
import com.euphor.paperpad.utils.RelatedItem1;
import com.euphor.paperpad.utils.Utils1;


import java.io.IOException;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Category extends RealmObject implements CommunElements1, RelatedItem1 {

    @PrimaryKey
    private int id_c;

    private int id;
    //  private int id_category;
 /**************************/

    private  String ItemTitle1;
    private  String ItemIntro1;
    private Illustration ItemIllustration1;
    private String ItemType1;
    private RealmList<RelatedCatIds> RelatedCategories1;
    private int ItemExtra1;
    private RelatedContactForm RelatedContactForm;
    private RelatedLocation RelatedLocation;
 /***************************/
 private String attributes ;

    private RealmList<MyString> list_attributes =new RealmList<MyString>();
    private int cart_parent_category;
    private String thumb;
    private String image;
    private String checkboxes_color;
    private RealmList<Category> categories = new RealmList<Category>();
    private Category ParentCategory;
    private Section section;
    private int location_group_id;
    private String title;
    private String intro;
    private String display_type;
    private String display_type_smartphone;
    private String items_intro;
    private String items_price;
    private String search_option;
    private String alphabetic_index;
    private String share_button;
    private String position;
    private String visible;
    private boolean group_children_categories;
    private String display_gallery_button;
    private RealmList<Category> children_categories=new RealmList<Category>();
    private RealmList<Child_pages> children_pages=new RealmList<Child_pages>();
    private boolean needs_stripe_payment;
    private boolean selected = false;
    private Illustration illustration; //=new Illustration();
    private String parameters;
    private String CommunTitle1;
    private String CommunImagePath1;
    private String CommunDesc1;
    private Illustration illustration1;


    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        list_attributes= JsonSI.JsonToString(attributes);
        this.attributes = attributes;
    }

    public RealmList<MyString> getList_attributes() {
        return list_attributes;
    }

    public void setList_attributes(RealmList<MyString> list_attributes) {
        this.list_attributes = list_attributes;
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
        RelatedContactForm = relatedContactForm;
    }

    public void setRelatedLocation(RelatedLocation relatedLocation) {
        RelatedLocation = relatedLocation;
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

    @Override
    public String getCommunTitle1() {
        return getTitle();
    }

    @Override
    public String getCommunImagePath1() {
        return getThumb();
    }

    @Override
    public String getCommunDesc1() {
        return getIntro();
    }

    @Override
    public Illustration getIllustration1() {
        return getIllustration();
    }
    public int getId_c() {
        return id_c;
    }

    public void setId_c(int id_c) {
        this.id_c = id_c;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id_c= Increment.Primary_c(id);
        this.id = id;
    }

    public RealmList<Child_pages> getChildren_pages() {
        return children_pages;
    }

    public void setChildren_pages(RealmList<Child_pages> children_pages) {
        this.children_pages = children_pages;
    }

    public int getCart_parent_category() {
        return cart_parent_category;
    }

    public void setCart_parent_category(int cart_parent_category) {
        this.cart_parent_category = cart_parent_category;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {

        this.thumb = thumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {


        this.image = image;

       try {

            illustration= Utils1.Download_images(image,illustration);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String getCheckboxes_color() {
        return checkboxes_color;
    }

    public void setCheckboxes_color(String checkboxes_color) {
        this.checkboxes_color = checkboxes_color;
    }

    public RealmList<Category> getCategories() {
        return categories;
    }

    public void setCategories(RealmList<Category> categories) {
        this.categories = categories;
    }

    public Category getParentCategory() {
        return ParentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        ParentCategory = parentCategory;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getLocation_group_id() {
        return location_group_id;
    }

    public void setLocation_group_id(int location_group_id) {
        this.location_group_id = location_group_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(String display_type) {
        this.display_type = display_type;
    }

    public String getDisplay_type_smartphone() {
        return display_type_smartphone;
    }

    public void setDisplay_type_smartphone(String display_type_smartphone) {
        this.display_type_smartphone = display_type_smartphone;
    }

    public String getItems_intro() {
        return items_intro;
    }

    public void setItems_intro(String items_intro) {
        this.items_intro = items_intro;
    }

    public String getItems_price() {
        return items_price;
    }

    public void setItems_price(String items_price) {
        this.items_price = items_price;
    }

    public String getSearch_option() {
        return search_option;
    }

    public void setSearch_option(String search_option) {
        this.search_option = search_option;
    }

    public String getAlphabetic_index() {
        return alphabetic_index;
    }

    public void setAlphabetic_index(String alphabetic_index) {
        this.alphabetic_index = alphabetic_index;
    }

    public String getShare_button() {
        return share_button;
    }

    public void setShare_button(String share_button) {
        this.share_button = share_button;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public boolean isGroup_children_categories() {
        return group_children_categories;
    }

    public void setGroup_children_categories(boolean group_children_categories) {
        this.group_children_categories = group_children_categories;
    }

    public String getDisplay_gallery_button() {
        return display_gallery_button;
    }

    public void setDisplay_gallery_button(String display_gallery_button) {
        this.display_gallery_button = display_gallery_button;
    }

    public RealmList<Category> getChildren_categories() {
        return children_categories;
    }

    public void setChildren_categories(RealmList<Category> children_categories) {
        this.children_categories = children_categories;
    }

    public boolean isNeeds_stripe_payment() {
        return needs_stripe_payment;
    }

    public void setNeeds_stripe_payment(boolean needs_stripe_payment) {
        this.needs_stripe_payment = needs_stripe_payment;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {


        this.illustration = illustration;
    }

    public Category() {
    }

    public Category(int id, int cart_parent_category, String thumb, String image, String checkboxes_color, RealmList<Category> categories, Category parentCategory, Section section, int location_group_id, String title, String intro, String display_type, String display_type_smartphone, String items_intro, String items_price, String search_option, String alphabetic_index, String share_button, String position, String visible, boolean group_children_categories, String display_gallery_button, RealmList<Category> children_categories, RealmList<Child_pages> children_pages, boolean needs_stripe_payment, boolean selected, Illustration illustration) {
        this.id = id;
        this.cart_parent_category = cart_parent_category;
        this.thumb = thumb;
        this.image = image;
        this.checkboxes_color = checkboxes_color;
        this.categories = categories;
        ParentCategory = parentCategory;
        this.section = section;
        this.location_group_id = location_group_id;
        this.title = title;
        this.intro = intro;
        this.display_type = display_type;
        this.display_type_smartphone = display_type_smartphone;
        this.items_intro = items_intro;
        this.items_price = items_price;
        this.search_option = search_option;
        this.alphabetic_index = alphabetic_index;
        this.share_button = share_button;
        this.position = position;
        this.visible = visible;
        this.group_children_categories = group_children_categories;
        this.display_gallery_button = display_gallery_button;
        this.children_categories = children_categories;
        this.children_pages = children_pages;
        this.needs_stripe_payment = needs_stripe_payment;
        this.selected = selected;
        this.illustration = illustration;
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
    public Illustration getItemIllustration1() {
        return getIllustration();
    }

    @Override
    public String getItemType1() {
        return "Category";
    }

    @Override
    public RealmList<RelatedCatIds> getRelatedCategories1() {
        return null;
    }

    @Override
    public int getItemExtra1() {
        return getId_c();
    }

    @Override
    public RelatedContactForm getRelatedContactForm() {
        return null;
    }

    @Override
    public RelatedLocation getRelatedLocation() {
        return null;
    }
}
