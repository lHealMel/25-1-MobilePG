from flask import Blueprint, request, jsonify
from .gemini_client import ask_gemini, ask_add_gemini
from .spotify_client import song_search

gemini_bp = Blueprint("gemini", __name__)
spotify_bp = Blueprint("spotify", __name__)

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

    # if we could get an additional question's answer to reflect the song, implement this;
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


@spotify_bp.route("/search", methods=["POST"])
def search():
    data = request.json
    artist = data.get("artist", "")
    song_name = data.get("song", "")
    if not song_name or not artist:
        return jsonify({"error": "song name and artist name is required"}), 400

    try:
        response_text = song_search(artist, song_name)
        return jsonify(response_text)
    except Exception as e:
        return jsonify({"error": str(e)}), 500
