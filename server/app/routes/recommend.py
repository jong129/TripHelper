from flask import Blueprint, request, jsonify, current_app

recommend_bp = Blueprint('recommend', __name__)

@recommend_bp.route('/api/recommend', methods=['POST'])
def recommend():
    data = request.json
    purpose = data.get('purpose')
    activity = data.get('activity')
    accommodation = data.get('accommodation')

    if not all([purpose, activity, accommodation]):
        return jsonify({'success': False, 'message': '모든 필드를 입력해주세요.'}), 400

    recommendation = current_app.db.recommendations.find_one({
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
