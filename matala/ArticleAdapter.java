package il.co.freebie.matala;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by one 1 on 08-Jan-19.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> articles;
    private ArticleListener listener;

    interface ArticleListener {
        void onArticleClicked(int position,View view);
    }

    public void setListener(ArticleListener listener) {
        this.listener = listener;
    }

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_cell,parent,false);
        ArticleViewHolder articleViewHolderViewHolder = new ArticleViewHolder(view);
        return articleViewHolderViewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        //holder.articleImageIv.setImageBitmap(article.getArticleImage());
        holder.articleTitleTv.setText(article.getTitle());
        holder.articlePublishedWhenTv.setText(article.getPublichedAt());
        Picasso.get().load(article.getImageUrl()).resize(300,200).into(holder.articleImageIv);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImageIv;
        TextView articleTitleTv;
        TextView articlePublishedWhenTv;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            articleImageIv = itemView.findViewById(R.id.article_image_iv);
            articleTitleTv = itemView.findViewById(R.id.article_title_tv);
            articlePublishedWhenTv = itemView.findViewById(R.id.article_published_at_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                    {
                        listener.onArticleClicked(getAdapterPosition(),view);
                    }
                }
            });
        }
    }
}
