package com.example.triviaapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.triviaapp.Common.Common;
import com.example.triviaapp.Interface.ItemClickListener;
import com.example.triviaapp.Interface.RankingCallBack;
import com.example.triviaapp.ViewHolder.RankingViewHolder;
import com.example.triviaapp.model.Question;
import com.example.triviaapp.model.QuestionScore;
import com.example.triviaapp.model.Ranking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RankingFragment extends Fragment {

    private DatabaseReference questionScore, rankingDb;

    private int sum = 0;



    static RankingFragment newInstance(){
        return new RankingFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingDb = database.getReference("Ranking");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        //init view
        RecyclerView rankingList = myFragment.findViewById(R.id.rankingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUser(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
            }
        });

        FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class, R.layout.ranking_layout, RankingViewHolder.class, rankingDb.orderByChild("score")) {

            @Override
            protected void populateViewHolder(RankingViewHolder rankingViewHolder, final Ranking model, int i) {
                rankingViewHolder.cardName.setText(model.getUserName());
                rankingViewHolder.cardScore.setText(String.valueOf(model.getScore()));

                rankingViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(), CategoryScore.class);
                        scoreDetail.putExtra("viewUser", model.getUserName());
                        startActivity(scoreDetail);

                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);


        return myFragment;
    }


    private void updateScore(final String userName, final RankingCallBack<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    QuestionScore question = data.getValue(QuestionScore.class);
                    sum += question.getScore();
            }
                Log.d("SUM", sum + " ");

                Ranking ranking = new Ranking(userName, sum);
                callback.callBack(ranking);
                rankingDb.child(ranking.getUserName()).setValue(ranking);
            }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

    }

}
