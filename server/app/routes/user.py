from flask import Blueprint, request, jsonify, current_app
from werkzeug.security import generate_password_hash
from app.services.auth_service import verify_code

user_bp = Blueprint('user', __name__)

@user_bp.route('/api/reset-password', methods=['POST'])
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

    user = current_app.db.users.find_one({'id': user_id, 'email': email})
    if not user:
        return jsonify({'success': False, 'message': '사용자를 찾을 수 없습니다.'}), 404

    hashed_password = generate_password_hash(new_password)
    current_app.db.users.update_one({'id': user_id}, {'$set': {'password': hashed_password}})
    current_app.db.verification_codes.delete_one({'email': email})

    return jsonify({'success': True, 'message': '비밀번호가 성공적으로 변경되었습니다.'}), 200
