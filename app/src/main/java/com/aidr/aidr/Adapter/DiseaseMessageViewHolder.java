package com.aidr.aidr.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidr.aidr.DiseaseDB;
import com.aidr.aidr.Explainer;
import com.aidr.aidr.Model.Message;
import com.aidr.aidr.R;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONObject;

import java.io.InputStream;

public class DiseaseMessageViewHolder extends MessagesListAdapter.IncomingMessageViewHolder<Message> {

    private ViewGroup bubbleView;
    public static String imageDir = "images/";

    public DiseaseMessageViewHolder(View itemView) {
        super(itemView);
        bubbleView = (ViewGroup) itemView;
    }

    @Override
    public void onBind(final Message message) {
        super.onBind(message);

        TextView msgText = (TextView) bubbleView.findViewById(R.id.messageText);
        ImageView image = (ImageView) bubbleView.findViewById(R.id.diseaseImage);
        TextView title = (TextView) bubbleView.findViewById(R.id.diseaseTitle);
        Button detailBtn = (Button) bubbleView.findViewById(R.id.detailButton);

        /* Retrieve disease details */
        JSONObject detail = null;
        final int disId = message.getDetailId();

        if (disId != -1) {
            if (message.isDisease()) {
                detail = DiseaseDB.getDiseaseById(disId);
            } else {
                detail = DiseaseDB.getDrugById(disId);
            }
        }

        String titleDisease = "Sample Disease";
        String relatedImage = null;
        String shortdesc = "";

        if (detail != null) {
            try {
                titleDisease = (String) detail.get("name");
                relatedImage = (String) detail.get("image");
                if (!message.isDisease()) {
                    shortdesc = detail.getString("short-description");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        title.setText(titleDisease);
        if (!shortdesc.equals("")) {
            msgText.setText(shortdesc);
        }

        if (relatedImage != null || !relatedImage.equals("")) {
            String fulldir = imageDir + relatedImage;
            try {
                InputStream img = bubbleView.getContext().getAssets().open(fulldir);
                Bitmap bitmap = BitmapFactory.decodeStream(img);
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        bubbleView.invalidate();

        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Explainer.class);
                if (message.isDisease()) {
                    intent.putExtra("diseaseId",disId);
                    intent.putExtra("type","disease");
                } else {
                    intent.putExtra("drugId",disId);
                    intent.putExtra("type","medicine");
                }
                v.getContext().startActivity(intent);
            }
        });
    }
}
