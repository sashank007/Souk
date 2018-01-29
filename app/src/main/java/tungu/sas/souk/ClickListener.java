package tungu.sas.souk;

import android.view.View;

/**
 * Created by Student on 1/29/2018.
 */
public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}