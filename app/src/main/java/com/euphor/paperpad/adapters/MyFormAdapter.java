package com.euphor.paperpad.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Contact;
import com.euphor.paperpad.Beans.FieldFormContact;
import com.euphor.paperpad.Beans.FormValue;

import com.euphor.paperpad.utils.Colors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

public class MyFormAdapter extends ArrayAdapter<FieldFormContact> {

	private MainActivity mainActivity;
	private List<FieldFormContact> elements;
	private String[] mapField, mapFieldSup;
	//	private ImageLoader imageLoader;
	///private DisplayImageOptions opts ;
	//	private DisplayImageOptions options;
	private Colors colors;
	//	private MainActivity mainActivity;
	//	private Parameters parameters;
	private int layout_item;
	private Realm realm;

	LayoutInflater inflater;
	List<FormValue> objects;
	String initialValue;
	
	//private ArrayList<RestoredField> restoredFields;
	private List<Map<String, Object>> maps;
	protected String selectedItem;
	
	@Override
	public int getCount() {

		return elements.size();
	}


	@Override
	public long getItemId(int position) {

		return position;
	}

	//	@Override
	//	public int getPosition(String item) {
	//		
	//		return super.getPosition(item);
	//	}

	@Override
	public int getPosition(FieldFormContact item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
	}


	public void setInitialValue(String initialValue){
		this.initialValue = initialValue;
		if(!initialValue.isEmpty()){
			this.notifyDataSetInvalidated();
			//this.notifyDataSetChanged();
		}
	}
	
	public String getInitialValue(){
		return this.initialValue;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		FieldFormContact field = getItem(position);
		RecordHolder holder = new RecordHolder();
		
		return getFormView(position, convertView, parent, holder, field);
	}



	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public void setDropDownViewResource(int resource) {
		super.setDropDownViewResource(resource);
	}

	@Override
	public void setNotifyOnChange(boolean notifyOnChange) {
		super.setNotifyOnChange(notifyOnChange);
	}


	public MyFormAdapter(MainActivity activity,List<FieldFormContact> elements, /*ArrayList<RestoredField> restoredFields,*/ Realm realm, Colors colors, int layout_item) {

		super(activity, layout_item, elements);   // pour Realm
		//this.txtSize = txtSize;
		this.mainActivity = activity;
		this.elements = elements;
		//this.restoredFields = (restoredFields == null) ? new ArrayList<RestoredField>() : restoredFields;

		this.colors = colors;
		this.layout_item = layout_item;
		maps = new ArrayList<Map<String,Object>>();
		this.realm=realm;
		mapField = new String[elements.size()];
		mapFieldSup = new String[elements.size()];
		
		objects = new ArrayList<FormValue>();

        objects = realm.where(FormValue.class).findAll();
        //appController.getFormValueDao().queryForAll();

        //				objects = appController.getFormValueDao().queryForEq("field_id", field.getId_generated());

        initialValue = "";
		
	}





	static class RecordHolder {
		TextView txtTitle;
		EditText editText;
		LinearLayout form_input;
		int position;

	}
	
	

	
	
	
	
	private View getFormView(int pos, View row, ViewGroup parent, RecordHolder holder, FieldFormContact field){
		
		Map<String, Object> map;
		
		if(row == null){
			inflater = (LayoutInflater)mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = (LinearLayout) inflater.inflate(layout_item, parent, false);//new LinearLayout(context);
			holder.txtTitle = (TextView)row.findViewById(R.id.form_label_tv);
			holder.txtTitle.setTypeface(MainActivity.FONT_BODY);
			holder.txtTitle.setTextColor(colors.getColor(colors.getBody_color()));
			
			holder.form_input = (LinearLayout)row.findViewById(R.id.form_input);
			holder.position = pos;
			
			row.setTag(holder);
						
		}else{
			holder = (RecordHolder) row.getTag();
//			if(holder.position == pos)
//				return row;

		}
		
		
//		TableRow aRow = (TableRow) inflater.inflate(R.layout.form_row, null, false);
//		aRow.setTag(field);
		//				LinearLayout form_label = (LinearLayout)aRow.findViewById(R.id.form_label);


		/**    **/
		
		final int position = pos;//holder.position;
		holder.txtTitle.setText(field.getLabel());
				
		holder.form_input.removeAllViewsInLayout();

		
		if (field.getType().equalsIgnoreCase("text")) {
			//final FieldFormContact field_tmp = field;
			View viewText = inflater.inflate(R.layout.text_input, parent, false);
			EditText editText = (EditText)viewText.findViewById(R.id.text_input);
			editText.setBackgroundDrawable(mainActivity.getResources().getDrawable(R.drawable.border_for_views));
			editText.setTextSize(15);
			String value = mapField[position];//extracted(field, position);

			if (value != null) {
				editText.setText(""+value);
			}else if(!initialValue.isEmpty()){
				editText.setText("["+initialValue+"]Test value");
			}else {
				//editText.setText("  ");
				editText.setHint("  "+field.getPlaceholder()+"  ");
			}
			
			
			TextWatcher watcher = new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {

					mapField[position] = s.toString();
					//editText.setText(mapField[position]);

					//restoredFields.add(new RestoredField(field_tmp.getId(), s.toString(), ""));					
				}
			};
			
			editText.addTextChangedListener(watcher);
			
			editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(50);
			editText.setFilters(FilterArray);
			editText.setTag(field.getId());

			
			//allViews.add(viewText);
			map= new HashMap<String, Object>();
			map.put("view", editText);
			map.put("field", field);
			if(maps.size() > position)
				maps.set(position, map);
			else
				maps.add(map);
			holder.form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

			//restoredFields.add(new RestoredField(field.getId(), value, ""));

		}else 
		if (field.getType().equalsIgnoreCase("long_text")) {
			final FieldFormContact field_tmp = field;
			View viewText = inflater.inflate(R.layout.long_text_input, parent, false);
			EditText editText = (EditText)viewText.findViewById(R.id.text_input);
			editText.setBackgroundDrawable(mainActivity.getResources().getDrawable(R.drawable.border_for_views));
			editText.setTextSize(15);
			//viewText.setTag(position);
			String value = 	mapField[position];//extracted(field, position);

			if (value != null) {
				editText.setText(""+value);
			}else if(!initialValue.isEmpty()){
				editText.setText("["+initialValue+"]Test value");
			}else {
				//editText.setText("  ");
				editText.setHint("  "+field.getPlaceholder()+"  ");
			}
			editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(400);
			editText.setFilters(FilterArray);
			editText.setTag(field.getId());
			
			
			TextWatcher watcher = new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				
				@Override
				public void afterTextChanged(Editable s) {
					mapField[position] = s.toString();
					//editText.setText(mapField[position]);
//					String value = extracted(field_tmp, position);
//					if(!s.toString().equals(value))
						//restoredFields.add(new RestoredField(field_tmp.getId(), s.toString(), ""));					
				}
			};
			
			editText.addTextChangedListener(watcher);

			//					editText.getLayoutParams().width = 400 ;
			//					editText.setLayoutParams(editText.getLayoutParams());
			//allViews.add(viewText);
			map= new HashMap<String, Object>();
			map.put("view", editText);
			map.put("field", field);
			if(maps.size() > position)
				maps.set(position, map);
			else
				maps.add(map);
			holder.form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
			
			//restoredFields.add(new RestoredField(field.getId(), value, ""));

		}else if (field.getType().equalsIgnoreCase("email")) {
			final FieldFormContact field_tmp = field;
			View viewText = inflater.inflate(R.layout.text_input, parent, false);
			EditText editText = (EditText)viewText.findViewById(R.id.text_input);
			editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			editText.setTextSize(15);
			editText.setBackgroundDrawable(mainActivity.getResources().getDrawable(R.drawable.border_for_views));
			
			String value = 	mapField[position];//extracted(field, position);

			if (value != null) {
				editText.setText(value);
			}else if(!initialValue.isEmpty()){
				editText.setText(""+initialValue+"@test.com");
			}else {
				//editText.setText("  ");
				editText.setHint("  "+field.getPlaceholder()+"  ");
			}
			editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			

			TextWatcher watcher = new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					//restoredFields.add(new RestoredField(field_tmp.getId(), s.toString(), ""));			
					mapField[position] = s.toString();
					//editText.setText(mapField[position]);
				}
			};
			
			editText.addTextChangedListener(watcher);

			//allViews.add(editText);
			map= new HashMap<String, Object>();
			map.put("view", editText);
			map.put("field", field);
			if(maps.size() > position)
				maps.set(position, map);
			else
				maps.add(map);
			holder.form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
			
			//restoredFields.add(new RestoredField(field.getId(), value, ""));

		}else if (field.getType().equalsIgnoreCase("phone")) {
			final FieldFormContact field_tmp = field;
			View viewText = inflater.inflate(R.layout.text_input, parent, false);
			EditText editText = (EditText)viewText.findViewById(R.id.text_input);
			editText.setTextSize(15);
			String value = 	mapField[position];//extracted(field, position);

			if (value != null) {
				editText.setText(""+value);
			}else if(!initialValue.isEmpty()){
				editText.setText("["+initialValue+"]Test value");
			}else {
				//editText.setText("  ");
				editText.setHint("  "+field.getPlaceholder()+"  ");
			}
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(15);
			editText.setFilters(FilterArray);
			editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			editText.setInputType(InputType.TYPE_CLASS_PHONE);
			editText.setBackgroundDrawable(mainActivity.getResources().getDrawable(R.drawable.border_for_views));
			
			TextWatcher watcher = new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					//restoredFields.add(new RestoredField(field_tmp.getId(), s.toString(), ""));		
					mapField[position] = s.toString();
					//editText.setText(mapField[position]);
				}
			};
			
			editText.addTextChangedListener(watcher);
			
			//allViews.add(editText);
			map= new HashMap<String, Object>();
			map.put("view", editText);
			map.put("field", field);
			if(maps.size() > position)
				maps.set(position, map);
			else
				maps.add(map);
			holder.form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
			
			//restoredFields.add(new RestoredField(field.getId(), value, ""));

		}else if (field.getType().equalsIgnoreCase("date")) {
			final FieldFormContact field_tmp = field;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT	, LinearLayout.LayoutParams.MATCH_PARENT);
			View dateHourV = inflater.inflate(R.layout.date_input, parent, false);
			Button btnDate = (Button)dateHourV.findViewById(R.id.start_date);
			String value = 	mapField[position];//extracted(field, position);

			if (value != null) {
				btnDate.setText(value);
				String[] tmp = value.split("-");
				if (tmp.length==3) {
					//							date = new Date(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[3]));
					//							int jours = Integer.parseInt(tmp[3]);
					//							int days = Integer.parseInt(tmp[0]);
				}else {

				}

			}else if(!initialValue.isEmpty()){
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
				btnDate.setText((c.get(Calendar.DAY_OF_MONTH))+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
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
							c_.set(year, monthOfYear, dayOfMonth);

							if(c.getTimeInMillis() <= c_.getTimeInMillis()) {
								((Button)viewFinal).setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
								((Button)viewFinal).setTag(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
							}
							
							//restoredFields.add(new RestoredField(field_tmp.getId(), ((Button)viewFinal).getText().toString(), ""));		
							mapField[position] =  ((Button)viewFinal).getText().toString();
						}
					};
					final Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);

					DatePickerDialog datePicker = new DatePickerDialog(mainActivity, callBack , 2013, 12, 8);
					datePicker.updateDate(year, month, day);
					datePicker.setOwnerActivity((Activity) mainActivity);
					datePicker.getDatePicker().setCalendarViewShown(false);
					datePicker.show();

				}
			});

			//allViews.add(btnDate);
			map = new HashMap<String, Object>();
			map.put("view", btnDate);
			map.put("field", field);
			if(maps.size() > position)
				maps.set(position, map);
			else
				maps.add(map);
			

			holder.form_input.addView(dateHourV, params);
			
			//restoredFields.add(new RestoredField(field.getId(), value, ""));

		}else if (field.getType().equalsIgnoreCase("period")) {
			final FieldFormContact field_tmp = field;
			View viewPeriod = inflater.inflate(R.layout.period_input, parent, false);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT	, LinearLayout.LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.CENTER_VERTICAL;
			Button btnDe = (Button)viewPeriod.findViewById(R.id.start_date);
			String value = 	mapField[position];//extracted(field, position);
			if (value != null) {
				btnDe.setText(value);
			}else if(!initialValue.isEmpty()){
				btnDe.setText("["+initialValue+"]Test value");
			}else {
				btnDe.setText(field.getPlaceholder()+"...");
				btnDe.setSingleLine();
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
							//									Toast.makeText(context, "worked picker year :"+year+" month :"+monthOfYear+" day :"+dayOfMonth, Toast.LENGTH_SHORT).show();
							((Button)viewFinal).setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
							
							//restoredFields.add(new RestoredField(field_tmp.getId(), ((Button)viewFinal).getText().toString(), ""));		
							mapField[position] =  ((Button)viewFinal).getText().toString();

						}
					};
					final Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);

					DatePickerDialog datePicker = new DatePickerDialog(mainActivity, callBack , 2013, 12, 8);
					datePicker.updateDate(year, month, day);
					datePicker.setOwnerActivity((Activity) mainActivity);
					datePicker.getDatePicker().setCalendarViewShown(false);
					datePicker.show();
				}
			});
			Button btnA = (Button)viewPeriod.findViewById(R.id.end_date);
			//String value2 = extracted(field, position);
			String value2 = 	mapFieldSup[position];//extracted(field, position);

//			if (restoredFields != null && restoredFields.size()>0) {
//				for (int i = 0; i < restoredFields.size(); i++) {
//					if (field.getId() == restoredFields.get(i).getId()) {
//						value2 = restoredFields.get(i).getExtra();
//					}
//				}
//			}
			if (value2 != null) {
				btnA.setText(value2);
			}else if(!initialValue.isEmpty()){
				btnA.setText("["+initialValue+"]Test value");
			}else {
				btnA.setText(field.getPlaceholder()+"...");
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
							((Button)viewFinal).setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
							
							//restoredFields.add(new RestoredField(field_tmp.getId(), ((Button)viewFinal).getText().toString(), ""));		
							mapFieldSup[position] =  ((Button)viewFinal).getText().toString();

						}
					};
					final Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);

					DatePickerDialog datePicker = new DatePickerDialog(mainActivity, callBack , 2013, 12, 8);
					datePicker.updateDate(year, month, day);
					datePicker.setOwnerActivity((Activity) mainActivity);
					datePicker.getDatePicker().setCalendarViewShown(false);
					datePicker.show();
				}
			});
//			allViews.add(btnDe);
//			allViews.add(btnA);
			map= new HashMap<String, Object>();
			map.put("view", btnDe);
			map.put("second_view", btnA);
			map.put("field", field);
			if(maps.size() > position)
				maps.set(position, map);
			else
				maps.add(map);
			holder.form_input.addView(viewPeriod, params);

			//restoredFields.add(new RestoredField(field.getId(), value, ""));
		}
		else if (field.getType().equalsIgnoreCase("select")) {

			final FieldFormContact field_tmp = field;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT	, LinearLayout.LayoutParams.MATCH_PARENT);
			View dateHourV = inflater.inflate(R.layout.date_input, parent, false);
			final Button selectBtn = (Button)dateHourV.findViewById(R.id.start_date);
			selectBtn.setTextSize(15);
			String value = mapField[position];//extracted(field, position);
			if(value== null){
				value = field.getPlaceholder();
				selectBtn.setHint(value);
			}
//			else if(!initialValue.isEmpty()){
//				selectBtn.setText("["+initialValue+"]Test select value");
//			}
			else{
				selectBtn.setText(value);
				//field.getPlaceholder());
			}
//			List<FormValue> objects = new ArrayList<FormValue>();
//			try {
//				objects = appController.getFormValueDao().queryForAll();
//				objects = sortObject(objects, field_tmp);
//				//						objects = appController.getFormValueDao().queryForEq("field_id", field.getId_generated());
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}


			final List<FormValue> objects2 = sortObject(objects, field_tmp);
			selectBtn.setTag(-1);
			selectBtn.setOnClickListener(new OnClickListener() {
				Button btn;
				private AlertDialog myDialog;

				@Override
				public void onClick(View v) {
					
//					List<FormValue> objects = new ArrayList<FormValue>();
//					try {
//						objects = appController.getFormValueDao().queryForAll();
//						objects = sortObject(objects, field_tmp);
//						//						objects = appController.getFormValueDao().queryForEq("field_id", field.getId_generated());
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

					selectedItem = "";
					final String[] items = setItems(objects2);
					AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
					if (field_tmp.getPlaceholder()!=null) {
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
					//final Button 
					btn = (Button)v;
					//							btn.setTag(-1);
					builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								selectedItem = items[which];
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							btn.setTag(which);
							btn.setText(selectedItem);
							myDialog.hide();
							//restoredFields.add(new RestoredField(field_tmp.getId(), btn.getText().toString(), ""));		
							mapField[position] =  btn.getText().toString();
							
							HashMap<String, Object> map= new HashMap<String, Object>();
							map.put("view", btn);
							map.put("field", field_tmp);
							if(maps.size() > position)
								maps.set(position, map);
							else
								maps.add(map);

						}
					});

					/*builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//									Toast toast = Toast.makeText(context, "Selected: " + selectedItem, Toast.LENGTH_SHORT);
							//									toast.show();
							//									btn.setTag(which);
							btn.setText(selectedItem);
							myDialog.hide();
						}
					});

					builder.setNegativeButton(context.getResources().getString(R.string.cancel_cart), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//									Toast toast = Toast.makeText(context, "Selected: " + selectedItem, Toast.LENGTH_SHORT);
							//									toast.show();

							myDialog.hide();
						}
					});*/

					builder.setCancelable(true);
					myDialog = builder.create();
					myDialog.show();


				}
				
				
			});

//			allViews.add(selectBtn);
			map= new HashMap<String, Object>();
			map.put("view", selectBtn);
			map.put("field", field);
			if(maps.size() > position)
				maps.set(position, map);
			else
				maps.add(map);
			holder.form_input.addView(dateHourV, params);

			//restoredFields.add(new RestoredField(field.getId(), value, ""));
		}
		else if (field.getType().equalsIgnoreCase("postal_code")) {
			final FieldFormContact field_tmp = field;
			View viewText = inflater.inflate(R.layout.text_input, parent, false);
			EditText editText = (EditText)viewText.findViewById(R.id.text_input);
			editText.setBackgroundDrawable(mainActivity.getResources().getDrawable(R.drawable.border_for_views));
			editText.setTextSize(15);
			String value = 	mapField[position];//extracted(field, position);
			if (value != null) {
				editText.setText("  "+value);
			}else if(!initialValue.isEmpty()){
				editText.setText("10000");
			}
			else {
				editText.setHint("  "+field.getPlaceholder()+"  ");
			}
			
			TextWatcher watcher = new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					//restoredFields.add(new RestoredField(field_tmp.getId(), s.toString(), ""));			
					mapField[position] = s.toString();
				}
			};
			
			editText.addTextChangedListener(watcher);

			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(5);
			editText.setFilters(FilterArray);
			editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			editText.setInputType(InputType.TYPE_CLASS_PHONE);


//			allViews.add(editText);
			map= new HashMap<String, Object>();
			map.put("view", editText);
			map.put("field", field);
			if(maps.size() > position)
				maps.set(position, map);
			else
				maps.add(map);
			holder.form_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

		}else if (field.getType().equalsIgnoreCase("date_hour")) {
			final FieldFormContact field_tmp = field;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT	, LinearLayout.LayoutParams.MATCH_PARENT);
			View dateHourV = inflater.inflate(R.layout.date_hour_input, parent, false);
			Button btnDate = (Button)dateHourV.findViewById(R.id.start_date);
			
			String value = 	mapField[position];//extracted(field, position);
			if (value != null) {
				btnDate.setText(value);
			}else if(!initialValue.isEmpty()){
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
				btnDate.setText((c.get(Calendar.DAY_OF_MONTH))+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
			}else {
				btnDate.setText(field.getPlaceholder()+"...");
				btnDate.setSingleLine();
			}
			Button btnHour = (Button)dateHourV.findViewById(R.id.start_time);
//			String value2 = extracted(field, position);
////			if (restoredFields != null && restoredFields.size()>0) {
////				for (int i = 0; i < restoredFields.size(); i++) {
////					if (field.getId() == restoredFields.get(i).getId()) {
////						value2 = restoredFields.get(i).getExtra();
////					}
////				}
////			}
//			if (value2 != null) {
//				btnHour.setText(value2);
//				btnHour.setSingleLine();
//			}
			String value2 = 	mapFieldSup[position];//extracted(field, position);

//			if (restoredFields != null && restoredFields.size()>0) {
//				for (int i = 0; i < restoredFields.size(); i++) {
//					if (field.getId() == restoredFields.get(i).getId()) {
//						value2 = restoredFields.get(i).getExtra();
//					}
//				}
//			}
			if (value2 != null) {
				btnHour.setText(value2);
			}else if(!initialValue.isEmpty()){
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR, c.get(Calendar.HOUR_OF_DAY) + 1);
				btnHour.setText((c.get(Calendar.HOUR_OF_DAY))+" : "+(c.get(Calendar.MINUTE)+1));
			}else {
				btnHour.setText(field.getPlaceholder()+"...");
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

							Calendar c = Calendar.getInstance();

							Calendar c_ = Calendar.getInstance();
							c_.set(year, monthOfYear, dayOfMonth,0, 0);

							if(c.getTimeInMillis() <= c_.getTimeInMillis()) {
								((Button)viewFinal).setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
							}
							
							//restoredFields.add(new RestoredField(field_tmp.getId(), ((Button)viewFinal).getText().toString(), ""));
							mapField[position] =  ((Button)viewFinal).getText().toString();

						}
					};

					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);

					DatePickerDialog datePicker = new DatePickerDialog(mainActivity, callBack , 2013, 12, 8);
					datePicker.updateDate(year, month, day);
					datePicker.setOwnerActivity((Activity) mainActivity);
					datePicker.getDatePicker().setCalendarViewShown(false);
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
							
							//restoredFields.add(new RestoredField(field_tmp.getId(), ((Button)viewFinal).getText().toString(), ""));		
							mapFieldSup[position] =  ((Button)viewFinal).getText().toString();

						}

					};
					//Date d = new Date(System.currentTimeMillis() + (1000 * 60 * 30));
					c.setTimeInMillis(System.currentTimeMillis() + (1000 * 60 * 30));
					int hour = c.get(Calendar.HOUR_OF_DAY);
					int minutes = c.get(Calendar.MINUTE);

					TimePickerDialog timePickerDialog = new TimePickerDialog(mainActivity, callBack, hour, minutes, true);
					timePickerDialog.updateTime(hour, minutes);
					timePickerDialog.setOwnerActivity((Activity) mainActivity);
					timePickerDialog.show();

				}
			});
//			allViews.add(btnDate);
//			allViews.add(btnHour);
			map= new HashMap<String, Object>();
			map.put("view", btnDate);
			map.put("second_view", btnHour);
			map.put("field", field);
			if(maps.size() > position)
				maps.set(position, map);
			else
				maps.add(map);
			holder.form_input.addView(dateHourV, params);

		}
		

		//tableLayout.addView(aRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//		View divider_color = new View(mainActivity);
//		divider_color.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
//		divider_color.setBackgroundColor(colors.getColor(colors.getBody_color(), "40"));
		//tableLayout.addView(divider_color, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 1));

		row.setOnClickListener(new OnClickListener() {
			String initialValue = "AAA";

			@Override
			public void onClick(View v) {
					

					time = System.currentTimeMillis() - time;
					if(/*time > 1500 ||*/ clicks > 8){
						clicks = 0;
					}else{
						clicks++;
					}
					
					if(clicks == 8){
						
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setCancelable(false);
						//LayoutInflater li = LayoutInflater.from(getActivity());
						//View promptsView = li.inflate(R.layout.prompt, null);
						EditText editTextDialogUserInput = new EditText(getContext());//(EditText)promptsView.findViewById(R.id.editTextDialogUserInput);
						editTextDialogUserInput.setText(initialValue);
						editTextDialogUserInput.addTextChangedListener(new TextWatcher() {

							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								// TODO Auto-generated method stub

							}

							@Override
							public void beforeTextChanged(CharSequence s, int start, int count,
									int after) {
								// TODO Auto-generated method stub

							}

							@Override
							public void afterTextChanged(Editable s) {
								initialValue = s.toString();
								Log.i("menu ID", initialValue);

							}
						});
						builder.setView(editTextDialogUserInput);
						builder.setTitle("Remplir avec des valeurs de tests").setMessage("Entrez vos initiales")
						.setPositiveButton("Valider",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								setInitialValue(initialValue);
								dialog.cancel();
							}
						}).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

						builder.create().show();

					}
				}
			});
	
		
	


		//tableLayout.addView(aRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//		View divider_color = new View(context);
//		divider_color.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
//		divider_color.setBackgroundColor(colors.getColor(colors.getBody_color(), "40"));
		//tableLayout.addView(divider_color, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 1));
		
		//if(restoredFields.size() > elements.size())restoredFields.remove(position);
	
		return row;
	}
	private long time;
	private int clicks = 0;
	
	public List<Map<String, Object>> getMapViews(){
		
		return maps;
	}
	
//	public void setRestoredFields(ArrayList<RestoredField> restoredFields){
//		
//		this.restoredFields = restoredFields;
//	}

//	public ArrayList<RestoredField> getRestoredFields()
//	{
//		return restoredFields;
//	}
	
//	private String extracted(FieldFormContact field, int position) {
//		String value = null;
//		if (restoredFields != null && restoredFields.size()>0) {
//			for (int i = 0; i < restoredFields.size(); i++) {
//				if (field.getId() == restoredFields.get(i).getId() && i == position) {
//					value = restoredFields.get(i).getData();
//				}
//			}
//		}
//		return value;
//	}
	
	protected String[] setItems(List<FormValue> objects) {
		String[] result = new String[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			result[i] = objects.get(i).getText();
		}

		return result;
	}
	
	private Collection<FieldFormContact> sortFields(Collection<FieldFormContact> fields,
			Contact pContact) {
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

            realm.where(FieldFormContact.class).findAll();
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
}
