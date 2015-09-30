package wu.a.autolayout;

import android.content.Context;
import android.widget.ImageView;

public class ImageViewParser extends ViewParser<ImageView> {
	

	public ImageViewParser(Context context) {
		super(new ImageView(context));
	}

	public ImageViewParser(ImageView view) {
		super(view);
	}

	void parse(String an,String av){
		if(ATT_SRC.equals(an)){
			if (av.startsWith(PREFIX_AT)) {
				String[] strs = av.substring(1).split(REGULAR_EXPRESSION);
				int id = view.getResources().getIdentifier(strs[1], strs[0],view.getContext().getPackageName());
				view.setBackgroundResource(id);
			}else
			if(av.startsWith(PREFIX_DRAWABLE)){
//				view.setImageBitmap(bm);
			}
		}else 
			super.parse(an,av);
	}

}
