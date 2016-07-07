/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.adapters.MyFormAdapter;
import com.euphor.paperpad.Beans.Application;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.Beans.FieldFormContact;
import com.euphor.paperpad.Beans.FormValue;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.Section;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.RandomString;
import com.euphor.paperpad.utils.RestoredField;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter;
import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter.PostCallBack;
import com.euphor.paperpad.utils.jsonUtilities.FormFields;
import com.euphor.paperpad.utils.jsonUtilities.OrderField;
import com.euphor.paperpad.utils.jsonUtilities.ValuePeriod;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * @author euphordev02
 *
 */
public class FormContactFragment extends Fragment implements PostCallBack/*, ScrollViewListener*/ {

    private static final String URL_CONTACT = Constants.BASE_URL+"/api/application/contact";
    private Colors colors;

    private ArrayList<RestoredField> restoredFields;
    private Contact contact;
    private int section_id;
    protected String selectedItem;
    private AlertDialog myErrorDialog;
    protected AlertDialog beforeComandDialog;
    private boolean isTablet;
    private boolean newDesign;
    private Realm realm;
    private boolean send = false;
    private MyFormAdapter adapter;
    private ListView list;
    /**
     * Create a new instance of MyDialogFragment
     */
    public static FormContactFragment newInstance() {
        FormContactFragment f = new FormContactFragment();
        return f;
    }

    public FormContactFragment() {
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onAttach(Activity activity) {
        //appController = ((MyApplication)getActivity().getApplication()).getAppController();

        colors = ((MainActivity)activity).colors;
        		realm = Realm.getInstance(getActivity());
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {

            colors= new Colors(ParamColor);
        }

        ((MainActivity)activity).bodyFragment = "FormFragment";

        int id_contact;

        if(((MainActivity)getActivity()).extras == null)
            ((MainActivity)getActivity()).extras = new Bundle();

        if (getArguments() != null) {
            restoredFields = getArguments().getParcelableArrayList("Restored_fields");
            id_contact = getArguments().getInt("Contact");
            newDesign = getArguments().getBoolean("newDesign", false);
            contact =  realm.where(Contact.class).equalTo("id",id_contact).findFirst();
            section_id = getArguments().getInt("Section_id_form");
        }
        if (restoredFields != null)
        {
            ((MainActivity)getActivity()).extras = getArguments();
        }

        isTablet = Utils.isTablet(activity);

        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.form_layout_solution, container, false);



        LinearLayout lyt = (LinearLayout)v.findViewById(R.id.imageFormLayout);
        if(lyt != null && newDesign || !isTablet)
        {
            lyt.setVisibility(View.GONE);
            v.findViewById(R.id.contactFormLayout).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }


        TextView titleContactsTV = (TextView)v.findViewById(R.id.TitleTV);
        titleContactsTV.setTypeface(MainActivity.FONT_TITLE);
        if ((contact != null && contact.getTitle().isEmpty()) || contact == null) {
            v.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());
            titleContactsTV.setText(getActivity().getResources().getString(R.string.contact_title));
            titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));

        }else {
            v.findViewById(R.id.TitleHolder).setBackgroundDrawable(colors.getForePD());
            titleContactsTV.setText(contact.getTitle());
            titleContactsTV.setTextColor(colors.getColor(colors.getBackground_color()));

        }
        TextView subTitle = (TextView)v.findViewById(R.id.subTitle);
        String text_intro = contact.getText_intro();
        if(text_intro != null){

            subTitle.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Small);
            subTitle.setTypeface(MainActivity.FONT_BODY);
            subTitle.setTextColor(colors.getColor(colors.getBody_color()));
            subTitle.setText(text_intro);

        }else{
            subTitle.setVisibility(View.GONE);
        }
//		subTitle.setTypeface(MainActivity.FONT_BODY);
//		subTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
//		LinearLayout.LayoutParams params_subTitle = (LayoutParams) subTitle.getLayoutParams();
//		params_subTitle.setMargins(0, 7, 7, 7);
//		subTitle.setLayoutParams(params_subTitle);
//		subTitle.setText(contact.getDetails());  // n'est plus sous iOS

        //}
        Button btnValider = (Button)v.findViewById(R.id.btn_valide_form);
        btnValider.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        ColorStateList color_txt = new ColorStateList(new int[][]{{android.R.attr.state_pressed},{}},
                new int[]{colors.getColor(colors.getTitle_color()),
                        colors.getColor(colors.getBackground_color())});

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
        drawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(colors.getColor(colors.getBackground_color())));
        drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getTitle_color(), "80")));


        btnValider.setTextColor(color_txt);
        btnValider.setBackgroundDrawable(drawable);

        btnValider.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                send = true;
                /******** ajouter pour activer boutton***/
                list.smoothScrollToPosition(adapter.getCount() - 1);
                final FormFields formfieldsTmp = validateAllFields(adapter.getMapViews());
                if(formfieldsTmp !=null)
                    sendCommand(formfieldsTmp);

            }
        });
      v.findViewById(R.id.tableFormHolder).setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.30f));//colors.getColor(colors.getTitle_color(), "10"));
        //		Collection<FieldFormContact> fields = contact.getFields1();
        List<FieldFormContact> fields = new RealmList<>();
        if(contact !=null)
        fields = contact.getFields();
        else{
        fields = realm.where(FieldFormContact.class).findAll();//appController.getFieldFormContactDao().queryForAll();
      //  fields = sortFields(fields, contact);
      }


        //maps = new ArrayList<Map<String,Object>>();

        if (fields != null && fields.size()>0) {
            list = (ListView)v.findViewById(R.id.listForm);
            adapter = new MyFormAdapter((MainActivity) getActivity(), /*new RealmList<FieldFormContact>()*/fields, /*restoredFields, */realm, colors, R.layout.form_row_item);
            if(list != null){
                list.setAdapter(adapter);
                this.list.setOnScrollListener(new OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                        if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){

                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        }

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem,
                                         int visibleItemCount, int totalItemCount) {

                        if(send && (firstVisibleItem + visibleItemCount) == totalItemCount){
                            send = false;
                            String alert_msg = "";
                            if(contact !=null) {
                                alert_msg  = contact.getAlert_message();
                            }
                            final FormFields formfieldsTmp = validateAllFields(adapter.getMapViews());
                            if (formfieldsTmp != null) {
                                if (alert_msg != null && !alert_msg.isEmpty()) {
                                    beforeComandDialog = new AlertDialog.Builder(getActivity()).create();
                                    //				 beforeComandDialog.setTitle("Impossible de valider la commande");
                                    beforeComandDialog.setMessage(alert_msg);
                                    beforeComandDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.continue_command), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            sendCommand(formfieldsTmp);
                                        }
                                    } );
                                    beforeComandDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_cart), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    } );
                                    beforeComandDialog.show();
                                }else {
                                    sendCommand(formfieldsTmp);
                                }
                            }else {
                                //Toast.makeText(getActivity(), getResources().getString(R.string.cancel_cart), 3000).show();
                                sendCommand(formfieldsTmp);
                            }
                        }

                    }
                });
            }

        }


        if (v.findViewById(R.id.imageForm) != null) {
            ImageView imageForm = (ImageView) v.findViewById(R.id.imageForm);

            List<Section> sections = realm.where(Section.class).equalTo("id", section_id).findAll();

            if(contact !=null) {
                for (int i = 0; i < sections.size(); i++) {

                    if (sections.get(i).getContacts() != null && sections.get(i).getContacts().size() > 0) {
                        realm.beginTransaction();
                        contact.setSection(sections.get(i));
                        realm.commitTransaction();
                    }
                }
            /*}*/
                List<Contact> contacts = realm.where(Contact.class).findAll();
                Section section = new Section();
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getSection() != null)
                        section = realm.where(Section.class).equalTo("id_s", contacts.get(i).getSection().getId_s()).findFirst();
                }

                //appController.getSectionsDao().queryForId(contact.getSection().getId_section());
                Illustration illustration = section.getIllustrationObj();
                if (illustration != null) {
                    if (!illustration.getPath().isEmpty()) {
                        Glide.with(getActivity()).load(new File(illustration.getPath())).into(imageForm);
                    } else {
                        try {
                            Glide.with(getActivity()).load(illustration.getLink()).into(imageForm);
                        } catch (IllegalArgumentException e) {
                            Log.d("FormContactFragment", illustration.getLink());
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
            }
        }

        return v;
    }

    protected String[] setItems(List<FormValue> objects) {
        String[] result = new String[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            result[i] = objects.get(i).getText();
        }

        return result;
    }

    private Collection<FieldFormContact> sortFields(Collection<FieldFormContact> fields,   Contact pContact) {
        Collection<FieldFormContact> result = new ArrayList<FieldFormContact>();
        for (Iterator<FieldFormContact> iterator = fields.iterator(); iterator.hasNext();) {
            FieldFormContact fieldForm = (FieldFormContact) iterator.next();
            if (fieldForm.getContact().getId_con() == pContact.getId_con()) {
                result.add(fieldForm);
            }
        }
        fields = result;
        return result;

    }

    private List<FormValue> sortObject(List<FormValue> objects, FieldFormContact field) {
        List<FormValue> result = new ArrayList<FormValue>();
        for (Iterator<FormValue> iterator = objects.iterator(); iterator.hasNext();) {
            FormValue formValue = (FormValue) iterator.next();
            for (int i = 0; i <field.getValues().size() ; i++) {

                if (formValue.getId() == field.getValues().get(i).getId()) {
                /* rappelle toi de ce truk, tu inverses l egalité pr lier les champs*/
                /*formValue.getField().getId_generated() == field.getId_generated()*/
                    result.add(formValue);
                }
            }
        }
        objects = result;
        return result;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    boolean error = false;

    protected FormFields validateAllFields(List<Map<String, Object>> list) {

        if(list.size() < adapter.getCount())return null;


        int id_app = -1;
        int account_id = 0;
        FormFields formFields;
        List<FormValue> objects = new ArrayList<FormValue>();

        Application application = realm.where(Application.class).findFirst();//appController.getApplicationDataDao().queryForId(1);
        if (application != null) {
            if (application.getParameters() != null) {
                account_id = application.getParameters().getAccount_id();
                id_app = application.getParameters().getId();
            }
        }


        objects =  realm.where(FormValue.class).findAll(); //appController.getFormValueDao().queryForAll();

        //					objects = appController.getFormValueDao().queryForEq("field_id", field.getId_generated());

        if (account_id !=0 && id_app != -1) {
            String key = (new RandomString(16)).nextString();
            String pushToken = ((MainActivity)getActivity()).regid;
            formFields = new FormFields(section_id, id_app, key , "fr", new ArrayList<OrderField>(), pushToken);
        }else {
            return null;
        }
        int height = 0;
        restoredFields = new ArrayList<RestoredField>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            FieldFormContact field = (FieldFormContact) map.get("field");
            OrderField orderField = new OrderField(field.getId());
            View view = (View)map.get("view");
            View secondView = (View)map.get("second_view");
            String value = "";

            height += (view != null) ? view.getHeight() + 50 : 0;
            height += (secondView != null) ? secondView.getHeight() + 50 : 0;

            if (field.getType().equals("text")) {

                value = ((EditText) view).getText().toString();



                if (value.isEmpty() || value.length() < 3) {
                    if(!field.getOptional())
                    {
                        error = true;
                        ((EditText) view).setError("veuillez remplir ce champ");
                        break;
                    }
                }else {
                    error = false;
                    restoredFields.add(new RestoredField(field.getId(), value, ""));
                    orderField.setValue(value);
                }


            }else if (field.getType().equalsIgnoreCase("long_text")) {

                value = ((EditText) view).getText().toString();
                if (value.isEmpty()) {
                    if(!field.getOptional())
                    {
                        error = true;
                        ((EditText) view).setError("veuillez remplir ce champ");
                        break;
                    }
                }else {
                    error = false;
                    restoredFields.add(new RestoredField(field.getId(), value, ""));
                    orderField.setValue(value);
                }


            }else if (field.getType().equalsIgnoreCase("phone")) {

                value = ((EditText) view).getText().toString();
                if (value.isEmpty() || value.length() < 8) {
                    if(!field.getOptional()) {
                        error = true;
                        ((EditText) view).setError("veuillez remplir ce champ");
                        break;
                    }
                }else {
                    error = false;
                    restoredFields.add(new RestoredField(field.getId(), value, ""));
                    orderField.setValue(value);
                }

            }else if (field.getType().equals("email")) {

                EditText editText = (EditText) view;
                value = editText.getText().toString();
                error = !Utils.isEmailValid(value);
                if (error) {
                    if(!field.getOptional()){
                        editText.setError("Email Invalide");
                        break;
                    }
                }else {
                    restoredFields.add(new RestoredField(field.getId(), value, ""));
                    orderField.setValue(value);
                }


            }else if (field.getType().equals("date")){

                Button button = (Button) view;
                value = button.getText().toString();
                Date date = Utils.ConvertToDate(value, "dd/MM/yyyy");
                if (date == null) {
                    if(!field.getOptional()) {
                        button.setError("date invalide");
                        error = true;
                        break;
                    }
                }else {
                    error = false;
                    restoredFields.add(new RestoredField(field.getId(), value, ""));
                    orderField.setValue_date(Utils.ConvertToDate(value, "dd/MM/yyyy"));
                }



            }else if (field.getType().equals("period")){

                Button button = (Button) view;
                Button button2 = (Button) secondView;
                Date date = Utils.ConvertToDate(button.getText().toString(), "dd/MM/yyyy");
                Date date1 = Utils.ConvertToDate(button2.getText().toString(), "dd/MM/yyyy");
                if (date != null && date1 != null) {
                    ValuePeriod period = new ValuePeriod();
                    period.setEnd(date1);
                    period.setStart(date);
                    orderField.setValue_period(period);
                    restoredFields.add(new RestoredField(field.getId(), button.getText().toString(), button2.getText().toString()));
                    error = false;
                }else {
                    if (date == null) {
                        if(!field.getOptional())
                            button.setError("Date invalide");
                    }
                    if (date1 == null) {
                        if(!field.getOptional())
                            button2.setError("Date invalide");
                    }
                    if(!field.getOptional())
                    {
                        error = true;
                        break;
                    }
                }

            }else if (field.getType().equals("select")){
                Button button = (Button) view;
                int choice = (Integer)button.getTag();

                List<FormValue> objects_ = sortObject(objects, field);
                if (objects_.size()>0) {
                    FormValue valeur = null;
                    if (choice != -1) {
                        valeur = objects_.get(choice);
                    }else if(!button.getText().toString().isEmpty()){
                        valeur = getExistField(objects_, button.getText().toString());
                    }
                    if (valeur != null && valeur.getId() != -1) {
                        //	value = valeur.getId() + "";
                        int value_id =valeur.getId();
                        orderField.setValue_id(/*valeur.getText()*/ value_id);
                        restoredFields.add(new RestoredField(field.getId(),
                                valeur.getText()+ "", "")); /* modifié par Nizar */
                        error = false;
                    } else {
                        if (!field.getOptional()) {
                            //this.list.smoothScrollToPosition(i);
                            error = true;
                            button.setError("choix invalide");
                            break;
                        }

                    }
                }else {

                    if (!field.getOptional()) {
                        error = true;
                        button.setError("choix invalide");
                        break;
                    }
                    else{
                        error = false;
                    }
                }
            }else if (field.getType().equals("postal_code")) {

                value = ((EditText) view).getText().toString();
                if (value.isEmpty() || value.length() != 5){
                    if(!field.getOptional()) {
                        error = true;
                        ((EditText) view).setError("Code postal invalide");
                        break;
                    }
                }else {
                    error = false;
                    restoredFields.add(new RestoredField(field.getId(), value, ""));
                    orderField.setValue(value);
                }


            }else if (field.getType().equals("date_hour")) {

                Button button = (Button) view;
                Button button2 = (Button) secondView;
                value = button.getText().toString();
                String value2 = button2.getText().toString();
                value2 = value2.replaceAll(" ", "");
                String[] tmp = value2.split(":");
                if (tmp.length == 2) {
                    String heure = tmp[0];
                    String minutes = tmp[1];
                    if (heure.length() == 1) {
                        heure = "0"+heure;
                    }
                    if (minutes.length() == 1) {
                        minutes = "0"+minutes;
                    }
                    value2 = heure+":"+minutes;
                }

                Date date = Utils.ConvertToDate(value+" "+value2, "dd/MM/yyyy hh:mm");
                if (date != null) {
                    error = false;
                    restoredFields.add(new RestoredField(field.getId(), value, value2));
                    orderField.setValue_date_time(date);
                }else {
                    if(!field.getOptional())
                    {
                        error = true;
                        button2.setError("Date et/ou heure invalides");
                        break;
                    }
                }


            }
            if (!error) {
                formFields.getFields().add(orderField);
            }else {
                return null;
            }

        }

        if (error) {
            this.list.smoothScrollByOffset(height);//ScrollToPosition(i, height);
            return null;
        }else {
            return formFields;
        }

    }

    public FormValue getExistField(List<FormValue> objects, String textField){
        for(int i = 0; i < objects.size(); i++){
            if(objects.get(i).getText().equals(textField))return objects.get(i);
        }
        return null;
    }

    public int sendCommand(FormFields formFields) {

        AppJsonWriter.PostSendAsyncTask asyncTask = new AppJsonWriter.PostSendAsyncTask(FormContactFragment.this);
        if (formFields != null) {
            AppJsonWriter appJsonWriter = new AppJsonWriter(realm);
            String[] params = { URL_CONTACT, appJsonWriter.writeFormJson(formFields)};
            asyncTask.execute(params);
        }else {

            this.list.smoothScrollToPosition(adapter.getCount() - 1);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.error));
            builder.setMessage(getString(R.string.survey_vote_incomplete_message));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //					Toast toast = Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT);
                    //					toast.show();
                    myErrorDialog.hide();
                }
            });
            builder.setCancelable(true);
            myErrorDialog = builder.create();
            myErrorDialog.show();
        }
        return asyncTask.status;
    }



    @Override
    public void onPosted(boolean posted) {
        if (posted) {
            Toast.makeText(getActivity(), getString(R.string.msg_sent), Toast.LENGTH_SHORT).show();
            //			dialog.dismiss();
        }else {
            Toast.makeText(getActivity(), getString(R.string.msg_not_sent), Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void getResult(HashMap<String, Object> result) {
        // TODO Auto-generated method stub

    }


}
