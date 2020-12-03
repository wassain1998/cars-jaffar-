package com.example.a2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;



public class MyDatabase extends SQLiteAssetHelper {

    public static final String DB_NAME = "cars.db"; //ملف الداتا بيس يكون نفس اسم car_db
    public static final int DB_VERSION = 3;//اصدار الداتا بيس, وعند تحديث الجدول يجب اعطاء قيمة اكبر من السابقة مثلا القيمة 1 نجعلها 2 بد التحديث واذا كانت 4 نجعلها 5 بعد التحديث

    //حتى اذا غيرنا الجدول نغير فقط الاسماء وقيمة الاصدار
    public  static final String CAR_TB_NAME = "car";
    public  static final String CAR_CLN_ID = "id";
    public  static final String CAR_CLN_MODEL = "model";
    public  static final String CAR_CLN_COLOR = "color";
    public  static final String CAR_CLN_DPL = "distancePerLetter";
    public  static final String CAR_CLN_IMAGE = "image";
    public  static final String CAR_CLN_DESCRIPTION = "description";

    public MyDatabase (Context context) {
        super(context, DB_NAME, null , DB_VERSION);
    }

    //@Override
    //يتم استدعائها عند انشاء الداتا بيس
  //  public void onCreate (SQLiteDatabase sqLiteDatabase){
        //انشاء جدول واضافته على قواعد البيانات
    //    sqLiteDatabase.execSQL("CREATE TABLE "+CAR_TB_NAME+" ("+CAR_CLN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
      //          CAR_CLN_MODEL+" TEXT , "+CAR_CLN_COLOR+" Text, "+CAR_CLN_DPL+" REAL)");


   // }

 /*  @Override
    //يتم استدعائها عند كل تحديث للداتا بيس
    //بطريقة اخرى يتم استدعائها عندما يتم تغيير قيمة الاصدار من قيمة الى قيمة اعلى
    public void onUpgrade (SQLiteDatabase sqLiteDatabase, int i, int i1 ){

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+CAR_TB_NAME);//احذف الجدول
        onCreate(sqLiteDatabase);//انشأ الجدول من جديد


    }

  */
    //دالة الاضافة
    public boolean insertCar(Car car) {
        SQLiteDatabase db = getWritableDatabase();// نستدعي مؤشر الداتا بيس الخاص بالكتابة
        ContentValues values = new ContentValues();
        values.put(CAR_CLN_MODEL, car.getModel());
        values.put(CAR_CLN_COLOR, car.getColor());
        values.put(CAR_CLN_DPL, car.getDp1());


        long result = db.insert (CAR_TB_NAME, null , values);
        return result !=-1;
    }

    //دالة التعديل

    public boolean updateCar(Car car) {
        SQLiteDatabase db = getWritableDatabase();/// نستدعي مؤشر الداتا بيس الخاص بالكتابة


        ContentValues values = new ContentValues();//نستدعي هذا الكلاس
        //نملىء البيانات
        values.put(CAR_CLN_MODEL, car.getModel());
        values.put(CAR_CLN_COLOR, car.getColor());
        values.put(CAR_CLN_DPL, car.getDp1());

        String args [] = {String.valueOf(car.getId())};
        int  result = db.update (CAR_TB_NAME, values, "id=?", args);
        //اذا القيمة اكبر من صفر يعني يوجد تعديل واذا صفر يعني لايوجد تعديل
        return result >0;
    }


    //ارجاع عدد الصفوف في جدول معين
    public long getCarsCount(){
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db,CAR_TB_NAME);
    }



    //دالة الحذف
    public boolean deleteCar(Car car) {
        SQLiteDatabase db = getWritableDatabase();//عملية الحذف تعتبر عملية قراءة


        ContentValues values = new ContentValues();
        values.put(CAR_CLN_MODEL, car.getModel());
        values.put(CAR_CLN_COLOR, car.getColor());
        values.put(CAR_CLN_DPL, car.getDp1());


        String args [] = {String.valueOf(car.getId())};
        int  result = db.delete (CAR_TB_NAME,  "id=?", args);
        return result >0;
    }



    //دالة الاستراجاع
    public ArrayList <Car> getAllCars(){
        ArrayList<Car> cars = new ArrayList<>();
        SQLiteDatabase db =  getReadableDatabase();
        Cursor cursor =   db.rawQuery( "SELECT *  FROM "+CAR_TB_NAME , null);
        //كود التعامل مع ال Cursorوتحويله لمصفوفة من نوع Car
        //فحص هل ال Cursorيحتوي على بيانات

        if(cursor != null && cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(CAR_CLN_ID));
                String model = cursor.getString(cursor.getColumnIndex(CAR_CLN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(CAR_CLN_COLOR));
                String image = cursor.getString(cursor.getColumnIndex(CAR_CLN_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(CAR_CLN_DESCRIPTION));
                double dbl   = cursor.getDouble(cursor.getColumnIndex(CAR_CLN_DPL));

                Car c = new Car(id,model,color,dbl,description,image);
                //ننشأ سيارة جديدة
                cars.add(c);
            }

            while (cursor.moveToNext());
            cursor.close();//غلق ال crusor حتى لايبقى مفتوح
        }

        return cars;
    }




    //دالة البحث
    //يرجى فصل دالة البحث عن الملف في ملف اخر
    public ArrayList <Car> getCars(String modelsearch){//البحث عن موديل السيارات
        ArrayList<Car> cars = new ArrayList<>();
        SQLiteDatabase db =  getReadableDatabase();
        Cursor cursor =   db.rawQuery( "SELECT *  FROM "+CAR_TB_NAME+" WHERE "+CAR_CLN_MODEL+" =? " ,new  String[]{modelsearch});

        if(cursor != null && cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(CAR_CLN_ID));
                String model = cursor.getString(cursor.getColumnIndex(CAR_CLN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(CAR_CLN_COLOR));
                String image = cursor.getString(cursor.getColumnIndex(CAR_CLN_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(CAR_CLN_DESCRIPTION));
                double dbl   = cursor.getDouble(cursor.getColumnIndex(CAR_CLN_DPL));

                Car c = new Car(id,model,color,dbl,image,description);
                cars.add(c);
            }

            while (cursor.moveToNext());
            cursor.close();
        }

        return cars;
    }

}
