


package org.accenture.product.lemonade;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * GridView adapter to show the list of applications and shortcuts
 */
public class ShortcutsAdapter  extends ArrayAdapter<ShortcutInfo> {
    private final LayoutInflater mInflater;
    private final IconCache mIconCache;

    public ShortcutsAdapter(Context context, ArrayList<ShortcutInfo> apps) {
        super(context, 0, apps);
        mInflater = LayoutInflater.from(context);
        mIconCache = ((LauncherApplication)context.getApplicationContext()).getIconCache();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ShortcutInfo info = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.application_boxed, parent, false);
        }

        final TextView textView = (TextView) convertView;
        textView.setCompoundDrawablesWithIntrinsicBounds(null,
                new FastBitmapDrawable(info.getIcon(mIconCache)), null, null);
        textView.setText(info.getTitle(mIconCache));

        return convertView;
    }
}
