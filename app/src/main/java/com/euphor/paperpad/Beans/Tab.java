package com.euphor.paperpad.Beans;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tab extends RealmObject {

    @PrimaryKey
   private int section_id;
   /* private int id;*/




	private Application application;
	private String title;
	private String icon;
	private int order;
    private Illustration illustration; //=new Illustration();
	private boolean isHomeGrid = false;

    /*public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public Tab(boolean isHomeGrid) {
        this.isHomeGrid = isHomeGrid;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
       /*try {
           if(icon!=null) {
               String paths = ListUrl.Download_images(icon);
               //   String  paths= Utils.Download_images(image);
               if (!paths.isEmpty()) {
                   illustration = new Illustration(section_id, icon, paths, "", "", true);

                   illustration.setId(section_id);
                   illustration.setPath(paths);
                   illustration.setLink(icon);
                   illustration.setDownloaded(true);

               }
           }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public boolean getIsHomeGrid() {
        return isHomeGrid;
    }

    public void setIsHomeGrid(boolean isHomeGrid) {
        this.isHomeGrid = isHomeGrid;
    }

   public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }

    public Tab() {
    }

    public Tab(/*int id,*/ Application application, String title, String icon, int order, int section_id, boolean isHomeGrid) {
      //  this.id = id;
        this.application = application;
        this.title = title;
        this.icon = icon;
        this.order = order;
        this.section_id = section_id;
        this.isHomeGrid = isHomeGrid;
    }
}
