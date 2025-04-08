from google import genai
import os

# hide API key
client = genai.Client(api_key=os.getenv("GOOGLE_API_KEY"))

response = client.models.generate_content(
    model="gemini-2.0-flash",
    contents="한국어 테스트!",
)

print(response.text)
