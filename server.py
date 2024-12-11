from flask import Flask, request, jsonify
from pymongo import MongoClient
import uuid
import re

app = Flask(__name__)

# MongoDB 연결 설정
client = MongoClient("mongodb://localhost:27017/")
db = client['user_database']
users_collection = db['users']
verification_codes_collection = db['verification_codes']

# 이메일 인증 API (회원가입 및 비밀번호 찾기 공통)
@app.route('/api/send-verification-code', methods=['POST'])
def send_verification_code():
    data = request.json
    email = data.get('email')

    if not email or not re.match(r"[^@]+@[^@]+\.[^@]+", email):
        return jsonify({'message': '유효한 이메일을 입력해주세요.'}), 400

    verification_code = "{:06d}".format(uuid.uuid4().int % 1000000)
    verification_codes_collection.update_one(
        {'email': email},
        {'$set': {'code': verification_code}},
        upsert=True
    )

    print(f"인증번호 생성: {verification_code} (이메일: {email})")
    return jsonify({'message': '인증번호가 발송되었습니다.', 'code': verification_code}), 200

# 이메일 인증 API (아이디 찾기 전용)
@app.route('/api/send-verification-code/id', methods=['POST'])
def send_verification_code_for_id():
    data = request.json
    email = data.get('email')

    if not email or not re.match(r"[^@]+@[^@]+\.[^@]+", email):
        return jsonify({'message': '유효한 이메일을 입력해주세요.'}), 400

    verification_code = "{:06d}".format(uuid.uuid4().int % 1000000)
    verification_codes_collection.update_one(
        {'email': email},
        {'$set': {'code': verification_code}},
        upsert=True
    )

    print(f"아이디 찾기용 인증번호 생성: {verification_code} (이메일: {email})")
    return jsonify({'message': '인증번호가 발송되었습니다.'}), 200

# 이메일 인증 API (비밀번호 찾기 전용)
@app.route('/api/send-verification-code/password', methods=['POST'])
def send_verification_code_for_password():
    data = request.json
    email = data.get('email')

    if not email or not re.match(r"[^@]+@[^@]+\.[^@]+", email):
        return jsonify({'message': '유효한 이메일을 입력해주세요.'}), 400

    verification_code = "{:06d}".format(uuid.uuid4().int % 1000000)
    verification_codes_collection.update_one(
        {'email': email},
        {'$set': {'code': verification_code}},
        upsert=True
    )

    print(f"비밀번호 찾기용 인증번호 생성: {verification_code} (이메일: {email})")
    return jsonify({'message': '인증번호가 발송되었습니다.'}), 200

# 로그인 API
@app.route('/api/login', methods=['POST'])
def login():
    data = request.json
    user_id = data.get('id')
    password = data.get('password')

    if not user_id:
        return jsonify({'message': '아이디를 입력해주세요.'}), 400
    if not password:
        return jsonify({'message': '비밀번호를 입력해주세요.'}), 400

    user = users_collection.find_one({'id': user_id, 'password': password})
    if user:
        return jsonify({'success': True, 'message': '로그인 성공!'}), 200
    else:
        return jsonify({'success': False, 'message': '아이디 또는 비밀번호가 잘못되었습니다.'}), 401

# 아이디 중복 확인 API
@app.route('/api/check-id', methods=['POST'])
def check_id():
    data = request.json
    user_id = data.get('id')

    if not user_id:
        return jsonify({'message': '아이디가 필요합니다.'}), 400

    existing_user = users_collection.find_one({'id': user_id})
    if existing_user:
        return jsonify({'message': '아이디가 중복되었습니다.'}), 409

    return jsonify({'message': '사용 가능한 아이디입니다.'}), 200

# 회원가입 API
@app.route('/api/sign-up', methods=['POST'])
def sign_up():
    data = request.json
    email = data.get('email')
    verification_code = data.get('verificationCode')
    user_id = data.get('id')
    password = data.get('password')

    if not email:
        return jsonify({'message': '이메일을 입력해주세요.'}), 400
    if not verification_code:
        return jsonify({'message': '인증번호를 입력해주세요.'}), 400
    if not user_id:
        return jsonify({'message': '아이디를 입력해주세요.'}), 400
    if not password:
        return jsonify({'message': '비밀번호를 입력해주세요.'}), 400

    verification_entry = verification_codes_collection.find_one({'email': email})
    if not verification_entry or verification_entry['code'] != verification_code:
        return jsonify({'message': '인증번호가 일치하지 않습니다.'}), 400

    existing_user = users_collection.find_one({'id': user_id})
    if existing_user:
        return jsonify({'message': '아이디가 중복되었습니다.'}), 409

    new_user = {
        'email': email,
        'id': user_id,
        'password': password
    }
    users_collection.insert_one(new_user)
    verification_codes_collection.delete_one({'email': email})

    return jsonify({'message': '회원가입이 완료되었습니다.'}), 201

# 아이디 찾기 API
@app.route('/api/find-id', methods=['POST'])
def find_id():
    data = request.json
    email = data.get('email')
    verification_code = data.get('verificationCode')

    if not email:
        return jsonify({'message': '이메일을 입력해주세요.'}), 400
    if not verification_code:
        return jsonify({'message': '인증번호를 입력해주세요.'}), 400

    verification_entry = verification_codes_collection.find_one({'email': email})
    if not verification_entry or verification_entry['code'] != verification_code:
        return jsonify({'message': '인증번호가 일치하지 않습니다.'}), 400

    user = users_collection.find_one({'email': email})
    if not user:
        return jsonify({'message': '등록된 이메일이 없습니다.'}), 404

    verification_codes_collection.delete_one({'email': email})

    return jsonify({'userId': user['id']}), 200

# 비밀번호 재설정 API
@app.route('/api/reset-password', methods=['POST'])
def reset_password():
    data = request.json
    user_id = data.get('userId')
    email = data.get('email')
    verification_code = data.get('verificationCode')
    new_password = data.get('newPassword')

    if not user_id:
        return jsonify({'message': '아이디를 입력해주세요.'}), 400
    if not email:
        return jsonify({'message': '이메일을 입력해주세요.'}), 400
    if not verification_code:
        return jsonify({'message': '인증번호를 입력해주세요.'}), 400
    if not new_password:
        return jsonify({'message': '새 비밀번호를 입력해주세요.'}), 400

    verification_entry = verification_codes_collection.find_one({'email': email})
    if not verification_entry or verification_entry['code'] != verification_code:
        return jsonify({'message': '인증번호가 일치하지 않습니다.'}), 400

    user = users_collection.find_one({'id': user_id, 'email': email})
    if not user:
        return jsonify({'message': '사용자를 찾을 수 없습니다.'}), 404

    users_collection.update_one({'id': user_id}, {'$set': {'password': new_password}})
    verification_codes_collection.delete_one({'email': email})

    return jsonify({'message': '비밀번호가 성공적으로 변경되었습니다.'}), 200

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
