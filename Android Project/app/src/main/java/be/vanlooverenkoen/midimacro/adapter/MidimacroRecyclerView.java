package be.vanlooverenkoen.midimacro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import be.vanlooverenkoen.midimacro.model.MarginPosition;

/**
 * Custom RecyclerViewAdapter for handling margins the right way
 *
 * @param <VH> the viewholder that needs to be used
 * @author Koen Van Looveren
 * @since 1.0
 */
public abstract class MidimacroRecyclerView<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    //region Variables
    private final int DP4_MARGIN;
    private final int DP16_MARGIN;
    //endregion


    public MidimacroRecyclerView(Context context) {
        float d = context.getResources().getDisplayMetrics().density;
        DP4_MARGIN = (int) (4 * d);
        DP16_MARGIN = (int) (16 * d);
    }

    /**
     * Will calculate where the view is located in the recyclerview and will add extra margins
     *
     * @param position of the view
     * @param size     of the complete recyclerview list
     * @param view     that needs to get margins
     */
    void setMargins(int position, int size, View view) {
        if (position == 0) {
            setMargins(MarginPosition.TOP, view);
        } else if (position == size - 1) {
            setMargins(MarginPosition.BOTTOM, view);
        } else {
            setMargins(MarginPosition.MIDDLE, view);
        }
    }

    /**
     * Sets the margins
     *
     * @param marginPosition of the view in the Recyclerview
     * @param view           that needs to get margins
     */
    private void setMargins(MarginPosition marginPosition, View view) {
        if (marginPosition == MarginPosition.TOP) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(DP16_MARGIN, DP16_MARGIN, DP16_MARGIN, DP4_MARGIN);
            view.setLayoutParams(params);
        } else if (marginPosition == MarginPosition.BOTTOM) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(DP16_MARGIN, DP4_MARGIN, DP16_MARGIN, DP16_MARGIN);
            view.setLayoutParams(params);
        } else if (marginPosition == MarginPosition.MIDDLE) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(DP16_MARGIN, DP4_MARGIN, DP16_MARGIN, DP4_MARGIN);
            view.setLayoutParams(params);
        }
    }
}
