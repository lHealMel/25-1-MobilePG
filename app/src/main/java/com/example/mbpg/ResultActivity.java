package com.example.mbpg;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mbpg.api.GeminiService;
import com.example.mbpg.api.SongArtistPair;
import com.example.mbpg.api.SpotifyResultCallback;
import com.example.mbpg.api.SpotifyService;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.util.List;


public class ResultActivity extends AppCompatActivity {

    private TextView resultText, genreText, songsText, currentTrackText;
    private Button btnBackToHome;
    private static final String TAG = "ResultActivity";
    private static final String CLIENT_ID = "17969602a6a74820b44f5cacaad948ba"; // client ID from API
    private static final String REDIRECT_URI = "com.example.mbpg://auth"; // callback uri
    private static final String SPOTIFY_PACKAGE_NAME = "com.spotify.music";

    private SpotifyAppRemote mSpotifyAppRemote;
    private String mTrackUriToPlay = null; // API 응답 후 Spotify 연결 전에 재생할 트랙 URI 임시 저장
    private String mTrackNameToPlay = null; // 재생할 트랙 이름
    private boolean firstTrackProcessed = false; // 추천 목록 중 첫 번째 재생 가능한 트랙 처리 여부 플래그

    @Override
    protected void onStart() {
        super.onStart();
        // result activity로 넘어올 때 Spotify에 연결 method를 실행합니다.
        // 이미 연결 시도 중이거나 연결된 상태라면 중복 호출을 방지할 수 있습니다.
        if (mSpotifyAppRemote == null || !mSpotifyAppRemote.isConnected()) {
            Log.d(TAG, "onStart: Attempting to connect to Spotify.");
            connectToSpotify(); //
        } else {
            Log.d(TAG, "onStart: Already connected or connecting to Spotify.");
            handleSuccessfulConnection();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        firstTrackProcessed = false;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.result), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        resultText = findViewById(R.id.result_text);
        genreText = findViewById(R.id.genre_text);
        songsText = findViewById(R.id.songs_text);
        currentTrackText = findViewById(R.id.current_track_text); // 현재 재생 곡 정보 TextView
        btnBackToHome = findViewById(R.id.btn_back_to_home);


        // MBTI 결과 가져오기
        String mbti = getIntent().getStringExtra("MBTI_RESULT");
        if (mbti == null || mbti.isEmpty()) {
            mbti = "N/A"; // 기본값 설정
            Log.w(TAG, "MBTI_RESULT not found in Intent, defaulting to N/A");
        }
        resultText.setText(getString(R.string.mbti_type, mbti));

        // GeminiService를 통해 MBTI 기반 노래-가수 가져오기
        fetchRecommendations(mbti);

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
    }

    /**
     * GeminiService를 호출하여 MBTI에 따른 음악 추천 정보를 가져오고 UI에 표시합니다.
     * 추천 곡 목록을 받으면 각 곡에 대해 Spotify 트랙 정보를 조회합니다.
     *
     * @param mbti 사용자의 MBTI 유형
     */
    private void fetchRecommendations(String mbti) {
        GeminiService.getResultByMbti(mbti, result -> runOnUiThread(() -> {
            if (result == null) {
                Log.e(TAG, "GeminiService result is null for MBTI: " + mbti);
                genreText.setText(getString(R.string.mbti_genre, "정보 없음"));
                songsText.setText(getString(R.string.mbti_songs, "추천 곡을 가져오지 못했습니다."));
                return;
            }

            genreText.setText(getString(R.string.mbti_genre, result.getGenres()));
            CharSequence songsDisplay = result.getSongs();
            songsText.setText(getString(R.string.mbti_songs, songsDisplay != null ? songsDisplay.toString() : "추천 곡 없음"));

            List<SongArtistPair> pairs = result.getSongArtistPairs();
            if (pairs != null && !pairs.isEmpty()) {
                Log.d(TAG, pairs.size() + " song/artist pairs received.");
                firstTrackProcessed = false; // 새 추천 목록에 대해 플래그 리셋
                mTrackUriToPlay = null; // 이전 트랙 정보 초기화
                mTrackNameToPlay = null;

                for (SongArtistPair pair : pairs) {
                    if (firstTrackProcessed) break; // 이미 첫 곡을 처리했으면 더 이상 Spotify API 호출 안 함

                    String artistForSearch = pair.getArtistSearchName();
                    String song = pair.getSong();

                    if (artistForSearch != null && !artistForSearch.isEmpty() && song != null && !song.isEmpty()) {
                        Log.d(TAG, "Calling SpotifyService for Artist: " + artistForSearch + ", Song: " + song);
                        fetchSpotifyTrackInfo(artistForSearch, song);
                    } else {
                        Log.w(TAG, "Skipping Spotify call due to empty artist or song. Artist: [" + artistForSearch + "], Song: [" + song + "]");
                    }
                }
            } else {
                Log.d(TAG, "No song/artist pairs available to query Spotify.");
                songsText.setText(getString(R.string.mbti_songs, "추천 곡 목록이 비어있습니다."));
            }
        }));
    }

    /**
     * SpotifyService를 호출하여 특정 아티스트와 곡명에 대한 Spotify 트랙 정보를 가져옵니다.
     *
     * @param artistForSearch 검색용 아티스트명
     * @param song            곡명
     */
    private void fetchSpotifyTrackInfo(String artistForSearch, String song) {
        SpotifyService.geturifrommbti(artistForSearch, song, new SpotifyResultCallback() {
            @Override
            public void onSuccess(String trackUri, String trackName, String artistName, String albumArtUrl, String previewUrl, boolean isPlayable) {
                Log.i(TAG, "Spotify Success for " + trackName + " - " + artistName + ": URI " + trackUri + ", Playable: " + isPlayable);

                if (isPlayable && !firstTrackProcessed) {
                    firstTrackProcessed = true; // 첫 번째 재생 가능한 트랙 처리됨으로 표시
                    if (mSpotifyAppRemote != null && mSpotifyAppRemote.isConnected()) {
                        Log.d(TAG, "Spotify connected. Playing track immediately: " + trackName);
                        playTrack(trackUri, trackName);
                    } else {
                        // Spotify App Remote가 아직 연결되지 않은 경우, URI를 저장해두고 연결 후 재생
                        mTrackUriToPlay = trackUri;
                        mTrackNameToPlay = trackName;
                        Log.d(TAG, "Spotify App Remote not connected yet. Saving URI of the first track to play later: " + trackUri);
                        // 연결 시도가 진행 중이므로, 연결이 완료되면 handleSuccessfulConnection에서 재생될 것임.
                    }
                } else if (isPlayable && firstTrackProcessed) {
                    Log.d(TAG, "First track already processed or playing. Skipping automatic playback for: " + trackName);
                } else if (!isPlayable) {
                    Log.w(TAG, "Track " + trackName + " by " + artistName + " is not playable on Spotify.");
                }
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "Spotify Error for " + song + " by " + artistForSearch + ": " + message);
                // 특정 곡 검색 실패 시 사용자에게 알릴 수 있음 (예: Toast)
                // Toast.makeText(ResultActivity.this, song + " 정보 조회 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Spotify App Remote를 사용하여 지정된 트랙 URI를 재생합니다.
     *
     * @param trackUri  재생할 Spotify 트랙 URI
     * @param trackName 재생할 트랙의 이름 (로깅 및 UI 업데이트용)
     */
    private void playTrack(String trackUri, String trackName) {
        if (mSpotifyAppRemote == null || !mSpotifyAppRemote.isConnected()) {
            Log.w(TAG, "Spotify App Remote is not connected. Cannot play track: " + trackUri);
            // 연결이 끊겼거나 없는 경우, 다시 연결 시도하거나 사용자에게 알림
            Toast.makeText(this, "Spotify에 연결되지 않았습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            // 필요하다면 mTrackUriToPlay에 다시 저장하여 연결 후 재생 시도
            this.mTrackUriToPlay = trackUri;
            this.mTrackNameToPlay = trackName;
            connectToSpotify(); // 연결 재시도
            return;
        }

        mSpotifyAppRemote.getPlayerApi().play(trackUri)
                .setResultCallback(empty -> {
                    String playingMsg = "재생 시작: " + (trackName != null ? trackName : trackUri);
                    Log.d(TAG, playingMsg);
                    Toast.makeText(ResultActivity.this, playingMsg, Toast.LENGTH_SHORT).show();
                    currentTrackText.setText("현재 재생 중: " + trackName);
                    currentTrackText.setVisibility(View.VISIBLE);
                    mTrackUriToPlay = null; // 재생 시작 후 임시 저장된 URI 초기화
                    mTrackNameToPlay = null;
                })
                .setErrorCallback(throwable -> {
                    Log.e(TAG, "Cannot start playback for track: " + (trackName != null ? trackName : trackUri), throwable);
                    Toast.makeText(ResultActivity.this, "재생에 실패했습니다: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    currentTrackText.setText("재생 실패");
                    currentTrackText.setVisibility(View.VISIBLE);
                    mTrackUriToPlay = null; // 오류 시에도 초기화
                    mTrackNameToPlay = null;
                });
    }

    /**
     * Spotify App Remote SDK에 연결
     */
    private void connectToSpotify() {
        Log.d(TAG, "connectToSpotify() called. mSpotifyAppRemote is " + (mSpotifyAppRemote == null ? "null" : "not null"));
        if (mSpotifyAppRemote != null && mSpotifyAppRemote.isConnected()) {
            Log.d(TAG, "Already connected to Spotify. Skipping new connection attempt.");
            return;
        } else {
            Log.d(TAG, "Not connected");
        }

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        Log.d(TAG, "connectionParams set");
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d(TAG, "Spotify 연결 성공");
                        handleSuccessfulConnection();
                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e(TAG, "Spotify 연결 실패: " + (throwable.getMessage() != null ? throwable.getMessage() : "알 수 없는 오류"), throwable);
                        String generalErrorMessage = "Spotify 연결에 실패했습니다. 잠시 후 다시 시도해주세요.";

                        if (throwable instanceof com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp) {
                            new AlertDialog.Builder(ResultActivity.this)
                                    .setTitle("Spotify 앱 미설치")
                                    .setMessage("음악을 재생하려면 Spotify 앱이 필요합니다. 지금 설치하시겠습니까?")
                                    .setPositiveButton("설치하기", (dialog, which) -> {
                                        try {
                                            // Play Store 앱으로 직접 이동
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + SPOTIFY_PACKAGE_NAME)));
                                        } catch (ActivityNotFoundException anfe) {
                                            // Play Store 앱이 없는 경우 (예: 일부 중국 제조사 기기 또는 커스텀 롬) 웹 브라우저로 이동
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + SPOTIFY_PACKAGE_NAME)));
                                        }
                                    })
                                    .setNegativeButton("나중에", (dialog, which) -> {
                                        Toast.makeText(ResultActivity.this, "Spotify 앱 설치 후 이용해주세요.", Toast.LENGTH_LONG).show();
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info) // 아이콘 설정 (선택 사항)
                                    .setCancelable(false) // 뒤로가기 버튼으로 닫히지 않도록 설정 (선택 사항)
                                    .show();
                        } else if (throwable instanceof com.spotify.android.appremote.api.error.UserNotAuthorizedException) {
                            Toast.makeText(ResultActivity.this, "Spotify 앱 인증에 실패했습니다. Spotify 앱에서 로그인 상태를 확인해주세요.", Toast.LENGTH_LONG).show();
                        } else {
                            String specificMessage = throwable.getMessage();
                            if (specificMessage != null && !specificMessage.isEmpty()) {
                                Toast.makeText(ResultActivity.this, "Spotify 연결 실패: " + specificMessage, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ResultActivity.this, generalErrorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

        Log.d(TAG, "func connect to spotify done");
    }

    /**
     * Spotify App Remote 연결 성공 후 호출됩니다.
     * 저장된 트랙이 있다면 재생하고, 플레이어 상태를 구독합니다.
     */
    private void handleSuccessfulConnection() {
        Log.d(TAG, "handleSuccessfulConnection() 호출됨 - Spotify 제어 가능 상태");

        // Spotify App Remote 연결 성공 시, 저장된 트랙 URI가 있으면 재생
        if (mTrackUriToPlay != null) {
            Log.d(TAG, "Spotify App Remote connected. Playing saved track: " + (mTrackNameToPlay != null ? mTrackNameToPlay : mTrackUriToPlay));
            playTrack(mTrackUriToPlay, mTrackNameToPlay);
        }

        // 현재 재생 중인 트랙 정보 구독 (선택 사항, UI 업데이트 등에 활용 가능)
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(playerState -> {
            final Track track = playerState.track;
            if (track != null) {
                Log.d(TAG, "현재 재생 중: " + track.name + " by " + track.artist.name + (playerState.isPaused ? " (일시중지됨)" : ""));
                if (!playerState.isPaused && (mTrackNameToPlay == null || !mTrackNameToPlay.equals(track.name))) {
                    // 외부에서 재생이 시작되었거나 다른 곡으로 변경된 경우
                    currentTrackText.setText("현재 재생 중: " + track.name);
                    currentTrackText.setVisibility(View.VISIBLE);
                } else if (playerState.isPaused && currentTrackText.getText().toString().contains(track.name)) {
                    currentTrackText.setText("일시중지됨: " + track.name);
                }
            } else {
                Log.d(TAG, "현재 재생 중인 트랙 없음");
                currentTrackText.setText("재생 중인 트랙 없음");
                currentTrackText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 앱이 화면에서 사라지거나 종료될 때 Spotify 연결 해제
        // 주의: onStop에서 항상 연결을 해제하면 백그라운드 재생이 중단될 수 있습니다.
        // 앱의 요구사항에 따라 연결 해제 시점을 조절해야 합니다.
        // 예를 들어, 사용자가 명시적으로 음악을 중지하거나 앱을 완전히 종료할 때 해제할 수 있습니다.
        if (mSpotifyAppRemote != null && mSpotifyAppRemote.isConnected()) {
            SpotifyAppRemote.disconnect(mSpotifyAppRemote); // 필요에 따라 주석 해제 또는 로직 변경
            Log.d(TAG, "Spotify 연결 해제됨 (onStop).");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티가 완전히 종료될 때 Spotify 연결 해제
        if (mSpotifyAppRemote != null && mSpotifyAppRemote.isConnected()) {
            SpotifyAppRemote.disconnect(mSpotifyAppRemote);
            Log.d(TAG, "Spotify 연결 해제됨 (onDestroy).");
        }
    }
}
