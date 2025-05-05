import google.generativeai as genai
import os
import re
from pydantic import BaseModel
import json

# hide api key
GEMINI_API_KEY = os.getenv("GOOGLE_API_KEY")
genai.configure(api_key=GEMINI_API_KEY)


class MbtiInfo(BaseModel):
    song: str
    genre: str
    reason: str


generation_config = genai.types.GenerationConfig(
    temperature=0.3,
    # response_mime_type='application/json',
    # response_schema=MbtiInfo
)

model = genai.GenerativeModel(
    "gemini-2.0-flash",
    generation_config=generation_config
)


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
  1. song name - artist names,  
  2. song name - artist names, 
  ...

MBTI: {mbti}
"""

def build_prompt_add(mbti: str, add: dict) -> str:
    return f"""
You are a music recommendation assistant.

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

# ask gemini with just mbti
def ask_gemini(mbti: str) -> dict:
    prompt = build_prompt(mbti)
    response = model.generate_content(prompt)
    response_json = response_2json(response.text)
    return response_json

# ask gemini with mbti, additional information. / IDK about dictionary. it depends on the survey's item
def ask_add_gemini(mbti: str, add:dict) -> dict:
    prompt = build_prompt_add(mbti, add)
    response = model.generate_content(prompt)
    response_json = response_2json(response.text)
    return response_json


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
