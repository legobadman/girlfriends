# coding=utf-8

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pathlib import Path
from functools import lru_cache
import os, json, datetime

# =========================================================
# 基础配置
# =========================================================
CONFIG_PATH = "config.txt"   # 内容是 JSON 格式，但后缀为 .txt 方便编辑

app = FastAPI(title="Zeno Resource Logic Server")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# =========================================================
# 加载配置
# =========================================================
def load_config():
    if not Path(CONFIG_PATH).exists():
        raise FileNotFoundError(f"配置文件不存在: {CONFIG_PATH}")
    with open(CONFIG_PATH, "r", encoding="utf-8") as f:
        return json.load(f)

CONFIG = load_config()
KEYWORD_MAP = CONFIG.get("keywords", {})

# =========================================================
# 工具函数
# =========================================================
def find_matching_paths(keyword: str):
    """返回该关键字对应的多个根路径"""
    return [Path(p) for p in KEYWORD_MAP.get(keyword, [])]

@lru_cache(maxsize=2048)
def find_resource(url_path: str) -> Path:
    """
    查找资源的真实路径，带缓存。
    例如 url_path = '风景图/A类图片/IMG_001.jpg'
    """
    parts = url_path.strip("/").split("/")
    if not parts:
        raise HTTPException(status_code=400, detail="空路径")

    keyword = parts[0]
    sub_path = Path(*parts[1:]) if len(parts) > 1 else Path()

    candidate_roots = find_matching_paths(keyword)
    if not candidate_roots:
        raise HTTPException(status_code=404, detail=f"未知关键字: {keyword}")

    # 遍历各个硬盘，找到第一个存在的
    for root in candidate_roots:
        candidate = root / sub_path
        if candidate.exists():
            return candidate.resolve()

    raise HTTPException(status_code=404, detail=f"未找到路径: {url_path}")

# =========================================================
# API 路由
# =========================================================
@app.get("/{path:path}")
def get_resource_info(path: str):
    """
    根据 URL 返回资源信息。
    - 若路径仅含关键字，如 /风景图 → 聚合多个硬盘下同名目录内容
    - 若路径更深，如 /风景图/A类图片 → 查找第一个存在的目录或文件
    """
    parts = path.strip("/").split("/")
    if not parts or not parts[0]:
        raise HTTPException(status_code=400, detail="空路径")

    keyword = parts[0]
    sub_path = Path(*parts[1:]) if len(parts) > 1 else None
    candidate_roots = find_matching_paths(keyword)

    if not candidate_roots:
        raise HTTPException(status_code=404, detail=f"未知关键字: {keyword}")

    # -------------------------------
    # 情况1：仅关键字（需要聚合）
    # -------------------------------
    if sub_path is None or str(sub_path) == ".":
        merged_files = []
        for root in candidate_roots:
            if root.exists() and root.is_dir():
                try:
                    for item in root.iterdir():
                        merged_files.append({
                            "name": item.name,
                            "from": str(root),  # 标明来源盘符
                            "isDirectory": item.is_dir(),
                            "size": os.path.getsize(item) if item.is_file() else None,
                            "modified": datetime.datetime.fromtimestamp(item.stat().st_mtime).isoformat(),
                            "extension": item.suffix if item.is_file() else None
                        })
                except PermissionError:
                    continue

        # 可选择：按文件名去重（同名目录或文件仅保留一个）
        unique = {}
        for f in merged_files:
            unique.setdefault(f["name"], f)
        merged_files = list(unique.values())

        return {
            "path": f"[聚合目录] {keyword}",
            "type": "merged",
            "count": len(merged_files),
            "files": merged_files
        }

    # -------------------------------
    # 情况2：包含子路径（走原逻辑）
    # -------------------------------
    else:
        local_path = find_resource(path)
        if local_path.is_dir():
            files = []
            try:
                for item in local_path.iterdir():
                    files.append({
                        "name": item.name,
                        "isDirectory": item.is_dir(),
                        "size": os.path.getsize(item) if item.is_file() else None,
                        "modified": datetime.datetime.fromtimestamp(item.stat().st_mtime).isoformat(),
                        "extension": item.suffix if item.is_file() else None
                    })
            except PermissionError:
                raise HTTPException(status_code=403, detail=f"没有权限访问: {local_path}")

            return {
                "path": str(local_path),
                "type": "directory",
                "count": len(files),
                "files": files
            }

        elif local_path.is_file():
            return {
                "path": str(local_path),
                "type": "file",
                "name": local_path.name,
                "size": os.path.getsize(local_path),
                "modified": datetime.datetime.fromtimestamp(local_path.stat().st_mtime).isoformat(),
                "extension": local_path.suffix
            }

        else:
            raise HTTPException(status_code=404, detail="无效路径")

# =========================================================
# 启动提示
# =========================================================
@app.on_event("startup")
def startup_info():
    print("✅ Logic Server 已启动")
    print(f"配置文件: {CONFIG_PATH}")
    print(f"关键字列表: {list(KEYWORD_MAP.keys())}")
    print(f"缓存上限: {find_resource.cache_info().maxsize}")

