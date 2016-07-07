/**
 * 
 */
package com.euphor.paperpad.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.euphor.paperpad.Beans.MyString;
import com.euphor.paperpad.Beans.Parameters;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Question;
import com.euphor.paperpad.Beans.Score;
import com.euphor.paperpad.Beans.Survey_;

import com.euphor.paperpad.utils.Colors;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.AutoResizeTextView;
import com.euphor.paperpad.widgets.NonSwipeableViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * @author euphordev02
 *
 */
public class SurveyPageFragment extends Fragment {

	private static final String KEY_QUESTIONS = "questions";
	private NonSwipeableViewPager pager;
	private int id_survey;

	private Colors colors;
	private TableLayout tableLayout;
	private ArrayList<View> allViews;
	private boolean isTablet;
	private List<Question>  questions;
	public List<Map<String, Object>> maps;
	Object value = 0.0f;
	protected String selectedItem;
	protected AlertDialog myDialog;
	SurveyFragment surveyFragment;
	protected AlertDialog myErrorDialog;
    private LayerDrawable layerDrawable;
    public Realm realm;
	/**
	 * 
	 */
	public SurveyPageFragment() {

	}

	static SurveyPageFragment newInstance(int question_id, NonSwipeableViewPager pager, int survey_id, SurveyFragment surveyFragment) {
		SurveyPageFragment frag=new SurveyPageFragment();
		Bundle args=new Bundle();
		frag.setPager(pager);
		frag.setId_survey(survey_id);
		args.putInt(KEY_QUESTIONS, question_id);
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
        isTablet = Utils.isTablet(activity);

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

    private Bitmap addTransparentBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 1, bmp.getHeight() + borderSize * 1, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bmp, borderSize/2, borderSize/2, null);

        return bmpWithBorder;
    }

    private Drawable buildRatingBarDrawables(Bitmap[] images) {
        final int[] requiredIds = {android.R.id.background, android.R.id.secondaryProgress, android.R.id.progress};
        //final float[] roundedCorners = new float[] { 0, 0, 0, 0, 0, 0, 0, 0 };// { 5, 5, 5, 5, 5, 5, 5, 5 };
        Drawable[] pieces = new Drawable[3];

        for (int i = 0; i < 3; i++) {
            ShapeDrawable sd = new ShapeDrawable();//new RoundRectShape(roundedCorners, new RectF(5, 5, 5, 5), null));
            //sd.setPadding(10, 0, 10, 0);
            BitmapShader bitmapShader = new BitmapShader(addTransparentBorder(images[i], 12), Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            ///sd.setBounds(50, 50, 50, 50);
            sd.getPaint().setShader(bitmapShader);
            ClipDrawable cd = new ClipDrawable(sd, Gravity.LEFT, ClipDrawable.HORIZONTAL);
            if (i == 0) {
                pieces[i] = sd;//sd;
            } else {
                pieces[i] = cd;
            }
        }
        LayerDrawable ld = new LayerDrawable(pieces);
        for (int i = 0; i < 3; i++) {
            ld.setId(i, requiredIds[i]);
        }
        return ld;
    }


    /* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.survey_page_layout, container, false);
        view.setBackgroundColor(colors.getColor(colors.getBackground_color()));

		LinearLayout next = (LinearLayout)view.findViewById(R.id.btn_valide_vote_holder);
		ColorStateList colorSelector = new ColorStateList(
				new int[][] {
						new int[] {android.R.attr.state_pressed},
						new int[] {}},
						new int[] {	colors.getColor(colors.getBackground_color()),colors.getColor(colors.getTitle_color()) });
		TextView btn_valide_vote = (TextView)view.findViewById(R.id.btn_valide_vote);
		btn_valide_vote.setTextColor(colorSelector);
		ArrowImageView nextArrow = (ArrowImageView)view.findViewById(R.id.nextArrow);
		nextArrow.setLayoutParams(new LinearLayout.LayoutParams(20, 20));

		StateListDrawable d_ = new StateListDrawable();
		d_.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(colors.getColor(colors.getTitle_color())));// colors.makeGradientToColor(colors.getTitle_color())); //
		d_.addState(new int[]{}, new ColorDrawable(colors.getColor(colors.getBackground_color())));

		nextArrow.setDiffOfColorCode(colors.getColor(colors.getTitle_color()), colors.getColor(colors.getBackground_color()));
		nextArrow.setBackgroundDrawable(d_);
//		Paint paint = new Paint();
//		paint.setColor(colors.getColor(colors.getTitle_color()));
//		nextArrow.setPaint(paint );
		StateListDrawable stateListDrawable = new StateListDrawable();
		stateListDrawable.addState(
				new int[] { android.R.attr.state_pressed },
				new ColorDrawable(colors.getColor(colors
						.getTitle_color())));
		stateListDrawable.addState(
				new int[] { android.R.attr.state_selected },
				new ColorDrawable(colors.getColor(colors
						.getTitle_color())));
		next.setBackgroundDrawable(stateListDrawable);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fillingFinished()) {
					getPager().setCurrentItem(1, true);
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(getResources().getString(R.string.error));
					builder.setMessage(getResources().getString(R.string.survey_vote_incomplete_message));
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//							Toast toast = Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT);
							//							toast.show();
							myErrorDialog.hide();
						}
					});
					builder.setCancelable(true);
					myErrorDialog = builder.create();
					myErrorDialog.show();
				}


			}
		});
        questions = new ArrayList<Question>();
		Survey_ survey = null;
        survey = realm.where(Survey_.class).equalTo("id",id_survey).findFirst();
        // appController.getSurveyDao().queryForId(id_survey);
        //			questions = survey.getQuestions1();

        questions = realm.where(Question.class).findAll();
        // appController.getQuestionDao().queryForAll();
      /* questions = sortQuestions(questions, survey);   à vérifier N4r */

        //			fields = appController.getFieldSatistfactionDao().queryForAll();
        //			fields = sortFields(fields, survey);
        TextView subTitle = (AutoResizeTextView)view.findViewById(R.id.subTitle_vote);
		if(survey != null && !survey.getTitle().isEmpty())
			subTitle.setText(survey.getTitle());
		else
			subTitle.setText(getResources().getString(R.string.survey_vote));
		subTitle.setTextColor(colors.getColor(colors.getTitle_color()));

		tableLayout = (TableLayout)view.findViewById(R.id.tableForm_vote);
		tableLayout.setStretchAllColumns(true);

		/*Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point sizePoint = new Point();
		display.getSize(sizePoint);
		tableLayout.setLayoutParams(new TableLayout.LayoutParams(sizePoint.x - 200, TableLayout.LayoutParams.WRAP_CONTENT));
*/
		//		Drawable divider_color = getResources().getDrawable(R.drawable.shape_rounded_corners);
		//		divider_color.setColorFilter(colors.getColor(colors.getBody_color()), PorterDuff.Mode.MULTIPLY);
		//		tableLayout.setDividerDrawable(divider_color);

		view.findViewById(R.id.tableFormHolder_survey).setBackgroundColor(colors.getColor(colors.getForeground_color(), "40")); //.setBackgroundDrawable(rounded_back );


		allViews = new ArrayList<View>();
		maps = new ArrayList<Map<String,Object>>();
		for (Iterator<Question> iterator = questions.iterator(); iterator.hasNext();) {
			Question question = (Question) iterator.next();
			Map<String, Object> map= new HashMap<String, Object>();

			TableRow aRow = (TableRow) inflater.inflate(R.layout.vote_row, null, false);
			aRow.setTag(question);
			// add label question
			TextView vote_label_tv = (AutoResizeTextView)aRow.findViewById(R.id.vote_label_tv);
			vote_label_tv.setText(question.getLabel().replace("[br]","\n"));
			vote_label_tv.setTextColor(colors.getColor(colors.getTitle_color()));
			//vote_label_tv.setTextSize(20);
			//add question content
			LinearLayout vote_input = (LinearLayout)aRow.findViewById(R.id.vote_input);
			vote_input.setOrientation(LinearLayout.HORIZONTAL);

			if(question.getType().equals("score")) {
				Score score = question.getScore();
				if (score != null) {
					if (score.getType().equalsIgnoreCase("numbers")) {
						value = -1.0f;
						final int size = score.getCount();
						final LinearLayout voteView = (LinearLayout) inflater.inflate(R.layout.vote_numbers_layout, null, false);
						voteView.setTag(Float.valueOf((Float)value));
						final ArrayList<View> listNumbersViews = new ArrayList<View>();
						for (int i = 0; i < size; i++) {
							View numberVote = inflater.inflate(R.layout.vote_number, null, false);
							numberVote.setSelected(false);
							TextView number = (TextView)numberVote.findViewById(R.id.number_txt);
							number.setText(""+(i+1));
							number.setTextColor(Color.parseColor(Utils.progressiveColor(i+1, size)));
							voteView.addView(numberVote, new LinearLayout.LayoutParams(45, LayoutParams.MATCH_PARENT, 1));
							numberVote.setBackgroundDrawable(getResources().getDrawable(R.drawable.vote_numbers_rounded_corners));
							listNumbersViews.add(numberVote);
							//							numberVote.setBackgroundColor(Color.parseColor(Utils.progressiveColor(i+1, size)));
							final int ii = i;
							numberVote.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Drawable drawable = (Drawable) getResources().getDrawable(R.drawable.vote_numbers_rounded_corners);
									for (int j = 0; j < listNumbersViews.size(); j++) {
										listNumbersViews.get(j).setSelected(false);
										listNumbersViews.get(j).setBackgroundDrawable(drawable);
										TextView txt = (TextView)listNumbersViews.get(j).findViewById(R.id.number_txt);
										txt.setTextColor(Color.parseColor(Utils.progressiveColor(j+1, size)));
										listNumbersViews.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.vote_numbers_rounded_corners));
									}
									drawable.setColorFilter(Color.parseColor(Utils.progressiveColor(ii+1, size)), Mode.MULTIPLY);
									v.setBackgroundDrawable(drawable);
									TextView txt = (TextView)v.findViewById(R.id.number_txt);
									txt.setTextColor(Color.WHITE);
									v.setSelected(true);
									value = (float)(ii+1)/(float)10;
									voteView.setTag(Float.valueOf((Float)value));

								}
							});
						}
						map= new HashMap<String, Object>();
						map.put("view", voteView);
						map.put("question", question);
						map.put("value", value);
						maps.add(map);
						vote_input.setTag(question);
						vote_input.addView(voteView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
						allViews.add(vote_input);

					}else if (score.getType().equalsIgnoreCase("stars")) {
						value = -1.0f;
						final LinearLayout voteView = (LinearLayout) inflater.inflate(R.layout.custom_rating_bar/*.vote_numbers_layout*/, null, false);
						voteView.setTag(Float.valueOf((Float)value));

                        RatingBar ratingBar = (RatingBar)voteView.findViewById(R.id.custom_rating_bar);
                        ratingBar.setNumStars(score.getCount());

                        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();

                        stars.getDrawable(2).setColorFilter(colors.getColor(colors.getNavigation_background_color()), PorterDuff.Mode.SRC_ATOP);
                        stars.getDrawable(1).setColorFilter(colors.getColor(colors.getNavigation_background_color()), PorterDuff.Mode.SRC_ATOP);
                        stars.getDrawable(0).setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);

                        ratingBar.setProgressDrawable(stars);

						map.put("view", voteView);
						map.put("question", question);
						map.put("value", value);
						maps.add(map);
						vote_input.setTag(question);
						vote_input.setBackgroundColor(Color.TRANSPARENT);
						vote_input.addView(voteView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
					}
                    else if (score.getType().equalsIgnoreCase("image")) {
                        value = -1.0f;
                        final LinearLayout voteView = (LinearLayout) inflater.inflate(R.layout.custom_rating_bar, null, false);
                        voteView.setTag(Float.valueOf((Float)value));

                        Bitmap[] bitmaps = new Bitmap[3];
                        //Illustration[] illusts = new Illustration[]{survey.getScore_illustration_off(), survey.getScore_illustration_on()};
                        if(survey.getScore_illustration_off() == null && survey.getScore_illustration_on() == null)break;;

                        File imgFile = new File(survey.getScore_illustration_off().getPath());
                        if(imgFile.exists()){
                            bitmaps[0] = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            bitmaps[1] = bitmaps[0].copy(bitmaps[0].getConfig(), false);
                        }

                        File imgFile_ = new File(survey.getScore_illustration_on().getPath());
                        if(imgFile_.exists()){
                            bitmaps[2] = BitmapFactory.decodeFile(imgFile_.getAbsolutePath());
                        }


                        RatingBar ratingBar = (RatingBar)voteView.findViewById(R.id.custom_rating_bar);
                        //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ratingBar.getLayoutParams();
                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                            ratingBar.setPadding(0, 10, 0, 10);
                        }

                        int stars_up = (int) Math.nextUp((float)((score.getCount() * 24) / bitmaps[0].getWidth()));
                        ratingBar.setNumStars(score.getCount() + stars_up + 2);
                        ratingBar.setStepSize(1.4f);
                        //ratingBar.setPadding(10, 0, 10, 0);
                        if(bitmaps != null && bitmaps[0] != null) {
                            //layerDrawable = (LayerDrawable) tileify(buildRatingBarDrawables(bitmaps), false);//
                            layerDrawable = (LayerDrawable) buildRatingBarDrawables(bitmaps);
                            ratingBar.setProgressDrawable(layerDrawable);
                        }


                        //ratingBar.setProgressDrawable(setRatingBarDrawables(stars, bitmaps));



                        map.put("view", voteView);
                        map.put("question", question);
                        map.put("value", value);
                        maps.add(map);
                        vote_input.setTag(question);
                        vote_input.setBackgroundColor(Color.TRANSPARENT);
                        vote_input.addView(voteView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    }else if (score.getType().equalsIgnoreCase("text")) {

						final View dateHourV = inflater.inflate(R.layout.date_input, null, false);
						value = -1.0f;
						dateHourV.setTag(Float.valueOf((Float)value));
						final Button selectBtn = (Button)dateHourV.findViewById(R.id.start_date);
						selectBtn.setHint("Choose...");
						final Collection<MyString> options = score.getList_options();
						selectBtn.setTag(-1);
						selectBtn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								selectedItem = "";
								final String[] items = setItems(options);

								AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
								builder.setTitle("Choose");
								selectedItem = items[0];
								v.setTag(-1);

								final Button btn = (Button)v;
								builder.setSingleChoiceItems((CharSequence[]) items, 0, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which ) {
										selectedItem = items[which];
										btn.setTag(which);
										btn.setText(selectedItem);
                                        btn.setBackgroundColor(colors.getColor(colors.getTitle_color()));
										value = (float)(which+1)/(float)(items.length);
										dateHourV.setTag(Float.valueOf((Float)value));

									}
								});

								builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										//										Toast toast = Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT);
										//										toast.show();
										myDialog.hide();
									}
								});
								builder.setCancelable(true);
								myDialog = builder.create();
								myDialog.show();


							}
						});

						map= new HashMap<String, Object>();
						map.put("view", dateHourV);
						map.put("question", question);
						map.put("value", value);
						maps.add(map);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT	, LinearLayout.LayoutParams.MATCH_PARENT);
						vote_input.addView(dateHourV, params);

					}else if (score.getType().equalsIgnoreCase("smileys")) {
						value = -1.0f;
						final View smilies_view = inflater.inflate(R.layout.smilies_note, null, false);
						smilies_view.setTag(Float.valueOf((Float)value));
						final ImageView imageBad = (ImageView) smilies_view.findViewById(R.id.imgBad1);
						final ImageView imgNeutral = (ImageView) smilies_view.findViewById(R.id.imgNeutral2);
						final ImageView imgGood = (ImageView) smilies_view.findViewById(R.id.imgGood3);
						final ImageView imgBrilliant = (ImageView) smilies_view.findViewById(R.id.imgBrilliant4);
						final ImageView imgWonderful = (ImageView) smilies_view.findViewById(R.id.ImgWonderful5);
						imageBad.setAlpha(0.5f);
						imgNeutral.setAlpha(0.5f);
						imgGood.setAlpha(0.5f);
						imgBrilliant.setAlpha(0.5f);
						imgWonderful.setAlpha(0.5f);

						imageBad.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								imgNeutral.setAlpha(0.5f);
								imgGood.setAlpha(0.5f);
								imgBrilliant.setAlpha(0.5f);
								imgWonderful.setAlpha(0.5f);
								v.setAlpha(1f);
								value = 0.0f;
								smilies_view.setTag(Float.valueOf((Float)value));
							}
						});

						imgNeutral.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								imageBad.setAlpha(0.5f);
								imgGood.setAlpha(0.5f);
								imgBrilliant.setAlpha(0.5f);
								imgWonderful.setAlpha(0.5f);
								v.setAlpha(1f);
								value = 0.25f;
								smilies_view.setTag(Float.valueOf((Float)value));

							}
						});

						imgGood.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								imageBad.setAlpha(0.5f);
								imgNeutral.setAlpha(0.5f);
								imgBrilliant.setAlpha(0.5f);
								imgWonderful.setAlpha(0.5f);
								v.setAlpha(1f);
								value = 0.5f;
								smilies_view.setTag(Float.valueOf((Float)value));
							}
						});

						imgBrilliant.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								imageBad.setAlpha(0.5f);
								imgNeutral.setAlpha(0.5f);
								imgGood.setAlpha(0.5f);
								imgWonderful.setAlpha(0.5f);
								v.setAlpha(1f);
								value = 0.75f;
								smilies_view.setTag(Float.valueOf((Float)value));
							}
						});

						imgWonderful.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								imageBad.setAlpha(0.5f);
								imgNeutral.setAlpha(0.5f);
								imgGood.setAlpha(0.5f);
								imgBrilliant.setAlpha(0.5f);
								v.setAlpha(1f);
								value = 0.1f;
								smilies_view.setTag(Float.valueOf((Float)value));
							}
						});

						map= new HashMap<String, Object>();
						map.put("view", smilies_view);
						map.put("question", question);
						map.put("value", value);
						maps.add(map);
						vote_input.addView(smilies_view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
					}



				}
			}else if (question.getType().equals("text")) {
				value = "";
				final View viewText = inflater.inflate(R.layout.text_input, null, false);
				viewText.setTag(value);
				EditText editText = (EditText)viewText.findViewById(R.id.text_input);
				editText.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.border_for_views));
				editText.setTextSize(15);
				editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

				InputFilter[] FilterArray = new InputFilter[1];
				FilterArray[0] = new InputFilter.LengthFilter(250);
				editText.setFilters(FilterArray);
				//				editText.setTag(question.getId_question());
				editText.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if (count>0) {
							value = 1f;
							viewText.setTag(value);
						}

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						value = 1f;
						viewText.setTag(value);
					}
				});
				map= new HashMap<String, Object>();
				map.put("value", value);
				map.put("view", editText);
				map.put("question", question);
				maps.add(map);
				vote_input.setTag(question);
				vote_input.addView(viewText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
			}

			tableLayout.addView(aRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
			View divider_color = new View(getActivity());
			divider_color.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
			divider_color.setBackgroundColor(colors.getBackMixColor(colors.getForeground_color(), 0.30f));////.setBackgroundColor(colors.getColor(colors.getBody_color(), "40"));
			tableLayout.addView(divider_color, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 1));

		}

		return view;
	}


	protected boolean fillingFinished() {
		boolean result = false;
		for (int i = 0; i < maps.size(); i++) {
			Question question = (Question) maps.get(i).get("question");
			Object tag = null;
			View view = (View) maps.get(i).get("view");
			if (question.getType().equals("text")) {
				EditText editText = (EditText)view.findViewById(R.id.text_input);
				if (!editText.getText().toString().isEmpty()) {
					maps.get(i).put("value", editText.getText().toString());
					view.setTag(editText.getText().toString());
					result = true;
				}else {
					if (question.isOptional()) {
						return true;
					}else {
						return false;
					}
					// make text non obligatory 
				}
			}else {
				tag = (Float) view.getTag();
				if (tag !=null) {
					if (tag.equals(Float.valueOf(-1.0f)) && !question.isOptional()) {
						return false;
					}else {
						result = true;
					}
				}else {
					return false;
				}
			}


		}
		return result;
	}

	protected String[] setItems(Collection<MyString> objects2) {
        String[] result = new String[objects2.size()];
		int i = 0;
		for (Iterator<MyString> iterator = objects2.iterator(); iterator.hasNext();) {
            MyString stringScore = (MyString) iterator.next();
			result[i] = stringScore.getMyString();
			i++;
		}
		//		for (int i = 0; i < objects2.size(); i++) {
		//			result[i] = objects2.get(i).getString();
		//		}

		return result;
	}


	private List<Question> sortQuestions(List<Question> questions,
			Survey_ survey) {
		List<Question> result = new ArrayList<Question>();
		if (questions.size()>0 && survey!= null) {
			for (Iterator<Question> iterator = questions.iterator(); iterator.hasNext();) {
				Question question = (Question) iterator.next();
				if (question.getSurvey().getId_survey() == survey.getId_survey()) {
					result.add(question);
				}
			}
		}

		questions = result;
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

	/**
	 * @return the maps
	 */
	public List<Map<String, Object>> getMaps() {
		return maps;
	}

	/**
	 * @param maps the maps to set
	 */
	public void setMaps(List<Map<String, Object>> maps) {
		this.maps = maps;
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

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		getPager().setCurrentItem(0);
		super.onResume();
	}

}
