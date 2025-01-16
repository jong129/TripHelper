from flask import Flask
from pymongo import MongoClient

def create_app():
    app = Flask(__name__)
    app.config.from_object('config.settings')
    client = MongoClient(app.config['MONGO_URI'])
    app.db = client['user_database']
    return app
