package com.example.a2;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class CarRVAdapter extends RecyclerView.Adapter<CarRVAdapter.CarViewHolder> {

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    private ArrayList<Car> cars;
    private OnRecyclerViewItemClickListener listener;
    public CarRVAdapter(ArrayList<Car> cars, OnRecyclerViewItemClickListener listener){
        this.cars = cars;
        this.listener = listener;
    }


    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //نعمل انفلات للشاشة كلها (استدعاء) لأن هذه الدالة الفوق تستخدم لأستدعاء العناصر التي تظهر اول مرة بعدها يتم استبدالها بدالة onBindViewHolder
        View v = LayoutInflater.from(parent.getContext() ).inflate(R.layout.custom_car_layout,null,false);
        CarViewHolder viewHolder = new CarViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        //الربط
        Car c = cars.get(position);
        if (c.getImage() != null && !c.getImage().isEmpty())//اذا كانت الصورة ليست null وليست فارغة انزل نفذ السطر الذي تحت
            holder.iv.setImageURI(Uri.parse(c.getImage()));//بعد تحقق الشرط في الاعلى يتم تخزين الصورة داخل الداتا بيس  على شكل نص لذلك نقوم بتحويلها لمسار لغرض عرضها على شكل صورة
        else{
            holder.iv.setImageResource(R.drawable.city2);//اذا كانت الصورة فارغة استخدم الصورة الافتراضية
        }
        //يفضل عمل اداة شرط للعناصر كما موجود فوق السطر الأعلى
        holder.tv_model.setText(c.getModel());
        holder.tv_color.setText(c.getColor());


        //نعمل try-cutch للون لتجنب المشاكل التي يتسبب بها تنصيب اللون حيث اذا لم يتم تنصيب الون نتجنب الاخطاء
        try{
        holder.tv_color.setTextColor(Color.parseColor(c.getColor()));//تنصيب اللون على العنصر
        }
        catch (Exception e){
        }

        holder.tv_dpl.setText(String.valueOf(c.getDp1()));//حتى يتم تحويلها لنص
        holder.id = c.getId();
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder{
        //نعمل افلات للعناصر المووجودة داخل ملف custom_car_layout
        ImageView iv;
        TextView tv_model, tv_color, tv_dpl;
        int id;
        public CarViewHolder(@NonNull View itemView){
            super(itemView);
            iv = itemView.findViewById(R.id.custom_car_iv);
            tv_model = itemView.findViewById(R.id.custom_car_tv_model);
            tv_color = itemView.findViewById(R.id.custom_car_tv_color);
            tv_dpl = itemView.findViewById(R.id.custom_car_tv_dpl);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                 //   int id = (int) iv.getTag();
                listener.onItemClick(id);
                }
                });
            }
        }
    }


