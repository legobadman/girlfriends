# coding=utf-8

import uvicorn

if __name__ == "__main__":
    uvicorn.run(
        "server:app",   # 模块路径
        host="192.168.1.3",
        port=8080,
        reload=False,                # 如果要调试，建议先关掉reload
        log_level="debug"
    )