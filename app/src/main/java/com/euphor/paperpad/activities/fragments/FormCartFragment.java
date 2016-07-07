/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Allowed_period_day;
import com.euphor.paperpad.Beans.Allowed_period_weekdays;
import com.euphor.paperpad.Beans.Application;
import com.euphor.paperpad.Beans.Cart;
import com.euphor.paperpad.Beans.CartItem;
import com.euphor.paperpad.Beans.Disallowed_period_everyday;
import com.euphor.paperpad.Beans.Field;
import com.euphor.paperpad.Beans.Value;
import com.euphor.paperpad.Beans.paymentStripe.PaymentStripe;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Constants;
import com.euphor.paperpad.utils.Installation;
import com.euphor.paperpad.utils.RestoredField;
import com.euphor.paperpad.utils.Stripe;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.utils.VerifiedCard;
import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter;
import com.euphor.paperpad.utils.jsonUtilities.AppJsonWriter.PostCallBack;
import com.euphor.paperpad.utils.jsonUtilities.Order;
import com.euphor.paperpad.utils.jsonUtilities.OrderField;
import com.euphor.paperpad.utils.jsonUtilities.ValuePeriod;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.euphor.paperpad.widgets.ObservableScrollView;
import com.euphor.paperpad.widgets.RangeTimePickerDialog;
import com.euphor.paperpad.widgets.ScrollViewListener;

//import com.google.android.gms.internal.el;
//import com.google.android.gms.internal.fo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class FormCartFragment extends DialogFragment implements PostCallBack, ScrollViewListener{

	public static final String URL_CART = Constants.BASE_URL+"/api/application/cart_submit";
	private Dialog dialog;

	private Colors colors;
	private ArrayList<View> allViews;
	private Order order;
	private List<Map<String, Object>> maps;
	private TableLayout tableLayout;
	ArrayList<RestoredField> restoredFields;
	AlertDialog beforeComandDialog;
	String mail;
	private int id_app;
	private String valueD, valueH;
	private AlertDialog myDialog;
	protected String selectedItem;
    public Realm realm;
	/**
	 * 
	 */
	public FormCartFragment() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create a new instance of MyDialogFragment
	 */
	public static FormCartFragment newInstance(int num) {
		FormCartFragment f = new FormCartFragment();
		return f;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onAttach(android.app.Activity)
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

        ((MainActivity)getActivity()).bodyFragment = "FormDialogFragment";
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
	 * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressWarnings("unused")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		((MainActivity)getActivity()).bodyFragment = "FormDialogFragment";


		View v = inflater.inflate(R.layout.form_dialog_layout, container, false);
		v.setBackgroundColor(colors.getColor(colors.getBackground_color()));
		dialog = getDialog();

		getDialog().setCancelable(false);

		tableLayout = (TableLayout)v.findViewById(R.id.tableForm);
		tableLayout.setStretchAllColumns(true);



		ObservableScrollView scrollView = (ObservableScrollView) v.findViewById(R.id.sv_form_cart);
		scrollView.setBackgroundColor(colors.getColor(colors.getForeground_color(), "80"));
		scrollView.setScrollViewListener(this);

		//
		v.findViewById(R.id.validation).setBackgroundColor(colors.getColor(colors.getForeground_color()));
		TextView labelPanier = (TextView)v.findViewById(R.id.labelPanier);
		labelPanier.setTypeface(MainActivity.FONT_BODY);
		labelPanier.setTextColor(colors.getColor(colors.getBackground_color()));

		Collection<Field> fields = new ArrayList<Field>();
		List<Cart> carts = new ArrayList<Cart>();
        carts = realm.where(Cart.class).findAll();
        //appController.getCartDao().queryForAll();
        fields =realm.where(Field.class).findAll();// appController.getFieldDao().queryForAll();

        if (carts.size()>0) {
            TextView tv_dialog = (TextView)v.findViewById(R.id.tv_dialog);
            dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
            //				dialog.setTitle(carts.get(0).getForm_label());
            labelPanier.setText(carts.get(0).getValidation_button());
            tv_dialog.setTypeface(MainActivity.FONT_BODY);
            tv_dialog.setText(carts.get(0).getForm_label());
            tv_dialog.setTextColor(colors.getColor(colors.getTitle_color()));
        }
        maps = new ArrayList<Map<String,Object>>();


		Button btnAnnuler = (Button)v.findViewById(R.id.btn_cancel_form);

		btnAnnuler.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		Button btnValider = (Button)v.findViewById(R.id.btn_valide_form);

		btnValider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				sendCommand(validateAllFields(maps));
				
			}
		});

		ColorStateList colorSelector = new ColorStateList(
				new int[][] { 
						new int[] { android.R.attr.state_pressed }, new int[] {} },
						new int[] { colors.getColor(colors.getBackground_color()), colors.getColor(colors.getTitle_color()) });

		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBody_color())));
		drawable.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		StateListDrawable drawable_ = new StateListDrawable();
		drawable_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getBody_color())));
		drawable_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		btnAnnuler.setBackgroundDrawable(drawable);
		btnAnnuler.setTextColor(colorSelector);
		btnValider.setBackgroundDrawable(drawable_);				
		btnValider.setTextColor(colorSelector);

		if (fields != null && fields.size()>0) {
			allViews = new ArrayList<View>();
			for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) {
				Field field = (Field) iterator.next();
				

				//				tableLayout.addView(aRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
				boolean addField = field.getShow().contains("always") || field.getShow().isEmpty() || (MainActivity.stripe_or_not==1 && field.getShow().contains("if_payment")) || (MainActivity.stripe_or_not==0 && field.getShow().contains("if_no_payment"));
				if (addField) {
					Map<String, Object> map = new HashMap<String, Object>();
					TableRow aRow = (TableRow) inflater.inflate(
							R.layout.form_row, null, false);
					aRow.setTag(field);
					//				LinearLayout form_label = (LinearLayout)aRow.findViewById(R.id.form_label);
					TextView form_label_tv = (AutoResizeTextView) aRow
							.findViewById(R.id.form_label_tv);
					form_label_tv.setTypeface(MainActivity.FONT_BODY);
					
					form_label_tv.setText(field.getLabel());
					form_label_tv.setTextColor(colors.getColor(colors
							.getBody_color()));
					form_label_tv
							.setTextSize(
									TypedValue.COMPLEX_UNIT_SP,
									getResources().getDimension(
											R.dimen.small_text_size)
											/ getResources()
													.getDisplayMetrics().density);
					LinearLayout form_input = (LinearLayout) aRow
							.findViewById(R.id.form_input);
					form_input.setOrientation(LinearLayout.HORIZONTAL);
					Drawable selectDrawable = getResources().getDrawable(
							R.drawable.border_for_views);
					selectDrawable.setColorFilter(
							colors.getColor(colors.getBackground_color()),
							Mode.MULTIPLY);
					if (field.getType().equalsIgnoreCase("text")) {

						View viewText = inflater.inflate(R.layout.text_input,
								null, false);
						EditText editText = (EditText) viewText
								.findViewById(R.id.text_input);
						//					editText.setBackgroundDrawable(selectDrawable);
						editText.setBackgroundDrawable(getActivity()
								.getResources().getDrawable(
										R.drawable.border_for_views));
						editText.setTextSize(15);
						String value = extracted(field);
						if (value != null) {
							//editText.setText(value);
							editText.setHint(value);
							editText.setTextSize(
									TypedValue.COMPLEX_UNIT_SP,
									getResources().getDimension(
											R.dimen.small_text_size));
						} else {
							editText.setHint("  " + field.getPlaceholder()
									+ "  ");
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
						map = new HashMap<String, Object>();
						map.put("view", editText);
						map.put("field", field);
						maps.add(map);
						form_input
								.addView(
										viewText,
										new LinearLayout.LayoutParams(
												LinearLayout.LayoutParams.MATCH_PARENT,
												LinearLayout.LayoutParams.MATCH_PARENT));

					} else if (field.getType().equalsIgnoreCase("long_text")) {

						View viewText = inflater.inflate(
								R.layout.long_text_input, null, false);
						EditText editText = (EditText) viewText
								.findViewById(R.id.text_input);
						//					editText.setBackgroundDrawable(selectDrawable);
						editText.setBackgroundDrawable(getActivity()
								.getResources().getDrawable(
										R.drawable.border_for_views));
						editText.setTextSize(15);
						String value = extracted(field);
						if (value != null) {
							//editText.setText(value);
							editText.setHint(value);
							editText.setTextSize(
									TypedValue.COMPLEX_UNIT_SP,
									getResources().getDimension(
											R.dimen.small_text_size));
						} else {
							editText.setHint("  " + field.getPlaceholder()
									+ "  ");
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
						map = new HashMap<String, Object>();
						map.put("view", editText);
						map.put("field", field);
						maps.add(map);
						form_input
								.addView(
										viewText,
										new LinearLayout.LayoutParams(
												LinearLayout.LayoutParams.MATCH_PARENT,
												LinearLayout.LayoutParams.MATCH_PARENT));

					} else if (field.getType().equalsIgnoreCase("email")) {
						String value = extracted(field);
						View viewText = inflater.inflate(R.layout.text_input,
								null, false);
						EditText editText = (EditText) viewText
								.findViewById(R.id.text_input);
						editText.setBackgroundDrawable(getActivity()
								.getResources().getDrawable(
										R.drawable.border_for_views));
						editText.setTextSize(15);

						editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
						if (value != null) {
							editText.setText(value);
							editText.setTextSize(
									TypedValue.COMPLEX_UNIT_SP,
									getResources().getDimension(
											R.dimen.small_text_size));
						} else {
							editText.setHint("  " + field.getPlaceholder()
									+ "  ");
						}
						editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

						//					editText.setOnClickListener(new OnClickListener() {
						//						
						//						@Override
						//						public void onClick(View v) {
						//
						//			                InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						//			                in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
						//						}
						//					});
						//					
						//				    editText.setOnEditorActionListener(new OnEditorActionListener() {
						//				        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						//				        	
						//				            if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
						//				                InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						//				                in.hideSoftInputFromWindow(editText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
						//				            }
						//				            return false;
						//				        }
						//				    });

						allViews.add(editText);
						map = new HashMap<String, Object>();
						map.put("view", editText);
						map.put("field", field);
						maps.add(map);
						form_input
								.addView(
										viewText,
										new LinearLayout.LayoutParams(
												LinearLayout.LayoutParams.MATCH_PARENT,
												LinearLayout.LayoutParams.MATCH_PARENT));

					} else if (field.getType().equalsIgnoreCase("phone")) {

						View viewText = inflater.inflate(R.layout.text_input,
								null, false);
						EditText editText = (EditText) viewText
								.findViewById(R.id.text_input);
						editText.setBackgroundDrawable(getActivity()
								.getResources().getDrawable(
										R.drawable.border_for_views));
						editText.setTextSize(15);
						String value = extracted(field);
						if (value != null) {
							editText.setText(value);
							editText.setTextSize(
									TypedValue.COMPLEX_UNIT_SP,
									getResources().getDimension(
											R.dimen.small_text_size));
						} else {
							editText.setHint("  " + field.getPlaceholder()
									+ "  ");
						}
						InputFilter[] FilterArray = new InputFilter[1];
						FilterArray[0] = new InputFilter.LengthFilter(15);
						editText.setFilters(FilterArray);
						editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
						editText.setInputType(InputType.TYPE_CLASS_PHONE);

//						editText.setBackgroundDrawable(selectDrawable);
						allViews.add(editText);
						map = new HashMap<String, Object>();
						map.put("view", editText);
						map.put("field", field);
						maps.add(map);
						form_input
								.addView(
										viewText,
										new LinearLayout.LayoutParams(
												LinearLayout.LayoutParams.MATCH_PARENT,
												LinearLayout.LayoutParams.MATCH_PARENT));

					} else if (field.getType().equalsIgnoreCase("date")) {

						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.MATCH_PARENT);
						View dateHourV = inflater.inflate(R.layout.date_input,
								null, false);
						Button btnDate = (Button) dateHourV
								.findViewById(R.id.start_date);
						btnDate.setTypeface(MainActivity.FONT_BODY);
						//					btnDate.setBackgroundColor(colors.getColor(colors.getBackground_color()));
						String value = extracted(field);
						//					btnDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.small_text_size));
						if (value != null) {
							btnDate.setText(value);
							
							String[] tmp = value.split("-");
							if (tmp.length == 3) {
								//							date = new Date(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[3]));
								//							int jours = Integer.parseInt(tmp[3]);
								//							int days = Integer.parseInt(tmp[0]);
							} else {

							}

						}

						btnDate.setText(field.getPlaceholder());
						btnDate.setImeOptions(EditorInfo.IME_ACTION_NEXT);
						btnDate.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final View viewFinal = v;
								OnDateSetListener callBack = new OnDateSetListener() {

									@Override
									public void onDateSet(DatePicker view,
											int year, int monthOfYear,
											int dayOfMonth) {

										Calendar c = Calendar.getInstance();

										Calendar c_ = Calendar.getInstance();
										c_.set(year, monthOfYear, dayOfMonth,
												0, 0);

										if ((c.getTimeInMillis() <= c_
												.getTimeInMillis())
												|| c.get(Calendar.DAY_OF_MONTH) <= c_
														.get(Calendar.DAY_OF_MONTH)) {
											((Button) viewFinal)
													.setText(dayOfMonth + "/"
															+ (monthOfYear + 1)
															+ "/" + year);
											((Button) viewFinal).setTag(year
													+ "-" + (monthOfYear + 1)
													+ "-" + dayOfMonth);
										}
									}
								};
								final Calendar c = Calendar.getInstance();

								int year = c.get(Calendar.YEAR);
								int month = c.get(Calendar.MONTH);
								int day = c.get(Calendar.DAY_OF_MONTH);

								DatePickerDialog datePicker = new DatePickerDialog(
										getActivity(), callBack, 2013, 12, 8);
								datePicker.updateDate(year, month, day);
								datePicker.setOwnerActivity(getActivity());
								datePicker.getDatePicker()
										.setCalendarViewShown(false);
								datePicker.show();

							}
						});

						allViews.add(btnDate);
						map = new HashMap<String, Object>();
						map.put("view", btnDate);
						map.put("field", field);
						maps.add(map);
						form_input.addView(dateHourV, params);

					} else if (field.getType().equalsIgnoreCase("period")) {

						View viewPeriod = inflater.inflate(
								R.layout.period_input, null, false);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.MATCH_PARENT);
						params.gravity = Gravity.CENTER_VERTICAL;
						Button btnDe = (Button) viewPeriod
								.findViewById(R.id.start_date);
						//					btnDe.setBackgroundColor(colors.getColor(colors.getBackground_color()));
						String value = extracted(field);
						//					btnDe.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.small_text_size));
						if (value != null) {
							btnDe.setText(value);
						} else {
							//						btnDe.setText(field.getPlaceholder()+"...");
							btnDe.setText(R.string.date_form_cart);
						}
						btnDe.setImeOptions(EditorInfo.IME_ACTION_NEXT);
						btnDe.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final View viewFinal = v;
								OnDateSetListener callBack = new OnDateSetListener() {

									@Override
									public void onDateSet(DatePicker view,
											int year, int monthOfYear,
											int dayOfMonth) {
										//									Toast.makeText(getActivity(), "worked picker year :"+year+" month :"+monthOfYear+" day :"+dayOfMonth, Toast.LENGTH_SHORT).show();
										Calendar c = Calendar.getInstance();

										Calendar c_ = Calendar.getInstance();
										c_.set(year, monthOfYear, dayOfMonth,
												0, 0);

										if ((c.getTimeInMillis() <= c_
												.getTimeInMillis())
												|| c.get(Calendar.DAY_OF_MONTH) <= c_
														.get(Calendar.DAY_OF_MONTH)) {
											((Button) viewFinal)
													.setText(dayOfMonth + "/"
															+ (monthOfYear + 1)
															+ "/" + year);
										}
									}
								};
								final Calendar c = Calendar.getInstance();
								int year = c.get(Calendar.YEAR);
								int month = c.get(Calendar.MONTH);
								int day = c.get(Calendar.DAY_OF_MONTH);

								DatePickerDialog datePicker = new DatePickerDialog(
										getActivity(), callBack, 2013, 12, 8);
								datePicker.updateDate(year, month, day);
								datePicker.setOwnerActivity(getActivity());
								datePicker.getDatePicker()
										.setCalendarViewShown(false);
								datePicker.show();
							}
						});
						Button btnA = (Button) viewPeriod
								.findViewById(R.id.end_date);
						//					btnA.setBackgroundColor(colors.getColor(colors.getBackground_color()));
						String value2 = extracted(field);
						//					btnA.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.small_text_size));
						if (restoredFields != null && restoredFields.size() > 0) {
							for (int i = 0; i < restoredFields.size(); i++) {
								if (field.getId() == restoredFields.get(i)
										.getId()) {
									value2 = restoredFields.get(i).getExtra();
								}
							}
						}
						if (value2 != null) {
							btnA.setText(value2);
						} else {
							//						btnA.setText(field.getPlaceholder()+"...");
							btnA.setText(R.string.date_form_cart);
						}
						btnA.setText(R.string.date_form_cart);
						btnA.setImeOptions(EditorInfo.IME_ACTION_NEXT);
						btnA.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final View viewFinal = v;
								OnDateSetListener callBack = new OnDateSetListener() {

									@Override
									public void onDateSet(DatePicker view,
											int year, int monthOfYear,
											int dayOfMonth) {

										Calendar c = Calendar.getInstance();

										Calendar c_ = Calendar.getInstance();
										c_.set(year, monthOfYear, dayOfMonth,
												0, 0);

										if ((c.getTimeInMillis() <= c_
												.getTimeInMillis())
												|| c.get(Calendar.DAY_OF_MONTH) <= c_
														.get(Calendar.DAY_OF_MONTH)) {
											((Button) viewFinal)
													.setText(dayOfMonth + "/"
															+ (monthOfYear + 1)
															+ "/" + year);
										}
									}
								};
								final Calendar c = Calendar.getInstance();
								int year = c.get(Calendar.YEAR);
								int month = c.get(Calendar.MONTH);
								int day = c.get(Calendar.DAY_OF_MONTH);

								DatePickerDialog datePicker = new DatePickerDialog(
										getActivity(), callBack, 2013, 12, 8);
								datePicker.updateDate(year, month, day);
								datePicker.setOwnerActivity(getActivity());
								datePicker.getDatePicker()
										.setCalendarViewShown(false);
								datePicker.show();
							}
						});
						allViews.add(btnDe);
						allViews.add(btnA);
						map = new HashMap<String, Object>();
						map.put("view", btnDe);
						map.put("second_view", btnA);
						map.put("field", field);
						maps.add(map);
						form_input.addView(viewPeriod, params);

					} else if (field.getType().equalsIgnoreCase("select")) {

						final Field field_tmp = field;
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.MATCH_PARENT);
						View dateHourV = inflater.inflate(R.layout.date_input,
								null, false);
						Button selectBtn = (Button) dateHourV
								.findViewById(R.id.start_date);
						selectBtn.setTextSize(15);
						selectBtn.setHint(field.getPlaceholder());
						List<Value> objects = new ArrayList<Value>();
                        objects = field_tmp.getValues();// realm.where(Value.class).findAll();//appController.getValueDao().queryForAll();
                     //   objects = sortObject(objects, field_tmp);
                        //						objects = appController.getFormValueDao().queryForEq("field_id", field.getId_generated());
                        final List<Value> objects2 = objects;
						selectBtn.setTag(-1);
						selectBtn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								selectedItem = "";
								final String[] items = setItems(objects2);
								AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
								if (field_tmp.getPlaceholder() != null) {
									builder.setTitle(field_tmp.getPlaceholder());
								}
								//							builder.setIcon(R.drawable.snowflake);
								try {
									selectedItem = items[0];
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								v.setTag(-1);
								final Button btn = (Button) v;
								//							btn.setTag(-1);
								builder.setSingleChoiceItems(items, -1,
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												selectedItem = items[which];
												btn.setTag(which);
												btn.setText(selectedItem);
												myDialog.hide();
											}
										});

								/*builder.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												//									Toast toast = Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT);
												//									toast.show();
												//									btn.setTag(which);
												btn.setText(selectedItem);
												myDialog.hide();
											}
										});

								builder.setNegativeButton(
										getActivity().getResources().getString(
												R.string.cancel_cart),
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												//									Toast toast = Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT);
												//									toast.show();

												myDialog.hide();
											}
										});*/

								builder.setCancelable(true);
								myDialog = builder.create();
								myDialog.show();

							}
						});

						allViews.add(selectBtn);
						map = new HashMap<String, Object>();
						map.put("view", selectBtn);
						map.put("field", field);
						maps.add(map);
						form_input.addView(dateHourV, params);

					} else if (field.getType().equalsIgnoreCase("postal_code")) {

						View viewText = inflater.inflate(R.layout.text_input,
								null, false);
						EditText editText = (EditText) viewText
								.findViewById(R.id.text_input);
						editText.setBackgroundDrawable(getActivity()
								.getResources().getDrawable(
										R.drawable.border_for_views));
						editText.setTextSize(15);
						String value = extracted(field);
						editText.setTextSize(
								TypedValue.COMPLEX_UNIT_SP,
								getResources().getDimension(
										R.dimen.small_text_size));
						if (value != null) {
							editText.setText(value);
						} else {
							editText.setHint("  " + field.getPlaceholder()
									+ "  ");
						}
						editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
						editText.setInputType(InputType.TYPE_CLASS_NUMBER);
						InputFilter[] FilterArray = new InputFilter[1];
						FilterArray[0] = new InputFilter.LengthFilter(5);
						editText.setFilters(FilterArray);
//						editText.setBackgroundDrawable(selectDrawable);
						editText.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

							}
						});
						allViews.add(editText);
						map = new HashMap<String, Object>();
						map.put("view", editText);
						map.put("field", field);
						maps.add(map);
						form_input
								.addView(
										viewText,
										new LinearLayout.LayoutParams(
												LinearLayout.LayoutParams.MATCH_PARENT,
												LinearLayout.LayoutParams.MATCH_PARENT));

					} else if (field.getType().equalsIgnoreCase("date_hour")) {

						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.MATCH_PARENT);
						View dateHourV = inflater.inflate(
								R.layout.date_hour_input, null, false);
						Button btnDate = (Button) dateHourV
								.findViewById(R.id.start_date);
						//					btnDate.setBackgroundColor(colors.getColor(colors.getBackground_color()));
						final Calendar c = Calendar.getInstance();
						int year = c.get(Calendar.YEAR);
						int month = c.get(Calendar.MONTH);
						int day = c.get(Calendar.DAY_OF_MONTH);

						String value = day + "/" + (month + 1) + "/" + year; //extracted(field);
						//					btnDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.small_text_size));
						if (value != null) {
							btnDate.setText(value);
						} else {
							//						btnDate.setText(field.getPlaceholder());
							btnDate.setSingleLine();
						}
						Button btnHour = (Button) dateHourV
								.findViewById(R.id.start_time);
						//					btnHour.setBackgroundColor(colors.getColor(colors.getBackground_color()));
						//					btnHour.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.small_text_size));
						String value2 = null;
						if (restoredFields != null && restoredFields.size() > 0) {
							for (int i = 0; i < restoredFields.size(); i++) {
								if (field.getId() == restoredFields.get(i)
										.getId()) {
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
						btnDate.setText(value);//R.string.date_form_cart);
						final Allowed_period_day apd = realm.where(Allowed_period_day.class).equalTo("id_generated",field.getId_f()).findFirst();
						//appController.getAllowedPeriodByFieldIdDB(field.getId_generated());
						final Allowed_period_weekdays apw = realm.where(Allowed_period_weekdays.class).equalTo("id_generated",field.getId_f()).findFirst();
						//appController.getAllowedWeekDaysByFieldIdDB(field.getId_generated());
						final Disallowed_period_everyday dpe = realm.where(Disallowed_period_everyday.class).equalTo("id_generated",field.getId_f()).findFirst();
						//appController.getDisallowedEverydayByFieldIdDB(field.getId_generated());
						btnDate.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final View viewFinal = v;
								//final Calendar c = Calendar.getInstance();
								OnDateSetListener callBack = new OnDateSetListener() {

									@Override
									public void onDateSet(DatePicker view,
											int year, int monthOfYear,
											int dayOfMonth) {

										Calendar c = Calendar.getInstance();

										Calendar c_ = Calendar.getInstance();
										c_.set(year, monthOfYear, dayOfMonth);

										if ((c.getTimeInMillis() <= c_
												.getTimeInMillis()) /*&&  valueH.compareTo(c_.get(Calendar.HOUR)+" : "+c_.get(Calendar.MINUTE)) >= 0*/) {
											((Button) viewFinal)
													.setText(dayOfMonth + "/"
															+ (monthOfYear + 1)
															+ "/" + year);
											valueD = year + "/"
													+ (monthOfYear + 1) + "/"
													+ dayOfMonth;
										} else {
											AlertDialog.Builder builder = new AlertDialog.Builder(
													getActivity());
											builder.setTitle(
													getString(R.string.time_error_title))
													.setMessage(
															getString(R.string.time_error_msg))

													.setPositiveButton(
															getString(R.string.time_error_btn),
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {
																	dialog.cancel();
																}
															});

											builder.create().show(); // create and show the alert dialog
											((Button) viewFinal).setText(c_
													.get(Calendar.DAY_OF_MONTH)
													+ "/"
													+ (c_.get(Calendar.MONTH) + 1)
													+ "/"
													+ c_.get(Calendar.YEAR));

										}

									}
								};

								int year = c.get(Calendar.YEAR);
								int month = c.get(Calendar.MONTH);
								int day = c.get(Calendar.DAY_OF_MONTH);

								final DatePickerDialog datePickerDialog = new DatePickerDialog(
										getActivity(), callBack, year, month,
										day);
								datePickerDialog.updateDate(year, month, day);
								datePickerDialog.setOwnerActivity(getActivity());
								datePickerDialog.getDatePicker()
										.setCalendarViewShown(false);
								datePickerDialog.setButton(
										DatePickerDialog.BUTTON_NEGATIVE,
										getResources().getString(
												R.string.cancel_cart),
										datePickerDialog);
//								datePickerDialog.setButton(
//										DatePickerDialog.BUTTON_POSITIVE,
//										getResources().getString(
//												R.string.choose_cart),
//										datePickerDialog);
								
								datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.choose_cart), new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										int day = datePickerDialog.getDatePicker().getDayOfMonth();
										int month = datePickerDialog.getDatePicker().getMonth();
										int year  = datePickerDialog.getDatePicker().getYear();
										long date = datePickerDialog.getDatePicker().getCalendarView().getDate();
										Calendar now  = Calendar.getInstance();
										Calendar calendar = Calendar.getInstance();
										calendar.setTimeInMillis(date);
										Log.i("test date", day+" date : "+date+ " calendar "+ calendar.toString());
										
										if (apd != null) {

											now.add(Calendar.DAY_OF_MONTH, apd.getDay());
											if (now.get(Calendar.DAY_OF_MONTH) == day && now.get(Calendar.MONTH) == month && now.get(Calendar.YEAR) == year ) {
												Toast.makeText(getActivity(), "date correct", Toast.LENGTH_LONG).show();
											}else {
												Toast.makeText(getActivity(), "date incorrect", Toast.LENGTH_LONG).show();

												AlertDialog.Builder builder = new AlertDialog.Builder(
														getActivity());
												builder.setTitle(getString(R.string.time_error_title)).setMessage(getString(R.string.text_order_period_day, apd.getDay()))
												.setPositiveButton(getString(R.string.time_error_btn), new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int id) {
														dialog.cancel();
														datePickerDialog.show();
													}
												});

												builder.create().show(); // create and show the alert dialog


											}
										}else if (apw != null) {
											if (apw.getList_allowed_days() != null && apw.getList_allowed_days().size()>0) {
												boolean corrct = false;
												int index = 0;
												String allowedDays = "";
												for (Iterator<MyString> iterator2 = apw.getList_allowed_days().iterator(); iterator2.hasNext();) {
                                                    MyString allowedDay = (MyString) iterator2.next();
													index++;
													if (allowedDay.getMyString().equalsIgnoreCase("Sunday")) {
														allowedDays = allowedDays + getString(R.string.sunday);
													}else if (allowedDay.getMyString().equalsIgnoreCase("monday")) {
														allowedDays = allowedDays + getString(R.string.monday);
													}else if (allowedDay.getMyString().equalsIgnoreCase("tuesday")) {
														allowedDays = allowedDays + getString(R.string.tuesday);
													}else if (allowedDay.getMyString().equalsIgnoreCase("WEDNESDAY")) {
														allowedDays = allowedDays + getString(R.string.wednesday);
													}else if (allowedDay.getMyString().equalsIgnoreCase("THURSDAY")) {
														allowedDays = allowedDays + getString(R.string.thursday);
													}else if (allowedDay.getMyString().equalsIgnoreCase("FRIDAY")) {
														allowedDays = allowedDays + getString(R.string.friday);
													}else if (allowedDay.getMyString().equalsIgnoreCase("SATURDAY")) {
														allowedDays = allowedDays + getString(R.string.saturday);
													}
													
													if (apw.getList_allowed_days().size()-1 > 0 && index == apw.getList_allowed_days().size()-1) {
														allowedDays = allowedDays + " et ";
													}else if (index == apw.getList_allowed_days().size()) {
													}else {
														allowedDays = allowedDays + ", ";
													}
													if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {// sunday
														if (allowedDay.getMyString().equalsIgnoreCase("Sunday")) {
															corrct = corrct || true;
														}else {
															corrct = corrct || false;
														}
													}else if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {// monday
														if (allowedDay.getMyString().equalsIgnoreCase("monday")) {
															corrct = corrct || true;
														}else {
															corrct = corrct || false;
														}
													}else if (calendar.get(Calendar.DAY_OF_WEEK) == 3) {// tuesday
														if (allowedDay.getMyString().equalsIgnoreCase("tuesday")) {
															corrct = corrct || true;
														}else {
															corrct = corrct || false;
														}
													}else if (calendar.get(Calendar.DAY_OF_WEEK) == 4) {
														if (allowedDay.getMyString().equalsIgnoreCase("WEDNESDAY")) {
															corrct = corrct || true;
														}else {
															corrct = corrct || false;
														}
													}else if (calendar.get(Calendar.DAY_OF_WEEK) == 5) {
														if (allowedDay.getMyString().equalsIgnoreCase("THURSDAY")) {
															corrct = corrct || true;
														}else {
															corrct = corrct || false;
														}


													}else if (calendar.get(Calendar.DAY_OF_WEEK) == 6) {
														if (allowedDay.getMyString().equalsIgnoreCase("FRIDAY")) {
															corrct = corrct || true;
														}else {
															corrct = corrct || false;
														}


													}else if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
														if (allowedDay.getMyString().equalsIgnoreCase("SATURDAY")) {
															corrct = corrct || true;
														}else {
															corrct = corrct || false;
														}


													}

												}
												if (!corrct) {

													AlertDialog.Builder builder = new AlertDialog.Builder(
															getActivity());
													builder.setTitle(getString(R.string.time_error_title)).setMessage(getString(R.string.text_order_period, apw.getStart_hour(), apw.getEnd_hour(), allowedDays))
													.setPositiveButton(getString(R.string.time_error_btn), new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog, int id) {
															dialog.cancel();
															datePickerDialog.show();
														}
													});

													builder.create().show(); // create and show the alert dialog


												}
											}
										}else if (dpe != null) {
											
										}
//										dialog.dismiss();
										
									}
								});

								datePickerDialog.show();

							}
						});

						c.setTimeInMillis(System.currentTimeMillis()
								+ (1000 * 60 * 29));
						int hour = c.get(Calendar.HOUR_OF_DAY);
						int minutes = c.get(Calendar.MINUTE);

						valueH = hour + " : " + minutes;

						btnHour.setText(valueH);//R.string.time_form_cart);
						valueD = year + "/" + (month + 1) + "/" + day;
						btnHour.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final View viewFinal = v;
								final Calendar c = Calendar.getInstance();
								OnTimeSetListener callBack = new OnTimeSetListener() {

									@Override
									public void onTimeSet(TimePicker view,
											int hourOfDay, int minute) {

										String heure = "" + hourOfDay;
										String minutes = "" + minute;
										if (heure.length() == 1) {
											heure = "0" + heure;
										}
										if (minutes.length() == 1) {
											minutes = "0" + minutes;
										}
										Calendar c = Calendar.getInstance();
										int year = c.get(Calendar.YEAR);
										int month = c.get(Calendar.MONTH);
										int day = c.get(Calendar.DAY_OF_MONTH);

										Calendar c_ = Calendar.getInstance();
										c_.set(Calendar.HOUR_OF_DAY, hourOfDay);
										c_.set(Calendar.MINUTE, minute);
										//									Date d = new Date();
										//									d.setHours(hourOfDay);
										//									d.setMinutes(minute);
										if (((c.getTimeInMillis() <= c_
												.getTimeInMillis()))
												&& valueD.compareTo(year + "/"
														+ (month + 1) + "/"
														+ day) >= 0) {//.get(Calendar.HOUR_OF_DAY) <= hourOfDay && c.get(Calendar.MINUTE) <= minute)){ 

											((Button) viewFinal).setText(heure+ " : " + minutes);
											valueH = heure + " : " + minutes;
										} else {

											AlertDialog.Builder builder = new AlertDialog.Builder(
													getActivity());
											builder.setTitle(
													getString(R.string.time_error_title))
													.setMessage(
															getString(R.string.time_error_msg))
													.setPositiveButton(
															getString(R.string.time_error_btn),
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {
																	dialog.cancel();
																}
															});

											builder.create().show(); // create and show the alert dialog

											heure = ""
													+ c.get(Calendar.HOUR_OF_DAY);
											minutes = ""
													+ c.get(Calendar.MINUTE);
											if (heure.length() == 1) {
												heure = "0" + heure;
											}
											if (minutes.length() == 1) {
												minutes = "0" + minutes;
											}
											((Button) viewFinal).setText(heure
													+ ":" + minutes);
										}
									}

								};

								c.setTimeInMillis(System.currentTimeMillis()
										+ (1000 * 60 * 30));
								int hour = c.get(Calendar.HOUR_OF_DAY);
								int minutes = c.get(Calendar.MINUTE);

								valueH = hour + " : " + minutes;
								if (dpe != null) {
									String maxTime = dpe.getEnd_hour();
									String[] time = maxTime.split("H");
									if (time.length > 1) {
										try {
											hour = Integer.parseInt(time[0]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										try {
											minutes = Integer.parseInt(time[1]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								}
								

								RangeTimePickerDialog timePickerDialog = new RangeTimePickerDialog(
										getActivity(), callBack, hour, minutes,
										true);
								if (apd != null) {
									String minTime = apd.getStart_hour();
									String[] time =  minTime.split("H");
									if (time.length > 1) {
										int hourStart = 0;
										try {
											hourStart = Integer.parseInt(time[0]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										int minute = 0;
										try {
											minute = Integer.parseInt(time[1]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										hour = hourStart;
										minutes = minute;
										timePickerDialog.setMin(hourStart, minute );
									}
									
									String maxTime = apd.getEnd_hour();
									time = maxTime.split("H");
									if (time.length > 1) {
										int hourStart = 0;
										try {
											hourStart = Integer.parseInt(time[0]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										int minute = 0;
										try {
											minute = Integer.parseInt(time[1]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										timePickerDialog.setMax(hourStart, minute );
									}
									
								}else if (apw != null) {
									String minTime = apw.getStart_hour();
									String[] time =  minTime.split("H");
									if (time.length > 1) {
										int hourStart = 0;
										try {
											hourStart = Integer.parseInt(time[0]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										int minute = 0;
										try {
											minute = Integer.parseInt(time[1]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										hour = hourStart;
										minutes = minute;
										
										timePickerDialog.setMin(hourStart, minute );
									}
									
									String maxTime = apw.getEnd_hour();
									time = maxTime.split("H");
									if (time.length > 1) {
										int hourStart = 0;
										try {
											hourStart = Integer.parseInt(time[0]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										int minute = 0;
										try {
											minute = Integer.parseInt(time[1]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										timePickerDialog.setMax(hourStart, minute );
									}
									
								}else if (dpe != null) {
									timePickerDialog.setReverseTime(true);
									String minTime = dpe.getStart_hour();
									String[] time =  minTime.split("H");
									if (time.length > 1) {
										int hourStart = 0;
										try {
											hourStart = Integer.parseInt(time[0]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										int minute = 0;
										try {
											minute = Integer.parseInt(time[1]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										timePickerDialog.setMin(hourStart, minute );
									}
									
									String maxTime = dpe.getEnd_hour();
									time = maxTime.split("H");
									if (time.length > 1) {
										int hourStart = 0;
										try {
											hourStart = Integer.parseInt(time[0]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										int minute = 0;
										try {
											minute = Integer.parseInt(time[1]);
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										hour = hourStart;
										minutes = minute;
										timePickerDialog.setMax(hourStart, minute );
									}
									
								}
								timePickerDialog.updateTime(hour, minutes);
								timePickerDialog.setOwnerActivity(getActivity());
								timePickerDialog.setButton(
										DatePickerDialog.BUTTON_NEGATIVE,
										getResources().getString(
												R.string.cancel_cart),
										timePickerDialog);
								timePickerDialog.setButton(
										DatePickerDialog.BUTTON_POSITIVE,
										getResources().getString(
												R.string.choose_cart),
										timePickerDialog);
								

								timePickerDialog.show();

							}
						});
						allViews.add(btnDate);
						allViews.add(btnHour);
						map = new HashMap<String, Object>();
						map.put("view", btnDate);
						map.put("second_view", btnHour);
						map.put("field", field);
						maps.add(map);
						form_input.addView(dateHourV, params);

					}
					tableLayout.addView(aRow, new TableLayout.LayoutParams(
							TableLayout.LayoutParams.MATCH_PARENT,
							TableLayout.LayoutParams.WRAP_CONTENT));
					View divider_color = new View(getActivity());
					divider_color.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT, 1));
					divider_color.setBackgroundColor(colors.getColor(
							colors.getBody_color(), "40"));
					tableLayout.addView(divider_color,
							new TableLayout.LayoutParams(
									TableLayout.LayoutParams.MATCH_PARENT, 1));
				}
			}
		}


		v.setLayoutParams(new FrameLayout.LayoutParams(450, FrameLayout.LayoutParams.WRAP_CONTENT));
		return v ;
	}

	private String extracted(Field field) {
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




	protected Order validateAllFields(List<Map<String, Object>> list) {
		boolean error = false;
		int account_id = -1;
		id_app = -1;
        Application application = realm.where(Application.class).findFirst();
        //appController.getApplicationDataDao().queryForId(1);
        if (application != null) {
            if (application.getParameters() != null) {
                account_id = application.getParameters().getAccount_id();
                id_app = application.getParameters().getId();
            }
        }
        if (account_id !=-1 && id_app != -1) {
			String pushToken = ((MainActivity)getActivity()).regid;

			String application_unique_identifier = Installation.id(getActivity());
			order = new Order(account_id, id_app, "jndhsckgcfhscndqhjhk5855xs4s7saZkjANJVWkj", "fr", new ArrayList<OrderField>(), null, pushToken, "0",application_unique_identifier );
		}else {
			return null;
		}

		restoredFields = new ArrayList<RestoredField>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Field field = (Field) map.get("field");
			OrderField orderField = new OrderField(field.getId());
			View view = (View)map.get("view");
			View secondView = (View)map.get("second_view");
			String value = "";
			boolean isOptional = false;
			if (field.getOptional() ) {
				isOptional = field.getOptional();
			}

			if (field.getType().equals("text")) {

				value = ((EditText) view).getText().toString();
				
				String regExpn = "[a-zA-Z]{3,}?[0-9]{0,}?";

				CharSequence inputStr = value;

				Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(inputStr);

				if (matcher.matches()){
					error = false;
					restoredFields.add(new RestoredField(field.getId(), value, ""));
					orderField.setValue(value);
				}
					
				else{
					if(!field.getOptional())
					{
						error = true;
						((EditText) view).setError("Veuillez remplir ce champ");
						break;
					}
				}
//				if (value.isEmpty() || value.length() < 3) {
//					if(!field.getOptional())
//					{
//						error = true;
//						((EditText) view).setError("veuillez remplir ce champ");
//						break;
//					}
//				}else {
//					error = false;
//					restoredFields.add(new RestoredField(field.getId(), value, ""));
//					orderField.setValue(value);
//				}


			}else if (field.getType().equals("long_text")) {

				value = ((EditText) view).getText().toString();
				if (value.isEmpty()) {
					if(!field.getOptional())
					{
						error = true;
						((EditText) view).setError("veuillez remplir ce champ");
						break;
					}
					error = true;
					((EditText) view).setError("veuillez remplir ce champ");
					break;
				}else {
					error = false;
					restoredFields.add(new RestoredField(field.getId(), value, ""));
					orderField.setValue(value);
				}


			}else if (field.getType().equalsIgnoreCase("phone")) {

				value = ((EditText) view).getText().toString();
				if (value.isEmpty() || value.length() < 8) {
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
						editText.setError("Email Invalide");
						break;
					}
				}else {
					mail = value;
					restoredFields.add(new RestoredField(field.getId(), value, ""));
					orderField.setValue(value);
				}


			}else if (field.getType().equals("date")) {

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



			}else if (field.getType().equals("period")) {

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
					error = true;
					break;
				}

			}else if (field.getType().equals("select")) {
				Button button = (Button) view;
				int choice = (Integer)button.getTag();
				List<Value> objects = new ArrayList<Value>();
                objects = field.getValues();// realm.where(Value.class).findAll();
                //appController.getValueDao().queryForAll();
              //  objects = sortObject(objects, field);
                //					objects = appController.getFormValueDao().queryForEq("field_id", field.getId_generated());

                if (objects.size()>0) {
					Value valeur = null;
					if (choice != -1) {
						valeur = objects.get(choice);
					}
					if (valeur != null && valeur.getId() != -1) {
						value = valeur.getId() + "";
						orderField.setValue(value);
						restoredFields.add(new RestoredField(field.getId(),
								value, ""));
						error = false;
					} else {
						if (!field.getOptional()) {
							error = true;
							button.setError("choix invalide");
							break;
						}

					}
				}else {
					error = false;
				}	
			}else if (field.getType().equals("postal_code")) {

				value = ((EditText) view).getText().toString();
//				String regExpn = "[0-9]{4,}?";
//
//				CharSequence inputStr = value;
//
//				Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
//				Matcher matcher = pattern.matcher(inputStr);
//
//				if (matcher.matches()){
//					error = false;
//					restoredFields.add(new RestoredField(field.getId(), value, ""));
//					orderField.setValue(value);
//				}
//					
//				else{
//					if(!field.getOptional())
//					{
//						error = true;
//						((EditText) view).setError("Code postal invalide");
//						break;
//					}
//				}		
				
				if (value.isEmpty() || value.length() != 5) {
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
				order.getFields().add(orderField);
			}else {
				return null;
			}

		}

		if (error) {
			return null;
		}else {
			return order;
		}

	}


	public int sendCommand(Order order) {

		AppJsonWriter.PostSendAsyncTask asyncTask = new AppJsonWriter.PostSendAsyncTask(FormCartFragment.this);
		if (order != null) {
			
			AppJsonWriter appJsonWriter = new AppJsonWriter(realm);
			List<CartItem> items = new ArrayList<CartItem>();
            items = realm.where(CartItem.class).findAll();
            //appController.getCartItemDao().queryForAll();

      /*  protected void addItemToDB(CartItem cartItem) {
            realm.beginTransaction();
            //appController.getCartItemDao().createOrUpdate(cartItem);
            realm.where(CartItem.class).findAll().add(cartItem);
            realm.copyToRealmOrUpdate(cartItem);
            realm.commitTransaction();

        }*/
			order.setProducts(appJsonWriter.getProductsFromCartItems(items));
			String[] params = { URL_CART, appJsonWriter.writeJson(order) };
			asyncTask.execute(params);
			
		}
		return asyncTask.status;
	}


	private static ArrayList<View> getViewsByTag(ViewGroup root, String tag){
		ArrayList<View> views = new ArrayList<View>();
		final int childCount = root.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = root.getChildAt(i);
			if (child instanceof ViewGroup) {
				views.addAll(getViewsByTag((ViewGroup) child, tag));
			}

			final Object tagObj = child.getTag();
			if (tagObj != null && tagObj.equals(tag)) {
				views.add(child);
			}

		}
		return views;
	}



	protected ArrayList<RestoredField> retainFields(List<Map<String, Object>> list) {
		restoredFields = new ArrayList<RestoredField>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Field field = (Field) map.get("field");
			OrderField orderField = new OrderField(field.getId());
			View view = (View)map.get("view");
			View secondView = (View)map.get("second_view");
			String value = "";

			if (field.getType().equals("text")) {

				value = ((EditText) view).getText().toString();
				restoredFields.add(new RestoredField(field.getId(), value, ""));
				orderField.setValue(value);


			}else if (field.getType().equalsIgnoreCase("phone")) {

				value = ((EditText) view).getText().toString();
				restoredFields.add(new RestoredField(field.getId(), value, ""));
				orderField.setValue(value);

			}else if (field.getType().equals("email")) {

				EditText editText = (EditText) view;
				value = editText.getText().toString();
				restoredFields.add(new RestoredField(field.getId(), value, ""));
				orderField.setValue(value);


			}else if (field.getType().equals("date")) {

				Button button = (Button) view;
				value = button.getText().toString();
				Date date = Utils.ConvertToDate(value, "dd/MM/yyyy");
				restoredFields.add(new RestoredField(field.getId(), value, ""));
				orderField.setValue_date(Utils.ConvertToDate(value, "dd/MM/yyyy"));



			}else if (field.getType().equals("period")) {

				Button button = (Button) view;
				Button button2 = (Button) secondView;
				Date date = Utils.ConvertToDate(button.getText().toString(), "dd/MM/yyyy");
				Date date1 = Utils.ConvertToDate(button2.getText().toString(), "dd/MM/yyyy");
				restoredFields.add(new RestoredField(field.getId(), button.getText().toString(), button2.getText().toString()));

			}else if (field.getType().equals("select")) {

				Button btn = (Button) view;
//				Value valeur = (Value)(spinner.getItemAtPosition(spinner.getSelectedItemPosition()));
				int position = (Integer) btn.getTag();
				restoredFields.add(new RestoredField(field.getId(), ""+position, ""));

			}else if (field.getType().equals("postal_code")) {

				value = ((EditText) view).getText().toString();
				restoredFields.add(new RestoredField(field.getId(), value, ""));


			}else if (field.getType().equals("date_hour")) {

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

				restoredFields.add(new RestoredField(field.getId(), value, value2));


			}

		}

		return restoredFields;

	}

	@Override
	public void onStop() {
		restoredFields =  retainFields(maps);
		((MainActivity)getActivity()).extras = new Bundle();
		((MainActivity)getActivity()).extras.putParcelableArrayList("Restored_fields", restoredFields);
		if (dialog != null) {
			dialog.dismiss();
		}
		super.onStop();
	}

	@Override
	public void onPosted(boolean posted) {
		if (posted) {
						dialog.dismiss();
			Toast.makeText(getActivity(), getString(R.string.command_validated), Toast.LENGTH_SHORT).show();
			//appController.emptyCartItems();
        /*    realm.beginTransaction();
            realm.where(CartItem.class).findAll().clear();
            realm.commitTransaction();*/
			((MainActivity)getActivity()).fillCart();

		}else {
			Toast.makeText(getActivity(), getString(R.string.command_not_validated), Toast.LENGTH_SHORT).show();
		}


	}

	@Override
	public void getResult(HashMap<String, Object> result) {
		int status = (Integer)result.get("Status");
		String response = (String) result.get("Result");
		if (status == 200) {

            Realm  r = Realm.getInstance(getActivity());
            r.beginTransaction();
            r.createOrUpdateObjectFromJson(PaymentStripe.class,response);
            r.commitTransaction();
            PaymentStripe paymentStripe = realm.where(PaymentStripe.class).findFirst();
			/*	PaymentStripe paymentStripe = mapper.readValue(response, PaymentStripe.class);*/
            //				Log.i("Stripe", paymentStripe.getPayment_info().getOrder_id());
				StripePaymentFragment fragment = new StripePaymentFragment();
				int orderId = 0;
				if (paymentStripe!= null) {
                    // pour payement stripe //
					if (paymentStripe.getPayment_info() != null && realm !=null) {
                        Stripe.SaveStripeInfo(paymentStripe.getPayment_info(), realm);
                       /* appController.SaveStripeInfo(paymentStripe.getPayment_info());*/
						orderId = Integer.parseInt(paymentStripe.getPayment_info().getOrder_id());
						VerifiedCard card = new VerifiedCard("", id_app, orderId, mail);
						fragment.setCard(card ); /*activer avec stripe */
						//			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
						dialog.dismiss();
						fragment.setStyle( DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog );
						fragment.show(getActivity().getSupportFragmentManager(), "Stripe");

					}
				}


        }


	}

	@Override
	public void onScrollChanged(ObservableScrollView observableScrollView,
			int x, int y, int oldx, int oldy) {
		View view  = (View) maps.get(0).get("view");
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

	}
	
	private List<Value> sortObject(List<Value> objects, Field field) {
		List<Value> result = new ArrayList<Value>();
		for (Iterator<Value> iterator = objects.iterator(); iterator.hasNext();) {
			Value formValue = (Value) iterator.next();
			if (formValue.getField().getId_f() == field.getId_f()) {
				result.add(formValue);
			}
		}
		objects = result;
		return result;
	}
	
	protected String[] setItems(List<Value> objects) {
		String[] result = new String[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			result[i] = objects.get(i).getText();
		}

		return result;
	}
	
	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setOnDismissListener(null);
		super.onDestroyView();
	}




}
