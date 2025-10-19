# coding=utf-8

import uvicorn

if __name__ == "__main__":
    uvicorn.run(
        "server:app",   # 模块路径
        host="192.168.3.104",
        port=80,
        reload=False,                # 如果要调试，建议先关掉reload
        log_level="debug"
    )