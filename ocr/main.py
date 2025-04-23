import os, tempfile, shutil
from flask import Flask, request, jsonify
import requests, pytesseract, psycopg2
from pdf2image import convert_from_path
from PIL import Image
from extract import keywords

DB = {
    "host": os.getenv("DB_HOST", "postgres"),
    "port": os.getenv("DB_PORT", "5432"),
    "user": os.getenv("DB_USER", "postgres"),
    "password": os.getenv("DB_PASS", "root"),
    "dbname": os.getenv("DB_NAME", "dms")
}

STORAGE_URL = os.getenv("STORAGE_URL", "http://storage:8080/api/files/") # <- меняется в зависимости от реализации системы хранения файлов

app = Flask(__name__)

def db_conn():
    return psycopg2.connect(**DB)

def save_to_db(doc_id, full_text, tags):
    with db_conn() as conn, conn.cursor() as cur:
        cur.execute("""
            INSERT INTO ocr_text (id, text, tags)
            VALUES (%s, %s, %s)
            ON CONFLICT (id) DO UPDATE SET text = EXCLUDED.text, tags = EXCLUDED.tags
        """, (doc_id, full_text, tags))
        conn.commit()

def ensure_table():
    with db_conn() as conn, conn.cursor() as cur:
        cur.execute("""
        CREATE TABLE IF NOT EXISTS ocr_text (
            id   text PRIMARY KEY,
            text text,
            tags text[]
        );""")
        conn.commit()

@app.route("/api/ocr", methods=["POST"])
def run_ocr():
    doc_id = request.json.get("id")
    if not doc_id:
        return {"error": "id required"}, 400

    r = requests.get(STORAGE_URL + doc_id)
    if r.status_code != 200:
        return {"error": f"storage returned {r.status_code}"}, 502

    tmpdir = tempfile.mkdtemp()
    try:
        file_path = os.path.join(tmpdir, doc_id)
        with open(file_path, "wb") as f:
            f.write(r.content)

        images = []
        if file_path.lower().endswith(".pdf"):
            images = convert_from_path(file_path)
        else:
            images = [Image.open(file_path)]

        full_text = ""
        for img in images:
            full_text += pytesseract.image_to_string(img, lang="rus+eng") + "\n"

        tag_list = keywords(full_text, k=10)
        save_to_db(doc_id, full_text, tag_list)

        return jsonify({"id": doc_id,
                        "pages": len(images),
                        "tags": tag_list})
    finally:
        shutil.rmtree(tmpdir, ignore_errors=True)

if __name__ == "__main__":
    ensure_table()
    app.run(debug=True, host="0.0.0.0", port=5000)