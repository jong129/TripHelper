from flask import Flask, request, jsonify
from pymongo import MongoClient
from werkzeug.security import generate_password_hash, check_password_hash
import uuid
import re
from datetime import datetime, timedelta

app = Flask(__name__)

# MongoDB 연결 설정
client = MongoClient("mongodb://localhost:27017/")
db = client['user_database']
users_collection = db['users']
verification_codes_collection = db['verification_codes']
recommendations_collection = db['recommendations']

# 인증번호 유효 기간 설정 (5분)
VERIFICATION_CODE_EXPIRY = timedelta(minutes=5)

# 이메일 정규식
EMAIL_REGEX = r"[^@]+@[^@]+\.[^@]+"


# 유효성 검사 유틸리티
def is_valid_email(email):
    return re.match(EMAIL_REGEX, email)


# 이메일 인증 코드 확인 함수
def verify_code(email, code):
    verification_entry = verification_codes_collection.find_one({'email': email})
    if not verification_entry:
        return False, "인증번호가 존재하지 않습니다."
    if verification_entry['code'] != code:
        return False, "인증번호가 일치하지 않습니다."
    if verification_entry['expiry'] < datetime.utcnow():
        return False, "인증번호가 만료되었습니다."
    return True, None


# 에러 핸들러
@app.errorhandler(Exception)
def handle_exception(e):
    return jsonify({'success': False, 'message': '서버 에러가 발생했습니다.', 'error': str(e)}), 500


# 이메일 인증 API
@app.route('/api/send-verification-code', methods=['POST'])
def send_verification_code():
    data = request.json
    email = data.get('email')

    if not email or not is_valid_email(email):
        return jsonify({'success': False, 'message': '유효한 이메일을 입력해주세요.'}), 400

    verification_code = "{:06d}".format(uuid.uuid4().int % 1000000)
    expiry_time = datetime.utcnow() + VERIFICATION_CODE_EXPIRY

    verification_codes_collection.update_one(
        {'email': email},
        {'$set': {'code': verification_code, 'expiry': expiry_time}},
        upsert=True
    )

    print(f"인증번호 생성: {verification_code} (이메일: {email})")
    return jsonify({'success': True, 'message': '인증번호가 발송되었습니다.'}), 200


# 로그인 API
@app.route('/api/login', methods=['POST'])
def login():
    data = request.json
    user_id = data.get('id')
    password = data.get('password')

    if not user_id or not password:
        return jsonify({'success': False, 'message': '아이디와 비밀번호를 입력해주세요.'}), 400

    user = users_collection.find_one({'id': user_id})
    if not user or not check_password_hash(user['password'], password):
        return jsonify({'success': False, 'message': '아이디 또는 비밀번호가 잘못되었습니다.'}), 401

    return jsonify({'success': True, 'message': '로그인 성공!'}), 200


# 회원가입 API
@app.route('/api/sign-up', methods=['POST'])
def sign_up():
    data = request.json
    email = data.get('email')
    verification_code = data.get('verificationCode')
    user_id = data.get('id')
    password = data.get('password')

    if not all([email, verification_code, user_id, password]):
        return jsonify({'success': False, 'message': '모든 필드를 입력해주세요.'}), 400

    if not is_valid_email(email):
        return jsonify({'success': False, 'message': '유효한 이메일을 입력해주세요.'}), 400

    valid, error_message = verify_code(email, verification_code)
    if not valid:
        return jsonify({'success': False, 'message': error_message}), 400

    if users_collection.find_one({'id': user_id}):
        return jsonify({'success': False, 'message': '아이디가 중복되었습니다.'}), 409

    hashed_password = generate_password_hash(password)

    users_collection.insert_one({
        'email': email,
        'id': user_id,
        'password': hashed_password
    })
    verification_codes_collection.delete_one({'email': email})

    return jsonify({'success': True, 'message': '회원가입이 완료되었습니다.'}), 201


# 비밀번호 재설정 API
@app.route('/api/reset-password', methods=['POST'])
def reset_password():
    data = request.json
    user_id = data.get('userId')
    email = data.get('email')
    verification_code = data.get('verificationCode')
    new_password = data.get('newPassword')

    if not all([user_id, email, verification_code, new_password]):
        return jsonify({'success': False, 'message': '모든 필드를 입력해주세요.'}), 400

    valid, error_message = verify_code(email, verification_code)
    if not valid:
        return jsonify({'success': False, 'message': error_message}), 400

    user = users_collection.find_one({'id': user_id, 'email': email})
    if not user:
        return jsonify({'success': False, 'message': '사용자를 찾을 수 없습니다.'}), 404

    hashed_password = generate_password_hash(new_password)
    users_collection.update_one({'id': user_id}, {'$set': {'password': hashed_password}})
    verification_codes_collection.delete_one({'email': email})

    return jsonify({'success': True, 'message': '비밀번호가 성공적으로 변경되었습니다.'}), 200


# 비밀번호 찾기 API
@app.route('/api/change-password', methods=['POST'])
def change_password():
    data = request.json
    user_id = data.get('userId')
    old_password = data.get('oldPassword')
    new_password = data.get('newPassword')

    if not all([user_id, old_password, new_password]):
        return jsonify({'success': False, 'message': '모든 필드를 입력해주세요.'}), 400

    user = users_collection.find_one({'id': user_id})
    if not user or not check_password_hash(user['password'], old_password):
        return jsonify({'success': False, 'message': '기존 비밀번호가 잘못되었습니다.'}), 401

    hashed_password = generate_password_hash(new_password)
    users_collection.update_one({'id': user_id}, {'$set': {'password': hashed_password}})

    return jsonify({'success': True, 'message': '비밀번호가 성공적으로 변경되었습니다.'}), 200

# 추천 시스템 API
@app.route('/api/recommend', methods=['POST'])
def recommend():
    data = request.json
    purpose = data.get('purpose')
    activity = data.get('activity')
    accommodation = data.get('accommodation')

    if not all([purpose, activity, accommodation]):
        return jsonify({'success': False, 'message': '모든 필드를 입력해주세요.'}), 400

    recommendation = recommendations_collection.find_one({
        'purpose': purpose,
        'activity': activity,
        'accommodation': accommodation
    })

    if recommendation:
        return jsonify({
            'success': True,
            'recommendation': recommendation['recommendation']
        }), 200
    else:
        return jsonify({
            'success': False,
            'message': '조건에 맞는 추천 여행지를 찾을 수 없습니다.'
        }), 404


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
