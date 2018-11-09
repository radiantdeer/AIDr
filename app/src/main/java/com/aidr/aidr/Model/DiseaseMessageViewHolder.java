package com.aidr.aidr.Model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidr.aidr.DiseaseDB;
import com.aidr.aidr.DiseaseExplainer;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONObject;

import java.io.InputStream;

public class DiseaseMessageViewHolder extends MessagesListAdapter.IncomingMessageViewHolder<Message> {

    private TextView title;
    private ImageView image;
    private Button detail;
    private ViewGroup bubbleView;
    public static String imageDir = "images/";

    public DiseaseMessageViewHolder(View itemView) {
        super(itemView);
        bubbleView = (ViewGroup) itemView;
        ViewGroup mainLayout = (ViewGroup) bubbleView.getChildAt(0);
        image = (ImageView) mainLayout.getChildAt(1);
        title = (TextView) mainLayout.getChildAt(2);
        detail = (Button) mainLayout.getChildAt(3);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        /* Retrieve disease details */
        JSONObject diseaseDetail = null;
        final int disId = message.getDetailId();

        if (disId != -1) {
            diseaseDetail = DiseaseDB.getDiseaseById(disId);
        }

        String titleDisease = "Sample Disease";
        String relatedImage = null;

        if (diseaseDetail != null) {
            try {
                titleDisease = (String) diseaseDetail.get("name");
                relatedImage = (String) diseaseDetail.get("image");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        title.setText(titleDisease);

        if (relatedImage != null) {
            String fulldir = imageDir + relatedImage;
            try {
                InputStream img = bubbleView.getContext().getAssets().open(fulldir);
                Bitmap bitmap = BitmapFactory.decodeStream(img);
                image.setImageBitmap(bitmap);
                bubbleView.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DiseaseExplainer.class);
                intent.putExtra("diseaseId",disId);
                v.getContext().startActivity(intent);
            }
        });
    }
}
