//package com.example.chatapp;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.chatapp.databinding.ItemSentBinding;
//import com.example.chatapp.databinding.ItemStatusBinding;
//
//import java.util.ArrayList;
//
//public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewholder> {
//
//    Context context ;
////    ArrayList<UserStatus> userStatuses ;
//
////    public TopStatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
////        this.context = context;
////        this.userStatuses = userStatuses;
////    }
//
//    @NonNull
//    @Override
//    public TopStatusViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.item_status,parent,false);
//        return new TopStatusViewholder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TopStatusViewholder holder, int position) {
//
//            holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//    }
//
//    @Override
//    public int getItemCount() {
//        return userStatuses.size();
//    }
//
//    public class TopStatusViewholder extends RecyclerView.ViewHolder {
//
//          ItemStatusBinding binding ;
//          public TopStatusViewholder(@NonNull View itemView) {
//              super(itemView);
//              binding = ItemStatusBinding.bind(itemView);
//          }
//      }
//}
