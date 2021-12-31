package com.mip.chatapp.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mip.chatapp.MessageActivity;
import com.mip.chatapp.R;
import com.mip.chatapp.model.Chat;
import com.mip.chatapp.model.User;
import java.util.List;

/**Let’s assume you want to display a list in your Android app. For this you will use the ListView provided by Android.
 * ListViews don’t actually contain any data themselves. It’s just a UI element without data in it.
 * You can populate your ListViews by using an Android adapter.
 * Adapter is an interface whose implementations provide data and control the display of that data.
 * ListViews own adapters that completely control the ListView’s display.
 * So adapters control the content displayed in the list as well as how to display it.*/

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    /**As the name suggests, it's the context of current state of the application/object.
     * It lets newly-created objects understand what has been going on.
     * Typically you call it to get information regarding another part of your program (activity and package/application).
     * You can get the context by invoking getApplicationContext(), getContext(), getBaseContext()
     * or this (when in a class that extends from Context, such as the Application, Activity, Service and IntentService classes).*/
    private final Context m_context;
    private final List<User> m_users;
    private boolean isChat;

    String theLastMessage;

    public UserAdapter(Context m_context, List<User> m_users, boolean isChat) {
        this.m_context = m_context;
        this.m_users = m_users;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /**LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts.
         *
         * findViewById just gives you a reference to a view than has already been created.
         You might think that you haven't created any views yet, but whenever you call setContentView in onCreate,
         the activity's layout along with its subviews gets inflated (created) behind the scenes.*/

        View view = LayoutInflater.from(m_context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = m_users.get(position);
        holder.m_username.setText(user.getUsername());

        if(user.getImageURL().equals("default")) {
            holder.m_profile_image.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            Glide.with(m_context).load(user.getImageURL()).into(holder.m_profile_image);
        }

        if(isChat){
            lastMessage(user.getID(), holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }

        if(isChat)
        {
            if(user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }
            else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(m_context, MessageActivity.class);
                intent.putExtra("userID", user.getID());
                m_context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_users.size();
    }

    /**A ViewHolder is more than just a dumb object that only holds the item’s views.
     *  It is the very object that represents each item in our collection and will be used to display it.*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView m_username;
        public ImageView m_profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            m_username = itemView.findViewById(R.id.username);
            m_profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    private void lastMessage(String userID, TextView last_msg){
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");

        dbRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userID) ||
                            chat.getReceiver().equals(userID) && chat.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chat.getMessage();
                    }
                }

                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
