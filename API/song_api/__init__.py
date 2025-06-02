from flask import Flask
from flask_cors import CORS

def create_app():
    app = Flask(__name__)
    CORS(app)
    from API.song_api.routes import gemini_bp
    from API.song_api.routes import spotify_bp
    app.register_blueprint(gemini_bp)
    app.register_blueprint(spotify_bp)

    return app