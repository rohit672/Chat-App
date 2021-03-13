package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.ItemRecieverBinding;
import com.example.chatapp.databinding.ItemSentBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageAdapter extends  RecyclerView.Adapter{


    Context context ;
    ArrayList<Message> mssgs ;

    final int ITEM_SENT = 1 ;
    final int ITEM_RECEIVE = 2 ;

    String senderRoom , receiverRoom ;

    public MessageAdapter(Context context , ArrayList<Message> mssgs , String senderRoom , String receiverRoom) {
          this.context = context ;
          this.mssgs = mssgs ;
          this.senderRoom = senderRoom ;
          this.receiverRoom = receiverRoom ;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

           if (viewType == ITEM_SENT) {
                 View view = LayoutInflater.from(context).inflate(R.layout.item_sent,parent,false);
                 return new SentViewholder(view);
           }

           else  {

               View view = LayoutInflater.from(context).inflate(R.layout.item_reciever,parent,false);
               return new ReceiveViewholder(view);
           }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


              int reactions[] = new int[]{
                      R.drawable.like,
                      R.drawable.heart,
                      R.drawable.laughing,
                      R.drawable.surprised,
                      R.drawable.sad,
                      R.drawable.angry
              };

                 ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build() ;

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {

             if (holder.getClass() == SentViewholder.class) {
                SentViewholder viewholder = (SentViewholder) holder ;
                viewholder.binding.feeling.setImageResource(reactions[pos]);
                viewholder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else {

                ReceiveViewholder viewholder = (ReceiveViewholder) holder ;
                viewholder.binding.feeling.setImageResource(reactions[pos]) ;
                viewholder.binding.feeling.setVisibility(View.VISIBLE);
            }

            mssgs.get(position).setFeeling(pos);
            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(mssgs.get(position).getMssgId())
                    .setValue(mssgs.get(position));

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .child(mssgs.get(position).getMssgId())
                    .setValue(mssgs.get(position));


            return true; // true is closing popup, false is requesting a new selection
        });


             if(holder.getClass() == SentViewholder.class) {

                 SentViewholder viewholder = (SentViewholder) holder ;
                 viewholder.binding.messageSent.setText(mssgs.get(position).getMssg());

                    if (mssgs.get(position).getFeeling() >=0 ) {
                         viewholder.binding.feeling.setImageResource(reactions[mssgs.get(position).getFeeling()]);
                         viewholder.binding.feeling.setVisibility(View.VISIBLE);

                   }
                   else {
                       viewholder.binding.feeling.setVisibility(View.GONE);
                   }

                     viewholder.binding.messageSent.setOnTouchListener(new View.OnTouchListener() {
                         @Override
                         public boolean onTouch(View v, MotionEvent event) {
                             popup.onTouch(v,event) ;
                             return false;
                         }
                     });
             }
             else  {

                  ReceiveViewholder viewholder = (ReceiveViewholder) holder ;
                 viewholder.binding.messageReceived.setText(mssgs.get(position).getMssg());

                 if (mssgs.get(position).getFeeling() >= 0 ) {
                     viewholder.binding.feeling.setImageResource(reactions[mssgs.get(position).getFeeling()]);
                     viewholder.binding.feeling.setVisibility(View.VISIBLE);

                 }
                 else {
                     viewholder.binding.feeling.setVisibility(View.GONE);
                 }

                 viewholder.binding.messageReceived.setOnTouchListener(new View.OnTouchListener() {
                     @Override
                     public boolean onTouch(View v, MotionEvent event) {
                         popup.onTouch(v,event) ;
                         return false;
                     }
                 });
             }
     }

    @Override
    public int getItemViewType(int position) {

        Message message = mssgs.get(position) ;
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return ITEM_SENT ;
        }
        else return ITEM_RECEIVE;

    }

    @Override
    public int getItemCount() {
        return mssgs.size();
    }

    public class SentViewholder extends RecyclerView.ViewHolder{

          ItemSentBinding binding ;

          public SentViewholder(@NonNull View itemView) {
              super(itemView);
              binding =  ItemSentBinding.bind(itemView);
          }
      }

      public class ReceiveViewholder extends RecyclerView.ViewHolder {

          ItemRecieverBinding binding ;
          public ReceiveViewholder(@NonNull View itemView) {
              super(itemView);
              binding = ItemRecieverBinding.bind(itemView);
          }
      }
}
