package com.example.mbpg.data;

import com.example.mbpg.model.MbtiResult;

public class DummyMusicData {

    public static MbtiResult getResultByMbti(String mbti) {
        // 추후 외부 API 연동 예정
        return new MbtiResult(mbti, "", "");
    }
}
