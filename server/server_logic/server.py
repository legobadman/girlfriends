# coding=utf-8

# server_logic/server.py
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pathlib import Path
import os, json, datetime

app = FastAPI()

# 允许局域网访问
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 可限制为你的app的IP段
    allow_methods=["*"],
    allow_headers=["*"],
)

ROOTS = {
    "C": "C:/",
    "D": "D:/",
    "F": "F:/",
    "G": "G:/",
    "H": "H:/"
}

def path_from_url(url_path: str) -> Path:
    """将 /F/A/文件夹 转为本地 F:/A/文件夹"""
    parts = url_path.strip("/").split("/")
    drive = parts[0]
    base = ROOTS.get(drive)
    if not base:
        raise HTTPException(status_code=404, detail="Unknown drive")
    local_path = Path(base, *parts[1:])
    return local_path

@app.get("/{path:path}")
def get_file_info(path: str):
    local_path = path_from_url(path)
    if not local_path.exists():
        raise HTTPException(status_code=404, detail="File not found")

    if local_path.is_dir():
        files = []
        for item in local_path.iterdir():
            files.append({
                "name": item.name,
                "isDirectory": item.is_dir(),
                "size": os.path.getsize(item) if item.is_file() else None,
                "modified": datetime.datetime.fromtimestamp(item.stat().st_mtime).isoformat(),
                "extension": item.suffix if item.is_file() else None
            })
        return {"path": str(local_path), "files": files}

    else:
        # 返回单文件的元信息（非数据本体）
        return {
            "name": local_path.name,
            "size": os.path.getsize(local_path),
            "modified": datetime.datetime.fromtimestamp(local_path.stat().st_mtime).isoformat(),
            "extension": local_path.suffix,
            "path": str(local_path)
        }
