import google.generativeai as genai
import os
import re
import json


def build_prompt(mbti: str) -> str:
    return f"""
You are a music recommendation assistant.

Given a user's MBTI type, suggest only :
1. A recommended music only one genre
2. With that genre, short explanation
2. 3 to 5 specific song's name with artist names

Format your response like:
- Genre: ...
- Genre reasons: ...
- Songs:
  1. ...
  2. ...
  3. ...

MBTI: {mbti}
"""


def ask_gemini(mbti: str) -> str:
    prompt = build_prompt(mbti)
    response = model.generate_content(prompt)
    return response.text


def response_2json(text: str) -> dict:
    lines = text.strip().splitlines()

    genre = ""
    genre_reason = ""
    songs = []

    for line in lines:
        line = line.strip()
        if line.startswith("- Genre:"):
            genre = line.replace("- Genre:", "").strip()
        elif line.startswith("- Genre reasons:"):
            genre_reason = line.replace("- Genre reasons:", "").strip()
        elif re.match(r"\d+\.\s+\"?.+\"?\s+-", line):
            # 예: 1. "Holocene" - Bon Iver
            match = re.match(r'\d+\.\s+"?(.*?)"?\s+-\s+(.*)', line)
            if match:
                title = match.group(1)
                artist = match.group(2)
                songs.append(f"{title} - {artist}")

    return {
        "genre": genre,
        "genre_reason": genre_reason,
        "songs": songs
    }


if __name__ == "__main__":
    # hide API key
    GEMINI_API_KEY = os.getenv("GOOGLE_API_KEY")

    genai.configure(api_key=GEMINI_API_KEY)
    model = genai.GenerativeModel("gemini-2.0-flash")

    generation_config = genai.types.GenerationConfig(
        temperature=0.3  # 기본은 0.7~1.0 → 낮추면 더 예측 가능해짐
    )
    res = ask_gemini("INFP")
    res_json = response_2json(res)
    print(res)
    print('\n\n\n', res_json)
