<center>![image alt text](http://git.paperpad.fr/android-projects/opbusiness/raw/master/Docs/screenshots/image_0.png) </center>

## <center> **Business Application**</center>

# **Business Android : documentation technique**

## **Configuration requise**

* JRE 1.6.0 ou supérieur

* Android Studio 1.2.1 ou supérieur

* SDK Android 22

* Gradle system version 1.2.3

## **Fichier de configuration de l'app**

L'application télécharge les données en JSON formatté.

Exemple de fichier :[http://backoffice.paperpad.fr/api/application/compiled_app/id/476/lang/fr](http://backoffice.paperpad.fr/api/application/compiled_app/id/476/lang/fr).

## **Dépendances**


Les dépendances sont gérées par **Gradle**. Les dépendances sont importées depuis des dépôt Git, avec des chemins SSH. Le client SSH du Mac doit être correctement configuré pour pouvoir se connecter au dépôt (voir ce[ tutoriel](https://help.github.com/articles/generating-ssh-keys/) mais à adapter pour Gitlab).

### **Librairies utilisées par Business**

Les dépendances sont listées dans le fichier out.txt :

* [android-rss-reader-library-master](http://git.paperpad.fr/android-libraries/android-rss-reader-library-master)

* [facebook-android-sdk-3-6-0](http://git.paperpad.fr/android-libraries/facebook-android-sdk-3-6-0)

* [library-sliding-menu](http://git.paperpad.fr/android-libraries/library-sliding-menu)

* [progresswheel-master](http://git.paperpad.fr/android-libraries/progresswheel-master)

* [stripe-android-master](http://git.paperpad.fr/android-libraries/stripe-android-master)

* [forecast-master](http://git.paperpad.fr/android-libraries/forecast-master)

* [uil_library](http://git.paperpad.fr/android-libraries/uil_library)

* [headerlistview](http://git.paperpad.fr/android-libraries/headerlistview)

* [paymentkit_lib](http://git.paperpad.fr/android-libraries/paymentkit_lib)

la téléchargement de ces libraries est gérée par le scripte qui traite le fichier **settings.gradle** afin de récupérer les noms et lancer les commandes de téléchargment.

Pour mettre à jour les dépendances et télécharger les librairies, exécuter en ligne de commande le scripte **Business_Script** en utilisant la commande: **ruby importor.rb**
###  **L’application Business**

L’application Business est un moteur de génération des applications Android :<center>![image alt text](http://git.paperpad.fr/android-projects/opbusiness/raw/master/Docs/screenshots/image_1.png) </center>


On commence par la partie WEB. Le [backoffice Paperpad](http://paperpad.fr/) offre le flux de JSON, après le téléchargement de ce dernier on passe à la partie mobile dans 	 	 	 	

laquelle on fait un Parsing pour stocker les données de l’application dans la base, ensuite, à travers le module Business Data qui est représenté l’application finale.

**[Backoffice Paperpad](http://paperpad.fr/)** : est l'ensemble des activités de supports, de contrôle, d'administration de entreprise Paperpad. Elles sont facilement reconnaissables et pouvant être fonctionnellement séparées du reste des opérations.

## **Java DOC**

Java doc de l'application Business est disponible ici : [Java DOC](http://git.paperpad.fr/android-projects/opbusiness/tree/master/Docs/JavaDoc)
## **API de Backoffice PAPERPAD**



L'API du backoffice a un appel unique qui retourne tous les contenus de l'application, par exemple :

http://backoffice.paperpad.fr/api/application/compiled_app/id/476/lang/fr

Le téléchargement des contenus est assuré par un ensemble des classes contrôlées par la séquence de mise à jour des contenus.

La vérification de la date  s'occupe de la séquence de mise à jour des contenus. Elle est en principe appelée dans le Parsing de JSON comme elle indique la Figure 2.2.


![image alt text](http://git.paperpad.fr/android-projects/opbusiness/raw/master/Docs/screenshots/image_2.png)



###### <center>**Figure 2.2 Le schéma de la mise à jour**</center>



Cette séquence occupe, dans l'ordre, de ces tâches :

* Vérification de la présence de mise à jour en vérifiant la date du JSON sur le serveur et compare avec celle des contenus dans la base locale.

* S’il faut mettre à jour les contenus, le JSON est téléchargé puis passé au Parsing. On crée simplement l'objet racine du modèle (Application), tout le reste est géré 	par les classes du modèle (chaque objet créé ses objets enfants).

* S’il y a des coffrets cadeaux rattachés à l'application, ils seront téléchargeables en background.



* S’il y a une vidéo d'introduction, elle 	sera téléchargeable par Youtube, Android, Player et API.

* Les images qui ne sont pas déjà présentes 	dans le cache sont téléchargées et stockées par Picasso.

## **Utilisation de JSON**



La séquence de démarrage se lance en passant l'identifiant de l'application, le suivi de la progression et du statut de la mise à jour se fait à l'aide de Parsing de JSON.

En cas d'échec du téléchargement, le suivi du téléchargement des images passe au block la dernière image téléchargé, elle peut être affichée dans l'écran de chargement. Le nombre total des images est calculé pendant la réception de JSON avant le Parsing, ces images peuvent être utilisées pour suivre la progression du téléchargement. Quand le téléchargement est terminé, le block de la page d’accueil est appelé.

La structure du JSON représente la hiérarchie des contenus de l'application.

Les principaux éléments de cette structure sont :

* **parameters** : les paramètres généraux de l'application : polices, couleurs, options ;



* **home_swipe** : paramétrage du Swipe d'accueil ;

* **phone_home_grid et tablette_home_grid** : paramétrage du gille d'accueil ;

* **tiles** : paramètres de contenu et de mise en page des éléments de la grille ;

* **tabs** : 	les onglets du principal menu de navigation ;

* **cart** : 	les options que l'utilisateur doit remplir pour valider une demande ;

* **sections** : les contenus eux-mêmes.

Les contenus sont enregistrés dans la base SQLite et sont lisibles sans connexion internet. Cette base de données est implémentée avec OrmLite qui est un module ORM (Object Relational Mapping) très léger. Il permet d’abstraire sur une couche objet les données issues d’une base de données comme SQLite dans notre cas d’utilisation sur Android.

# **Parsing de JSON**

l’outil utilisé c’est **Realm** il permet le parsing de json et stockage des données, la fonction utilisée en Background dans l’application Business :



       private  class ImportAsyncTask extends AsyncTask<Void, Integer, Integer> {
       String jsonObject;

       private ImportAsyncTask(String jsonObject) {
       this.jsonObject = jsonObject;
       }

       @Override

       protected void onPreExecute() {

       super.onPreExecute();

       Realm r = Realm.*getInstance*(getApplicationContext(), getPackageName());

       r.beginTransaction();

       long time = System.*currentTimeMillis*();

       r.where(ApplicationBean.class).findAll().clear();

       r.where(AgendaGroup.class).findAll().clear();

       r.where(Album.class).findAll().clear();

       r.removeAllChangeListeners();

       time = System.*currentTimeMillis*() - time;

       Log.*e*("le temp de pour vider la base :" + time / 1000, " (s)");

       r.commitTransaction();

       Log.*e*("size d' applicationBean :" + r.where(ApplicationBean.class).findAll().size(), "");

   }



      @Override
      protected Integer doInBackground(Void... voids) {
      Realm r = Realm.*getInstance*(getApplicationContext(), getPackageName()); r.beginTransaction();

       long time = System.*currentTimeMillis*();

       r.createOrUpdateObjectFromJson(ApplicationBean.class, jsonObject);

       time = System.currentTimeMillis() - time;

       Log.e("le temp de parsing de JSON et stockage des données :" + time / 1000, "(s)");

       r.commitTransaction();

       return null;

   }



       @Override
       protected void onProgressUpdate(Integer... values)
        { super.onProgressUpdate(values);

       // pw.incrementProgressByValue(values[0]);

   }



         @Override protected void onPostExecute(Integer integer) {   
          Realm r = Realm.getInstance(getApplicationContext(), getPackageName());


       Parameters_section ps = r.where(Parameters_section.class).findFirst();

       if (ps != null) {

           String console_id = ps.getConsole_id();

                   if (!console_id.isEmpty() && console_id != null) {

                       MyBoxCat.*getMyBox_Cat*(console_id);

                     }

                   else {

                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                       intent.setFlags(Intent.*FLAG_ACTIVITY_NEW_TASK*);

                       startActivity(intent);

                   }

           } else {

               Intent intent = new Intent(getApplicationContext(), MainActivity.class);

               intent.setFlags(Intent.*FLAG_ACTIVITY_NEW_TASK*);

               startActivity(intent);

           }

   }

}

au début on teste si la base de données est vide, sinon on effectue une suppression de la base pour la préparer au nouveau flux de json.

La fonction qui se charge de Parsing et stockage c’est :        r.createOrUpdateObjectFromJson(ApplicationBean.class, jsonObject);

avec jsonObject c’est un objet qui contient JSON formaté du backoffice Paperpad.

L’activité MainActivity attache  les fragments tant que l’application est en marche, les fragments disponibles sont :

* [AgendaFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/AgendaFragment.java)

* [AgendaSplitFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/AgendaSplitFragment.java)

* [AlbumsGridFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/AlbumsGridFragment.java)

* [BusinessDialogFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/BusinessDialogFragment.java)

* [CalendarHebdoPagerFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/CalendarHebdoPagerFragment.java)

* [CalendarMonthFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/CalendarMonthFragment.java)

* [CalendarWeekPageFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/CalendarWeekPageFragment.java)

* [CategorieGridFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/CategorieGridFragment.java)

* [CategoryFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/CategoryFragment.java)

* [CategoryIndexFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/CategoryIndexFragment.java)

* [CollectionGridFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/CollectionGridFragment.java)

* [CollumnScrollFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/CollumnScrollFragment.java)

* [ColonnePageFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/ColonnePageFragment.java)

* [ContactsFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/ContactsFragment.java)

* [DashboardFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/DashboardFragment.java)

* [DashboardFragmentPhone](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/DashboardFragmentPhone.java)

* [DesignCategoryFlowFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/DesignCategoryFlowFragment.java)

* [DirectoryFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/DirectoryFragment.java)

* [EventListFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/EventListFragment.java)

* [EventPageFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/EventPageFragment.java)

* [FilteredCategoriesFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/FilteredCategoriesFragment.java)

* [FormContactFragment.java](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/FormContactFragment.java)

* [FormContactFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/FormContactFragment.java)

* [FreeFormulaFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/FreeFormulaFragment.java)

* [FullscreenCategoryFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/FullscreenCategoryFragment.java)

* [GaleryFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/GaleryFragment.java)

* [GalerySliderFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/GalerySliderFragment.java)

* [GallerySlidePageFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/GallerySlidePageFragment.java)

* [HomeGridFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/HomeGridFragment.java)

* [InteractiveMapFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/InteractiveMapFragment.java)

* [ListOfPageFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/ListOfPageFragment.java)

* [MapV2Fragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/MapV2Fragment.java)

* [MultiSelectPagesFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/MultiSelectPagesFragment.java)

* [MyBoxGridFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/MyBoxGridFragment.java)

* [MyBoxPageFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/MyBoxPageFragment.java)

* [MyFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/MyFragment.java)

* [NavItemDetailFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/NavItemDetailFragment.java)

* [NavItemListFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/NavItemListFragment.java)

* [OrderFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/OrderFragment.java)

* [PagesFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/PagesFragment.java)

* [PanoramaFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/PanoramaFragment.java)

* [PlayerYouTubeFrag](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/PlayerYouTubeFrag.java)

* [QPDFViewerFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/QPDFViewerFragment.java)

* [RadioPlayerFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/RadioPlayerFragment.java)

* [RadiosSectionFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/RadiosSectionFragment.java)

* [RSSFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/RSSFragment.java)

* [SampleUploadListener](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SampleUploadListener.java)

* [SimplePageFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SimplePageFragment.java)

* [SliderCategoryFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SliderCategoryFragment.java)

* [SliderCategoryFragment_](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SliderCategoryFragment_.java)

* [SplitFilteredCategoriesFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SplitFilteredCategoriesFragment.java)

* [SplitListCategoryFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SplitListCategoryFragment.java)

* [SplitListCategoryFragment_](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SliderCategoryFragment_.java)

* [SplitListFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SplitListFragment.java)

* [StreetViewFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/StreetViewFragment.java)

* [StripePaymentFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/StripePaymentFragment.java)

* [SurveyFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SurveyFragment.java)

* [SurveyPageFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SurveyPageFragment.java)

* [SurveySubmitFragment]( http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SurveySubmitFragment.java)

* [SurveyThanksFragment]( http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SurveyThanksFragment.java)

* [SwipePage]( http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SwipePage.java)

* [SwipperFragment]( http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/SwipperFragment.java)

* [TabsFragment]( http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/TabsFragment.java)

* [TabsSupportFragment]( http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/TabsSupportFragment.java)

* [WebHomeFragment](http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/WebHomeFragment.java)

* [WebViewFragment]( http://git.paperpad.fr/android-projects/opbusiness/blob/master/app/src/main/java/com/euphor/paperpad/activities/fragments/WebViewFragment.java)

## **Sélection et Paiement**



La pile peut gérer une sélection de produit ainsi que un paiement avec le service « Stripe », comme elle indique la figure 2.3.

<center>![image alt text](http://git.paperpad.fr/android-projects/opbusiness/raw/master/Docs/screenshots/image_3.png) </center>



###### <center> **Figure 2.3 le schéma explicatif du service de paiement « Stripe »** </center>



## **Diagramme de 	package de l’application**



L’application Business contient plusieurs dépendances, et le digramme suivant, offre un survol sur les packages utilisés.

**	 	 	 	**
<center>
![image alt text](http://git.paperpad.fr/android-projects/opbusiness/raw/master/Docs/screenshots/image_4.jpg)
</center>
**	 	 	 	**

###### <center> Figure 2.19 Le diagramme de packages de l’application Business


Le diagramme de package nous offre une vision superficielle sur le projet, pour plus de détails, une étude de package main sera établie par la suite.

Le diagramme de classe ci-dessus (cf. Figure 2.20) représente la structure des classes et les interfaces utilisées pour implémenter le package main de l’application Business.

**Diagrammes de classe de l’application**

**	**
<center>
![image alt text](http://git.paperpad.fr/android-projects/opbusiness/raw/master/Docs/screenshots/image_5.jpg)
</center>
## **Les statistiques de l’application générée**



La principale classe de gestion des statisitques est **AppJsonWriter**. Elle est utilisée pour enregistrer les sessions et les visites de chaque éléments et pour envoyer ces données à l'API du Backoffice Paperpad.

Les statistiques sont enregistrées en local, Les entités Hit et Session sont utilisées pour ce stockage. Elle n'ont pas de relation directe avec les autres entités des éléments visités (Page, Section…), car ces dernières peuvent être supprimmées en cas de mise à jour des contenus. La référence à l'élément visité se fait par l'ID.

## **Suivi des sessions et visites**

### **Début de session**

Une instance de **AppSession** est alloué automatiquement dans l’activité MainActivity. Quand l'application passe au premier plan, il faut commencer une session :

AppSession appSession = new AppSession(id_menu,

       prod_or_sand,

       "sales",

       application_unique_identifier,

       application_version ,

       regid,

       Build.MANUFACTURER,

       Build.MODEL,

       "android",

       device_screen_resolution,

       5,

       Build.VERSION.SDK_INT+"",

       device_type,

       session_id,

       tsLong,

       System.currentTimeMillis()/1000 ,

       hits );

### **Visites**

Chaque visite ayant une durée, il faut notifier le début et la fin de la visite.

La classe **AppHit** permet de suivre simplement le temps passé :

Cette classe s'occupe d'enregistrer l'heure au début et à la fin de la visite pour calculer sa durée.

Le début de la visite est notifié avec la méthode onStop du fragment. Il doit être fait après qu'une session ait démarré.

Par exemple sur un fragment, quand la vue apparaît :

il y’ a initialisation de la variable **time**, pour récupérer le temps courant.

Pour notifier la fin de la visite :

AppHit hit = new AppHit(System.*currentTimeMillis*() / 1000, time / 1000, "sales_page", page.getId());



Les types de visites disponibles sont :

* **sales_events_section**

* **sales_album**

* **events_section**

* **sales_category**

* **sales_page**

* **sales_contacts_section**

* **sales_gallery_section**

* **sales_home**

* **sales_map_section**

* **sales_mybox_section**

* **sales_web_section**

### **Fin de session**

Quand l'app passe en arrière plan il faut terminer la session. En principe dans l'app delegate :

@Override

public void onStop() {

  AppHit hit = new AppHit(System.*currentTimeMillis*()/1000, time/1000, "sales_web_section", 0);

  ((MyApplication)getActivity().getApplication()).hits.add(hit);

  super.onStop();

}

À ce moment la session est enregistré dans la base, avec sa durée et toutes ses visites.

## **Requête du Backoffice**



Quand une session est terminée, si la connexion internet est disponible, une requête est faite vers l'API du Backoffice pour envoyer la session.

Si la connexion n'est pas disponible, les données restent dans la base jusqu'au prochain lancement de l'application, ou une nouvelle tentative de requête sera faite.

La requête envoyée au backoffice est formatée de cette manière :

{

"sessions": [

{

"device_type": "tablet",

"application_id": **28**,

"device_screen_resolution": "1024x768",

"session_unique_identifier": "14247964658r86y87seH3eL3j1E3",

"device_screen_size": **10**,

"device_manufacturer": "Apple",

"start": **1424796465**,

"device_model": "iPad Simulator",

"application_unique_identifier": "84290382-0C03-4D9D-AFBC",

"device_platform": "ios",

"hits": [

{

"type": "sales_page",

"id": **3640**,

"end": **1424796469**,

"start": **1424796461**

},

{

"type": "sales_category",

"id": **2036**,

"end": **1424796470**,

"start": **1424796469**

}

],

"end": **1424796473**,

"application_mode": "production",

"application_push_token" : "AAA-BBB-CCC",

"application_version": "1.20.4",

"device_system_version": "8.1",

"application_type": "sales"

}

]

}

A partir de cette statistique on aura une idée claire à propos du comportement d’utilisateur, ce qui nous permet implicitement d’anticiper les points à améliorer, dans les prochaines mises à jours afin de faciliter l’accès et présenter les contenus les plus visités d’une manière plus sophistiquée.

# **Téléchargement des images**

Les image sont téléchargées lors de la parsing de JSON. Elles sont stockées dans le dossier String DIRECTORY_IMAGES = *activity*.getPackageName() + "_images"; de l'application.

La classe **Illustration** contient des paramètres, Il est considéré que les noms d'images ne sont pas unique du coup  les identifiants du contenu de l'image sont des Ids incrémentés au moment du stockage.

## **Utilisation de Glide**

La bibliothèque **Glide** est responsable de l’affichage des images téléchargées.

**Glide** est disponible sur : [https://github.com/bumptech/glide](https://github.com/bumptech/glide)
<center>
![image alt text](http://git.paperpad.fr/android-projects/opbusiness/raw/master/Docs/screenshots/Sélection_080.png)
</center>




# **Modèle de données**

Les modèles (Beans) héritent de la classe **RealmObject**.



    public class Person extends RealmObject {
    @Required // Name is not nullable
    private String name;
    private String imageUrl; // imageUrl is an optional field
    private RealmList<Dog> dogs; // A person has many dogs (a relationship)
    // ... Generated getters and setters ...
}

 il y a des limitations pour l’outil **Realm**: [https://realm.io/](https://realm.io/) qui sont :

* L’absence de la Liste des **string**.

* Liste devient RealmList.

* L’absence de la Liste des **int**.

* L’absence du comportement d’ID auto-increment.

* L’écriture dans la base de données est une transaction.

 // Persist your data easily
 realm.beginTransaction();
 realm.copyToRealm(dog);
 realm.commitTransaction();

….

**La partie MyBOX**

Les application mybox utilisent un autre backoffice, pendant le parsing de JSON de **paperpad**  y a une autre requête vers le nouveau backoffice afin de récupérer le nouveau JSON.
Le parsing de ce nouveau JSON se fait en parrallèle avec le traitement.
la classe qui s’en charge de parser le nouveau JSON c’est : **MyBox_App**

Les méthodes utilisées sont : **getMyBox_App** et **getMyBox_Cat**

public static void getMyBox_App(String console_id) {



      Log.*e*("device name "+*activityBox*.getDeviceName(),"");

   if(*activityBox *!=null)
   *mRequestQueue *= Volley.*newRequestQueue*(*activityBox*.getApplicationContext());

   Log.*e*("Before url","");

   String url = "http://consolemybox.apicius.com/services_ios/get_coffret?console_id=" + console_id + "&langue=fr";

   Log.*e*(" request","") ;

   JsonArrayRequest request = new JsonArrayRequest(url,  new Response.Listener<JSONArray>() {

       @Override

       public void onResponse(JSONArray response) {

           Log.*e*("After request onReponse","") ;

           if (response != null) {

               if (*asyncTask *!= null) {

                   *asyncTask*.cancel(true);

               }

               *asyncTask *= new ImportAsyncTask(response);

               *asyncTask*.execute();

           }

       }
       }, new Response.ErrorListener() {



       @Override

       public void onErrorResponse(VolleyError volleyError) {

           Log.*e*("volley Error MyBox App"+ volleyError.toString(),"") ;

       }

   }

   );

}







                 public static void getMyBox_Cat(String console_id) { if(*activityCat *!=null)    *mRequestQueue *= Volley.*newRequestQueue*(*activityCat*.getApplicationContext());String url = "http://consolemybox.apicius.com/services_ios/get_categorie_console?console_id=" + console_id + "&langue=fr";


                   JsonArrayRequest request = new JsonArrayRequest(url,  new Response.Listener<JSONArray>() {

                       @Override

                       public void onResponse(JSONArray response) {

                       if (response != null) {

                           if (*asyncTask *!= null) {

                               *asyncTask*.cancel(true);

                           }

                           *asyncTask *= new ImportAsyncTask(response);

                           *asyncTask*.execute();

                            }

                           }

                   }, new Response.ErrorListener() {

                       @Override

                       public void onErrorResponse(VolleyError volleyError) {

                       }

                   }

                   );

               }
