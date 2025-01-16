from datetime import datetime
from flask import current_app

def verify_code(email, code):
    verification_entry = current_app.db.verification_codes.find_one({'email': email})
    if not verification_entry:
        return False, "인증번호가 존재하지 않습니다."
    if verification_entry['code'] != code:
        return False, "인증번호가 일치하지 않습니다."
    if verification_entry['expiry'] < datetime.utcnow():
        return False, "인증번호가 만료되었습니다."
    return True, None
