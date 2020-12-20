package com.example.a2;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import android.content.ContentValues;

import java.util.Date;

//تابع لقواعد البيانات الخارجية
public class DatabaseAccess  {

    private SQLiteDatabase database;
    private SQLiteOpenHelper  openHelper;
    private static DatabaseAccess instance;


    private DatabaseAccess (Context context){
        this.openHelper = new MyDatabase(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if (instance == null){
            instance = new DatabaseAccess(context);
        }
        return instance;

    }

    //اي عملية على الداتا بيس نفتح خط مع الداتا بيس ثم ننفذ العملية ثم نغلق الخط مع الداتا بيس

    public void open (){//نفتح الخط مع قواعد البيانات
        this.database = this.openHelper.getWritableDatabase();

    }

    public void close () {//نغلق الخط مع قواعد لبيانات
        if (this.database != null){
            this.database.close();
        }

    }





    //دالة الاضافة
    public boolean insertCar(Car car) {
    //    SQLiteDatabase db = getWritableDatabase();// نستدعي مؤشر الداتا بيس الخاص بالكتابة
        ContentValues values = new ContentValues();
        values.put(MyDatabase.CAR_CLN_MODEL, car.getModel());
        values.put(MyDatabase.CAR_CLN_COLOR, car.getColor());
        values.put(MyDatabase.CAR_CLN_DPL, car.getDp1());
        values.put(MyDatabase.CAR_CLN_IMAGE, car.getImage());
        values.put(MyDatabase.CAR_CLN_DESCRIPTION, car.getDescription());


        long result = database.insert (MyDatabase.CAR_TB_NAME, null , values);
        return result !=-1;
    }

    //دالة التعديل

    public boolean updateCar(Car car) {
     //   SQLiteDatabase db = getWritableDatabase();/// نستدعي مؤشر الداتا بيس الخاص بالكتابة


        ContentValues values = new ContentValues();//نستدعي هذا الكلاس
        //نملىء البيانات
        values.put(MyDatabase.CAR_CLN_MODEL, car.getModel());
        values.put(MyDatabase.CAR_CLN_COLOR, car.getColor());
        values.put(MyDatabase.CAR_CLN_DPL, car.getDp1());
        values.put(MyDatabase.CAR_CLN_IMAGE, car.getImage());
        values.put(MyDatabase.CAR_CLN_DESCRIPTION, car.getDescription());
       // Description
        String  args[] = {String.valueOf(car.getId())};
        int  result = database.update (MyDatabase.CAR_TB_NAME, values, "id=?", args);
        //اذا القيمة اكبر من صفر يعني يوجد تعديل واذا صفر يعني لايوجد تعديل
        return result >0;
    }


    //ارجاع عدد الصفوف في جدول معين
    public long getCarsCount(){
       // SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(database,MyDatabase.CAR_TB_NAME);
    }



    //دالة الحذف
    public boolean deleteCar(Car car) {
     //   SQLiteDatabase db = getWritableDatabase();//عملية الحذف تعتبر عملية قراءة


  //     ContentValues values = new ContentValues();
    //    values.put(CAR_CLN_MODEL, car.getModel());
    //    values.put(CAR_CLN_COLOR, car.getColor());
     //   values.put(CAR_CLN_DPL, car.getDp1());


        String args [] = {String.valueOf(car.getId())};
        int  result = database.delete (MyDatabase.CAR_TB_NAME,  "id=?", args);
        return result >0;
    }



    //دالة الاستراجاع
    public ArrayList <Car> getAllCars(){
        ArrayList<Car> cars = new ArrayList<>();
       // SQLiteDatabase db =  getReadableDatabase();
        Cursor cursor =   database.rawQuery( "SELECT *  FROM "+MyDatabase.CAR_TB_NAME , null);
        //كود التعامل مع ال Cursorوتحويله لمصفوفة من نوع Car
        //فحص هل ال Cursorيحتوي على بيانات

        if(cursor != null && cursor.moveToFirst()){
            do{
               // int id = cursor.getInt(0);
                //String model = cursor.getString(1);
              //  String color = cursor.getString(2);
            //    double dbl = cursor.getDouble(3);
              //  String image = cursor.getString(4);
          //      String description = cursor.getString(5);


                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_CLN_ID));
                String model = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_COLOR));
                double dbl   = cursor.getDouble(cursor.getColumnIndex(MyDatabase.CAR_CLN_DPL));
               String image = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_IMAGE));//هذا السطر يوجد فيه خطأ غير معلوم لذلك بدلناه بالسطر الذي تحته
             //   String image = cursor.getString(4);//هذا السطر بديل للسطر الاعلى بسبب خطأ مجهول وان الرقم 4 هو تسلسل السطر من السطر الاول يحمل الرقم 0 الى هذا السطر
                String description = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_DESCRIPTION));

                Car c = new Car(id,model,color,dbl,image,description);
                cars.add(c);
            }

            while (cursor.moveToNext());
            cursor.close();
        }

        return cars;
    }




    //دالة البحث
    //يرجى فصل دالة البحث عن الملف في ملف اخر
    public ArrayList <Car> getCars(String modelsearch){
        ArrayList<Car> cars = new ArrayList<>();
      //  SQLiteDatabase db =  getReadableDatabase();
        Cursor cursor =   database.rawQuery( "SELECT *  FROM "+MyDatabase.CAR_TB_NAME+" WHERE "+MyDatabase.CAR_CLN_MODEL+" =? " ,new  String[]{modelsearch});

        if(cursor != null && cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_CLN_ID));
                String model = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_COLOR));
                double dbl   = cursor.getDouble(cursor.getColumnIndex(MyDatabase.CAR_CLN_DPL));
                String image = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_DESCRIPTION));

                Car c = new Car(id,model,color,dbl,image,description);
                cars.add(c);
            }

            while (cursor.moveToNext());
            cursor.close();
        }

        return cars;
    }



    //فديو 257 دقيقة 3:03
    public Car getCar(int carId){
        ArrayList<Car> cars = new ArrayList<>();
        String[] Items={MyDatabase.CAR_CLN_ID,MyDatabase.CAR_CLN_MODEL,MyDatabase.CAR_CLN_COLOR,MyDatabase.CAR_CLN_DPL,MyDatabase.CAR_CLN_IMAGE,MyDatabase.CAR_CLN_DESCRIPTION};
        String[] ID={carId+""};
        Cursor cursor=database.query(MyDatabase.CAR_TB_NAME,Items,MyDatabase.CAR_CLN_ID+" =?",ID,null,null,null);
      //  Cursor cursor =   database.rawQuery( "SELECT *  FROM "+MyDatabase.CAR_TB_NAME+"WHERW "+MyDatabase.CAR_CLN_ID+"=?",new String[]{String.valueOf(carId)});
        if(cursor != null && cursor.moveToFirst()){

                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_CLN_ID));
                String model = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_COLOR));
                double dbl   = cursor.getDouble(cursor.getColumnIndex(MyDatabase.CAR_CLN_DPL));
                String image = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_IMAGE));
                //String image = cursor.getString(4);//هذا السطر بديل للسطر الاعلى بسبب خطأ مجهول وان الرقم 4 هو تسلسل السطر من السطر الاول يحمل الرقم 0 الى هذا السطر
                String description = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_DESCRIPTION));

                Car c = new Car(id,model,color,dbl,image,description);
                cars.add(c);
            cursor.close();
            return c;
        }
        return null;
    }






}
