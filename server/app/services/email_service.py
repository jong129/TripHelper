import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

def send_email(recipient, subject, body):
    sender_email = "whdgh78921@gmail.com"  # 자신의 이메일 주소
    sender_password = "kifm wemd cyzk kasi"  # 자신의 이메일 비밀번호 (앱 비밀번호 권장)
    smtp_server = "smtp.gmail.com"  # Gmail SMTP 서버
    smtp_port = 587  # SMTP 포트 번호

    # 이메일 메시지 생성
    message = MIMEMultipart()
    message["From"] = sender_email
    message["To"] = recipient
    message["Subject"] = subject
    message.attach(MIMEText(body, "plain"))

    try:
        # SMTP 서버 연결 및 이메일 전송
        with smtplib.SMTP(smtp_server, smtp_port) as server:
            server.starttls()  # TLS 보안 연결 시작
            server.login(sender_email, sender_password)
            server.send_message(message)
        print(f"이메일 전송 성공: {recipient}")
        return True
    except Exception as e:
        print(f"이메일 전송 실패: {e}")
        return False
