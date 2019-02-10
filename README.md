# Radial Menu

### Cool and stylish way to create semi-circle radial menus for your android apps

<img src="https://i.imgur.com/u8xHi7q.gif" data-canonical-src="https://i.imgur.com/u8xHi7q.gif" width="216" height="384" />

## Installation
Add this into your build.gradle dependencies section.
```
implementation 'com.eminayar.radialmenu:radial-menu:0.0.1.3'
```

## Sample Usages

You can check sample application to see how to `Radial Menu` is being used in both XML and Java. It is 
quite simple to use radial menu in your any kind of layout. Place it in your xml and in java side `do not forget to setCenterView`

## Restrictions

For now it is only supporting to open above a view. There is no way for showing it from sides of screen. Any PRs are welcomed for this feature.

### XML Part of Usage
 
 ````xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:layout_width="match_parent"
        android:layout_alignParentBottom="true"
        android:layout_height="wrap_content"
        android:text="Show/Hide"
        android:onClick="showClose"
        android:id="@+id/button"/>

    <com.naylalabs.semiradialmenu.RadialMenuView
        android:id="@+id/radial_menu_view"
        android:layout_width="match_parent"
        android:layout_above="@id/button"
        android:layout_height="wrap_content"
        android:visibility="gone" />

</RelativeLayout>
 ````
Radial Menu sample usage [sample module] (https://github.com/kngfrhzs/radial-menu/tree/master/app).

There is ```MenuItemView``` model defined in project. You should create an `ArrayList<MenuItemView>` to 
inflate your radial menu. It is basic model that asking you to pass these parameters at constructor ; `context, title, icon, backgroundColor`. 

### JAVA Part of Usage

````java
        radialMenuView = findViewById(R.id.radial_menu_view);
        button = findViewById(R.id.button);

        MenuItemView itemOne = new MenuItemView(this ,"Ask Questions",R.drawable.ic_profile_white, R.color.orange);
        MenuItemView itemTwo = new MenuItemView(this,"Friends",R.drawable.ic_babies_calendar, R.color.green);
        MenuItemView itemThree = new MenuItemView(this,"Gallery", R.drawable.ic_drawer_settings, R.color.vividPurple);
        MenuItemView itemFour = new MenuItemView(this,"Settings", R.drawable.ic_blog_white, R.color.darkRed);
        MenuItemView itemFive = new MenuItemView(this, "Profile", R.drawable.ic_profile_white, R.color.darkGreen2);
        ArrayList<MenuItemView> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);
        items.add(itemThree);
        items.add(itemFour);
        items.add(itemFive);
        radialMenuView
          .setListener(this)
          .setMenuItems(items)
          .setCenterView(button)
          .setInnerCircle(true, R.color.white)
          .setOffset(10)
          .build();
```` 

You should implement `RadialMenuView.RadialMenuListener` to detect which button is clicked.

```` @Override
    public void onItemClicked(int i) {
        Toast.makeText(this, "Item clicked - " + String.valueOf(i), Toast.LENGTH_SHORT).show();
    }
````

## Contribution

Open to any kind of new idea, development suggestion or bug fixing. And If anyone want to contribute , I will appreciate. It is enough to just create a new PR that explaining problem and solution.

### Developed By

Muhammed Emin Ayar - emin@naylalabs.com

### Hire Us for your projects

As `NaylaLabs` (https://www.naylalabs.com) We are a team of young and dynamic developers that have extensive experience in the digital world. Your needs are handled with our creative engineering approach to find the best solution.

### Thanks

Special thanks to developers of `CircularReveal` (https://github.com/ozodrukh/CircularReveal) library to let reveal animation possible for API 14+.

### License

```
Copyright 2019 Muhammed Emin AYAR

The MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
