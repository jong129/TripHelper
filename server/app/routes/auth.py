from flask import Blueprint, request, jsonify, current_app
from werkzeug.security import generate_password_hash, check_password_hash
from app.utils.validation import is_valid_email
from app.services.email_service import send_email  # 이메일 발송 함수 추가
from datetime import datetime, timedelta
import random

auth_bp = Blueprint('auth', __name__)

# 인증번호 검증 로직 (이름 변경)
def verify_code_logic(email, code):
    # 인증번호 검증 로직
    verification_entry = current_app.db.verification_codes.find_one({'email': email})
    if not verification_entry:
        return False, '인증번호가 존재하지 않습니다.'
    if verification_entry['code'] != code:
        return False, '인증번호가 잘못되었습니다.'
    if verification_entry['expiry'] < datetime.utcnow():
        return False, '인증번호가 만료되었습니다.'
    return True, None

# 이메일 인증번호 발송
@auth_bp.route('/api/send-verification-code', methods=['POST'])
def send_verification_code():
    data = request.json
    email = data.get('email')

    if not email or not is_valid_email(email):
        return jsonify({'success': False, 'message': '유효한 이메일을 입력해주세요.'}), 400

    # 인증번호 생성
    verification_code = f"{random.randint(100000, 999999)}"
    
    # 인증번호 DB 저장
    current_app.db.verification_codes.update_one(
        {'email': email},
        {'$set': {'code': verification_code, 'expiry': datetime.utcnow() + timedelta(minutes=5)}},
        upsert=True
    )

    # 이메일 발송
    subject = "이메일 인증번호"
    body = f"인증번호는 {verification_code}입니다. 5분 이내에 입력해주세요."
    if send_email(email, subject, body):  # 이메일 전송 성공 여부 확인
        return jsonify({'success': True, 'message': '인증번호가 발송되었습니다.'}), 200
    else:
        return jsonify({'success': False, 'message': '이메일 전송에 실패했습니다.'}), 500

# 인증번호 검증 API (HTTP 엔드포인트)
@auth_bp.route('/api/verify-code', methods=['POST'])
def verify_code_endpoint():
    data = request.json
    email = data.get('email')
    code = data.get('code')

    # verify_code_logic 호출
    valid, message = verify_code_logic(email, code)
    if not valid:
        return jsonify({'success': False, 'message': message}), 400

    return jsonify({'success': True, 'message': '인증에 성공하였습니다.'}), 200

# 로그인 API
@auth_bp.route('/api/login', methods=['POST'])
def login():
    data = request.json
    user_id = data.get('id')
    password = data.get('password')

    if not user_id or not password:
        return jsonify({'success': False, 'message': '아이디와 비밀번호를 입력해주세요.'}), 400

    user = current_app.db.users.find_one({'id': user_id})
    if not user or not check_password_hash(user['password'], password):
        return jsonify({'success': False, 'message': '아이디 또는 비밀번호가 잘못되었습니다.'}), 401

    return jsonify({'success': True, 'message': '로그인 성공!'}), 200

# 회원가입 API
@auth_bp.route('/api/sign-up', methods=['POST'])
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

    # verify_code_logic 호출
    valid, error_message = verify_code_logic(email, verification_code)
    if not valid:
        return jsonify({'success': False, 'message': error_message}), 400

    if current_app.db.users.find_one({'id': user_id}):
        return jsonify({'success': False, 'message': '아이디가 중복되었습니다.'}), 409

    hashed_password = generate_password_hash(password)
    current_app.db.users.insert_one({
        'email': email,
        'id': user_id,
        'password': hashed_password
    })
    current_app.db.verification_codes.delete_one({'email': email})

    return jsonify({'success': True, 'message': ' 회원가입이 완료되었습니다.'}), 201

# 아이디 중복 확인 API
@auth_bp.route('/api/check-id', methods=['POST'])
def check_id():
    data = request.json
    user_id = data.get('id')

    if not user_id:
        return jsonify({'success': False, 'message': '아이디를 입력해주세요.'}), 400

    # 사용자 ID가 데이터베이스에 존재하는지 확인
    user = current_app.db.users.find_one({'id': user_id})
    if user:
        return jsonify({'success': True, 'exists': True, 'message': '사용 불가능한 아이디입니다.'}), 200
    else:
        return jsonify({'success': True, 'exists': False, 'message': '사용 가능한 아이디입니다.'}), 200
