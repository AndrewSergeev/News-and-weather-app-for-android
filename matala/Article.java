package il.co.freebie.matala;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;

import okhttp3.Response;

/**
 * Created by one 1 on 08-Jan-19.
 */

public class Article implements Serializable {
    private String sourceName;
    private String title;
    private String author;
    private String description;
    private String url;
    private String imageUrl;
    private String publishedAt;
    private String articleContent;

    public Article(String sourceName, String title, String author, String description, String url,
                   String imageUrl, String publichedAt, String articleContent)
    {
        this.sourceName = sourceName;
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.publishedAt = publichedAt;
        this.articleContent = articleContent;

    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublichedAt() {
        return publishedAt;
    }

    public void setPublichedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }
}
