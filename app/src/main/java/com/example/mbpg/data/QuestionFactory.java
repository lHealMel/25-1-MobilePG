package com.example.mbpg.data;

import java.util.ArrayList;
import java.util.List;

import gachon.termproject.mbtimusic.model.Question;

public class QuestionFactory {
    public static List<Question> create() {
        List<Question> list = new ArrayList<>();

        list.add(new Question("사교적인 편인가요?", "사람들과 어울리는 걸 좋아해요", "혼자 있는 시간이 좋아요", "EI"));
        list.add(new Question("새로운 사람을 만나는 건 어떤가요?", "즐거워요", "조금 피곤해요", "EI"));
        list.add(new Question("현실적인 편인가요?", "현실 중심이에요", "상상력이 풍부해요", "SN"));
        list.add(new Question("일처리를 어떻게 하나요?", "계획적으로 해요", "그때그때 해요", "JP"));
        list.add(new Question("결정을 내릴 때", "논리적으로 판단해요", "감정적으로 공감해요", "TF"));
        list.add(new Question("사람들과 함께 있을 때", "말이 많아져요", "경청하는 편이에요", "EI"));
        list.add(new Question("아이디어를 떠올릴 때", "과거 경험을 바탕으로 해요", "미래를 상상해요", "SN"));
        list.add(new Question("문제를 해결할 때", "객관적 사실로 판단해요", "사람의 입장을 고려해요", "TF"));
        list.add(new Question("일정을 관리할 때", "정해진 계획이 좋아요", "유동적인 게 좋아요", "JP"));
        list.add(new Question("주말엔?", "사람들과 만나서 시간 보내요", "혼자 쉬는 걸 선호해요", "EI"));
        list.add(new Question("정보를 받아들일 때", "있는 그대로 받아들여요", "의미를 해석해요", "SN"));
        list.add(new Question("다툼이 생기면", "논리적으로 해결하려 해요", "감정을 다독이려 해요", "TF"));
        list.add(new Question("마감일 전에", "미리미리 끝내는 스타일이에요", "마지막에 몰아서 해요", "JP"));
        list.add(new Question("새로운 것을 배울 때", "구체적인 사례로 배우는 걸 좋아해요", "개념과 이론이 좋아요", "SN"));
        list.add(new Question("감정을 표현하는 편인가요?", "잘 표현해요", "속으로 감추는 편이에요", "TF"));
        list.add(new Question("계획된 여행 vs 즉흥 여행", "계획된 여행이 좋아요", "즉흥 여행이 재밌어요", "JP"));

        return list;
    }
}
