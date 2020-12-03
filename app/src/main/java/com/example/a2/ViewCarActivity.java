package com.example.a2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.database.Cursor;

import com.google.android.material.textfield.TextInputEditText;

public class ViewCarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText et_model, et_color, et_dpl, et_description;
    private ImageView iv;

    private int  carId = -1;
    private DatabaseAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car);

        toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);



        et_model = findViewById(R.id.et_details_model);
        et_color = findViewById(R.id.et_details_color);
        et_dpl = findViewById(R.id.et_details_dpl);
        et_description = findViewById(R.id.et_details_description);

        db = DatabaseAccess.getInstance(this);
        Intent intent = getIntent();//اجلب لي ال intent في ملف mainactivity في دالة الضغط على ال fab
        //خزن قيمة المتغير التي تكون مرسلة من ال intent
       carId =  intent.getIntExtra(MainActivity.CAR_KEY, -1 );//فديو 256

        if (carId == -1 ){
            //اذا كانت عملية عرض لانعمل شيء
        }

        else {
            //عملية عرض
            //اذا كانت عملية تعديل فأنا اريد ان اعرض البيانات في حقول معطلة يعني امنع المستخدم من التعديل عليها الا لو ضغط على زر تعديل

            disableFields();
            db.open();
          Car c =   db.getCar(carId);//جلبنا كل معلومات السيارة
            db.close();
            if (c != null){

                fillCarToFileds(c);
            }



        }



    }

    private void fillCarToFileds(Car c){
        if (c.getImage() !=null && c.getImage().equals(""))
        iv.setImageURI(Uri.parse(c.getImage()));//حتى نحول الرابط من النص الى URI اي رابط حقيقي متصل بقواعد البيانات يجلب الصورة
        et_model.setText(c.getModel());
        et_color.setText(c.getColor());
        et_description.setText(c.getDescription());
        et_dpl.setText(c.getDp1()+"");
    }

    private void disableFields(){
        //دالة وضيفتها تعطيل الحقول فقط

        iv.setEnabled(false);
        et_model.setEnabled(false);
        et_color.setEnabled(false);
        et_description.setEnabled(false);
        et_dpl.setEnabled(false);
    }

    private void enabledFields(){//حتى نعطل الحقول مرة وحدة ونرجعهم مرة وحدة
        iv.setEnabled(true);
        et_model.setEnabled(true);
        et_color.setEnabled(true);
        et_description.setEnabled(true);
        et_dpl.setEnabled(true);

    }


    private void clearFields(){//بعد عملية ابلاضافة يتم تفريغ الحقول
        iv.setImageURI(null);
        et_model.setText(" ");
        et_color.setText(" ");
        et_description.setText(" ");
        et_dpl.setText(" ");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.details_menu, menu);
        MenuItem save = menu.findItem(R.id.details_menu_save);
        MenuItem edit = menu.findItem(R.id.details_menu_edit);
        MenuItem delete = menu.findItem(R.id.details_menu_edit);

        if (carId == -1 ){
            //اذا كانت عملية اضافة نظهر فقط زر ال save
            save.setVisible(true);//اظهار
            edit.setVisible(false);//اخفاء
            delete.setVisible(false);//اخفاء


        }

        else {
            //اذا كانت عملية عرض نخفي زر ال save ونظهر زر ال edit وال delete
            save.setVisible(false);//اخفاء
            edit.setVisible(true);//اظهار
            delete.setVisible(true);//اظهار

        }

      return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.details_menu_save:
                return true;
            case R.id.details_menu_edit:
                return true;
            case R.id.details_menu_delete:
                return true;
        }
        return false;
    }

}
