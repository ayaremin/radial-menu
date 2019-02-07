package com.naylalabs.radialmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.naylalabs.semiradialmenu.MenuItemView;
import com.naylalabs.semiradialmenu.RadialMenuView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RadialMenuView.RadialMenuListener {

    RadialMenuView radialMenuView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radialMenuView = findViewById(R.id.radial_menu_view);
        button = findViewById(R.id.button);

        MenuItemView itemOne = new MenuItemView(this ,"Soru Sor",R.drawable.ic_profile_white, R.color.orange);
        MenuItemView itemTwo = new MenuItemView(this,"Arkadaşlar",R.drawable.ic_babies_calendar, R.color.green);
        MenuItemView itemThree = new MenuItemView(this,"Galeri", R.drawable.ic_drawer_settings, R.color.vividPurple);
        MenuItemView itemFour = new MenuItemView(this,"Naber", R.drawable.ic_blog_white, R.color.darkRed);
        MenuItemView itemFive = new MenuItemView(this, "Selam", R.drawable.ic_profile_white, R.color.darkGreen2);
        ArrayList<MenuItemView> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);
        items.add(itemThree);
        //items.add(itemFour);
        //items.add(itemFive);
        radialMenuView.setListener(this).setMenuItems(items).setRotated(true).setCenterView(button).setInnerCircle(true, R.color.gray).build();
    }

    public void showClose(View view) {
        radialMenuView.show();
    }

    @Override
    public void onItemClicked(int i) {
        Toast.makeText(this, "Item clicked - " + String.valueOf(i), Toast.LENGTH_SHORT).show();
    }
}
