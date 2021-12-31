package com.mip.chatapp.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MESSAGE_TYPE_LEFT = 0, MESSAGE_TYPE_RIGHT = 1;

    /**As the name suggests, it's the context of current state of the application/object.
     * It lets newly-created objects understand what has been going on.
     * Typically you call it to get information regarding another part of your program (activity and package/application).
     * You can get the context by invoking getApplicationContext(), getContext(), getBaseContext()
     * or this (when in a class that extends from Context, such as the Application, Activity, Service and IntentService classes).*/
    private final Context m_context;
    private final List<Chat> m_chat;
    private String imageURL;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context m_context, List<Chat> m_chat, String imageURL) {
        this.m_context = m_context;
        this.m_chat = m_chat;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /**LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts.
         *
         * findViewById just gives you a reference to a view than has already been created.
         You might think that you haven't created any views yet, but whenever you call setContentView in onCreate,
         the activity's layout along with its subviews gets inflated (created) behind the scenes.*/

        if(viewType == MESSAGE_TYPE_RIGHT) {
            View view = LayoutInflater.from(m_context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

        View view = LayoutInflater.from(m_context).inflate(R.layout.chat_item_left, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = m_chat.get(position);

        holder.showMessage.setText(chat.getMessage());
        if(imageURL.equals("default")) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            Glide.with(m_context).load(imageURL).into(holder.profileImage);
        }

        if(position == m_chat.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else{
                holder.txt_seen.setText("Delivered");
            }
        }else{
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return m_chat.size();
    }

    /**A ViewHolder is more than just a dumb object that only holds the item’s views.
     *  It is the very object that represents each item in our collection and will be used to display it.*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public ImageView profileImage;
        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.show_message);
            profileImage = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(m_chat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MESSAGE_TYPE_RIGHT;
        }

        return MESSAGE_TYPE_LEFT;
    }
}
