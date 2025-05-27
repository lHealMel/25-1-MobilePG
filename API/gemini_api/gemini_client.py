from google import genai
from google.genai import types
import os
import re
from pydantic import BaseModel
import json

# hide api key
GEMINI_API_KEY = os.getenv("GOOGLE_API_KEY")
client = genai.Client(api_key=GEMINI_API_KEY)


class MbtiInfo(BaseModel):
    song: str
    genre: str
    reason: str


def build_prompt(mbti: str) -> str:
    return f"""
You are a music recommendation assistant.
The goal is to recommend Korean songs to Koreans.
The purpose is to recommend songs that the user is satisfied with according to the MBTI.
If the artist name is not Korean, write the name most well-known name. 
Or, if artist has only english name, write both like: artist name in Korean(artist name in English)

Given a user's MBTI type, suggest only :
1. A recommended music only one genre
2. With that genre, short explanation
3. 3 to 5 specific song's name with artist names
4. The songs 

Format your response like:
- Genre: ...(*Korean*) 
- Genre reasons: ...(*Korean*) 
- Songs:
  1. song name - artist names, 
  2. song name - artist names, 
  ...
  
Example of your response:
- Genre: 발라드 (Ballad)
- Genre reasons: INFP 분들은 감수성이 풍부하고 내면의 깊이가 깊어 서정적인 발라드 음악을 통해 감정을 표현하고 위로받는 것을 좋아합니다. 잔잔한 멜로디와 진솔한 가사는 INFP의 공감 능력을 자극하고 깊은 감정적 연결을 만들어냅니다.
- Songs:
  1. 아이유(IU) - 밤편지
  2. 성시경 - 희재
  3. 정승환 - 너였다면
  4. 백예린(Yerin Baek) - Bye bye my blue
You must obey this format. Just 'OBEY' this format. Don't refer to the contents.

MBTI: {mbti}
"""


def build_prompt_add(mbti: str, add: dict) -> str:
    return f"""
You are a music recommendation assistant.
The goal is to recommend Korean songs to Koreans.
The purpose is to recommend songs that the user is satisfied with according to the MBTI.
User's additional information is like : Today's feeling, Today's weather, etc.

Given a user's MBTI type, suggest only :
1. A recommended music only one genre
2. With that genre, short explanation
3. 3 to 5 specific song's name with artist names
4. User's additional information is {add}

Format your response like:
- Genre: ...
- Genre reasons: ...
- Songs:
  1. song name - artist names,  
  2. song name - artist names, 
  ...

MBTI: {mbti}
"""


model_name = "gemini-2.5-flash-preview-04-17"


# ask gemini with just mbti
def ask_gemini(mbti: str) -> dict:
    prompt = build_prompt(mbti)
    response = client.models.generate_content(
        model=model_name,
        contents=prompt,
        config=types.GenerateContentConfig(
            temperature=0.3
        )
    )
    print(response.text)
    return response_2json(response.text)


# ask gemini with mbti, additional information. / IDK about dictionary. it depends on the survey's item
def ask_add_gemini(mbti: str, add: dict) -> dict:
    prompt = build_prompt_add(mbti, add)
    response = client.models.generate_content(
        model=model_name,
        contents=prompt,
        config=types.GenerateContentConfig(
            temperature=0.3
        )
    )
    return response_2json(response.text)


def response_2json(text: str) -> dict:
    lines = text.strip().splitlines()

    genre = ""
    genre_reason = ""
    songs = {}

    for line in lines:
        line = line.strip()
        if line.startswith("- Genre:"):
            genre = line.replace("- Genre:", "").strip()
        elif line.startswith("- Genre reasons:"):
            genre_reason = line.replace("- Genre reasons:", "").strip()
        elif re.match(r"\d+\.\s+\"?.+\"?\s+-", line):
            match = re.match(r'\d+\.\s+"?(.*?)"?\s+-\s+(.*)', line)
            if match:
                title = match.group(1)
                artist = match.group(2)
                songs[title] = artist

    return {
        "genre": genre,
        "genre_reason": genre_reason,
        "songs": songs
    }


if __name__ == "__main__":
    res = ask_gemini("INFP")
    print(res)
