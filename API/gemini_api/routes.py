from flask import Blueprint, request, jsonify
from .gemini_client import ask_gemini

gemini_bp = Blueprint("gemini", __name__)

"""
Get the response from gemini
input(str) : mbti 
output(dict) : genre, genre explanation, songs
response(json)
"""


@gemini_bp.route("/ask", methods=["POST"])
def ask():
    data = request.json
    mbti = data.get("mbti", "").upper()
    if not mbti:
        return jsonify({"error": "MBTI is required"}), 400

    try:
        response_text = ask_gemini(mbti)
        return jsonify(response_text)
    except Exception as e:
        return jsonify({"error": str(e)}), 500
