from flask import Blueprint, request, jsonify
from .gemini_client import ask_gemini

gemini_bp = Blueprint("gemini", __name__)

@gemini_bp.route("/ask", methods=["POST"])
def ask():
    data = request.json
    user_prompt = data.get("prompt", "")

    if not user_prompt:
        return jsonify({"error": "Prompt is required"}), 400

    try:
        response_text = ask_gemini(user_prompt)
        return jsonify({"response": response_text})
    except Exception as e:
        return jsonify({"error": str(e)}), 500