/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Application;
import com.euphor.paperpad.Beans.FieldSatisfaction;
import com.euphor.paperpad.Beans.Question;
import com.euphor.paperpad.Beans.Survey_;
import com.euphor.paperpad.Beans.ValueSatisfaction;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.RandomString;
import com.euphor.paperpad.utils.RestoredField;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter;
import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter.PostCallBack;
import com.euphor.paperpad.utils.jsonUtilities.OrderField;
import com.euphor.paperpad.utils.jsonUtilities.Response;
import com.euphor.paperpad.utils.jsonUtilities.SatisfactionData;
import com.euphor.paperpad.utils.jsonUtilities.ValuePeriod;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.euphor.paperpad.widgets.NonSwipeableViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class SurveySubmitFragment extends Fragment implements PostCallBack {

	private static final String KEY_FIELDS = "fields";
	private static final String ID_SURVEY = "id_survey";
	private NonSwipeableViewPager pager;
	ArrayList<RestoredField> restoredFields;
	public static final String URL_SURVEY = Constants.BASE_URL+"/api/survey/save";
	private Dialog dialog;

	private Colors colors;
	private ArrayList<View> allViews;
	private List<Map<String, Object>> maps;
	private TableLayout tableLayout;
	private int section_id;
	private int id_survey;
	private SatisfactionData formFields;
	private AlertDialog myErrorDialog;
	protected String selectedItem;
	private AlertDialog myDialog;
	SurveyFragment surveyFragment;
    public Realm realm;
	/**
	 * 
	 */
	public SurveySubmitFragment() {
		// TODO Auto-generated constructor stub
	}


	static SurveySubmitFragment newInstance(int id_survey, NonSwipeableViewPager pager, int pSection_id, SurveyFragment surveyFragment) {
		SurveySubmitFragment frag = new SurveySubmitFragment();
		Bundle args=new Bundle();
		frag.setPager(pager);
		frag.setSection_id(pSection_id);
		frag.setId_survey(id_survey);
		args.putInt(ID_SURVEY, id_survey);
		frag.setArguments(args);
		frag.setSurveyFragment(surveyFragment);

		return(frag);
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		//new AppController(getActivity());
				realm = Realm.getInstance(getActivity());
		colors = ((MainActivity)activity).colors;
        Parameters ParamColor = realm.where(Parameters.class).findFirst();
        if (colors==null) {
            colors = new Colors(ParamColor);
        }

        ((MainActivity)getActivity()).bodyFragment = "SurveyFragment";
		((MainActivity)getActivity()).extras = new Bundle();
		if (getArguments() != null) {
			restoredFields = getArguments().getParcelableArrayList("Restored_fields");
		}
		if (restoredFields != null) {
			((MainActivity)getActivity()).extras = getArguments();
		}
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

    }

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.survey_submit_layout, container, false);
		LinearLayout back = (LinearLayout)v.findViewById(R.id.btn_back_survey);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getPager().setCurrentItem(0, true);
			}
		});
		
		TextView subTitle = (TextView)v.findViewById(R.id.subTitle_survey);
		subTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
		LinearLayout.LayoutParams params_subTitle = (LinearLayout.LayoutParams) subTitle.getLayoutParams();
		params_subTitle.setMargins(0, 7, 7, 7);
		subTitle.setLayoutParams(params_subTitle);
		subTitle.setText(getResources().getString(R.string.survey_submit));
		subTitle.setTextColor(colors.getColor(colors.getForeground_color()));
		
		LinearLayout btnValider = (LinearLayout)v.findViewById(R.id.btn_valide_survey);
		btnValider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCommand(validateAllFields(maps));
			}
		});
		ColorStateList colorSelector = new ColorStateList(
				new int[][] {
						new int[] { android.R.attr.state_pressed },
						new int[] {} },
						new int[] {
						colors.getColor(colors
								.getBackground_color(), "AA"),
								colors.getColor(colors.getTitle_color()) });
		TextView btn_back_survey = (TextView)v.findViewById(R.id.btn_back_survey_txt);
		TextView btn_valide_survey_text = (TextView)v.findViewById(R.id.btn_valide_survey_text);
		btn_valide_survey_text.setTextColor(colorSelector);
		btn_back_survey.setTextColor(colorSelector);
		ArrowImageView nextArrow = (ArrowImageView)v.findViewById(R.id.nextArrow);
		ArrowImageView backArrow = (ArrowImageView)v.findViewById(R.id.backArrow);
		Paint paint = new Paint();
		paint.setColor(colors.getColor(colors.getTitle_color()));
		nextArrow.setPaint(paint);
		backArrow.setPaint(paint);
		StateListDrawable stateListDrawable = new StateListDrawable();
		stateListDrawable.addState(
				new int[] { android.R.attr.state_pressed },
				new ColorDrawable(colors.getColor(colors
						.getTitle_color())));
		stateListDrawable.addState(
				new int[] { android.R.attr.state_selected },
				new ColorDrawable(colors.getColor(colors
						.getTitle_color())));
		StateListDrawable stateListDrawableBack = new StateListDrawable();
		stateListDrawableBack.addState(
				new int[] { android.R.attr.state_pressed },
				new ColorDrawable(colors.getColor(colors
						.getTitle_color())));
		stateListDrawableBack.addState(
				new int[] { android.R.attr.state_selected },
				new ColorDrawable(colors.getColor(colors
						.getTitle_color())));
		btnValider.setBackgroundDrawable(stateListDrawable);
		back.setBackgroundDrawable(stateListDrawableBack);

		tableLayout = (TableLayout)v.findViewById(R.id.tableForm_survey);
		tableLayout.setStretchAllColumns(true);

		//		Drawable rounded_back = getResources().getDrawable(R.drawable.shape_rounded_corners);
		//		rounded_back.setColorFilter(colors.getColor(colors.getBackground_color(),"88"), PorterDuff.Mode.MULTIPLY);
		v.findViewById(R.id.tableFormHolder_survey).setBackgroundColor(colors.getColor(colors.getForeground_color(), "40")); //.setBackgroundDrawable(rounded_back );
		//		Collection<FieldFormContact> fields = contact.getFields1();
		Collection<FieldSatisfaction> fields = new ArrayList<FieldSatisfaction>();
        Survey_ survey = realm.where(Survey_.class).equalTo("id",id_survey).findFirst();
        ////appController.getSurveyDao().queryForId(id_survey);
        fields = survey.getFields();
        //			fields = appController.getFieldSatistfactionDao().queryForAll();
        //			fields = sortFields(fields, survey);
        maps = new ArrayList<Map<String,Object>>();

		if (fields != null && fields.size()>0) {
			allViews = new ArrayList<View>();
			for (Iterator<FieldSatisfaction> iterator = fields.iterator(); iterator.hasNext();) {
				FieldSatisfaction field = (FieldSatisfaction) iterator.next();
				Map<String, Object> map= new HashMap<String, Object>();

				TableRow aRow = (TableRow) inflater.inflate(R.layout.form_row, null, false);
				aRow.setTag(field);
				//				LinearLayout form_label = (LinearLayout)aRow.findViewById(R.id.form_label);

				TextView form_label_tv = (AutoResizeTextView)aRow.findViewById(R.id.form_label_tv);
				form_label_tv.setText(field.getLabel());
				form_label_tv.setTextColor(colors.getColor(colors.getBody_color()));
				LinearLayout form_input = (LinearLayout)aRow.findViewById(R.id.form_input);
				form_input.setOrientation(LinearLayout.HORIZONTAL);

				if (field.getType().equalsIgnoreCase("text")) {

					View viewText = inflater.inflate(R.layout.text_input, null, false);
					EditText editText = (EditText)viewText.findViewById(R.id.text_input);
					editText.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.border_for_views));
					editText.setTextSize(15);
					String value = extracted(field);
					if (value != null) {
						editText.setText(value);
					}else {
						editText.setHint("  "+field.getPlaceholder());
					}
					editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					InputFilter[] FilterArray = new InputFilter[1];
					FilterArray[0] = new InputFilter.LengthFilter(50);
					editText.setFilters(FilterArray);
					editText.setTag(field.getId());
					editText.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

						}
					});
					allViews.add(viewText);
					map= new HashMap<String, Object>();
					map.put("view", editText);
					map.put("field", field);
					maps.add(map);
					form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

				}else if (field.getType().equalsIgnoreCase("long_text")) {

					View viewText = inflater.inflate(R.layout.long_text_input, null, false);
					EditText editText = (EditText)viewText.findViewById(R.id.text_input);
					editText.setBackground(getActivity().getResources().getDrawable(R.drawable.border_for_views));
					editText.setTextSize(15);
					String value = extracted(field);
					if (value != null) {
						editText.setText(value);
					}else {
						editText.setHint("  "+field.getPlaceholder());
					}
					editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					InputFilter[] FilterArray = new InputFilter[1];
					FilterArray[0] = new InputFilter.LengthFilter(400);
					editText.setFilters(FilterArray);
					editText.setTag(field.getId());
					editText.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

						}
					});
					allViews.add(viewText);
					map= new HashMap<String, Object>();
					map.put("view", editText);
					map.put("field", field);
					maps.add(map);
					form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

				}else if (field.getType().equalsIgnoreCase("email")) {
					String value = extracted(field);
					View viewText = inflater.inflate(R.layout.text_input, null, false);
					EditText editText = (EditText)viewText.findViewById(R.id.text_input);
					editText.setBackground(getActivity().getResources().getDrawable(R.drawable.border_for_views));
					editText.setTextSize(15);

					editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					if (value != null) {
						editText.setText(value);
					}else {
						editText.setHint("  "+field.getPlaceholder()+" ");
					}
					editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

					allViews.add(editText);
					map= new HashMap<String, Object>();
					map.put("view", editText);
					map.put("field", field);
					maps.add(map);
					form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

				}else if (field.getType().equalsIgnoreCase("phone")) {

					View viewText = inflater.inflate(R.layout.text_input, null, false);
					EditText editText = (EditText)viewText.findViewById(R.id.text_input);
					editText.setBackground(getActivity().getResources().getDrawable(R.drawable.border_for_views));
					editText.setTextSize(15);

					String value = extracted(field);
					if (value != null) {
						editText.setText(value);
					}else {
						//						editText.setText(field.getPlaceholder());
						editText.setHint("  "+field.getPlaceholder()+" ");
					}
					InputFilter[] FilterArray = new InputFilter[1];
					FilterArray[0] = new InputFilter.LengthFilter(15);
					editText.setFilters(FilterArray);
					editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					editText.setInputType(InputType.TYPE_CLASS_PHONE);
					allViews.add(editText);
					map= new HashMap<String, Object>();
					map.put("view", editText);
					map.put("field", field);
					maps.add(map);
					form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

				}else if (field.getType().equalsIgnoreCase("date")) {

					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT	, LinearLayout.LayoutParams.MATCH_PARENT);
					View dateHourV = inflater.inflate(R.layout.date_input, null, false);
					Button btnDate = (Button)dateHourV.findViewById(R.id.start_date);
					String value = extracted(field);

					if (value != null) {
						btnDate.setText(value);
						String[] tmp = value.split("-");
						if (tmp.length==3) {
							//							date = new Date(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[3]));
							int jours = Integer.parseInt(tmp[3]);
							int days = Integer.parseInt(tmp[0]);
						}else {

						}

					}

					btnDate.setHint(field.getPlaceholder());
					btnDate.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					btnDate.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							final View viewFinal = v;
							OnDateSetListener callBack = new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear,
										int dayOfMonth) {
									Calendar c = Calendar.getInstance();

									Calendar c_ = Calendar.getInstance();
									c_.set(year, monthOfYear, dayOfMonth,0, 0);

									if(c.getTimeInMillis() >= c_.getTimeInMillis()) {
										((Button)viewFinal).setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
										((Button)viewFinal).setTag(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
									}
								}
							};
							final Calendar c = Calendar.getInstance();


							c.setTimeInMillis(System.currentTimeMillis() - (1000 * 60 * 60 * 24));

							int day = c.get(Calendar.DAY_OF_MONTH);
							int month = c.get(Calendar.MONTH);
							int year = c.get(Calendar.YEAR);

							DatePickerDialog datePicker = new DatePickerDialog(getActivity(), callBack , year, month, day);
							//							datePicker.updateDate(year, month, day);
							datePicker.setOwnerActivity(getActivity());
							datePicker.getDatePicker().setCalendarViewShown(false);
							datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_cart) , datePicker);
							datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.choose_cart), datePicker);
							datePicker.show();

						}
					});

					allViews.add(btnDate);
					map= new HashMap<String, Object>();
					map.put("view", btnDate);
					map.put("field", field);
					maps.add(map);
					form_input.addView(dateHourV, params);

				}else if (field.getType().equalsIgnoreCase("period")) {

					View viewPeriod = inflater.inflate(R.layout.period_input, null, false);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT	, LinearLayout.LayoutParams.MATCH_PARENT);
					params.gravity = Gravity.CENTER_VERTICAL;
					Button btnDe = (Button)viewPeriod.findViewById(R.id.start_date);
					String value = extracted(field);
					if (value != null) {
						btnDe.setText(value);
					}else {
						btnDe.setText(/*field.getPlaceholder()+"..."*/"");
					}
					btnDe.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					btnDe.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							final View viewFinal = v;
							OnDateSetListener callBack = new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear,
										int dayOfMonth) {
									//									Toast.makeText(getActivity(), "worked picker year :"+year+" month :"+monthOfYear+" day :"+dayOfMonth, Toast.LENGTH_SHORT).show();
									Calendar c = Calendar.getInstance();

									Calendar c_ = Calendar.getInstance();
									c_.set(year, monthOfYear, dayOfMonth,0, 0);

									if(c.getTimeInMillis() >= c_.getTimeInMillis()) {
										((Button)viewFinal).setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
									}
								}
							};
							final Calendar c = Calendar.getInstance();
							int year = c.get(Calendar.YEAR);
							int month = c.get(Calendar.MONTH);
							int day = c.get(Calendar.DAY_OF_MONTH);

							DatePickerDialog datePicker = new DatePickerDialog(getActivity(), callBack , 2013, 12, 8);
							datePicker.updateDate(year, month, day);
							datePicker.setOwnerActivity(getActivity());
							datePicker.getDatePicker().setCalendarViewShown(false);
							datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_cart) , datePicker);
							datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.choose_cart), datePicker);
							datePicker.show();
						}
					});
					Button btnA = (Button)viewPeriod.findViewById(R.id.end_date);
					String value2 = extracted(field);
					if (restoredFields != null && restoredFields.size()>0) {
						for (int i = 0; i < restoredFields.size(); i++) {
							if (field.getId() == restoredFields.get(i).getId()) {
								value2 = restoredFields.get(i).getExtra();
							}
						}
					}
					if (value2 != null) {
						btnA.setText(value2);
					}else {
						//						btnA.setText(field.getPlaceholder()+"...");
					}
					btnA.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					btnA.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							final View viewFinal = v;
							OnDateSetListener callBack = new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear,
										int dayOfMonth) {

									Calendar c = Calendar.getInstance();

									Calendar c_ = Calendar.getInstance();
									c_.set(year, monthOfYear, dayOfMonth,0, 0);

									if(c.getTimeInMillis() >= c_.getTimeInMillis()) {
										((Button)viewFinal).setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
									}
								}
							};
							final Calendar c = Calendar.getInstance();
							int year = c.get(Calendar.YEAR);
							int month = c.get(Calendar.MONTH);
							int day = c.get(Calendar.DAY_OF_MONTH);

							DatePickerDialog datePicker = new DatePickerDialog(getActivity(), callBack , 2013, 12, 8);
							datePicker.updateDate(year, month, day);
							datePicker.setOwnerActivity(getActivity());
							datePicker.getDatePicker().setCalendarViewShown(false);
							datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_cart) , datePicker);
							datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.choose_cart), datePicker);
							datePicker.show();
						}
					});
					allViews.add(btnDe);
					allViews.add(btnA);
					map= new HashMap<String, Object>();
					map.put("view", btnDe);
					map.put("second_view", btnA);
					map.put("field", field);
					maps.add(map);
					form_input.addView(viewPeriod, params);

				 }else if (field.getType().equalsIgnoreCase("select")) {

					final FieldSatisfaction field_tmp = field;
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT	, LinearLayout.LayoutParams.MATCH_PARENT);
					View dateHourV = inflater.inflate(R.layout.date_input, null, false);
					Button selectBtn = (Button)dateHourV.findViewById(R.id.start_date);
					selectBtn.setHint(field.getPlaceholder());
					List<ValueSatisfaction> objects = new ArrayList<ValueSatisfaction>();
                    objects = realm.where(ValueSatisfaction.class).findAllSorted("text");
                    //appController.getValueSatisfactionDao().queryForAll();
                    //objects = sortObject(objects, field_tmp);
                    //						objects = appController.getFormValueDao().queryForEq("field_id", field.getId_generated());
                    final List<ValueSatisfaction> objects2 = objects;
					selectBtn.setTag(-1);
					selectBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							selectedItem = "";
							final String[] items = setItems(objects2);
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							if (field_tmp.getPlaceholder()!=null) {
								builder.setTitle(field_tmp.getPlaceholder());
							}
							//							builder.setIcon(R.drawable.snowflake);
							selectedItem = items[0];
							v.setTag(-1);
							final Button btn = (Button)v;
							//							btn.setTag(-1);
							builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									selectedItem = items[which];
									btn.setTag(which);
									btn.setText(selectedItem);

								}
							});

							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									//									Toast toast = Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT);
									//									toast.show();
									myDialog.hide();
								}
							});
							builder.setCancelable(true);
							myDialog = builder.create();
							myDialog.show();


						}
					});

					allViews.add(selectBtn);
					map= new HashMap<String, Object>();
					map.put("view", selectBtn);
					map.put("field", field);
					maps.add(map);
					form_input.addView(dateHourV, params);


				}else if (field.getType().equalsIgnoreCase("postal_code")) {

					View viewText = inflater.inflate(R.layout.text_input, null, false);
					EditText editText = (EditText)viewText.findViewById(R.id.text_input);
					editText.setBackground(getActivity().getResources().getDrawable(R.drawable.border_for_views));
					editText.setTextSize(15);

					String value = extracted(field);
					if (value != null) {
						editText.setText(value);
					}
					editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					editText.setInputType(InputType.TYPE_CLASS_NUMBER);
					InputFilter[] FilterArray = new InputFilter[1];
					FilterArray[0] = new InputFilter.LengthFilter(5);
					editText.setFilters(FilterArray);

					editText.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {


						}
					});
					allViews.add(editText);
					map= new HashMap<String, Object>();
					map.put("view", editText);
					map.put("field", field);
					maps.add(map);
					form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

				}else if (field.getType().equalsIgnoreCase("date_hour")) {

					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT	, LinearLayout.LayoutParams.MATCH_PARENT);
					View dateHourV = inflater.inflate(R.layout.date_hour_input, null, false);
					Button btnDate = (Button)dateHourV.findViewById(R.id.start_date);
					String value = extracted(field);
					if (value != null) {
						btnDate.setText(value);
					}else {
						btnDate.setText(/*field.getPlaceholder()*/"");
						btnDate.setSingleLine();
					}
					Button btnHour = (Button)dateHourV.findViewById(R.id.start_time);
					String value2 = null;
					if (restoredFields != null && restoredFields.size()>0) {
						for (int i = 0; i < restoredFields.size(); i++) {
							if (field.getId() == restoredFields.get(i).getId()) {
								value2 = restoredFields.get(i).getExtra();
							}
						}
					}
					if (value2 != null) {
						btnHour.setText(value2);
						btnHour.setSingleLine();
					}
					btnDate.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					btnHour.setImeOptions(EditorInfo.IME_ACTION_NEXT);
					btnDate.setNextFocusForwardId(btnHour.getId());
					btnDate.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							final View viewFinal = v;
							final Calendar c = Calendar.getInstance();
							OnDateSetListener callBack = new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear,
										int dayOfMonth) {

									((Button)viewFinal).setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
								}
							};

							int year = c.get(Calendar.YEAR);
							int month = c.get(Calendar.MONTH);
							int day = c.get(Calendar.DAY_OF_MONTH);

							DatePickerDialog datePicker = new DatePickerDialog(getActivity(), callBack , 2013, 12, 8);
							datePicker.updateDate(year, month, day);
							datePicker.setOwnerActivity(getActivity());
							datePicker.getDatePicker().setCalendarViewShown(false);
							datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_cart) , datePicker);
							datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.choose_cart), datePicker);
							datePicker.show();

						}
					});

					btnHour.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							final View viewFinal = v;
							final Calendar c = Calendar.getInstance();
							OnTimeSetListener callBack = new OnTimeSetListener() {

								@Override
								public void onTimeSet(TimePicker view,
										int hourOfDay, int minute) {

									String heure = ""+hourOfDay;
									String minutes = ""+minute;
									if (heure.length() == 1) {
										heure = "0"+heure;
									}
									if (minutes.length() == 1) {
										minutes = "0"+minutes;
									}
									((Button)viewFinal).setText(heure+":"+minutes);
								}

							};
							int hour = c.get(Calendar.HOUR_OF_DAY);
							int minutes = c.get(Calendar.MINUTE);

							TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), callBack, hour, minutes, true);
							timePickerDialog.updateTime(hour, minutes);
							timePickerDialog.setOwnerActivity(getActivity());
							timePickerDialog.show();

						}
					});
					allViews.add(btnDate);
					allViews.add(btnHour);
					map= new HashMap<String, Object>();
					map.put("view", btnDate);
					map.put("second_view", btnHour);
					map.put("field", field);
					maps.add(map);
					form_input.addView(dateHourV, params);

				}

				//				tableLayout.addView(aRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));

				tableLayout.addView(aRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
				View divider_color = new View(getActivity());
				divider_color.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
				divider_color.setBackgroundColor(colors.getColor(colors.getBody_color(), "40"));
				tableLayout.addView(divider_color, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 1));
			}
		}


		return v;

	}

	private String extracted(FieldSatisfaction field) {
		String value = null;
		if (restoredFields != null && restoredFields.size()>0) {
			for (int i = 0; i < restoredFields.size(); i++) {
				if (field.getId() == restoredFields.get(i).getId()) {
					value = restoredFields.get(i).getData();
				}
			}
		}
		return value;
	}

	private Collection<FieldSatisfaction> sortFields(Collection<FieldSatisfaction> fields,
			Survey_ pSurvey) {
		Collection<FieldSatisfaction> result = new ArrayList<FieldSatisfaction>();
		for (Iterator<FieldSatisfaction> iterator = fields.iterator(); iterator.hasNext();) {
			FieldSatisfaction fieldForm = (FieldSatisfaction) iterator.next();
			if (fieldForm.getSurvey_().getId() == pSurvey.getId()) {
				result.add(fieldForm);
			}
		}
		fields = result;
		return result;

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	/**
	 * @return the pager
	 */
	public NonSwipeableViewPager getPager() {
		return pager;
	}


	/**
	 * @param pager the pager to set
	 */
	public void setPager(NonSwipeableViewPager pager) {
		this.pager = pager;
	}


	protected SatisfactionData validateAllFields(List<Map<String, Object>> list) {
		boolean error = false;
		int id_app = -1;
		int account_id = 0;
        Application application = realm.where(Application.class).findFirst();// appController.getApplicationDataDao().queryForId(1);
        if (application != null) {
            if (application.getParameters() != null) {
                account_id = application.getParameters().getAccount_id();
                id_app = application.getParameters().getId();
            }
        }
        if (account_id !=0 && id_app != -1) {
			String key = (new RandomString(16)).nextString();
			formFields = new SatisfactionData(section_id, id_app, key , "fr", new ArrayList<OrderField>(), new ArrayList<Response>());
		}else {
			return null;
		}

		restoredFields = new ArrayList<RestoredField>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			FieldSatisfaction field = (FieldSatisfaction) map.get("field");
			OrderField orderField = new OrderField(field.getId());
			View view = (View)map.get("view");
			View secondView = (View)map.get("second_view");
			String value = "";

			if (field.getType().equals("text")) {

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


			}else if (field.getType().equals("long_text") ) {

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


			}else if (field.getType().equalsIgnoreCase("phone") ) {

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

			}else if (field.getType().equals("email") ) {

				EditText editText = (EditText) view;
				value = editText.getText().toString();
				error = !Utils.isEmailValid(value);
				if (error) {
					if(!field.getOptional())
					{
						error = true;
						editText.setError("Email Invalide");
						break;
					}
				}else {
					restoredFields.add(new RestoredField(field.getId(), value, ""));
					orderField.setValue(value);
				}


			}else if (field.getType().equals("date") ) {

				Button button = (Button) view;
				value = button.getText().toString();
				Date date = Utils.ConvertToDate(value, "dd/MM/yyyy");
				if (date == null) {
					if(!field.getOptional())
					{
						button.setError("date invalide");
						error = true;
						break;
					}
				}else {
					error = false;
					restoredFields.add(new RestoredField(field.getId(), value, ""));
					orderField.setValue_date(Utils.ConvertToDate(value, "dd/MM/yyyy"));
				}



			}else if (field.getType().equals("period") ) {

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

			}else if (field.getType().equals("select") ) {
				Button button = (Button) view;
				int choice = (Integer)button.getTag();
				List<ValueSatisfaction> objects = new ArrayList<ValueSatisfaction>();
                objects = realm.where(ValueSatisfaction.class).findAllSorted("text");
                //appController.getValueSatisfactionDao().queryForAll();
               // objects = sortObject(objects, field);
                //					objects = appController.getFormValueDao().queryForEq("field_id", field.getId_generated());
                ValueSatisfaction valeur = null;
				if (choice != -1) {
					valeur = objects.get(choice);
				}
				if (valeur != null && valeur.getId() != -1) {
					value = valeur.getId() + "";
					orderField.setValue(value);
					restoredFields.add(new RestoredField(field.getId(), value, ""));
					error = false;
				}else {
					if(!field.getOptional())
					{
						error = true;
						button.setError("choix invalide");
						break;
					}
				}


			}else if (field.getType().equals("postal_code") ) {

				value = ((EditText) view).getText().toString();
				if (value.isEmpty()) {
					if(!field.getOptional())
					{
						error = true;
						((EditText) view).setError("Code postal invalide");
						break;
					}
				}else {
					error = false;
					restoredFields.add(new RestoredField(field.getId(), value, ""));
					orderField.setValue(value);
				}


			}else if (field.getType().equals("date_hour") ) {

				Button button = (Button) view;
				Button button2 = (Button) secondView;
				value = button.getText().toString();
				String value2 = button2.getText().toString();
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
		// add responses 
		if (getSurveyFragment() != null) {
			SurveyPageFragment fragment = (SurveyPageFragment) getSurveyFragment().findFragmentByPosition(0);
			List<Map<String, Object>> mapsResponse = fragment.maps;
			List<Response> responses = new ArrayList<Response>();

			for (int i = 0; i < mapsResponse.size(); i++) {
				Question question  = (Question) mapsResponse.get(i).get("question");
				int field_id = question.getId();
				Object value = ((View)mapsResponse.get(i).get("view")).getTag();
				String type = question.getType();
				Response response = new Response(field_id, value, type);
				responses.add(response);
			}
			formFields.setResponses(responses);
		}

		if (error) {
			return null;
		}else {
			return formFields;
		}

	}

	public int sendCommand(SatisfactionData formFields) {

		AppJsonWriter.PostSendAsyncTask asyncTask = new AppJsonWriter.PostSendAsyncTask(SurveySubmitFragment.this);
		if (formFields != null) {
			AppJsonWriter appJsonWriter = new AppJsonWriter(realm);
			String[] params = { URL_SURVEY, appJsonWriter.writeSatisfactionJson(formFields)};
			asyncTask.execute(params);
		}else {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getResources().getString(R.string.error));
			builder.setMessage(getResources().getString(R.string.survey_vote_incomplete_message));
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

	private List<ValueSatisfaction> sortObject(List<ValueSatisfaction> objects, FieldSatisfaction field) {
		List<ValueSatisfaction> result = new ArrayList<ValueSatisfaction>();
		for (Iterator<ValueSatisfaction> iterator = objects.iterator(); iterator.hasNext();) {
			ValueSatisfaction formValue = (ValueSatisfaction) iterator.next();
			if (formValue.getField().getId_generated() == field.getId_generated()) {
				result.add(formValue);
			}
		}
		objects = result;
		return result;
	}

	protected String[] setItems(List<ValueSatisfaction> objects) {
		String[] result = new String[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			result[i] = objects.get(i).getText();
		}

		return result;
	}


	/**
	 * @return the section_id
	 */
	public int getSection_id() {
		return section_id;
	}


	/**
	 * @param section_id the section_id to set
	 */
	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}


	/**
	 * @return the id_survey
	 */
	public int getId_survey() {
		return id_survey;
	}


	/**
	 * @param id_survey the id_survey to set
	 */
	public void setId_survey(int id_survey) {
		this.id_survey = id_survey;
	}


	@Override
	public void onPosted(boolean posted) {
		if (posted) {
			getPager().setCurrentItem(2, true);
		}else {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getResources().getString(R.string.error));
			builder.setMessage(getResources().getString(R.string.survey_connection_error));
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

	}


	/**
	 * @return the surveyFragment
	 */
	public SurveyFragment getSurveyFragment() {
		return surveyFragment;
	}


	/**
	 * @param surveyFragment the surveyFragment to set
	 */
	public void setSurveyFragment(SurveyFragment surveyFragment) {
		this.surveyFragment = surveyFragment;
	}


	@Override
	public void getResult(HashMap<String, Object> result) {
		// TODO Auto-generated method stub

	}

}
