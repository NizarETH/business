package com.euphor.paperpad.Beans;



import com.euphor.paperpad.utils.JsonSI;
import com.euphor.paperpad.utils.Utils1;

import java.io.IOException;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Tile extends RealmObject {

   /*@PrimaryKey*/
   private int id;
	//private int id_tile;

	private Tablet_home_grid tablet_home_grid;

	private Illustration illustration;

	private String title;

	private int category_id;
	private String category_display;

	private int x;

	private int y;
    private int width;

	private int height;
	private int image_id;
	private String image;

	private String images ;
    private RealmList<MyString> list_images= new RealmList<>();

	private String transition;
	private String type;
	private int opacity;
	private int padding;
	private String title_color;

	private boolean zoom_animation_effect;

  /*  public int getId_tile() {
        return id_tile;
    }

    public void setId_tile(int id_tile) {
        this.id_tile = id_tile;
    }
*/

    public void setImages(String images) {
        list_images= JsonSI.JsonToString(images);
        this.images = images;
    }

    public RealmList<MyString> getList_images() {
        return list_images;
    }

    public void setList_images(RealmList<MyString> list_images) {
        this.list_images = list_images;

        if (list_images.size() != 0) {
            for (MyString l : list_images) {
                if(l.getMyString() !=null) {
                    try {

                        if (!l.getMyString().isEmpty()) {

                            illustration= Utils1.Download_images(l.getMyString(),illustration);

                        }
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Tablet_home_grid getTablet_home_grid() {
        return tablet_home_grid;
    }

    public void setTablet_home_grid(Tablet_home_grid tablet_home_grid) {
        this.tablet_home_grid = tablet_home_grid;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_display() {
        return category_display;
    }

    public void setCategory_display(String category_display) {
        this.category_display = category_display;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
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
        /*try {
            //  String  paths= ListUrl.Download_images(image);
            String  paths= Utils1.Download_images(image);
            if (!paths.isEmpty() && paths!=null )
            {
                illustration = new Illustration(id,image,paths,"","",true );

                illustration.setId(id);
                illustration.setPath(paths);
                illustration.setLink(image);
                illustration.setDownloaded(true);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
      /*  try {
            String  paths= Utils.Download_images(image);
            if (!paths.isEmpty() && !image.isEmpty() )
            {
                illustration = new Illustration(id,image,paths,"","",true );
               // Utils.Download_images(image, illustration);
                illustration.setId(id);
                illustration.setPath(paths);
                illustration.setLink(image);
                illustration.setDownloaded(true);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public String getImages() {
        return images;
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public String getTitle_color() {
        return title_color;
    }

    public void setTitle_color(String title_color) {
        this.title_color = title_color;
    }

    public boolean isZoom_animation_effect() {
        return zoom_animation_effect;
    }

    public void setZoom_animation_effect(boolean zoom_animation_effect) {
        this.zoom_animation_effect = zoom_animation_effect;
    }
    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }

    public Tile() {
    }

    public Tile(/*int id_tile,*/ Tablet_home_grid tablet_home_grid, Illustration illustration, String title, int category_id, String category_display, int id, int x, int y, int width, int height, int image_id, String image, String images, String transition, String type, int opacity, int padding, String title_color, boolean zoom_animation_effect) {
       // this.id_tile = id_tile;
        this.tablet_home_grid = tablet_home_grid;
        this.illustration = illustration;
        this.title = title;
        this.category_id = category_id;
        this.category_display = category_display;
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image_id = image_id;
        this.image = image;
        this.images = images;
        this.transition = transition;
        this.type = type;
        this.opacity = opacity;
        this.padding = padding;
        this.title_color = title_color;
        this.zoom_animation_effect = zoom_animation_effect;
    }
}