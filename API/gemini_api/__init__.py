from flask import Flask
from flask_cors import CORS

def create_app():
    app = Flask(__name__)
    CORS(app)
    from API.gemini_api.routes import gemini_bp
    app.register_blueprint(gemini_bp)

    return app