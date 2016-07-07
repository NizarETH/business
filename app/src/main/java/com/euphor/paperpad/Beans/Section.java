 package com.euphor.paperpad.Beans;





 import com.euphor.paperpad.Beans.interactiveMap.map;

 import com.euphor.paperpad.utils.Utils1;
 import com.euphor.paperpad.utils.Increment;
 import java.io.IOException;

 import io.realm.RealmList;
 import io.realm.RealmObject;
 import io.realm.annotations.PrimaryKey;

 public class Section extends RealmObject {

     @PrimaryKey
     private int id_s;

     private int id;


     private Application application;

     private CategoriesMyBox categoriesMyBox;
     private MyBox myBox;
     private String title;
     private String name;
     private String type;
     private String illustration;
     private RealmList<Category> categories = new RealmList<Category>();
     private RealmList<Album> albums = new RealmList<Album>();
     private RealmList<Contact> contacts = new RealmList<Contact>();
     private String root_url;
     private String display_type;
     private boolean show_captions;
     private Center_location center_location;
     private boolean enable_street_view;
     private int default_display_distance;
     private RealmList<Locations_group> locations_groups = new RealmList<Locations_group>();
     private Street_view_default_position street_view_default_position;
     private RealmList<Survey_> surveys = new RealmList<Survey_>();
     private Illustration illustrationObj;
     private RealmList<Event> events = new RealmList<Event>();
     private Parameters_section parameters;
     private boolean show_left_panel;
     private boolean show_itinerary_button;
     private RealmList<Radio> radios = new RealmList<Radio>();

     private RealmList<map> maps = new RealmList<map>();

     public boolean isEnable_street_view() {
         return enable_street_view;
     }

     public Street_view_default_position getStreet_view_default_position() {
         return street_view_default_position;
     }

     public void setStreet_view_default_position(Street_view_default_position street_view_default_position) {
         this.street_view_default_position = street_view_default_position;
     }

     public void setEnable_street_view(boolean enable_street_view) {
         this.enable_street_view = enable_street_view;
     }

     public int getId_s() {
         return id_s;
     }

     public void setId_s(int id_s) {
         this.id_s = id_s;
     }

     private String gallery_design;

     public Application getApplication() {
         return application;
     }

     public void setApplication(Application application) {
         this.application = application;
     }

     public int getId() {
         return id;
     }

     public void setId(int id) {
         id_s= Increment.Primary_s(id);
         this.id = id;
     }



     public CategoriesMyBox getCategoriesMyBox() {
         return categoriesMyBox;
     }

     public void setCategoriesMyBox(CategoriesMyBox categoriesMyBox) {
         this.categoriesMyBox = categoriesMyBox;
     }

     public MyBox getMyBox() {
         return myBox;
     }

     public void setMyBox(MyBox myBox) {
         this.myBox = myBox;
     }

     public String getTitle() {
         return title;
     }

     public void setTitle(String title) {
         this.title = title;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getType() {
         return type;
     }

     public void setType(String type) {
         this.type = type;
     }

     public String getIllustration() {
         return illustration;
     }

     public RealmList<map> getMaps() {
         return maps;
     }

     public void setMaps(RealmList<map> maps) {
         this.maps = maps;
     }

     public void setIllustration(String illustration) {



         this.illustration = illustration;
        try {
            // String  paths= ListUrl.Download_images(illustration);
            illustrationObj= Utils1.Download_images(illustration,illustrationObj);

         } catch (IOException e) {
             e.printStackTrace();
         }
       /* try {
             String  paths= Utils.Download_images(illustration);
             if (!paths.isEmpty() && paths!=null )
             {
                 illustrationObj = new Illustration(id,illustration,paths,"","",true );
                 illustrationObj.setId(id);
                 illustrationObj.setPath(paths);
                 illustrationObj.setLink(illustration);
                 illustrationObj.setDownloaded(true);
                // Utils.Download_images(illustration,illustrationObj);
             }
         } catch (IOException e) {
             e.printStackTrace();
         }*/

     }




     public RealmList<Category> getCategories() {
         return categories;
     }

     public void setCategories(RealmList<Category> categories) {
         this.categories = categories;
     }

     public RealmList<Album> getAlbums() {
         return albums;
     }

     public void setAlbums(RealmList<Album> albums) {
         this.albums = albums;
     }

     public RealmList<Contact> getContacts() {
         return contacts;
     }

     public void setContacts(RealmList<Contact> contacts) {
         this.contacts = contacts;
     }

     public String getRoot_url() {
         return root_url;
     }

     public void setRoot_url(String root_url) {
         this.root_url = root_url;
     }

     public String getDisplay_type() {
         return display_type;
     }

     public void setDisplay_type(String display_type) {
         this.display_type = display_type;
     }

     public boolean isShow_captions() {
         return show_captions;
     }

     public void setShow_captions(boolean show_captions) {
         this.show_captions = show_captions;
     }

     public Center_location getCenter_location() {
         return center_location;
     }

     public void setCenter_location(Center_location center_location) {
         this.center_location = center_location;
     }

     public int getDefault_display_distance() {
         return default_display_distance;
     }

     public void setDefault_display_distance(int default_display_distance) {
         this.default_display_distance = default_display_distance;
     }

     public RealmList<Locations_group> getLocations_groups() {
         return locations_groups;
     }

     public void setLocations_groups(RealmList<Locations_group> locations_groups) {
         this.locations_groups = locations_groups;
     }

     public RealmList<Survey_> getSurveys() {
         return surveys;
     }

     public void setSurveys(RealmList<Survey_> surveys) {
         this.surveys = surveys;
     }

   public Illustration getIllustrationObj() {
         return illustrationObj;
     }

     public void setIllustrationObj(Illustration illustrationObj) {
         this.illustrationObj = illustrationObj;
     }

     public RealmList<Event> getEvents() {
         return events;
     }

     public void setEvents(RealmList<Event> events) {
         this.events = events;
     }

     public Parameters_section getParameters() {
         return parameters;
     }

     public void setParameters(Parameters_section parameters) {
         this.parameters = parameters;
     }

     public boolean isShow_left_panel() {
         return show_left_panel;
     }

     public void setShow_left_panel(boolean show_left_panel) {
         this.show_left_panel = show_left_panel;
     }

     public boolean isShow_itinerary_button() {
         return show_itinerary_button;
     }

     public void setShow_itinerary_button(boolean show_itinerary_button) {
         this.show_itinerary_button = show_itinerary_button;
     }

     public RealmList<Radio> getRadios() {
         return radios;
     }

     public void setRadios(RealmList<Radio> radios) {
         this.radios = radios;
     }

/*     public RealmList<map> getMaps() {
         return maps;
     }

     public void setMaps(RealmList<map> maps) {
         this.maps = maps;
     }*/

     public String getGallery_design() {
         return gallery_design;
     }

     public void setGallery_design(String gallery_design) {
         this.gallery_design = gallery_design;
     }

     public Section() {
     }

     public Section(int id) {
         this.id = id;
     }

     public Section(int id, Application application, CategoriesMyBox categoriesMyBox, MyBox myBox, String title, String name, String type, String illustration, RealmList<Category> categories, RealmList<Album> albums, RealmList<Contact> contacts, String root_url, String display_type, boolean show_captions, Center_location center_location, int default_display_distance, RealmList<Locations_group> locations_groups,  RealmList<Survey_> surveys,/* Illustration illustrationObj,*/ RealmList<Event> events, Parameters_section parameters, boolean show_left_panel, boolean show_itinerary_button, RealmList<Radio> radios, /*RealmList<map> maps,*/ String gallery_design) {
         this.id = id;
         this.application = application;
         this.categoriesMyBox = categoriesMyBox;
         this.myBox = myBox;
         this.title = title;
         this.name = name;
         this.type = type;
         this.illustration = illustration;
         this.categories = categories;
         this.albums = albums;
         this.contacts = contacts;
         this.root_url = root_url;
         this.display_type = display_type;
         this.show_captions = show_captions;
         this.center_location = center_location;
         this.default_display_distance = default_display_distance;
         this.locations_groups = locations_groups;

         this.surveys = surveys;
       //  this.illustrationObj = illustrationObj;
         this.events = events;
         this.parameters = parameters;
         this.show_left_panel = show_left_panel;
         this.show_itinerary_button = show_itinerary_button;
         this.radios = radios;
      /*   this.maps = maps;*/
         this.gallery_design = gallery_design;
     }
 }
