package com.aidr.aidr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONObject;

public class Explainer extends AppCompatActivity {

    private static String descriptionDir = "descriptions/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_explainer);

        WebView content = (WebView) findViewById(R.id.contentContainer);
        content.setWebChromeClient(new WebChromeClient());
        TextView textTitle = (TextView) findViewById(R.id.titleText);

        String type = getIntent().getStringExtra("type");
        int id;
        JSONObject payload;
        if (type.equals("disease")) {
            id = getIntent().getIntExtra("diseaseId",-1);
            payload = DiseaseDB.getDiseaseById(id);
        } else {
            id = getIntent().getIntExtra("drugId",-1);
            payload = DiseaseDB.getDrugById(id);
        }


        String title = "Sample Title";
        String text = "";
        if (id != -1) {
            try {
                title = (String) payload.get("name");
                text = (String) payload.get("description");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        textTitle.setText(title);
        String descPath = "file:///android_asset/" + descriptionDir + text;
        if (!text.equals("")) {
            content.loadUrl(descPath);
        }
    }

    public void switchToMain(View view) {
        finish();
    }

}
