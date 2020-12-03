package com.example.a2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.SearchView;
import android.view.View;
import android.widget.Toast;
//import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public  class  MainActivity extends AppCompatActivity {
    //نعمل انفلات داخل private حتى نتمكن من الوصول لباقي العناصر من خارج هذا الملف (MainActivity)
    private RecyclerView rv;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private   CarRVAdapter adapter;//نجلب كلاس ال CarRvAdapter ونعمل له انفلات
    private   DatabaseAccess db;

    private static final int ADD_CAR_REQ_CODE = 1;
    private static final int EDIT_CAR_REQ_CODE = 1;
    public static final String CAR_KEY = "car_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.main_rv);
        fab = findViewById(R.id.main_fb);

        db = DatabaseAccess.getInstance(this);
        db.open();//نفتح خط مع الداتا بيس
        ArrayList<Car> cars = db.getAllCars();//بعد مافتحت الخط نعطي ايعاز هات كل الكارز
        db.close();//نغلق الخط مع لداتا بيس بعد ان جلبنا الكارز

        adapter = new CarRVAdapter(cars, new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int carId) {
               Intent i = new Intent(getBaseContext(), ViewCarActivity.class);
               i.putExtra(CAR_KEY,carId);
               startActivityForResult(i, EDIT_CAR_REQ_CODE);
            }
        });
        rv.setAdapter(adapter);//الResyclerView يساوي ال Adaptetr
        RecyclerView.LayoutManager lm = new GridLayoutManager(this, 2 );//الاعمدة تكون اثنين
        rv.setLayoutManager(lm);
        rv.setHasFixedSize(true);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewCarActivity.class);
                startActivityForResult(intent, ADD_CAR_REQ_CODE);
            }
        });
    }
//نثبت المنيو عن طريق هذا الدالة

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);//نجلب واجهة المنيو
        SearchView searchView = (SearchView) menu.findItem(R.id.main_search).getActionView();
        searchView.setSubmitButtonEnabled(true);//دالة السهم الموجود عند محرك البحث اي بعد ان نكتب في البحث نضغط على السهم حتى تتم العملية

        //دالة الاستعلام
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //يتم استدعائها عندما المستخدم يضغط ال Submit
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "Submit clicked",Toast.LENGTH_SHORT).show();
                return false;
            }
            //يتم استدعائها عندما المستخدم يغير النص في الاستعلام
            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(MainActivity.this, "text changed",Toast.LENGTH_SHORT).show();
                return false;
            }

        });


        //يتم استدعاء هذه الدالة عندما نضط على زر X اي حذف النص واغلاق البحث
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(MainActivity.this, "search finched",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }


    //نتعامل مع عناصر المنيو عن طريق هذه الدالة
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



}//القوس الاخير




