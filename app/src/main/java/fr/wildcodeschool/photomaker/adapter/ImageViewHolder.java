package fr.wildcodeschool.photomaker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import fr.wildcodeschool.photomaker.R;

public class ImageViewHolder  extends RecyclerView.ViewHolder {
    private SimpleDraweeView imageView;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_layout_content);
    }

    public SimpleDraweeView getImageView() {
        return imageView;
    }
}
