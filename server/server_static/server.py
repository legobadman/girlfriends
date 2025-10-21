from fastapi import FastAPI, HTTPException
from fastapi.responses import FileResponse
from pathlib import Path
from functools import lru_cache
import json

# =========================================================
# 基础配置
# =========================================================
CONFIG_PATH = "config.txt"   # 与逻辑服务器共用，内容为 JSON 格式但扩展名为 .txt

app = FastAPI(title="Zeno Static File Server")

# =========================================================
# 加载配置
# =========================================================
def load_config():
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
    根据URL路径在多个硬盘下查找真实文件。
    示例:
        url_path = '风景图/A类图片/IMG_001.jpg'
    """
    parts = url_path.strip("/").split("/")
    if not parts:
        raise HTTPException(status_code=400, detail="空路径")

    keyword = parts[0]
    sub_path = Path(*parts[1:]) if len(parts) > 1 else Path()

    candidate_roots = find_matching_paths(keyword)
    if not candidate_roots:
        raise HTTPException(status_code=404, detail=f"未知关键字: {keyword}")

    for root in candidate_roots:
        candidate = root / sub_path
        if candidate.exists():
            return candidate.resolve()

    raise HTTPException(status_code=404, detail=f"文件未找到: {url_path}")

# =========================================================
# 主API
# =========================================================
@app.get("/{path:path}")
def serve_file(path: str):
    """
    直接返回文件内容（二进制数据）。
    示例:
        /风景图/A类图片/IMG_0001.jpg
    """
    local_path = find_resource(path)

    if not local_path.is_file():
        raise HTTPException(status_code=404, detail="不是文件或不存在")

    return FileResponse(local_path)

# =========================================================
# 启动提示
# =========================================================
@app.on_event("startup")
def startup_info():
    print("✅ Static File Server 已启动")
    print(f"配置文件: {CONFIG_PATH}")
    print(f"关键字列表: {list(KEYWORD_MAP.keys())}")
    print(f"缓存上限: {find_resource.cache_info().maxsize}")

