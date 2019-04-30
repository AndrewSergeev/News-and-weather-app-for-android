package il.co.freebie.matala;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by one 1 on 12-Jan-19.
 */

public class SelectedArticleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");
        setContentView(R.layout.activity_selected_article);

        final Article article = (Article) getIntent().getSerializableExtra("article");

        ImageView articleImageView = findViewById(R.id.selected_article_iv);
        TextView titleTv = findViewById(R.id.selected_article_title_tv);
        TextView authorAndSrcnameTv = findViewById(R.id.selected_article_author_and_srcname_tv);
        TextView publishedAtTv = findViewById(R.id.selected_article_pubtime_tv);
        TextView descriptionTv = findViewById(R.id.selected_article_description_tv);
        TextView contentTv = findViewById(R.id.selected_article_content_tv);
        Button readFullBtn = findViewById(R.id.to_full_article_btn);

        Picasso.get().load(article.getImageUrl()).into(articleImageView);
        titleTv.setText(article.getTitle());
        authorAndSrcnameTv.setText(article.getAuthor() + ", " + article.getSourceName());
        publishedAtTv.setText(article.getPublichedAt());
        descriptionTv.setText(article.getDescription());
        contentTv.setText(article.getArticleContent().split("\\[")[0]);

        readFullBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                startActivity(browserIntent);
            }
        });
    }
}
