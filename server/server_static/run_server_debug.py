# coding=utf-8

import uvicorn

if __name__ == "__main__":
    uvicorn.run(
        "server:app",   # ģ��·��
        host="192.168.1.3",
        port=8080,
        reload=False,                # ���Ҫ���ԣ������ȹص�reload
        log_level="debug"
    )