package il.co.freebie.matala;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by one 1 on 08-Jan-19.
 */

public class ArticlesListFragment extends Fragment {
    private static List<Article> articlesList;

    interface OnArticleFragmentListener{
        void onClicked(Article article);
    }

    OnArticleFragmentListener callBack;

    public static ArticlesListFragment newInstance(List<Article> i_articlesList){
        ArticlesListFragment fragment = new ArticlesListFragment();
        //Bundle bundle = new Bundle();
        //bundle.put
        articlesList = i_articlesList;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.articles_list_fragment, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArticleAdapter adapter = new ArticleAdapter(articlesList);
        adapter.setListener(new ArticleAdapter.ArticleListener() {
            @Override
            public void onArticleClicked(int position, View view) {
                callBack.onClicked(articlesList.get(position));
            }
        });

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callBack = (OnArticleFragmentListener)context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement OnArticleFragmentListener interface");
        }
    }
}
