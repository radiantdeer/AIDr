package com.aidr.aidr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONObject;

public class DiseaseExplainer extends AppCompatActivity {

    private static String descriptionDir = "descriptions/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_explainer);

        WebView content = (WebView) findViewById(R.id.contentContainer);
        content.setWebChromeClient(new WebChromeClient());
        TextView textTitle = (TextView) findViewById(R.id.titleText);

        int diseaseId = getIntent().getIntExtra("diseaseId",-1);

        String title = "Sample Title";
        String text = "";
        if (diseaseId != -1) {
            JSONObject disease = DiseaseDB.getDiseaseById(diseaseId);
            try {
                title = (String) disease.get("name");
                text = (String) disease.get("description");
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
