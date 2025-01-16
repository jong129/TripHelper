from app import create_app
from app.routes.auth import auth_bp
from app.routes.user import user_bp
from app.routes.recommend import recommend_bp

app = create_app()

# Blueprint 등록
app.register_blueprint(auth_bp)
app.register_blueprint(user_bp)
app.register_blueprint(recommend_bp)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
