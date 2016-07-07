package com.euphor.paperpad.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.InterfaceRealm.CommunElements1;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.utils.Utils;
import com.euphor.paperpad.widgets.PinImageView;


import java.io.File;
import java.util.List;


/**
 * Created by uness on 20/01/15.
 */
public class DesignCategoryFlowAdapter extends BaseAdapter{
    private final int hp;
    private final int wp;
    LayoutInflater inflater;
    private Context context;
    private List<CommunElements1> elements;
    private TextView titleFlow;
    private int color;

    private int dim;

    public DesignCategoryFlowAdapter(Context context, List<CommunElements1> elements, TextView titleFlow, int color) {
        this.context = context;
        this.elements = elements;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.titleFlow = titleFlow;
        this.color = color;
        this.dim = (int) Utils.dpToPixels(context, 60f);

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float density = metrics.density;
        Log.e("density "+density,"");
        if((density >= 3.0) || (density == 1.5))
        {
            hp = this.dim+40;
            wp = 50;//this.dim;
        }
        else if (density ==2.0)
        {
            hp = this.dim; //view.setPadding(this.dim/40 , this.dim/60, this.dim/40 , this.dim/60);
            wp = 20;//this.dim/2;
        }


        else {
            hp = this.dim /4; //view.setPadding(this.dim / 4, this.dim/6, this.dim / 4, this.dim/6);*/
            wp = 0;//this.dim/6;
        }
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public CommunElements1 getItem(int i) {
        return elements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        CommunElements1 element = getItem(i);

        if(view == null) {
            holder = new ViewHolder();

            if(element instanceof Child_pages) {

                view = inflater.inflate(R.layout.cover_flow_item, viewGroup, false);

            }

            else{

                view = inflater.inflate(R.layout.cover_flow_cats_item, viewGroup, false);

                holder.pinView = (PinImageView) view.findViewById(R.id.pinView);
                holder.pinView.setColor(Color.WHITE);

                holder.numberOfCats = (TextView) view.findViewById(R.id.numberOfCats);
                holder.numberOfCats.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Large);
                holder.numberOfCats.setTypeface(MainActivity.FONT_BODY);
                holder.numberOfCats.setTextColor(Color.BLACK);
            }


            holder.imageFlow = (ImageView) view.findViewById(R.id.imageFlow);
            holder.titleFlow = titleFlow; //(TextView) view.findViewById(R.id.titleFlow);
            holder.titleFlow.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Large);
            holder.titleFlow.setTypeface(MainActivity.FONT_REGULAR);
            holder.titleFlow.setTextColor(color);


            view.setTag(holder);

        }else{

            holder = (ViewHolder) view.getTag();
        }


        if(element != null){

            if(element instanceof Category){
                Category category = (Category)element;
                int numberOfCats = category.getChildren_pages().size();
                if(numberOfCats > 0)
                    holder.numberOfCats.setText(""+numberOfCats);
                else
                    view.findViewById(R.id.numberPinContainer).setVisibility(View.GONE);
            }


            Illustration illustration = element.getIllustration1();
            String path;
            if (!illustration.getPath().isEmpty()) {
                path = illustration.getPath();
                Glide.with(context).load(new File(path)).into(holder.imageFlow);
            }else if(illustration.getLink() != null){
                path = illustration.getLink();
                Glide.with(context).load(path).into(holder.imageFlow);
            }

            holder.titleFlow.setText(""+element.getCommunTitle1());

        }
        /*DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float density = metrics.density;
         //view.setPadding(metrics.widthPixels/50,metrics.widthPixels/100,metrics.widthPixels/50,metrics.widthPixels/100);
        *//*int hp=metrics.heightPixels;
        int wp=metrics.widthPixels;
        view.setPadding(hp /200 , wp/240, hp /200 , wp/240);*//*
        Log.e("density "+density,"");

        if((density >= 3.0) || (density == 1.5))

          view.setPadding(this.dim+40 , this.dim, this.dim+40 , this.dim);
        else if (density >1.5 && density <=2.0)

        view.setPadding(this.dim/40 , this.dim/60, this.dim/40 , this.dim/60);

        else
            view.setPadding(this.dim / 4, this.dim/6, this.dim / 4, this.dim/6);*/
        view.setPadding(wp,hp,wp,hp);
        return view;
    }

    class ViewHolder{
        ImageView imageFlow;
        TextView titleFlow;
        PinImageView pinView;
        TextView numberOfCats;
    }
}