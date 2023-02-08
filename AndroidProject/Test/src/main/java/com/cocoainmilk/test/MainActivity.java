package com.cocoainmilk.test;

import androidx.appcompat.app.AppCompatActivity;
import com.cocoainmilk.unity.core.Plugin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

interface ButtonOnClick
{
    void click() throws Exception;
}

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout layout = new GridLayout(this);
        layout.setOrientation(GridLayout.HORIZONTAL);
        layout.setColumnCount(4);
        addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        createButton("google_login", layout, () -> google_login());
    }

    void createButton(String title, ViewGroup viewGroup, ButtonOnClick onClick)

    {
        Button button = new Button(this);
        button.setText(title);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                try
                {
                    onClick.click();
                }
                catch (Exception ex)
                {
                    Log.e("Test", ex.toString());
                }
            }
        });

        viewGroup.addView(button);
    }

    void google_login() throws Exception
    {
        Plugin.request(this, 0, "google", "login", null);
    }
}