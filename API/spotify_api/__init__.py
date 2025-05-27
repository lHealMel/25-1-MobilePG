from flask import Flask

def create_app():
    app = Flask(__name__)

    from API.gemini_api.routes import gemini_bp
    app.register_blueprint(gemini_bp)

    return app