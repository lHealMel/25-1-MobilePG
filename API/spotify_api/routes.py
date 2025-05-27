from flask import Blueprint, request, jsonify
from .spotify_client import ask_gemini, ask_add_gemini

gemini_bp = Blueprint("gemini", __name__)

"""
Get the response from gemini
input(str) : mbti 
output(dict) : genre, genre explanation, songs
response(json)
"""


@gemini_bp.route("/MBTI", methods=["POST"])
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


@gemini_bp.route("/additionMBTI", methods=["POST"])
def addition_ask():
    data = request.json

    # if we could get additional question's answer to reflect the song, implement this;
    mbti = data.get("mbti", "").upper()
    addit = data.get("addit", "").upper()
    if not mbti:
        return jsonify({"error": "MBTI is required"}), 400
    elif not addit:
        return jsonify({"error": "addit is required"}), 400

    try:
        response_text = ask_gemini(mbti)
        return jsonify(response_text)
    except Exception as e:
        return jsonify({"error": str(e)}), 500