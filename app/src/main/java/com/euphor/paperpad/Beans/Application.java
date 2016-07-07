package com.euphor.paperpad.Beans;



import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Application extends RealmObject {

   @PrimaryKey
	private int id;
	


	private Parameters parameters;
	private Cart cart;
	private Home_swipe home_swipe;

	private RealmList<Section> sections = new RealmList<Section>();
	private RealmList<Tab> tabs = new RealmList<Tab>();
	private Tablet_home_grid tablet_home_grid;
	private Phone_home_grid phone_home_grid;
	private RealmList<AgendaGroup> agenda_groups = new RealmList<AgendaGroup>();
    private RealmList<MyString> attributes=new RealmList<MyString>();
    private RealmList<MyString> wine_maps =new RealmList<MyString>();
   // private RealmList<MyString> agenda_groups=new RealmList<MyString>();




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Home_swipe getHome_swipe() {
        return home_swipe;
    }

    public void setHome_swipe(Home_swipe home_swipe) {
        this.home_swipe = home_swipe;
    }

    public RealmList<Section> getSections() {
        return sections;
    }

    public void setSections(RealmList<Section> sections) {
        this.sections = sections;
    }

    public RealmList<Tab> getTabs() {
        return tabs;
    }

    public void setTabs(RealmList<Tab> tabs) {
        this.tabs = tabs;
    }

    public Tablet_home_grid getTablet_home_grid() {
        return tablet_home_grid;
    }

    public void setTablet_home_grid(Tablet_home_grid tablet_home_grid) {
        this.tablet_home_grid = tablet_home_grid;
    }

    public Phone_home_grid getPhone_home_grid() {
        return phone_home_grid;
    }

    public void setPhone_home_grid(Phone_home_grid phone_home_grid) {
        this.phone_home_grid = phone_home_grid;
    }



    public RealmList<MyString> getAttributes() {
        return attributes;
    }

    public void setAttributes(RealmList<MyString> attributes) {
        this.attributes = attributes;
    }

    public RealmList<MyString> getWine_maps() {
        return wine_maps;
    }

    public void setWine_maps(RealmList<MyString> wine_maps) {
        this.wine_maps = wine_maps;
    }

    public RealmList<AgendaGroup> getAgenda_groups() {
        return agenda_groups;
    }

    public void setAgenda_groups(RealmList<AgendaGroup> agenda_groups) {
        this.agenda_groups = agenda_groups;
    }

    public Application() {    }

    public Application(int id, Parameters parameters, Cart cart, Home_swipe home_swipe, RealmList<Section> sections, RealmList<Tab> tabs, Tablet_home_grid tablet_home_grid, Phone_home_grid phone_home_grid, RealmList<AgendaGroup> agendaGroups, RealmList<MyString> attributes, RealmList<MyString> wine_maps, RealmList<AgendaGroup> agenda_groups) {
        this.id = id;
        this.parameters = parameters;
        this.cart = cart;
        this.home_swipe = home_swipe;
        this.sections = sections;
        this.tabs = tabs;
        this.tablet_home_grid = tablet_home_grid;
        this.phone_home_grid = phone_home_grid;
        this.agenda_groups = agendaGroups;
        this.attributes = attributes;
        this.wine_maps = wine_maps;
        this.agenda_groups = agenda_groups;
    }
}
