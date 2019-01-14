package fr.wildcodeschool.photomaker.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import fr.wildcodeschool.photomaker.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<Uri> uriList;

    public ImageAdapter(List<Uri> uriList) {
        this.uriList = uriList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int i) {
        Uri uri = this.uriList.get(i);

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();

        Fresco.getImagePipeline().prefetchToBitmapCache(imageRequest, holder.getImageView().getContext());

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(holder.getImageView().getController())
                .setAutoPlayAnimations(false)
                .build();

        holder.getImageView().setController(controller);
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }
}
