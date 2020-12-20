package com.example.a2;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.SearchView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import android.view.MenuInflater;
import android.view.LayoutInflater;
//import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import android.view.ContextMenu;


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
    private static final  int PERMISSION_REQ_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View theInflatedView = inflater.inflate(R.layout.custom_car_layout, null);
        setContentView(theInflatedView);
        registerForContextMenu(theInflatedView);



        //كود الحصول على الصلاحية
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }


        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


       //  rc = findViewById(R.id.main_rv);




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
                //كود البحث
                //يتم البحث عندما المستخدم يضغط على زر Submit
                db.open();
                ArrayList<Car> cars = db.getCars(query);//البحث حسب النص الموجود في ال car الواحدة وليس كل ال cars اذ نلاحظ اننا كتبنا getCars وليس getAllCars
                db.close();
                adapter.setCars(cars);//بعدها اذهب للادبتر سيت كارز واعطيه اللستة التي اسمها cars
                adapter.notifyDataSetChanged();//حدث لي كل اللستة
                return false;
            }
            //يتم استدعائها عندما المستخدم يغير النص في الاستعلام
            @Override
            public boolean onQueryTextChange(String newText) {
                //كود البحث
             //يتم البحث خلال ما المستخدم يكتب
                db.open();
                ArrayList<Car> cars = db.getCars(newText);//البحث حسب النص الموجود في ال car الواحدة وليس كل ال cars اذ نلاحظ اننا كتبنا getCars وليس getAllCars
                db.close();
                adapter.setCars(cars);//بعدها اذهب للادبتر سيت كارز واعطيه اللستة التي اسمها cars
                adapter.notifyDataSetChanged();//حدث لي كل اللستة

                return false;
            }

        });

        //يتم استدعاء هذه الدالة عندما نضط على زر X اي حذف النص واغلاق البحث
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //كود البحث
                // يتم حذف الكتابة والعودة الى القائمة بعد ما المستخدم يبحث
                db.open();
                ArrayList<Car> cars = db.getAllCars();//عندما تضغط على زر ال x جيب كل السيارات او العناصر واعرضهم على الشاشة
                db.close();
                adapter.setCars(cars);//بعدها اذهب للادبتر سيت كارز واعطيه اللستة التي اسمها cars
                adapter.notifyDataSetChanged();//حدث لي كل اللستة
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_CAR_REQ_CODE){

            db.open();
            ArrayList<Car> cars = db.getAllCars();
            db.close();
            adapter.setCars(cars);
            adapter.notifyDataSetChanged();//عمل تحديث ل اللستة كلها
          //  adapter.notifyItemChanged();//item تحديث عنصر واحد فقط او كما معروف ب
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQ_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //تم الحصول على الصلاحية
                }

                else {

                }

        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.details_menu_save:
                Toast.makeText(this, "save item", Toast.LENGTH_LONG ).show();
                return true;

            case R.id.details_menu_edit:
                Toast.makeText(this, "edit item", Toast.LENGTH_LONG ).show();
                return true;


            case R.id.details_menu_delete:
                Toast.makeText(this, "delet item", Toast.LENGTH_LONG ).show();
                return true;


        }
        return false;
    }
}//القوس الاخير






