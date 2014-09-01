package org.canthack.tris.android.hgdroid;

import android.util.SparseArray;
import android.view.View;

/**
 * Generic ViewHolder, to get the multiple tags functionality
 * on all Android versions.
 * Taken from http://www.piwai.info/android-adapter-good-practices/
 */
public class ViewHolder {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
