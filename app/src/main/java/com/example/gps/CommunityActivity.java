package com.example.gps;

import android.os.Bundle;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gps.CommunityAdapter;
import com.example.gps.CommunityPost;
import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommunityAdapter adapter;
    private List<CommunityPost> posts = new ArrayList<>();
    private EditText etPostContent;
    private Button btnPost, btnPhotoSpot;

=======
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CommunityActivity extends AppCompatActivity {

>>>>>>> eb6a61de94925d9caf84c525f5335968ab81c5fb
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("산책 커뮤니티");
<<<<<<< HEAD

        initViews();
        setupRecyclerView();
        loadSamplePosts();
        setupButtons();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_posts);
        etPostContent = findViewById(R.id.et_post_content);
        btnPost = findViewById(R.id.btn_post);
        btnPhotoSpot = findViewById(R.id.btn_photo_spot);
    }

    private void setupRecyclerView() {
        adapter = new CommunityAdapter(posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadSamplePosts() {
        // 샘플 포스트 데이터
        posts.add(new CommunityPost("산책러버", "남한산성 1코스 완주했습니다! 정말 아름다운 경치였어요.", "2시간 전", 15, 3));
        posts.add(new CommunityPost("등산초보", "북한산 정상에서 찍은 사진입니다. 정말 멋진 전경이에요!", "5시간 전", 28, 7));
        posts.add(new CommunityPost("자연사랑", "오늘 날씨가 너무 좋아서 산책하기 딱이었습니다.", "1일 전", 12, 2));
        posts.add(new CommunityPost("포토그래퍼", "남한산성 서문 근처에서 찍은 사진입니다. 추천 포토스팟!", "2일 전", 45, 12));
        
        adapter.notifyDataSetChanged();
    }

    private void setupButtons() {
        btnPost.setOnClickListener(v -> {
            String content = etPostContent.getText().toString().trim();
            if (!content.isEmpty()) {
                // 새 포스트 추가
                CommunityPost newPost = new CommunityPost("나", content, "방금 전", 0, 0);
                posts.add(0, newPost);
                adapter.notifyItemInserted(0);
                etPostContent.setText("");
                Toast.makeText(this, "포스트가 등록되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        btnPhotoSpot.setOnClickListener(v -> {
            // 포토스팟 추천 기능
            showPhotoSpotRecommendations();
        });
    }

    private void showPhotoSpotRecommendations() {
        // 포토스팟 추천 다이얼로그 표시
        String[] photoSpots = {
            "남한산성 서문 - 역사적 분위기",
            "북한산 정상 - 서울 전경",
            "영월정 - 전통 건축물",
            "수어장대 - 조선시대 건축",
            "벌봉 - 자연 경관"
        };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("추천 포토스팟")
               .setItems(photoSpots, (dialog, which) -> {
                   String selectedSpot = photoSpots[which];
                   etPostContent.setText("📸 " + selectedSpot + "에서 찍은 사진입니다!");
                   Toast.makeText(this, "포토스팟이 입력되었습니다.", Toast.LENGTH_SHORT).show();
               })
               .show();
=======
>>>>>>> eb6a61de94925d9caf84c525f5335968ab81c5fb
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 