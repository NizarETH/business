	/**
	 * 
	 */
	package com.euphor.paperpad.adapters;

    import android.content.Context;
    import android.graphics.Color;
    import android.support.v4.app.Fragment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.View.OnClickListener;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.ImageView.ScaleType;

    import com.euphor.paperpad.InterfaceRealm.CommunElements1;
    import com.euphor.paperpad.R;
    import com.euphor.paperpad.Beans.Child_pages;
    import com.euphor.paperpad.Beans.Illustration;
    import com.euphor.paperpad.utils.Colors;
    import com.euphor.paperpad.widgets.AutoResizeTextView;
    import com.nostra13.universalimageloader.core.DisplayImageOptions;
    import com.nostra13.universalimageloader.core.ImageLoader;

    import java.util.List;

	/**
	 * @author euphordev02
	 *
	 */
	public class FreeFormulaPagesAdapter extends BaseAdapter {

		private Context context;
		private List<CommunElements1> elements;
		private ImageLoader imageLoader;
		DisplayImageOptions opts ;
		private DisplayImageOptions options;
		private Colors colors;
		private CallbackRelatedLinks callBack;

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return elements.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public CommunElements1 getItem(int position) {
			// TODO Auto-generated method stub
			return elements.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view==null) {
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.free_formule_item_list, parent,false);
			}
			//if (view==null) { // this has been removed due to problem loading views that are not shown first
				final CommunElements1 element = getItem(position);
				ViewHolder holder = new ViewHolder();
				holder.position = position;
				
				holder.title = (AutoResizeTextView)view.findViewById(R.id.tv_formule);
//				holder.title.setTextSize(context.getResources().getDimension(R.dimen.subtitleSize));
				holder.title.setText(element.getCommunTitle1());
				holder.title.setTextColor(Color.BLACK);

				holder.imageItem = (ImageView)view.findViewById(R.id.img_formule);
				if (element.getIllustration1() != null) {
					
					holder.imageItem.setScaleType(ScaleType.CENTER_CROP);
					Illustration illustration = element.getIllustration1();
					String path = !illustration.getPath().isEmpty()?"file:///"+illustration.getPath():illustration.getLink();
					imageLoader.displayImage(path, holder.imageItem, options);
				}

				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						callBack.onChildPageClick((Child_pages) element);
						
					}
				});
				
			//}
			return view;
		}

		/**
		 * @param imageLoader 
		 * @param options 
		 * @param fragment 
		 * 
		 */
		public 
		FreeFormulaPagesAdapter(Context context, List<CommunElements1> elements, ImageLoader imageLoader, DisplayImageOptions options, Colors colors, Fragment fragment) {
			this.options = options;
			this.context = context;
			this.elements = elements;
			this.imageLoader = imageLoader;
			 opts = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
			 this.colors = colors;
			 /*this.callBack = (CallbackRelatedLinks)fragment;*/
			 
		}

		/**
		 * @return the elements
		 */
		public List<CommunElements1> getElements() {
			return elements;
		}

		/**
		 * @param elements the elements to set
		 */
		public void setElements(List<CommunElements1> elements) {
			this.elements = elements;
		}
		
		static class ViewHolder{
			AutoResizeTextView title;
			ImageView imageItem;
			int position;
		}
		
		public class CallbackRelatedLinks{
            void onChildPageClick(Child_pages child_pages) {

            }
        }

	}
