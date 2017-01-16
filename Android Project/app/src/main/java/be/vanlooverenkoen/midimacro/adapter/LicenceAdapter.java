package be.vanlooverenkoen.midimacro.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import be.vanlooverenkoen.midimacro.R;
import be.vanlooverenkoen.midimacro.model.Licence;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter to show all the licences
 *
 * @author Koen Van Looveren
 * @since 1.0
 */
public class LicenceAdapter extends MidimacroRecyclerView<LicenceAdapter.ViewHolder> {
    //region Variables
    private final List<Licence> licences;
    private Activity activity;
    //endregion

    //region Constructor
    public LicenceAdapter(Activity activity, List<Licence> licences) {
        super(activity);
        this.licences = licences;
        this.activity = activity;
    }
    //endregion

    //region Adapter Methods
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.row_licence, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final Licence licence = licences.get(position);
        viewHolder.nameTv.setText(licence.getProductName());
        viewHolder.licenceTv.setText(licence.getLicense());
        viewHolder.openDocumentationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = licence.getGitUrl();
                int color = ContextCompat.getColor(activity, R.color.colorPrimary);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(color);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(activity, Uri.parse(url));
            }
        });
        viewHolder.copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Git Link", licence.getGitUrl());
                clipboard.setPrimaryClip(clip);
                //CroutonHelper.createCrouton(activity, "Link is copied to your clipboard");
            }
        });
        viewHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = String.format("Libary used by Automated Synology: %s\n%s", licence.getProductName(), licence.getGitUrl());
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Automated Synology License: " + licence.getProductName());
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        setMargins(position, licences.size(), viewHolder.cardView);
    }

    @Override
    public int getItemCount() {
        return licences.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.licence_tv)
        TextView licenceTv;
        @BindView(R.id.share_btn)
        Button shareBtn;
        @BindView(R.id.open_documentation_btn)
        Button openDocumentationBtn;
        @BindView(R.id.copy_btn)
        Button copyBtn;
        @BindView(R.id.cardview)
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    //endregion
}